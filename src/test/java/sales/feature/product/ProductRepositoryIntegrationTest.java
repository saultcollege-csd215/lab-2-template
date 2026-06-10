package sales.feature.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sales.feature.base.data.DataService;
import sales.feature.product.validation.ProductData;
import sales.testsupport.TestDatabase;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRepositoryIntegrationTest {

    private Path testDbPath;
    private DataService dataService;
    private ProductRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        testDbPath = TestDatabase.copyNorthwind();
        dataService = new DataService(TestDatabase.sqliteConnectionString(testDbPath));
        repository = dataService.getProductRepository();
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
    void all_returnsProductsFromSeedDatabase() throws Exception {
        var products = repository.all();

        assertThat(products).isNotEmpty();
        assertThat(products)
                .allSatisfy(product -> {
                    assertThat(product.id()).isPositive();
                    assertThat(product.name()).isNotBlank();
                    assertThat(product.category()).isNotNull();
                    assertThat(product.category().id()).isPositive();
                });
    }

    @Test
    void createUpdateDelete_roundTripPersistsExpectedValues() throws Exception {
        var seed = System.nanoTime();
        var createdId = repository.create(new ProductData.Validated("IT Product " + seed, 1, 9.99, 12, false));

        assertThat(createdId).isPositive();
        assertThat(repository.all())
                .filteredOn(p -> p.id() == createdId)
                .singleElement()
                .satisfies(p -> {
                    assertThat(p.name()).isEqualTo("IT Product " + seed);
                    assertThat(p.category().id()).isEqualTo(1);
                    assertThat(p.price()).isEqualTo(9.99);
                    assertThat(p.unitsInStock()).isEqualTo(12);
                    assertThat(p.discontinued()).isFalse();
                });

        repository.update(createdId, new ProductData.Validated("IT Updated Product " + seed, 1, 11.49, 5, true));

        assertThat(repository.all())
                .filteredOn(p -> p.id() == createdId)
                .singleElement()
                .satisfies(p -> {
                    assertThat(p.name()).isEqualTo("IT Updated Product " + seed);
                    assertThat(p.category().id()).isEqualTo(1);
                    assertThat(p.price()).isEqualTo(11.49);
                    assertThat(p.unitsInStock()).isEqualTo(5);
                    assertThat(p.discontinued()).isTrue();
                });

        repository.delete(createdId);

        assertThat(repository.all())
                .noneMatch(p -> p.id() == createdId);
    }
}

