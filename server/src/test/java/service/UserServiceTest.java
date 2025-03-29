package service;

import dataaccess.*;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.UUID;

public class UserServiceTest {

    private UserService userService;
    private UserDAO userDAO;
    private AuthDAO authDAO;
    //private AuthData expected;

    @BeforeEach
    public void setUp() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    //test register
    @Test
    public void testRegister() {
        UserData user = new UserData("csstudent", "schoolisgreat", "cs@gmail.com");
        try {
            userService.register(user);
        } catch (ResponseException | DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //register fail
    @Test
    public void testRegisterFail() {
        UserData user = new UserData("csstudent", "schoolisgreat", "cs@gmail.com");
        try {
            userService.register(user);
        } catch (ResponseException | DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(ResponseException.class, () -> userService.register(user));
    }

    //login success
    @Test
    public void testLogin() {
        UserData user = new UserData("csstudent", "schoolisgreat", "cs@gmail.com");
        try {
            userService.register(user);
        } catch (ResponseException | DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertDoesNotThrow(() -> userService.login(user));
    }

    //login fail
    @Test
    public void testLoginFail() {
        UserData user = new UserData("csstudent", "schoolisgreat", "cs@gmail.com");
        Assertions.assertThrows(ResponseException.class, () -> userService.login(user));
    }

    //logout success
    @Test
    public void testLogout() {
        UserData user = new UserData("csstudent", "schoolisgreat", "cs@gmail.com");
        AuthData authData;
        try {
            authData = userService.register(user);
        } catch (ResponseException | DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertDoesNotThrow(() -> userService.logout(authData.authToken()));
    }

    //logout fail
    @Test
    public void testLogoutFail() throws SQLException {
        UserData user = new UserData("csstudent", "schoolisgreat", "cs@gmail.com");
        AuthData authData;
        try {
            authData = userService.register(user);
        } catch (ResponseException | DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            userService.logout(authData.authToken());
        } catch (ResponseException | DataAccessException e) {
            Assertions.assertThrows(ResponseException.class, () -> userService.logout(authData.authToken()));
        }
    }

    //clear success
    @Test
    public void testClearUser() {
        UserData user = new UserData("csstudent", "schoolisgreat", "cs@gmail.com");
        AuthData auth = new AuthData(UUID.randomUUID().toString(), "csstudent");
        try {
            userService.register(user);
            authDAO.createAuth(auth);
        } catch (ResponseException | DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertDoesNotThrow(() -> userService.clearUsers());
    }
}
