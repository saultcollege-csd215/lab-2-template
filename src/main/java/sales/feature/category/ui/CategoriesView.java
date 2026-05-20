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

/**
 * A 'namespace' class for types and functions related to displaying the list of categories in the UI.
 */
public class CategoriesView {

    /**
     * The view model containing the data needed by the view
     * @param categories The set of categories to show
     * @param onCategorySelected The callback for when the user double-clicks a specific category
     */
    public record ViewModel(
            List<Category> categories,
            Consumer<Category> onCategorySelected
    ) {}

    /**
     * @param viewModel The data required by the view
     * @return The root Node of the scene for showing the list of categories in the database
     */
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
