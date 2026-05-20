package sales.feature.category;

import sales.core.Category;
import sales.feature.category.validation.CategoryValidator;
import sales.feature.base.validation.ValidationMessages;
import sales.feature.base.controller.BaseController;
import sales.feature.base.ui.MainWindow;
import sales.feature.category.ui.CategoriesView;
import sales.feature.category.ui.CategoryEditView;
import sales.feature.category.ui.CategoryNewView;

import sales.feature.category.validation.CategoryData;
import static sales.feature.category.validation.CategoryValidator.Result.*;

/**
 * Manages the category views and interactions.
 */
public class CategoryController extends BaseController {

    private final CategoryRepository repo;

    public CategoryController(MainWindow mainWindow, CategoryRepository repo) {
        super(mainWindow);
        this.repo = repo;
    }

    /**
     * Show the list of all categories
     */
    public void showCategories() {
        mainWindow.setTitle("Categories");
        accessDataOrShowError(() -> {
            var viewModel = new CategoriesView.ViewModel(
                    repo.all(),
                    this::showCategory
            );
            mainWindow.setMainScene(CategoriesView.createScene(viewModel));
        });

    }

    /**
     * Show the 'new category' view
     */
    public void showNewCategory() {
        mainWindow.setTitle("New Category");
        var viewModel = new CategoryNewView.ViewModel("", "", ValidationMessages.none(), this::createCategory);
        mainWindow.setMainScene(CategoryNewView.createScene(viewModel));
    }

    /**
     * Show the 'new category' view with validation messages for a given set of invalid category data
     * @param unvalidatedCategory The invalid category data
     * @param validationMessages The validation messages
     */
    public void showNewCategory(CategoryData.Unvalidated unvalidatedCategory, ValidationMessages validationMessages) {
        mainWindow.setTitle("New Category");
        var viewModel = new CategoryNewView.ViewModel(unvalidatedCategory.name(), unvalidatedCategory.description(), validationMessages, this::createCategory);
        mainWindow.setMainScene(CategoryNewView.createScene(viewModel));
    }

    /**
     * Show the 'edit category' view for a given category
     * @param c The category to show
     */
    public void showCategory(Category c) {
        showCategory(c.id(), CategoryData.Unvalidated.of(c), ValidationMessages.none());
    }

    /**
     * Show the 'edit category' view for a given set of category data, with validation messages
     * @param categoryId The id of the category being shown/edited
     * @param c The unvalidated/invalid category data
     * @param messages The validation messages
     */
    public void showCategory(int categoryId, CategoryData.Unvalidated c, ValidationMessages messages) {
        mainWindow.setTitle("Edit Category");

        accessDataOrShowError(() -> {
            var productsInCategory = repo.countProductsInCategory(categoryId);
            var viewModel = new CategoryEditView.ViewModel(
                    categoryId,
                    c.name(),
                    c.description(),
                    messages,
                    this::updateCategory,
                    productsInCategory > 0 ? null : this::deleteCategory // No delete option if category is in use
            );
            mainWindow.setMainScene(CategoryEditView.createScene(viewModel));
        });
    }

    /**
     * Attempt to create a new category from unvalidated user-entered data.
     * If validation fails, shows the 'new category' view again with validation messages.
     * @param c The unvalidated data entered by the user
     */
    public void createCategory(CategoryData.Unvalidated c) {
        accessDataOrShowError(() -> {
            var validationResult = CategoryValidator.validate(c, repo.allCategoryNames());
            switch (validationResult) {
                case Pass result -> {
                    repo.create(result.validatedCategoryData());
                    this.showCategories();
                }
                case Fail result -> this.showNewCategory(c, result.messages());
            }
        });
    }

    /**
     * Attempt to update a category based on unvalidated user-entered data.
     * If validation fails, shows the 'edit category' view again with validation messages.
     * @param categoryId The id of the category to update
     * @param c The unvalidated data
     */
    public void updateCategory(int categoryId, CategoryData.Unvalidated c) {
        accessDataOrShowError(() -> {
            var validationResult = CategoryValidator.validate(c, repo.allCategoryNames(categoryId));

            switch (validationResult) {
                case Pass result -> {
                    var data = result.validatedCategoryData();
                    var updatedCategory = repo.update(categoryId, data);
                    this.showCategory(updatedCategory);
                }
                case Fail result -> this.showCategory(categoryId, c, result.messages());
            }
        });
    }

    /**
     * Delete a category
     * @param categoryId The id of the category to delete
     */
    public void deleteCategory(int categoryId) {
        accessDataOrShowError(() -> {
            if (repo.countProductsInCategory(categoryId) > 0) {
                throw new RuntimeException("Can't delete categories that are being used. This should be protected against in validation.");
            }

            repo.delete(categoryId);
            this.showCategories();
        });
    }
}
