package dataaccess;

import exception.ResponseException;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.sql.SQLException;

public interface UserDAO {
    void createUser(UserData userData) throws DataAccessException, ResponseException, SQLException;

    UserData getUser(String username) throws DataAccessException, ResponseException, SQLException;

    void deleteAllUsers() throws DataAccessException, ResponseException, SQLException;

    boolean verifyPassword(String username, String password) throws DataAccessException, ResponseException, SQLException;
}
