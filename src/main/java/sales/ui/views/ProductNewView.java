package sales.ui.views;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import sales.core.Category;
import sales.core.validation.ValidationMessages;
import sales.ui.helpers.NumberField;
import sales.ui.helpers.JavaFXUtils;

import java.util.List;
import java.util.function.Consumer;

import sales.core.validation.ProductData;
import static sales.ui.helpers.JavaFXUtils.addValidatedFieldToGrid;

public class ProductNewView {

    public record ViewModel(
            String productName,
            int categoryId,
            double price,
            int unitsInStock,
            boolean discontinued,
            List<Category> categoryChoices,
            ValidationMessages messages,
            Consumer<ProductData.Unvalidated> onSave
    ) {}

    public static Node createScene(ViewModel viewModel) {

        var grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        var nameTextField = new TextField(viewModel.productName());
        addValidatedFieldToGrid("Name", nameTextField, grid, 0,viewModel.messages().get("name"));

        var categoryChoiceBox = JavaFXUtils.createChoiceBox(viewModel.categoryChoices(), Category::name);
        categoryChoiceBox.getSelectionModel().select(viewModel.categoryChoices().stream()
                .filter(c -> c.id() == viewModel.categoryId())
                .findFirst()
                .orElse(null));
        addValidatedFieldToGrid("Category", categoryChoiceBox, grid, 1, viewModel.messages().get("categoryId"));

        var priceTextField = new NumberField(viewModel.price(), false, 2);
        addValidatedFieldToGrid("Price", priceTextField, grid, 2,viewModel.messages().get("price"));

        var stockTextField = new NumberField(viewModel.unitsInStock(), false, 0);
        addValidatedFieldToGrid("Units In Stock", stockTextField, grid, 3, viewModel.messages().get("stock"));

        var discontinuedChoice = new CheckBox("");
        discontinuedChoice.setSelected(viewModel.discontinued());
        addValidatedFieldToGrid("Discontinued", discontinuedChoice, grid, 4, viewModel.messages().get("discontinued"));


        var pane = new BorderPane();
        pane.setCenter(grid);

        var saveButton = new Button("Save");
        saveButton.setOnAction(_ -> {
            var selectedCategory = categoryChoiceBox.getSelectionModel().getSelectedItem();
            viewModel.onSave().accept(new ProductData.Unvalidated(
                    nameTextField.getText(),
                    selectedCategory != null ? selectedCategory.id() : -1,
                    priceTextField.getValue(),
                    stockTextField.getIntValue(),
                    discontinuedChoice.isSelected()));

        });

        var spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox hbox = new HBox(spacer, saveButton);

        hbox.setPadding(new Insets(10));

        pane.setBottom(hbox);

        return pane;
    }
}
