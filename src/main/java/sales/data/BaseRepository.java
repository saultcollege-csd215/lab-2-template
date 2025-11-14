package sales.data;

import java.sql.Connection;
import java.sql.SQLException;

public class BaseRepository {

    protected final Connection connection;

    public BaseRepository(Connection connection) {
        this.connection = connection;
    }

    protected int getLastInsertId() throws SQLException {
        var statement = connection.prepareStatement("SELECT last_insert_rowid() AS LastID");
        var resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt("LastID");
    }

}
