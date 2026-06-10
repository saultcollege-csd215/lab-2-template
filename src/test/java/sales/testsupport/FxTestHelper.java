package sales.testsupport;

import javafx.application.Platform;
import javafx.scene.control.Cell;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test harness utility for running direct JavaFX node/event tests on the FX thread
 * without any TestFX dependency.
 */
public class FxTestHelper {

    private static volatile boolean fxInitialized = false;

    /**
     * Initializes the JavaFX toolkit once per JVM.
     * Safe to call from multiple test classes via @BeforeAll.
     */
    public static synchronized void initFx() throws InterruptedException {
        if (fxInitialized) return;
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException e) {
            // Toolkit was already started (e.g. by another test class)
            latch.countDown();
        }
        assertTrue(latch.await(5, TimeUnit.SECONDS), "JavaFX toolkit did not start within 5 seconds");
        fxInitialized = true;
    }

    /**
     * Runs the given action on the JavaFX Application Thread and waits for it to complete.
     * Any exception thrown by the action is re-thrown on the calling thread.
     *
     * @param action The action to run on the FX thread
     * @throws Exception if the action throws, or the latch times out
     */
    public static void runOnFxThread(FxRunnable action) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> error = new AtomicReference<>();
        Platform.runLater(() -> {
            try {
                action.run();
            } catch (Throwable t) {
                error.set(t);
            } finally {
                latch.countDown();
            }
        });
        assertTrue(latch.await(10, TimeUnit.SECONDS), "FX thread action timed out after 10 seconds");
        if (error.get() != null) {
            throw new RuntimeException("Exception on FX thread: " + error.get().getMessage(), error.get());
        }
    }

    /**
     * Simulates a double-click on a TableRow by creating a row via the table's row factory,
     * injecting the item via reflection on the protected {@code Cell.updateItem}, and firing
     * a synthetic double-click {@link MouseEvent}.
     *
     * <p>Requires the JVM to have been launched with:
     * {@code --add-opens javafx.controls/javafx.scene.control=ALL-UNNAMED}</p>
     *
     * @param table The TableView whose row factory creates rows
     * @param item  The item to place in the row
     * @param <T>   The type of the table row items
     * @throws Exception if reflection setup fails
     */
    public static <T> void doubleClickTableRow(TableView<T> table, T item) throws Exception {
        var rowFactory = table.getRowFactory();
        var row = rowFactory.call(table);

        // Cell.updateItem(T item, boolean empty) is protected; use reflection to call it
        Method updateItem = Cell.class.getDeclaredMethod("updateItem", Object.class, boolean.class);
        updateItem.setAccessible(true);
        updateItem.invoke(row, item, false);  // false = not empty

        MouseEvent doubleClick = new MouseEvent(
                MouseEvent.MOUSE_CLICKED,
                0, 0, 0, 0,
                MouseButton.PRIMARY,
                2,          // clickCount = 2
                false, false, false, false,
                true, false, false, true,
                false, false, null
        );
        row.fireEvent(doubleClick);
    }

    /**
     * A functional interface for actions that run on the JavaFX thread.
     * Allows checked exceptions so test setup code can be concise.
     */
    @FunctionalInterface
    public interface FxRunnable {
        void run() throws Exception;
    }
}




