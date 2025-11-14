package sales.controllers;

import javafx.scene.layout.BorderPane;
import sales.data.*;
import sales.ui.MainWindow;
import sales.ui.views.MainLayout;

public class AppController extends BaseController {

    private final ProductController productController;
    private final CategoryController categoryController;

    public AppController(MainWindow mainWindow, DataService dataService) {
        super(mainWindow);
        var productRepo = dataService.getProductRepository();
        var categoryRepo = dataService.getCategoryRepository();

        this.productController = new ProductController(mainWindow, productRepo, categoryRepo);
        this.categoryController = new CategoryController(mainWindow, categoryRepo);
    }

    public BorderPane setMainLayout() {
        return MainLayout.createScene(
                productController::showNewProduct,
                categoryController::showNewCategory,
                productController::showProducts,
                categoryController::showCategories
        );
    }

}
