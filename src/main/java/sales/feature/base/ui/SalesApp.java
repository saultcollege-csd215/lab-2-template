package sales.feature.base.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sales.feature.base.controller.AppController;
import sales.feature.category.CategoryController;
import sales.feature.product.ProductController;
import sales.feature.base.data.DataService;
import sales.feature.base.ui.views.ErrorView;

public class SalesApp extends Application implements MainWindow {

    private DataService dataService;
    private Stage primaryStage;
    private BorderPane mainLayout;

    @Override
    public void start(Stage primaryStage) {

        try {
            this.dataService = new DataService("jdbc:sqlite:northwind.db");
            var productRepo = dataService.getProductRepository();
            var categoryRepo = dataService.getCategoryRepository();

            var productController = new ProductController(this, productRepo, categoryRepo);
            var categoryController = new CategoryController(this, categoryRepo);
            var appController = new AppController(this, productController, categoryController);
            this.mainLayout = appController.setMainLayout();

            this.primaryStage = primaryStage;
            primaryStage.setScene(new Scene(mainLayout, 800, 600));

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

    public void setTitle(String title) {
        var t = "Sales Application";
        if (!title.isEmpty()) {
            t += " - " + title;
        }
        primaryStage.setTitle(t);
    }

    public void setMainScene(Node n) {
        mainLayout.setCenter(n);
    }

    public void showError(Exception e) {
        primaryStage.setTitle("Error");
        mainLayout.setCenter(ErrorView.createScene(e));
    }

}
