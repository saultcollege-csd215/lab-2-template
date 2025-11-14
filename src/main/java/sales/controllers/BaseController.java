package sales.controllers;

import sales.ui.MainWindow;

public class BaseController {

    protected final MainWindow mainWindow;

    public BaseController(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }
}
