package sales.testsupport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Creates throwaway SQLite database files for integration tests.
 */
public final class TestDatabase {

    private TestDatabase() {
    }

    public static Path copyNorthwind() throws IOException {
        var sourceDb = Path.of("northwind.db").toAbsolutePath();
        var tempDb = Files.createTempFile("northwind-test-", ".db");
        Files.copy(sourceDb, tempDb, StandardCopyOption.REPLACE_EXISTING);
        return tempDb;
    }

    public static String sqliteConnectionString(Path dbPath) {
        return "jdbc:sqlite:" + dbPath.toAbsolutePath();
    }
}

