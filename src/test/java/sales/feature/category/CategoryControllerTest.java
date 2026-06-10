package sales.feature.category;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sales.feature.base.ui.MainWindow;
import sales.testsupport.FxTestHelper;

public class CategoryControllerTest {

    @BeforeAll
    static void initFx() throws InterruptedException {
        FxTestHelper.initFx();
    }

    private static class MainWindowStub implements MainWindow {
        String lastTitle;
        Exception lastError;

        // TODO: Complete the implementation of this 'mock' MainWindow (see the corresponding stub in ProductControllerTest
        @Override
        public void setTitle(String title) {
        }

        @Override
        public void setMainScene(javafx.scene.Node n) {
        }

        @Override
        public void showError(Exception e) {
        }
    }

    // TODO: Before each test runs, create a MainWindowStub that can be used by the test

    @Test
    void showCategories_setsTitle() {
        // TODO: Verify that the controller sets the expected title on the MainWindow when showing the Categories view
    }

    @Test
    void createCategory_withValidData_callsRepoCreate() {
        // TODO: verify that the controller calls the 'create' method on the repo when valid data is provided
    }
}