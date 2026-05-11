package sales.ui.views;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import sales.core.Product;

import java.util.List;
import java.util.function.Consumer;

import static sales.ui.helpers.JavaFXUtils.createColumn;
import static sales.ui.helpers.JavaFXUtils.setOnDoubleClick;

public class ProductsView {

    public record ViewModel(
            List<Product> products,
            Consumer<Product> onProductSelected
    ) {}

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
