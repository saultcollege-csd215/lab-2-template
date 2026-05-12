package sales.feature.base.controller;

import javafx.scene.layout.BorderPane;
import sales.feature.category.CategoryController;
import sales.feature.product.ProductController;
import sales.feature.base.ui.MainWindow;
import sales.feature.base.ui.views.MainLayout;

/**
 * The main application controller. It manages the main layout and
 * delegates specific 'screens' to other controllers.
 */
public class AppController extends BaseController {

    /** The controller for product-related screens. */
    private final ProductController productController;
    /** The controller for category-related screens. */
    private final CategoryController categoryController;

    public AppController(MainWindow mainWindow, ProductController productController, CategoryController categoryController) {
        super(mainWindow);
        this.productController = productController;
        this.categoryController = categoryController;
    }

    /** Sets up the main BorderPane layout of the application and returns it. */
    public BorderPane setMainLayout() {
        return MainLayout.createScene(
                productController::showNewProduct,
                categoryController::showNewCategory,
                productController::showProducts,
                categoryController::showCategories
        );
    }

}
