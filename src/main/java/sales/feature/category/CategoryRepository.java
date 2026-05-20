package sales.feature.category;

import sales.core.Category;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import sales.feature.category.validation.CategoryData;
import sales.feature.base.data.BaseRepository;
import sales.feature.base.data.DataAccessException;

/**
 * An abstraction for accessing and manipulating Category data in the database.
 * All database access code related to Categories should be contained in this class.
 */
public class CategoryRepository extends BaseRepository {

    private static final Logger logger = Logger.getLogger(CategoryRepository.class.getName());

    public CategoryRepository(Connection connection) {
        super(connection);
    }

    /**
     * @return A list of all Categories in the database
     * @throws DataAccessException  If an SQLException occurs
     */
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

    /**
     * @return A list of the names of all categories in the database
     * @throws DataAccessException  If an SQLException occurs
     */
    public List<String> allCategoryNames() throws DataAccessException {
        return allCategoryNames(-1);
    }

    /**
     * Returns a list of the names of all categories in the database except the category with the given id.
     * This is useful for validating that a category name is unique when updating an existing category.
     * @param exceptThisCategoryId The id to exclude in the returned list
     * @return The list of all category names minus the 'exceptThisCategoryId' category name
     * @throws DataAccessException If an SQLException occurs
     */
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

    /**
     * @param categoryId The id of the category to look up
     * @return The number of products associated with the given category id
     * @throws DataAccessException  If an SQLException occurs
     */
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

    /**
     * Create a new category row for the given validated category data
     * @param c The validated category data
     * @return The created Category with its new id
     * @throws DataAccessException  If an SQLException occurs
     */
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

    /**
     * Updates a category with the given validated data
     * @param categoryId The id of the category to update
     * @param c The validated category data
     * @return The updated Category data
     * @throws DataAccessException  If an SQLException occurs
     */
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

    /**
     * Delete a category
     * @param categoryId The id of the category to delete
     * @throws DataAccessException  If an SQLException occurs
     */
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
