package sales.feature.base.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sales.feature.category.CategoryController;
import sales.feature.product.ProductController;
import sales.feature.base.data.DataService;

/**
 * The main JavaFX application class that hooks our program into the JavaFX system.
 */
public class SalesApp extends Application {

    /**
     * The service that provides data repositories for various entities in the app.
     */
    private DataService dataService;

    /**
     * The entry point of the app.
     * The call to SalesApp.launch in Main.main ultimately results in this method being called.
     * @param primaryStage The main Stage (window) of the app.
     */
    @Override
    public void start(Stage primaryStage) {

        try {

            this.dataService = new DataService("jdbc:sqlite:northwind.db");
            var productRepo = dataService.getProductRepository();
            var categoryRepo = dataService.getCategoryRepository();

            var layoutManager = new LayoutManager();
            var productController = new ProductController(layoutManager, productRepo, categoryRepo);
            var categoryController = new CategoryController(layoutManager, categoryRepo);

            layoutManager.init(primaryStage,
                    productController::showNewProduct,
                    categoryController::showNewCategory,
                    productController::showProducts,
                    categoryController::showCategories
            );
        } catch (Exception e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error has occurred during application startup.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            Platform.exit();
        }

    }

    /**
     * Close out database connections when the app closes.
     * This is
     */
    @Override
    public void stop() {
        if ( dataService != null ) {
            dataService.stop();
        }
    }

}
