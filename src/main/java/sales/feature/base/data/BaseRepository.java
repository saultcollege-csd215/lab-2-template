package sales.feature.base.data;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A base class containing elements common to all Repository classes
 */
public class BaseRepository {

    /**
     * All repositories hold a reference to a JDBC Connection object
     */
    protected final Connection connection;

    public BaseRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * @return The id of the last item inserted into the database.
     * @throws SQLException If an SQL exception occurs
     */
    protected int getLastInsertId() throws SQLException {
        var statement = connection.prepareStatement("SELECT last_insert_rowid() AS LastID");
        var resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt("LastID");
    }

}
