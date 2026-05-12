package sales.feature.base.ui.helpers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Utility functions to help with some trickier JavaFX tasks.
 */
public class JavaFXUtils {

    /**
     * Creates a TableColumn for a TableView, mapping from RowType to ColumnType using the provided mapper function.
     * @param title The title of the column
     * @param mapper Function to map from RowType to ColumnType.  E.g. Map from a Product to its name using Product::name
     * @return The created TableColumn
     * @param <RowType> The type of the rows in the TableView E.g. Product
     * @param <ColumnType> The type of the data in the column E.g. String (for a product name)
     * <p>
     *    Example usage:
     *                    // Create an Integer column named "ID" that will contain Product IDs
     *                    var idCol = JavaFXUtils.createColumn("ID", Product::id);
     *                    // Create a String column named "Name" that will contain Product names
     *                    var nameCol = JavaFXUtils.createColumn("Name", Product::name);
     * </p>
     */
    public static <RowType, ColumnType> TableColumn<RowType, ColumnType> createColumn(String title, Function<RowType, ColumnType> mapper) {
        var column = new TableColumn<RowType, ColumnType>(title);
        column.setCellValueFactory(cellData -> {
            var value = cellData.getValue();
            return new SimpleObjectProperty<>(mapper.apply(value));
        });
        return column;
    }

    /**
     * Sets a double-click handler that calls the provided onDoubleClick consumer when a row is double-clicked.
     * @param table The table to add the handler to
     * @param onDoubleClick The action to perform on double-click
     * @param <T> The type of the rows in the TableView
     */
    public static <T> void setOnDoubleClick(TableView<T> table, Consumer<T> onDoubleClick) {
        table.setRowFactory(_ -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    T rowData = row.getItem();
                    onDoubleClick.accept(rowData);
                }
            });
            return row;
        });
    }

    /**
     * Adds a labeled field to a GridPane with an associated error message label for validation feedback.
     * @param labelText The text for the label of the field
     * @param field The input field (e.g., TextField, ChoiceBox, etc.)
     * @param grid The GridPane to add the field to
     * @param rowIndex The row index in the GridPane where the field should be added
     * @param validationMessage The validation message to display (if any)
     */
    public static void addValidatedFieldToGrid(String labelText, Node field, GridPane grid, int rowIndex, String validationMessage) {
        var label = new Label(labelText + ": ");
        var errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        if (validationMessage != null && !validationMessage.isEmpty()) {
            errorLabel.setText(validationMessage);
        }
        grid.add(label, 0, rowIndex);
        grid.add(field, 1, rowIndex);
        grid.add(errorLabel, 2, rowIndex);
    }

    /**
     * Creates a populated ChoiceBox from a list of objects representing the choices.
     * @param choices The list of objects to represent the choices
     * @param displayMapper Function to map from choice object to its display string (e.g. Name)
     * @return The created ChoiceBox
     * @param <T> The type of the choice objects
     */
    public static <T> ChoiceBox<T> createChoiceBox(List<T> choices, Function<T, String> displayMapper) {

        var observableChoices = FXCollections.observableArrayList(choices);
        var choiceBox = new ChoiceBox<>(observableChoices);

        choiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(T item) {
                if (item == null) {
                    return "";
                }
                return displayMapper.apply(item);
            }

            @Override
            public T fromString(String name) {
                // not used unless the ChoiceBox is editable
                return null;
            }
        });
        return choiceBox;
    }
}
