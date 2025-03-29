package service;

import dataaccess.*;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

public class GameServiceTest {
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private GameService gameService;

    @BeforeEach
    public void setUp() {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        gameService = new GameService(gameDAO, authDAO);
    }

    //list success
    @Test
    public void testListGames() {
        String auth = UUID.randomUUID().toString();
        try {
            gameDAO.createGame("game1");
            authDAO.createAuth(new AuthData(auth, "user1"));
            gameService.listgames(auth);
        } catch (ResponseException | DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertDoesNotThrow(() -> gameService.listgames(auth));
    }

    //list fail
    @Test
    public void testListGamesFail() {
        String auth = UUID.randomUUID().toString();
        try {
            gameDAO.createGame("game1");
            gameDAO.createGame("game2");
            gameDAO.createGame("game3");
            authDAO.createAuth(new AuthData(auth, "user1"));
            gameService.listgames(auth);
        } catch (ResponseException | DataAccessException | SQLException e) {
            Assertions.assertThrows(ResponseException.class, () -> gameService.listgames(auth));
        }
    }

    //create success
    @Test
    public void testCreateGame() {
        String auth = UUID.randomUUID().toString();
        String gameName = "game1";
        try {
            authDAO.createAuth(new AuthData(auth, "user1"));
        } catch (DataAccessException | ResponseException | SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertDoesNotThrow(() -> gameService.creategame(auth, gameName));
    }

    //creat fail
    @Test
    public void testCreateGameFail() {
        String auth = UUID.randomUUID().toString();
        String gameName = "game!";
        Assertions.assertThrows(ResponseException.class, () -> gameService.creategame(auth, gameName));
    }

    //join success
    @Test
    public void testJoinGame() {
        String auth = UUID.randomUUID().toString();
        int id;
        JoinData join = null;
        try {
            authDAO.createAuth(new AuthData(auth, "user1"));
            gameService.creategame(auth, "gameygame");
            Collection<GameData> games = gameDAO.listGames();
            for (GameData gameData : games) {
                if (gameData.gameName().equals("gameygame")) {
                    id = gameData.gameID();
                    join = new JoinData("BLACK", id);
                    break;
                }
            }
        } catch (ResponseException | DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        JoinData finalJoin = join;
        Assertions.assertDoesNotThrow(() -> gameService.joingame(auth, finalJoin));
    }

    //join fail
    @Test
    public void testJoinGameFail() {
        String auth = UUID.randomUUID().toString();
        JoinData join = null;
        GameData game;
        try {
            authDAO.createAuth(new AuthData(auth, "user1"));
            gameDAO.createGame("gameygame");
            join = new JoinData("BLACK", 1);
            gameService.joingame(auth, join);
        } catch (ResponseException | DataAccessException | SQLException e) {
            JoinData finalJoin = join;
            Assertions.assertThrows(ResponseException.class, () -> gameService.joingame(auth, finalJoin));
        }
    }

    //clear success
    @Test
    public void testClearGames() {
        String auth = UUID.randomUUID().toString();
        String gameName = "game1";
        try {
            authDAO.createAuth(new AuthData(auth, "user1"));
            gameService.creategame(auth, gameName);
        } catch (DataAccessException | ResponseException | SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertDoesNotThrow(() -> gameService.clearGames());
    }
}
