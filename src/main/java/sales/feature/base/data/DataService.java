package sales.feature.base.data;

import sales.feature.category.CategoryRepository;
import sales.feature.product.ProductRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides access to data repositories that connect to the underlying database
 */
public class DataService {

    private static final Logger logger = Logger.getLogger(DataService.class.getName());

    /**
     * A JDBC Database connection object
     */
    private final Connection connection;

    /**
     * The Product repository
     */
    private final ProductRepository productRepository;

    /**
     * The Category repository
     */
    private final CategoryRepository categoryRepository;

    public DataService(String connectionString) throws DataAccessException {
        try {
            this.connection = DriverManager.getConnection(connectionString);
            this.productRepository = new ProductRepository(connection);
            this.categoryRepository = new CategoryRepository(connection);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not connect to database.", e);
            throw new DataAccessException("Could not connect to database.", e);
        }
    }

    /**
     * Closes the database connection
     */
    public void stop() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not close database connection.", e);
        }


    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }
}
