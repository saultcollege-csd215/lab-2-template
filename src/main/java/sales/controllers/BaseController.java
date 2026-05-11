package sales.controllers;

import sales.ui.MainWindow;

public class BaseController {

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
