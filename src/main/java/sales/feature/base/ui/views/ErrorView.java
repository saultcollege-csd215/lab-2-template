package sales.feature.base.ui.views;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ErrorView {

    /**
     * Creates the scene for displaying error messages in the app
     * @param e The Exception containing the error that occurred
     * @return The root Node of the Scene
     */
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
