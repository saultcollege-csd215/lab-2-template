package sales.data;

import sales.core.Category;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import sales.core.validation.CategoryData;

public class CategoryRepository extends BaseRepository {

    private static final Logger logger = Logger.getLogger(CategoryRepository.class.getName());

    public CategoryRepository(Connection connection) {
        super(connection);
    }

    public List<Category> all() throws DataAccessException {

        try {
            // Parameterized query
            var statement = connection.prepareStatement(
                    """
                            SELECT c.CategoryID, c.CategoryName, c.Description
                            FROM Categories c
                            """
            );

            var resultSet = statement.executeQuery();
            var categories = new ArrayList<Category>();
            while (resultSet.next()) {
                var product = new Category(
                        resultSet.getInt("CategoryID"),
                        resultSet.getString("CategoryName"),
                        resultSet.getString("Description")
                );
                categories.add(product);
            }
            return categories;

        } catch (SQLException e ) {
            logger.log(Level.SEVERE, "Error getting all categories", e);
            throw new DataAccessException("Error getting all categories", e);
        }
    }

    public List<String> allCategoryNames() throws DataAccessException {
        return allCategoryNames(-1);
    }
    public List<String> allCategoryNames(int exceptThisCategoryId) throws DataAccessException {
        try {
            var statement = connection.prepareStatement(
                    """
                            SELECT c.CategoryName
                            FROM Categories c
                            WHERE c.CategoryID <> ?
                            """
            );

            statement.setInt(1, exceptThisCategoryId);
            var resultSet = statement.executeQuery();
            var categoryNames = new ArrayList<String>();
            while (resultSet.next()) {
                categoryNames.add(resultSet.getString("CategoryName"));
            }
            return categoryNames;

        } catch (SQLException e ) {
            logger.log(Level.SEVERE, "Error getting category names", e);
            throw new DataAccessException("Error getting category names", e);
        }
    }

    public int countProductsInCategory(int categoryId) throws DataAccessException {
        try {
            var statement = connection.prepareStatement(
                    """
                        SELECT COUNT(*) AS ProductCount
                        FROM Products
                        WHERE CategoryID = ?
                        """
            );
            statement.setInt(1, categoryId);

            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("ProductCount");
            } else {
                return 0;
            }

        } catch (SQLException e ) {
            logger.log(Level.SEVERE, "Error getting product count", e);
            throw new DataAccessException("Error getting product count", e);
        }
    }

    public Category create(CategoryData.Validated c) throws DataAccessException {
        try{
            var statement = connection.prepareStatement(
                    """
                        INSERT INTO Categories(CategoryName, Description) VALUES (?, ?)
                        """
            );
            statement.setString(1, c.name());
            statement.setString(2, c.description());
            statement.execute();

            return new Category(
                    getLastInsertId(),
                    c.name(),
                    c.description()
            );

        } catch (SQLException e ) {
            logger.log(Level.SEVERE, "Error creating category", e);
            throw new DataAccessException("Error creating category", e);
        }
    }

    public Category update(int categoryId, CategoryData.Validated c) throws DataAccessException {
        try {
            var statement = connection.prepareStatement(
                    """
                        UPDATE Categories
                        SET CategoryName = ?, Description = ?
                        WHERE CategoryID = ?
                        """
            );
            statement.setString(1, c.name());
            statement.setString(2, c.description());
            statement.setInt(3, categoryId);

            statement.executeUpdate();

            return new Category(categoryId, c.name(), c.description());

        } catch (SQLException e ) {
            logger.log(Level.SEVERE, "Error updating category", e);
            throw new DataAccessException("Error updating category", e);
        }

    }

    public void delete(int categoryId) throws DataAccessException{
        try {
            var statement = connection.prepareStatement(
                    """
                        DELETE FROM Categories
                        WHERE CategoryID = ?
                        """
            );
            statement.setInt(1, categoryId);

            statement.executeUpdate();

        } catch (SQLException e ) {
            logger.log(Level.SEVERE, "Error deleting category", e);
            throw new DataAccessException("Error deleting category", e);
        }

    }
}
