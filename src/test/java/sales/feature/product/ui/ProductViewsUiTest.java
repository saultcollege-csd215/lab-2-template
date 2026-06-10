package sales.feature.product.ui;

import javafx.scene.Node;
import javafx.scene.control.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sales.core.Category;
import sales.core.Product;
import sales.feature.base.ui.helpers.NumberField;
import sales.feature.base.validation.ValidationMessages;
import sales.feature.product.validation.ProductData;
import sales.testsupport.FxTestHelper;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class ProductViewsUiTest {

    @BeforeAll
    static void initFx() throws InterruptedException {
        FxTestHelper.initFx();
    }

    // -------------------------------------------------------------------------
    // ProductNewView tests
    // -------------------------------------------------------------------------

    @Test
    void newProductSaveButton_sendsUserInputToCallback() throws Exception {
        var savedData = new AtomicReference<ProductData.Unvalidated>();
        var categories = List.of(
                new Category(1, "Beverages", ""),
                new Category(2, "Frozen", "")
        );

        AtomicReference<Node> root = new AtomicReference<>();

        FxTestHelper.runOnFxThread(() -> {
            root.set(ProductNewView.createScene(new ProductNewView.ViewModel(
                    "", 1, 0.0, 0, false,
                    categories, ValidationMessages.none(), savedData::set
            )));

            // Set the product name
            ((TextField)root.get().lookup("#name-field")).setText("Widget");

            // Set price and stock via NumberField.setValue
            ((NumberField)root.get().lookup("#price-field")).setValue(9.99);
            ((NumberField)root.get().lookup("#units-in-stock-field")).setValue(5);

            // Check the Discontinued checkbox
            ((CheckBox)root.get().lookup("#discontinued-field")).setSelected(true);

            ((Button)root.get().lookup("#save-button")).fire();
        });

        assertThat(savedData.get().name()).isEqualTo("Widget");
        assertThat(savedData.get().price()).isEqualTo(9.99);
        assertThat(savedData.get().unitsInStock()).isEqualTo(5);
        assertThat(savedData.get().discontinued()).isTrue();
    }

    @Test
    void newProductForm_displaysDefaultValuesFromViewModel() throws Exception {
        var categories = List.of(
                new Category(1, "Beverages", ""),
                new Category(2, "Frozen", "")
        );
        AtomicReference<Node> root = new AtomicReference<>();

        FxTestHelper.runOnFxThread(() -> root.set(
                ProductNewView.createScene(new ProductNewView.ViewModel(
                        "Pre-filled Name", 2, 12.50, 10, true,
                        categories, ValidationMessages.none(), _ -> {}
                ))
        ));

        assertThat(((TextField)root.get().lookup("#name-field")).getText())
                .isEqualTo("Pre-filled Name");
        assertThat(((CheckBox)root.get().lookup("#discontinued-field")).isSelected())
                .isTrue();
        assertThat(((NumberField)root.get().lookup("#price-field")).getValue())
                .isEqualTo(12.50);
        assertThat(((NumberField)root.get().lookup("#units-in-stock-field")).getIntValue())
                .isEqualTo(10);
    }

    @Test
    void newProductSaveCallback_includesSelectedCategoryId() throws Exception {
        var savedData = new AtomicReference<ProductData.Unvalidated>();
        var categories = List.of(
                new Category(1, "Beverages", ""),
                new Category(2, "Frozen", "")
        );
        AtomicReference<Node> root = new AtomicReference<>();

        FxTestHelper.runOnFxThread(() -> {
            // Pre-select category 2 via ViewModel
            root.set(ProductNewView.createScene(new ProductNewView.ViewModel(
                    "Test Product", 2, 0.0, 0, false,
                    categories, ValidationMessages.none(), savedData::set
            )));
            ((Button)root.get().lookup("#save-button")).fire();
        });

        assertThat(savedData.get().categoryId()).isEqualTo(2);
    }

    // -------------------------------------------------------------------------
    // ProductEditView tests
    // -------------------------------------------------------------------------

    @Test
    void editProductUpdateButton_callsOnUpdateWithCorrectIdAndData() throws Exception {
        var updatedId = new AtomicInteger(-1);
        var updatedData = new AtomicReference<ProductData.Unvalidated>();
        var categories = List.of(new Category(1, "Beverages", ""));

        FxTestHelper.runOnFxThread(() -> {
            var root = ProductEditView.createScene(new ProductEditView.ViewModel(
                    42, "Widget", 1, 5.99, 3, false,
                    categories, ValidationMessages.none(),
                    (id, data) -> { updatedId.set(id); updatedData.set(data); },
                    _ -> {}
            ));
            ((Button)root.lookup("#update-button")).fire();
        });

        assertThat(updatedId.get()).isEqualTo(42);
        assertThat(updatedData.get().name()).isEqualTo("Widget");
        assertThat(updatedData.get().categoryId()).isEqualTo(1);
    }

    @Test
    void editProductDeleteButton_callsOnDeleteWithCorrectId() throws Exception {
        var deletedId = new AtomicInteger(-1);
        var categories = List.of(new Category(1, "Beverages", ""));

        FxTestHelper.runOnFxThread(() -> {
            var root = ProductEditView.createScene(new ProductEditView.ViewModel(
                    77, "Widget", 1, 5.99, 3, false,
                    categories, ValidationMessages.none(),
                    (_, _) -> {},
                    deletedId::set
            ));
            ((Button)root.lookup("#delete-button")).fire();
        });

        assertThat(deletedId.get()).isEqualTo(77);
    }

    @Test
    void editProductValidationMessages_areShownBesideInvalidFields() throws Exception {
        var categories = List.of(new Category(1, "Beverages", ""));
        AtomicReference<Node> root = new AtomicReference<>();

        FxTestHelper.runOnFxThread(() -> root.set(
                ProductEditView.createScene(new ProductEditView.ViewModel(
                        1, "", 1, -1.0, 0, false,
                        categories,
                        ValidationMessages.of("name", "Name is required", "price", "Price is invalid"),
                        (_, _) -> {},
                        _ -> {}
                ))
        ));

        var nameError = (Label)(root.get().lookup("#name-validation-message"));
        var priceError = (Label)(root.get().lookup("#price-validation-message"));

        assertThat(nameError.getText()).isEqualTo("Name is required");
        assertThat(priceError.getText()).isEqualTo("Price is invalid");
    }

    @Test
    void editProductIdField_isDisplayedAsLabel() throws Exception {
        var categories = List.of(new Category(1, "Beverages", ""));
        AtomicReference<Node> root = new AtomicReference<>();

        FxTestHelper.runOnFxThread(() -> root.set(
                ProductEditView.createScene(new ProductEditView.ViewModel(
                        99, "Widget", 1, 5.99, 3, false,
                        categories, ValidationMessages.none(),
                        (_, _) -> {},
                        _ -> {}
                ))
        ));

        var idField = root.get().lookup("#id-field");
        assertThat(idField).isInstanceOf(Label.class);
        assertThat(((Label) idField).getText()).isEqualTo("99");
    }

    // -------------------------------------------------------------------------
    // ProductsView tests
    // -------------------------------------------------------------------------

    @Test
    @SuppressWarnings("unchecked")
    void productsView_tableDisplaysCorrectColumns() throws Exception {
        var cat = new Category(1, "Beverages", "");
        var products = List.of(new Product(1, "Widget", cat, 9.99, 5, false));
        AtomicReference<Node> root = new AtomicReference<>();

        FxTestHelper.runOnFxThread(() -> root.set(
                ProductsView.createScene(new ProductsView.ViewModel(products, _ -> {}))
        ));

        var table = (TableView<Product>) root.get().lookup(".table-view");
        // Verify column count and titles
        assertThat(table.getColumns()).hasSize(6);
        assertThat(table.getColumns().get(0).getText()).isEqualTo("ID");
        assertThat(table.getColumns().get(1).getText()).isEqualTo("Name");
        assertThat(table.getColumns().get(2).getText()).isEqualTo("Category");
        assertThat(table.getColumns().get(3).getText()).isEqualTo("Price");
        assertThat(table.getColumns().get(4).getText()).isEqualTo("Units In Stock");
        assertThat(table.getColumns().get(5).getText()).isEqualTo("Discontinued");
    }

    @Test
    @SuppressWarnings("unchecked")
    void productsView_tableContainsAllProductsFromViewModel() throws Exception {
        var cat = new Category(1, "Beverages", "");
        var products = List.of(
                new Product(1, "Widget", cat, 9.99, 5, false),
                new Product(2, "Gadget", cat, 14.99, 2, true)
        );
        AtomicReference<Node> root = new AtomicReference<>();

        FxTestHelper.runOnFxThread(() -> root.set(
                ProductsView.createScene(new ProductsView.ViewModel(products, _ -> {}))
        ));

        var table = (TableView<Product>) root.get().lookup(".table-view");
        assertThat(table.getItems()).containsExactlyElementsOf(products);
    }

    @Test
    @SuppressWarnings("unchecked")
    void productsView_doubleClickRow_callsOnProductSelected() throws Exception {
        var cat = new Category(1, "Beverages", "");
        var product = new Product(1, "Widget", cat, 9.99, 5, false);
        var products = List.of(product);
        var selectedProduct = new AtomicReference<Product>();
        AtomicReference<Node> root = new AtomicReference<>();

        FxTestHelper.runOnFxThread(() -> {
            root.set(ProductsView.createScene(new ProductsView.ViewModel(products, selectedProduct::set)));
            var table = (TableView<Product>) root.get().lookup(".table-view");
            FxTestHelper.doubleClickTableRow(table, product);
        });

        assertThat(selectedProduct.get()).isEqualTo(product);
    }
}




