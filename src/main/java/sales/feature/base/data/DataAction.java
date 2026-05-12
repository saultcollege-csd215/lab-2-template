package sales.feature.base.data;

@FunctionalInterface
public interface DataAction {
    void run() throws DataAccessException;
}
