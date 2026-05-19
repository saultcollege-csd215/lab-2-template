package sales.feature.base.ui;

import javafx.scene.Node;

/**
 * The main window of the application.
 * Allows controllers to set the title and main scene, and show errors.
 */
public interface MainWindow {

    /**
     * Sets the title of the main window.
     * @param title The title to set.
     */
    void setTitle(String title);

    /**
     * Sets the main scene of the application. (The main content area.)
     * @param n The node to set as the main scene.
     */
    void setMainScene(Node n);

    /**
     * Sets the main scene to show an error message
     * @param e The exception to show.
     */
    void showError(Exception e);

}
