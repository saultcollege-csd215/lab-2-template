package sales.ui.views;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import sales.core.validation.ValidationMessages;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import sales.core.validation.CategoryData;
import static sales.ui.helpers.JavaFXUtils.addValidatedFieldToGrid;

public class CategoryEditView {

    public record ViewModel (
                int categoryId,
                String categoryName,
                String categoryDescription,
                ValidationMessages messages,
                BiConsumer<Integer, CategoryData.Unvalidated> onUpdate,
                Consumer<Integer> onDelete
    ) {}

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
