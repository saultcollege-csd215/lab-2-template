package sales.ui.views;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ErrorView {

    public static Node createScene(Exception e) {
        var vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        var label = new Label("An error has occurred. See the program logs:");
        var errorMessage = new Label(e.getMessage());
        errorMessage.setWrapText(true);

        vbox.getChildren().addAll(label, errorMessage);

        return vbox;
    }
}
