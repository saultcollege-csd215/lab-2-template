package sales.feature.product.ui;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import sales.core.Product;

import java.util.List;
import java.util.function.Consumer;

import static sales.feature.base.ui.helpers.JavaFXUtils.createColumn;
import static sales.feature.base.ui.helpers.JavaFXUtils.setOnDoubleClick;

/**
 * A 'namespace' class related to UI for displaying the list of products
 */
public class ProductsView {

    /**
     * The view model for the "products" view
     * @param products The set of products to display
     * @param onProductSelected The callback for when one product is double-clicked
     */
    public record ViewModel(
            List<Product> products,
            Consumer<Product> onProductSelected
    ) {}

    /**
     * @param viewModel The view model containing all data used by this view
     * @return The root Node of the scene for this view
     */
    public static Node createScene(ViewModel viewModel) {
        var observableProducts = FXCollections.observableList(viewModel.products());
        TableView<Product> table = new TableView<>(observableProducts);

        var idCol = createColumn("ID", Product::id);
        var nameCol = createColumn("Name", Product::name);
        var categoryCol = createColumn("Category", Product::categoryName);
        var priceCol = createColumn("Price", Product::price);
        var stockCol = createColumn("Units In Stock", Product::unitsInStock);
        var discontinuedCol = createColumn("Discontinued", Product::discontinued);

        table.getColumns().addAll(List.of(idCol, nameCol, categoryCol, priceCol, stockCol, discontinuedCol));

        setOnDoubleClick(table, viewModel.onProductSelected());

        var pane = new VBox(table);
        // Make the table fill the vbox vertically
        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);


        return pane;
    }
}
