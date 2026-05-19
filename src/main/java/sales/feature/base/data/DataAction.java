package sales.feature.base.data;

/**
 * A functional interface that allows us to create lambda expressions that may throw DataAccessExceptions
 * See {@link sales.feature.base.controller.BaseController#accessDataOrShowError}
 */
@FunctionalInterface
public interface DataAction {
    void run() throws DataAccessException;
}
