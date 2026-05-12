package sales.feature.base.controller;

import sales.feature.base.data.DataAccessException;

@FunctionalInterface
public interface DataAction {
    void run() throws DataAccessException;
}
