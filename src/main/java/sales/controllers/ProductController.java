package sales.controllers;

import sales.core.Product;
import sales.core.validation.ProductValidator;
import sales.core.validation.ValidationMessages;
import sales.data.CategoryRepository;
import sales.data.DataAccessException;
import sales.data.ProductRepository;
import sales.ui.MainWindow;
import sales.ui.views.ProductEditView;
import sales.ui.views.ProductNewView;
import sales.ui.views.ProductsView;

import static sales.core.validation.ProductValidator.ProductData;
import static sales.core.validation.ProductValidator.Result.*;

public class ProductController extends BaseController {

    private final ProductRepository repo;
    private final CategoryRepository categoryRepo;

    public ProductController(MainWindow mainWindow, ProductRepository productRepository, CategoryRepository categoryRepository) {
        super(mainWindow);
        this.repo = productRepository;
        this.categoryRepo = categoryRepository;
    }

    public void showProducts() {
        mainWindow.setTitle("Products");
        try {
            var viewModel = new ProductsView.ViewModel(repo.all(), this::showProduct);
            mainWindow.setMainScene(ProductsView.createScene(viewModel));
        } catch (DataAccessException ex) {
            mainWindow.showError(ex);
        }
    }

    public void showNewProduct() {
        try {
            mainWindow.setTitle("New Product");
            var viewModel = new ProductNewView.ViewModel(
                    "",
                    -1,
                    "",
                    0.0,
                    0,
                    false,
                    categoryRepo.all(),
                    ValidationMessages.none(),
                    this::createProduct
                    );
            mainWindow.setMainScene(ProductNewView.createScene(viewModel));
        } catch (DataAccessException ex) {
            mainWindow.showError(ex);
        }
    }

    public void showNewProduct(ProductData.Unvalidated unvalidatedProduct, ValidationMessages validationMessages) {
        try {
            mainWindow.setTitle("New Product");
            var viewModel = new ProductNewView.ViewModel(
                    unvalidatedProduct.name(),
                    unvalidatedProduct.categoryId(),
                    unvalidatedProduct.categoryName(),
                    unvalidatedProduct.price(),
                    unvalidatedProduct.unitsInStock(),
                    unvalidatedProduct.discontinued(),
                    categoryRepo.all(),
                    validationMessages,
                    this::createProduct
            );
            mainWindow.setMainScene(sales.ui.views.ProductNewView.createScene(viewModel));
        } catch (DataAccessException ex) {
            mainWindow.showError(ex);
        }
    }

    public void showProduct(Product p) {
        try {
            mainWindow.setTitle("Product Details");
            var viewModel = new ProductEditView.ViewModel(
                    p.id(),
                    p.name(),
                    p.category().id(),
                    p.category().name(),
                    p.price(),
                    p.unitsInStock(),
                    p.discontinued(),
                    categoryRepo.all(),
                    ValidationMessages.none(),
                    this::updateProduct,
                    this::deleteProduct
            );
            mainWindow.setMainScene(ProductEditView.createScene(viewModel));
        } catch (DataAccessException e) {
            mainWindow.showError(e);
        }
    }

    public void showProduct(int productId, ProductData.Unvalidated unvalidatedProduct, ValidationMessages validationMessages) {
        try {
            mainWindow.setTitle("Product Details");
            var viewModel = new ProductEditView.ViewModel(
                    productId,
                    unvalidatedProduct.name(),
                    unvalidatedProduct.categoryId(),
                    unvalidatedProduct.categoryName(),
                    unvalidatedProduct.price(),
                    unvalidatedProduct.unitsInStock(),
                    unvalidatedProduct.discontinued(),
                    categoryRepo.all(),
                    validationMessages,
                    this::updateProduct,
                    this::deleteProduct
            );
            mainWindow.setMainScene(ProductEditView.createScene(viewModel));
        } catch (DataAccessException e) {
            mainWindow.showError(e);
        }
    }

    public void createProduct(ProductData.Unvalidated p) {
        try {
            var validationResult = ProductValidator.validate(p);

            switch (validationResult) {
                case Pass result -> {
                    var validatedProduct = result.validatedProduct();
                    var id = repo.create(validatedProduct);
                    showProduct(id, p, ValidationMessages.none());
                }
                case Fail result -> showNewProduct(p, result.messages());
            }
        } catch (DataAccessException e) {
            mainWindow.showError(e);
        }
    }

    public void updateProduct(int productId, ProductData.Unvalidated p) {
        try {
            var validationResult = ProductValidator.validate(p);

            switch (validationResult) {
                case Pass result -> {
                    var validatedProduct = result.validatedProduct();
                    repo.update(productId, validatedProduct);
                    showProduct(productId, p, ValidationMessages.none());
                }
                case Fail result -> {
                    var viewModel = new ProductEditView.ViewModel(
                            productId,
                            p.name(),
                            p.categoryId(),
                            p.categoryName(),
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
        } catch (DataAccessException e) {
            mainWindow.showError(e);
        }
    }

    public void deleteProduct(int productId) {
        try {
            repo.delete(productId);
            showProducts();
        } catch (DataAccessException e) {
            mainWindow.showError(e);
        }
    }

}
