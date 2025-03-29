package dataaccess;

import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class MySQLUserDAOTests {
    UserDAO userDAO;
    UserData userData;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException, ResponseException {
        DatabaseManager.createDatabase();
        userDAO = new MySQLUserDAO();
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE user")) {
                statement.executeUpdate();
            }
        }

        userData = new UserData("csStudent", "yippe!", "csStudent@byu.edu");
    }

    @AfterEach
    void tearDown() throws SQLException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE user")) {
                statement.executeUpdate();
            }
        }
    }

    //create
    @Test
    public void testCreateUser() {
        UserData user;
        try {
            userDAO.createUser(userData);
            user = userDAO.getUser(userData.username());
        } catch (DataAccessException | ResponseException | SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(userData.username(), user.username());
        //Assertions.assertTrue(verifyPassword(defaultUser.password(), user.password()));
        Assertions.assertEquals(userData.email(), user.email());
    }

    @Test
    public void testCreateUserFail() {
        UserData user;
        try {
            userDAO.createUser(userData);
            Assertions.assertThrows(ResponseException.class, () -> {userDAO.createUser(userData);});
        } catch (DataAccessException | ResponseException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //get
    @Test
    public void testGetUser() {
        try {
            userDAO.createUser(userData);
        } catch (DataAccessException | ResponseException | SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertDoesNotThrow(() -> {userDAO.getUser(userData.username());});
    }

    @Test
    public void testGetUserFail() {
        try {
            Assertions.assertNull(userDAO.getUser(userData.username()));
        } catch (DataAccessException | ResponseException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //delete all
    @Test
    public void testDeleteAllUsers() {
        try {
            userDAO.createUser(userData);
        } catch (DataAccessException | ResponseException | SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertDoesNotThrow(() -> {userDAO.deleteAllUsers();});
    }
}
