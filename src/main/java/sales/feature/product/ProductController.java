package sales.feature.product;

import sales.core.Product;
import sales.feature.product.validation.ProductValidator;
import sales.feature.base.validation.ValidationMessages;
import sales.feature.category.CategoryRepository;
import sales.feature.base.controller.BaseController;
import sales.feature.base.ui.MainWindow;
import sales.feature.product.ui.ProductEditView;
import sales.feature.product.ui.ProductNewView;
import sales.feature.product.ui.ProductsView;

import sales.feature.product.validation.ProductData;
import static sales.feature.product.validation.ProductValidator.Result.*;

/**
 * Manages the product-related views and interactions.
 */
public class ProductController extends BaseController {

    private final ProductRepository repo;
    private final CategoryRepository categoryRepo;

    public ProductController(MainWindow mainWindow, ProductRepository productRepo, CategoryRepository categoryRepo) {
        super(mainWindow);
        this.repo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    /**
     * Display a list of all products
     */
    public void showProducts() {
        mainWindow.setTitle("Products");
        accessDataOrShowError(() -> {
            var viewModel = new ProductsView.ViewModel(repo.all(), this::showProduct);
            mainWindow.setMainScene(ProductsView.createScene(viewModel));
        });
    }

    /**
     * Display the UI for creating a new product
     */
    public void showNewProduct() {
        accessDataOrShowError(() -> {
            mainWindow.setTitle("New Product");
            var viewModel = new ProductNewView.ViewModel(
                    "",
                    -1,
                    0.0,
                    0,
                    false,
                    categoryRepo.all(),
                    ValidationMessages.none(),
                    this::createProduct
                    );
            mainWindow.setMainScene(ProductNewView.createScene(viewModel));
        });
    }

    /**
     * Display the UI for creating a new product, with validation messages for invalid product data
     * @param unvalidatedProduct The invalid product data
     * @param validationMessages The validation messages
     */
    public void showNewProduct(ProductData.Unvalidated unvalidatedProduct, ValidationMessages validationMessages) {
        accessDataOrShowError(() -> {
            mainWindow.setTitle("New Product");
            var viewModel = new ProductNewView.ViewModel(
                    unvalidatedProduct.name(),
                    unvalidatedProduct.categoryId(),
                    unvalidatedProduct.price(),
                    unvalidatedProduct.unitsInStock(),
                    unvalidatedProduct.discontinued(),
                    categoryRepo.all(),
                    validationMessages,
                    this::createProduct
            );
            mainWindow.setMainScene(ProductNewView.createScene(viewModel));
        });
    }

    /**
     * Show the 'edit' UI for the given Product
     * @param p The Product to show
     */
    public void showProduct(Product p) {
        accessDataOrShowError(() -> {
            mainWindow.setTitle("Product Details");
            var viewModel = new ProductEditView.ViewModel(
                    p.id(),
                    p.name(),
                    p.category().id(),
                    p.price(),
                    p.unitsInStock(),
                    p.discontinued(),
                    categoryRepo.all(),
                    ValidationMessages.none(),
                    this::updateProduct,
                    this::deleteProduct
            );
            mainWindow.setMainScene(ProductEditView.createScene(viewModel));
        });
    }

    /**
     * Show the 'edit' product view with validation messages for invalid product data
     * @param productId The id of the product to show
     * @param unvalidatedProduct The unvalidated product data
     * @param validationMessages The validation messages to show in the UI
     */
    public void showProduct(int productId, ProductData.Unvalidated unvalidatedProduct, ValidationMessages validationMessages) {
        accessDataOrShowError(() -> {
            mainWindow.setTitle("Product Details");
            var viewModel = new ProductEditView.ViewModel(
                    productId,
                    unvalidatedProduct.name(),
                    unvalidatedProduct.categoryId(),
                    unvalidatedProduct.price(),
                    unvalidatedProduct.unitsInStock(),
                    unvalidatedProduct.discontinued(),
                    categoryRepo.all(),
                    validationMessages,
                    this::updateProduct,
                    this::deleteProduct
            );
            mainWindow.setMainScene(ProductEditView.createScene(viewModel));
        });
    }

    /**
     * Attempt to create a new Product with the given unvalidated product data.
     * If validation fails, show the 'edit product' UI with the validation messages.
     * @param p The unvalidated product data.
     */
    public void createProduct(ProductData.Unvalidated p) {
        accessDataOrShowError(() -> {

            var validationResult = ProductValidator.validate(p);

            switch (validationResult) {
                case Pass result -> {
                    var validatedProduct = result.validatedProductData();
                    var id = repo.create(validatedProduct);
                    showProduct(id, p, ValidationMessages.none());
                }
                case Fail result -> showNewProduct(p, result.messages());
            }
        });
    }

    /**
     * Attempt to update the given productId with the given product data.
     * If validation fails, show the 'edit' view with validation messages.
     * @param productId The ID of the product to update
     * @param p The unvalidated product data
     */
    public void updateProduct(int productId, ProductData.Unvalidated p) {
        accessDataOrShowError(() -> {
            var validationResult = ProductValidator.validate(p);

            switch (validationResult) {
                case Pass result -> {
                    var validatedProduct = result.validatedProductData();
                    repo.update(productId, validatedProduct);
                    showProduct(productId, p, ValidationMessages.none());
                }
                case Fail result -> {
                    var viewModel = new ProductEditView.ViewModel(
                            productId,
                            p.name(),
                            p.categoryId(),
                            p.price(),
                            p.unitsInStock(),
                            p.discontinued(),
                            categoryRepo.all(),
                            result.messages(),
                            this::updateProduct,
                            this::deleteProduct
                    );
                    mainWindow.setMainScene(ProductEditView.createScene(viewModel));
                }
            }
        });
    }

    /**
     * Delete a product
     * @param productId The id of the product to delete
     */
    public void deleteProduct(int productId) {
        accessDataOrShowError(() -> {
            repo.delete(productId);
            showProducts();
        });
    }

}
