package sales.feature.base.ui;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sales.feature.base.ui.views.ErrorView;

/**
 * Manages the main layout of the app
 */
public class LayoutManager implements MainWindow {

    /**
     * The primary stage of the app
     */
    private Stage primaryStage;

    /**
     * The main layout pane of the app.
     * The top panel contains the main menu.
     * The middle panel contains the current scene.
     */
    private BorderPane mainLayout;

    /**
     * Just a safety measure to ensure that the manager is initialized before any of its methods are called.
     */
    private boolean isInitialized = false;

    /**
     * @param primaryStage The primary Stage of the app
     * @param onNewProductClicked The function to call when the New Product menu item is clicked
     * @param onNewCategoryClicked The function to call when the New Category menu item is clicked
     * @param onViewProductsClicked The function to call when the Products menu item is clicked
     * @param onViewCategoriesClicked The function to call when the Categories menu item is clicked
     */
    public void init(Stage primaryStage,
                         Runnable onNewProductClicked,
                         Runnable onNewCategoryClicked,
                         Runnable onViewProductsClicked,
                         Runnable onViewCategoriesClicked
    ) {
        this.primaryStage = primaryStage;

        // Create a border layout with a main menu
        this.mainLayout = new BorderPane();

        Menu fileMenu = new Menu("File");

        Menu newMenu = new Menu("New");
        MenuItem newProductItem = new MenuItem("Product");
        newProductItem.setOnAction(_ -> onNewProductClicked.run());
        MenuItem newCategoryItem = new MenuItem("Category");
        newCategoryItem.setOnAction(_ -> onNewCategoryClicked.run());
        newMenu.getItems().addAll(newProductItem, newCategoryItem);

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(_ -> javafx.application.Platform.exit());

        fileMenu.getItems().addAll(newMenu, new SeparatorMenuItem(), exitItem);

        Menu viewMenu = new Menu("View");
        MenuItem productsItem = new MenuItem("Products");
        productsItem.setOnAction(_ -> onViewProductsClicked.run());
        MenuItem categoriesItem = new MenuItem("Categories");
        categoriesItem.setOnAction(_ -> onViewCategoriesClicked.run());

        viewMenu.getItems().addAll(productsItem, categoriesItem);

        var menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, viewMenu);

        this.mainLayout.setTop(menuBar);

        primaryStage.setScene(new Scene(this.mainLayout, 800, 600));
        primaryStage.show();

        isInitialized = true;
    }

    /**
     * Set the main app title
     * @param title The title to set.
     */
    @Override
    public void setTitle(String title) {
        assert(isInitialized) : "LayoutManager must be initialized before setting the title";

        var t = "Sales Application";
        if (!title.isEmpty()) {
            t += " - " + title;
        }
        primaryStage.setTitle(t);
    }

    /**
     * Set the main scene of the app (the main content area)
     * @param n The node to set as the root node of the main scene.
     */
    @Override
    public void setMainScene(Node n) {
        assert(isInitialized) : "LayoutManager must be initialized before setting the main scene";

        mainLayout.setCenter(n);
    }

    /**
     * Show an error message in the GUI for a given Exception.
     * @param e The exception to show.
     */
    @Override
    public void showError(Exception e) {
        assert(isInitialized) : "LayoutManager must be initialized before showing errors";

        primaryStage.setTitle("Error");
        mainLayout.setCenter(ErrorView.createScene(e));
    }
}
