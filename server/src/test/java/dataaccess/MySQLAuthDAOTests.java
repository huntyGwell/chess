package dataaccess;

import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.UUID;

public class MySQLAuthDAOTests {
    AuthDAO authDAO;
    AuthData authData;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException, ResponseException {
        DatabaseManager.createDatabase();
        authDAO = new MySQLAuthDAO();
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE auth")) {
                statement.executeUpdate();
            }
        }

        authData = new AuthData(UUID.randomUUID().toString(), "testUser");
    }

    @AfterEach
    void tearDown() throws SQLException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE auth")) {
                statement.executeUpdate();
            }
        }
    }

    //create
    @Test
    public void testCreatAuth() {
        Assertions.assertDoesNotThrow(() -> authDAO.createAuth(authData));
    }

    @Test
    public void testCreateAuthFail() {
        try {
            authDAO.createAuth(authData);
            Assertions.assertThrows(ResponseException.class, () -> {authDAO.createAuth(authData);});
        } catch (ResponseException | DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //get
    @Test
    public void testGetAuth() {
        try {
            authDAO.createAuth(authData);
        } catch (ResponseException | DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertDoesNotThrow(() -> authDAO.getAuth(authData.authToken()));
    }

    @Test
    public void testGetAuthFail() {
        try {
            Assertions.assertNull(authDAO.getAuth(authData.authToken()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetUsername() {
        try {
            authDAO.createAuth(authData);
        } catch (ResponseException | DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertDoesNotThrow(() -> authDAO.getUsername(authData.authToken()));
    }

    @Test
    public void testGetUsernameFail() {
        try {
            Assertions.assertNull(authDAO.getUsername(authData.authToken()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    //delete
    @Test
    public void testDeleteAllAuth() {
        try {
            authDAO.createAuth(authData);
        } catch (ResponseException | DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertDoesNotThrow(() -> authDAO.deleteAllAuths());
    }

    @Test
    public void testDeleteAuth() {
        try {
            authDAO.createAuth(authData);
        } catch (ResponseException | DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertDoesNotThrow(() -> authDAO.deleteAuth(authData));
    }
}
