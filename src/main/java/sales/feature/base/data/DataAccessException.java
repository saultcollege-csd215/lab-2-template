package sales.feature.base.data;

/**
 * This class allows us to keep a strict boundary between code that interacts directly with database/SQL
 * and code that does not.
 * Wrapping SQLExceptions in this error prevent SQLExceptions from leaking out of the data layer
 * and allows us to handle them in a more general way.
 */public class DataAccessException extends Exception {
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
