package dataaccess;

import exception.ResponseException;
import model.AuthData;

import java.sql.SQLException;

public interface AuthDAO {
    void createAuth(AuthData authData) throws DataAccessException, ResponseException, SQLException;

    AuthData getAuth(String authToken) throws DataAccessException;

    String getUsername(String authToken) throws DataAccessException;

    void deleteAuth(AuthData authData) throws DataAccessException, ResponseException, SQLException;

    void deleteAllAuths() throws DataAccessException, ResponseException, SQLException;
}
