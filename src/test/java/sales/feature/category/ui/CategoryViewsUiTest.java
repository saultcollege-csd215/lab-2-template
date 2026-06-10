package sales.feature.category.ui;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sales.testsupport.FxTestHelper;

class CategoryViewsUiTest {

    @BeforeAll
    static void initFx() throws InterruptedException {
        FxTestHelper.initFx();
    }

    @Test
    void newCategorySaveButton_sendsUserInputToCallback() {
        // TODO: test that the user's entered data is sent to the callback function when the Save button is activated
    }

    @Test
    void categoriesView_tableContainsAllCategoriesFromViewModel() {
        // TODO: test that the Categories view shows all the Categories from the provided view model
    }
}

