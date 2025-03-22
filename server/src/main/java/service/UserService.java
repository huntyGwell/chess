package service;

import dataAccess.UserDAO;
import dataAccess.AuthDAO;
import model.UserData;
import model.AuthData;
import java.util.UUID;

public class UserService {
    UserDAO userDAO;
    AuthDAO authDAO;

    public AuthData register(UserData user) throws Exception {
        if (userDAO.getUser(user.username()) != null) {
            throw new Exception("User already exists");
        }
        userDAO.createUser(user);
        String token = UUID.randomUUID().toString();
        AuthData authData = new AuthData(token, user.username());
        authDAO.addAuthData(authData);

        return authData;
    }

    public AuthData login(String username, String password) throws Exception {
        UserData user = userDAO.getUser(username);
        if (user == null || !user.password().equals(password)) {
            throw new Exception("Invalid credentials");
        }
        String token = UUID.randomUUID().toString();
        AuthData authData = new AuthData(token, username);
        authDAO.addAuthData(authData);
        return authData;
    }

    public void logout(String authToken) throws Exception {
        if (authDAO.getAuthData(authToken) == null) {
            throw new Exception("Unauthorized");
        }
        authDAO.removeAuthData(authToken);
    }
}
