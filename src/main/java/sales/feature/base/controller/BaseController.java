package sales.feature.base.controller;

import sales.feature.base.data.DataAction;
import sales.feature.base.ui.MainWindow;

/**
 * Base controller class that provides common functionality for all controllers.
 */
public class BaseController {

    /** The main window of the application, used to set titles, change scenes, and show errors. */
    protected final MainWindow mainWindow;

    public BaseController(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    /**
     * Runs the given action, and if it throws an exception, shows the error in the main window.
     * @param action The action to run that may throw an exception.
     */
    public void accessDataOrShowError(DataAction action) {
        try {
            action.run();
        } catch (Exception ex) {
            mainWindow.showError(ex);
        }
    }
}
