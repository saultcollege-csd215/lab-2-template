package sales.feature.base.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sales.feature.base.ui.views.MainLayout;
import sales.feature.category.CategoryController;
import sales.feature.product.ProductController;
import sales.feature.base.data.DataService;

public class SalesApp extends Application {

    private DataService dataService;

    @Override
    public void start(Stage primaryStage) {

        try {

            var layoutManager = new LayoutManager(primaryStage);

            this.dataService = new DataService("jdbc:sqlite:northwind.db");
            var productRepo = dataService.getProductRepository();
            var categoryRepo = dataService.getCategoryRepository();

            var productController = new ProductController(layoutManager, productRepo, categoryRepo);
            var categoryController = new CategoryController(layoutManager, categoryRepo);

            layoutManager.setMainLayout(MainLayout.createScene(
                    productController::showNewProduct,
                    categoryController::showNewCategory,
                    productController::showProducts,
                    categoryController::showCategories
            ));

            primaryStage.show();
        } catch (Exception e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error has occurred during application startup.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            Platform.exit();
        }

    }

    @Override
    public void stop() {
        if ( dataService != null ) {
            dataService.stop();
        }
    }

}
