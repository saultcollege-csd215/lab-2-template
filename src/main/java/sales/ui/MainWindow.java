package sales.ui;

import javafx.scene.Node;

public interface MainWindow {

    void setTitle(String title);

    void setMainScene(Node n);
    void showError(Exception e);

}
