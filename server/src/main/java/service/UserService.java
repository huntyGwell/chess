package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import exception.ResponseException;

import java.sql.SQLException;
import java.util.UUID;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData user) throws ResponseException, DataAccessException, SQLException {
        if (user.username() == null || user.password() == null) {
            DataAccessException e = new DataAccessException("Error: bad request");
            ResponseException r = new ResponseException(400, e.getMessage());
            throw r;
        }
        else if (userDAO.getUser(user.username()) == null) {
            userDAO.createUser(user);
        }
        else if (userDAO.getUser(user.username()) != null) {
            DataAccessException e = new DataAccessException("Error: already taken");
            ResponseException r = new ResponseException(403, e.getMessage());
            throw r;
        }
        AuthData authData = new AuthData(UUID.randomUUID().toString(), user.username());
        authDAO.createAuth(authData);
        return authData;
    }

    public AuthData login(UserData user) throws ResponseException, DataAccessException, SQLException {
        if (userDAO.getUser(user.username()) == null ||
                !userDAO.verifyPassword(user.username(), user.password())) { //figure out how to match the password
            DataAccessException e = new DataAccessException("Error: unauthorized");
            ResponseException r = new ResponseException(401, e.getMessage());
            throw r;
        }
        AuthData authData = new AuthData(UUID.randomUUID().toString(), user.username());
        authDAO.createAuth(authData);
        return authData;
    }

    public void logout(String auth) throws ResponseException, DataAccessException, SQLException {
        if (authDAO.getAuth(auth) == null) {
            DataAccessException e = new DataAccessException("Error: unauthorized");
            ResponseException r = new ResponseException(401, e.getMessage());
            throw r;
        }
        authDAO.deleteAuth(authDAO.getAuth(auth));
    }

    public void clearUsers() throws ResponseException, DataAccessException, SQLException {
        userDAO.deleteAllUsers();
        authDAO.deleteAllAuths();
    }
}
