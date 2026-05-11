package sales.controllers;

import sales.data.DataAccessException;

@FunctionalInterface
public interface DataAction {
    void run() throws DataAccessException;
}
