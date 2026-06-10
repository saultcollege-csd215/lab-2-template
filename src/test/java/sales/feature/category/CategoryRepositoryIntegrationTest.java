package sales.feature.category;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sales.feature.base.data.DataService;
import sales.feature.category.validation.CategoryData;
import sales.testsupport.TestDatabase;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryRepositoryIntegrationTest {

    private Path testDbPath;
    private DataService dataService;
    private CategoryRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        // TODO: Initialize the test database and repository here
    }

    @AfterEach
    void tearDown() throws Exception {
        if (dataService != null) {
            dataService.stop();
        }
        if (testDbPath != null) {
            Files.deleteIfExists(testDbPath);
        }
    }

    @Test
    void createUpdateDelete_roundTripPersistsExpectedValues() throws Exception {
        // TODO: test a complete create-update-delete cycle (see the corresponding test function in ProductRepositoryIntegrationTest)
    }

}

