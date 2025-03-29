package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class MySQLGameDAOTests {
    GameDAO gameDAO;
    int gameID;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException, ResponseException {
        DatabaseManager.createDatabase();
        gameDAO = new MySQLGameDAO();
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE game")) {
                statement.executeUpdate();
            }
        }
    }

    @AfterEach
    void tearDown() throws SQLException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE game")) {
                statement.executeUpdate();
            }
        }
    }

    //create
    @Test
    public void testCreatGame() {
        Assertions.assertDoesNotThrow(() -> {gameDAO.createGame("new game");});
    }

    @Test
    public void testCreatGameFail() {
        try {
            gameDAO.createGame("game name");
        } catch (ResponseException | DataAccessException | SQLException e) {
            Assertions.assertThrows(ResponseException.class, () -> {gameDAO.createGame("");});
        }
    }

    //get game
    @Test
    public void testGetGame() {
        try {
            gameID = gameDAO.createGame("game name");
        } catch (ResponseException | DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertDoesNotThrow(() -> {gameDAO.getGame(gameID);});
    }

    @Test
    public void testGetGameFail() {
        GameData gameData;
        try {
            gameDAO.createGame("game name");
            gameData = gameDAO.getGame(0);
        } catch (ResponseException | DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertNull(gameData);
    }

    //list
    @Test
    public void testListGames() {
        try {
            gameDAO.createGame("game name");
        } catch (ResponseException | DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertDoesNotThrow(() -> {gameDAO.listGames();});
    }

    //update
    @Test
    public void testUpdateGame() {
        try {
            gameID = gameDAO.createGame("game name");
            gameDAO.updateGame("WHITE", gameID, "whiteUser");
            Assertions.assertNotNull(gameDAO.getGame(gameID).whiteUsername());
        } catch (ResponseException | DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testUpdateGameFail() {
        try {
            gameID = gameDAO.createGame("game name");
            gameDAO.updateGame("WHITE", gameID, "whiteUser");
        } catch (ResponseException | DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(SQLException.class, () -> {gameDAO.updateGame("WHITE", gameID, "whiteUser");});
    }

    //clear
    @Test
    public void testClearGames() {
        try {
            gameDAO.createGame("game name");
        } catch (ResponseException | DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertDoesNotThrow(() -> {gameDAO.deleteAllGames();});
    }
}
