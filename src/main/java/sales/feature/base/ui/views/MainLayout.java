package sales.feature.base.ui.views;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;

public class MainLayout {

    public static BorderPane createScene(
            Runnable onNewProductClicked,
            Runnable onNewCategoryClicked,
            Runnable onViewProductsClicked,
            Runnable onViewCategoriesClicked
    ) {
        // Create a border layout with a main menu
        var layout = new BorderPane();

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

        layout.setTop(menuBar);

        return layout;
    }
}
