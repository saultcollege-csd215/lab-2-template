package sales.feature.category.ui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import sales.feature.base.validation.ValidationMessages;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import sales.feature.category.validation.CategoryData;
import static sales.feature.base.ui.helpers.JavaFXUtils.addValidatedFieldToGrid;

/**
 * A 'namespace' for types and functions related to the 'edit category' view
 */
public class CategoryEditView {

    /**
     * The data required by the 'edit category' view
     * @param categoryId
     * @param categoryName
     * @param categoryDescription
     * @param messages Validation messages to show in the UI (if any)
     * @param onUpdate The callback for when the user clicks the 'Update' button
     * @param onDelete The callback for when the user clicks the 'Delete' button
     */
    public record ViewModel (
                int categoryId,
                String categoryName,
                String categoryDescription,
                ValidationMessages messages,
                BiConsumer<Integer, CategoryData.Unvalidated> onUpdate,
                Consumer<Integer> onDelete
    ) {}

    /**
     * @param viewModel The data required by the view
     * @return The root Node of the scene for showing the 'edit category' view
     */
    public static Node createScene(ViewModel viewModel) {

        var grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        var nameTextField = new TextField(viewModel.categoryName());
        addValidatedFieldToGrid("Name", nameTextField, grid, 0,viewModel.messages().get("name"));

        var descTextField = new TextField(viewModel.categoryDescription());
        addValidatedFieldToGrid("Description", descTextField, grid, 1,viewModel.messages().get("description"));


        var pane = new BorderPane();
        pane.setCenter(grid);

        var saveButton = new Button("Update");
        saveButton.setOnAction(_ ->
            viewModel.onUpdate().accept(viewModel.categoryId(), new CategoryData.Unvalidated(
                nameTextField.getText(),
                descTextField.getText())
            )
        );

        var spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox hbox;

        if ( viewModel.onDelete() != null ) {
            var deleteButton = new Button("Delete");
            deleteButton.setOnAction(_ -> viewModel.onDelete().accept(viewModel.categoryId()));
            hbox = new HBox(deleteButton, spacer, saveButton);
        } else {
            hbox = new HBox(spacer, saveButton);
        }

        hbox.setPadding(new Insets(10));

        pane.setBottom(hbox);

        return pane;
    }


}
