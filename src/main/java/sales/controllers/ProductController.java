package sales.controllers;

import sales.core.Product;
import sales.core.validation.ProductValidator;
import sales.core.validation.ValidationMessages;
import sales.data.CategoryRepository;
import sales.data.ProductRepository;
import sales.ui.MainWindow;
import sales.ui.views.ProductEditView;
import sales.ui.views.ProductNewView;
import sales.ui.views.ProductsView;

import sales.core.validation.ProductData;
import static sales.core.validation.ProductValidator.Result.*;

public class ProductController extends BaseController {

    private final ProductRepository repo;
    private final CategoryRepository categoryRepo;

    public ProductController(MainWindow mainWindow, ProductRepository productRepo, CategoryRepository categoryRepo) {
        super(mainWindow);
        this.repo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    public void showProducts() {
        mainWindow.setTitle("Products");
        accessDataOrShowError(() -> {
            var viewModel = new ProductsView.ViewModel(repo.all(), this::showProduct);
            mainWindow.setMainScene(ProductsView.createScene(viewModel));
        });
    }

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
            mainWindow.setMainScene(sales.ui.views.ProductNewView.createScene(viewModel));
        });
    }

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

    public void deleteProduct(int productId) {
        accessDataOrShowError(() -> {
            repo.delete(productId);
            showProducts();
        });
    }

}
