package sales.feature.product;

import javafx.scene.Node;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sales.core.Category;
import sales.core.Product;
import sales.feature.base.data.DataAccessException;
import sales.feature.base.ui.MainWindow;
import sales.feature.category.CategoryRepository;
import sales.testsupport.FxTestHelper;
import sales.feature.product.validation.ProductData;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class ProductControllerTest {

    // -------------------------------------------------------------------------
    // JavaFX toolkit init (needed because controller calls createScene internally)
    // -------------------------------------------------------------------------

    @BeforeAll
    static void initFx() throws InterruptedException {
        FxTestHelper.initFx();
    }

    // -------------------------------------------------------------------------
    // Manual MainWindow stub
    // -------------------------------------------------------------------------

    private static class MainWindowStub implements MainWindow {
        String lastTitle;
        Node lastScene;
        Exception lastError;

        @Override public void setTitle(String title)  { lastTitle = title; }
        @Override public void setMainScene(Node n)    { lastScene = n; }
        @Override public void showError(Exception e)  { lastError = e; }
    }

    private MainWindowStub mainWindow;

    @BeforeEach
    void setUp() {
        mainWindow = new MainWindowStub();
    }

    // -------------------------------------------------------------------------
    // Stub helpers
    // -------------------------------------------------------------------------

    private static final List<Category> SOME_CATEGORIES = List.of(
            new Category(1, "Beverages", ""),
            new Category(2, "Frozen", "")
    );

    /** Returns a CategoryRepository stub whose all() returns the given list. */
    private CategoryRepository categoryRepoReturning(List<Category> categories) {
        return new CategoryRepository(null) {
            @Override public List<Category> all() { return categories; }
        };
    }

    // -------------------------------------------------------------------------
    // showProducts
    // -------------------------------------------------------------------------

    @Test
    void showProducts_setsTitle() {
        var repo = new ProductRepository(null) {
            @Override public List<Product> all() { return List.of(); }
        };
        var controller = new ProductController(mainWindow, repo, categoryRepoReturning(SOME_CATEGORIES));

        controller.showProducts();

        assertThat(mainWindow.lastTitle).isEqualTo("Products");
    }

    @Test
    void showProducts_whenRepoThrows_callsShowError() {
        var expectedException = new DataAccessException("db error", null);
        var repo = new ProductRepository(null) {
            @Override public List<Product> all() throws DataAccessException { throw expectedException; }
        };
        var controller = new ProductController(mainWindow, repo, categoryRepoReturning(SOME_CATEGORIES));

        controller.showProducts();

        assertThat(mainWindow.lastError).isSameAs(expectedException);
    }

    // -------------------------------------------------------------------------
    // createProduct
    // -------------------------------------------------------------------------

    @Test
    void createProduct_withValidData_callsRepoCreate() {
        var createCalled = new AtomicBoolean(false);
        var repo = new ProductRepository(null) {
            @Override public int create(ProductData.Validated p) throws DataAccessException {
                createCalled.set(true);
                return 42;
            }
        };
        var controller = new ProductController(mainWindow, repo, categoryRepoReturning(SOME_CATEGORIES));

        controller.createProduct(new ProductData.Unvalidated("Widget", 1, 9.99, 5, false));

        assertThat(createCalled.get()).isTrue();
    }

    @Test
    void createProduct_withInvalidData_doesNotCallRepoCreate() {
        var createCalled = new AtomicBoolean(false);
        var repo = new ProductRepository(null) {
            @Override public int create(ProductData.Validated p) throws DataAccessException {
                createCalled.set(true);
                return 99;
            }
        };
        var controller = new ProductController(mainWindow, repo, categoryRepoReturning(SOME_CATEGORIES));

        // blank name → validation failure
        controller.createProduct(new ProductData.Unvalidated("", 1, 9.99, 5, false));

        assertThat(createCalled.get()).isFalse();
        assertThat(mainWindow.lastTitle).isEqualTo("New Product");
    }

    // -------------------------------------------------------------------------
    // updateProduct
    // -------------------------------------------------------------------------

    @Test
    void updateProduct_withValidData_callsRepoUpdate() {
        var updatedId = new AtomicInteger(-1);
        var repo = new ProductRepository(null) {
            @Override public void update(int productId, ProductData.Validated p) {
                updatedId.set(productId);
            }
        };
        var controller = new ProductController(mainWindow, repo, categoryRepoReturning(SOME_CATEGORIES));

        controller.updateProduct(10, new ProductData.Unvalidated("Widget", 1, 9.99, 5, false));

        assertThat(updatedId.get()).isEqualTo(10);
    }

    @Test
    void updateProduct_withInvalidData_doesNotCallRepoUpdate() {
        var updateCalled = new AtomicBoolean(false);
        var repo = new ProductRepository(null) {
            @Override public void update(int productId, ProductData.Validated p) {
                updateCalled.set(true);
            }
        };
        var controller = new ProductController(mainWindow, repo, categoryRepoReturning(SOME_CATEGORIES));

        // blank name → validation failure
        controller.updateProduct(10, new ProductData.Unvalidated("", 1, 9.99, 5, false));

        assertThat(updateCalled.get()).isFalse();
    }

    // -------------------------------------------------------------------------
    // deleteProduct
    // -------------------------------------------------------------------------

    @Test
    void deleteProduct_callsRepoDelete() {
        var deletedId = new AtomicInteger(-1);
        var repo = new ProductRepository(null) {
            @Override public void delete(int productId) { deletedId.set(productId); }
            @Override public List<Product> all() { return List.of(); }
        };
        var controller = new ProductController(mainWindow, repo, categoryRepoReturning(SOME_CATEGORIES));

        controller.deleteProduct(7);

        assertThat(deletedId.get()).isEqualTo(7);
    }

    @Test
    void deleteProduct_thenShowsProducts() {
        var repo = new ProductRepository(null) {
            @Override public void delete(int productId) { /* no-op */ }
            @Override public List<Product> all() { return List.of(); }
        };
        var controller = new ProductController(mainWindow, repo, categoryRepoReturning(SOME_CATEGORIES));

        controller.deleteProduct(7);

        assertThat(mainWindow.lastTitle).isEqualTo("Products");
    }
}

