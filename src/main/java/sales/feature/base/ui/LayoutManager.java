package sales.feature.base.ui;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sales.feature.base.ui.views.ErrorView;

import java.awt.*;

public class LayoutManager implements MainWindow {

    Stage primaryStage;
    BorderPane mainLayout = new BorderPane();

    public LayoutManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setMainLayout(BorderPane mainLayout) {
        this.mainLayout = mainLayout;
        primaryStage.setScene(new Scene(this.mainLayout, 800, 600));
    }

    @Override
    public void setTitle(String title) {
        var t = "Sales Application";
        if (!title.isEmpty()) {
            t += " - " + title;
        }
        primaryStage.setTitle(t);
    }

    @Override
    public void setMainScene(Node n) {
        mainLayout.setCenter(n);
    }

    @Override
    public void showError(Exception e) {
        primaryStage.setTitle("Error");
        mainLayout.setCenter(ErrorView.createScene(e));
    }
}
