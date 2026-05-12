package sales.feature.category.ui;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import sales.core.Category;

import java.util.List;
import java.util.function.Consumer;

import static sales.feature.base.ui.helpers.JavaFXUtils.createColumn;
import static sales.feature.base.ui.helpers.JavaFXUtils.setOnDoubleClick;

public class CategoriesView {

    public record ViewModel(
            List<Category> categories,
            Consumer<Category> onCategorySelected
    ) {}

    public static Node createScene(ViewModel viewModel) {
        var observableProducts = FXCollections.observableList(viewModel.categories());
        TableView<Category> table = new TableView<>(observableProducts);

        var idCol = createColumn("ID", Category::id);
        var nameCol = createColumn("Name", Category::name);
        var descCol = createColumn("Description", Category::description);

        setOnDoubleClick(table, viewModel.onCategorySelected());

        table.getColumns().addAll(List.of(idCol, nameCol, descCol));

        var pane = new VBox(table);
        // Make the table fill the vbox vertically
        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);


        return pane;
    }
}
