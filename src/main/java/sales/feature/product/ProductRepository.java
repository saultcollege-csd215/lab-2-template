package sales.feature.product;

import sales.core.Category;
import sales.core.Product;
import sales.feature.product.validation.ProductData;
import sales.feature.base.data.BaseRepository;
import sales.feature.base.data.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A layer of abstraction between the underlying database and the app core.
 * The app retrieves data through the repository, thereby isolating itself from a specific
 * database.  I.e. *ALL* SQL and database-specific code is in Repository classes, and NOWHERE ELSE.
 */
public class ProductRepository extends BaseRepository {

    private static final Logger logger = Logger.getLogger(ProductRepository.class.getName());

    /**
     * @param connection The JDBC connection object for the specific database
     */
    public ProductRepository(Connection connection) {
        super(connection);
    }

    /**
     * @return The list of all Products in the database
     * @throws DataAccessException If an SQLException occurs
     */
    public List<Product> all() throws DataAccessException {

        try {
            // Parameterized query
            var statement = connection.prepareStatement(
                    """
                        SELECT p.ProductID, p.ProductName, p.CategoryID, c.CategoryName, c.Description, p.UnitPrice, p.UnitsInStock, p.Discontinued
                        FROM Products p
                        JOIN Categories c ON p.CategoryID = c.CategoryID
                        """
            );

            var resultSet = statement.executeQuery();
            var products = new ArrayList<Product>();
            while (resultSet.next()) {
                var product = new Product(
                        resultSet.getInt("ProductID"),
                        resultSet.getString("ProductName"),
                        new Category(
                                resultSet.getInt("CategoryID"),
                                resultSet.getString("CategoryName"),
                                resultSet.getString("Description")
                                ),
                        resultSet.getDouble("UnitPrice"),
                        resultSet.getInt("UnitsInStock"),
                        resultSet.getBoolean("Discontinued")
                );
                products.add(product);
            }
            return products;

        } catch (SQLException e ) {
            logger.log(Level.SEVERE, "Error getting products", e);
            throw new DataAccessException("Error getting products", e);
        }
    }

    /**
     * Create a new product row from the given valid product data
     * @param p The valid product data
     * @return The database id of the newly created product
     * @throws DataAccessException If an SQLException occurs
     */
    public int create(ProductData.Validated p) throws DataAccessException{
        try {
            var statement = connection.prepareStatement(
                    """
                        INSERT INTO Products (ProductName, CategoryID, UnitPrice, UnitsInStock, Discontinued)
                        VALUES (?, ?, ?, ?, ?)
                        """
            );

            statement.setString(1, p.name());
            statement.setInt(2, p.categoryId());
            statement.setDouble(3, p.price());
            statement.setInt(4, p.unitsInStock());
            statement.setBoolean(5, p.discontinued());

            statement.executeUpdate();

            return getLastInsertId();

        } catch (SQLException e ) {
            logger.log(Level.SEVERE, "Error creating product", e);
            throw new DataAccessException("Error creating product", e);
        }
    }

    /**
     * Updates the given product with valid product data
     * @param productId The id of the product to update
     * @param p The valid product data
     * @throws DataAccessException If an SQLException occurs
     */
    public void update(int productId, ProductData.Validated p) throws DataAccessException {
        try {
            var statement = connection.prepareStatement(
                    """
                        UPDATE Products
                        SET ProductName = ?, CategoryID = ?, UnitPrice = ?, UnitsInStock = ?, Discontinued = ?
                        WHERE ProductID = ?
                        """
            );

            statement.setString(1, p.name());
            statement.setInt(2, p.categoryId());
            statement.setDouble(3, p.price());
            statement.setInt(4, p.unitsInStock());
            statement.setBoolean(5, p.discontinued());
            statement.setInt(6, productId);

            statement.executeUpdate();

        } catch (SQLException e ) {
            logger.log(Level.SEVERE, "Error updating product", e);
            throw new DataAccessException("Error updating product", e);
        }
    }

    /**
     * Delete the given product from the database
     * @param productId The id of the product to delete
     * @throws DataAccessException  If an SQLException occurs
     */
    public void delete(int productId) throws DataAccessException {
        try {
            var statement = connection.prepareStatement(
                    """
                        DELETE FROM Products
                        WHERE ProductID = ?
                        """
            );

            statement.setInt(1, productId);

            statement.executeUpdate();

        } catch (SQLException e ) {
            logger.log(Level.SEVERE, "Error deleting product", e);
            throw new DataAccessException("Error deleting product", e);
        }
    }
}
