package sales.controllers;

import javafx.scene.layout.BorderPane;
import sales.data.*;
import sales.ui.MainWindow;
import sales.ui.views.MainLayout;

/**
 * The main application controller. It manages the main layout and
 * delegates specific 'screens' to other controllers.
 */
public class AppController extends BaseController {

    /** The controller for product-related screens. */
    private final ProductController productController;
    /** The controller for category-related screens. */
    private final CategoryController categoryController;

    public AppController(MainWindow mainWindow, DataService dataService) {
        super(mainWindow);
        var productRepo = dataService.getProductRepository();
        var categoryRepo = dataService.getCategoryRepository();

        this.productController = new ProductController(mainWindow, productRepo, categoryRepo);
        this.categoryController = new CategoryController(mainWindow, categoryRepo);
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
