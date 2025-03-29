package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exception.ResponseException;
import model.GameData;
import model.JoinData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

public class GameService {

    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public Collection<GameData> listgames(String auth) throws ResponseException, DataAccessException {
        if (authDAO.getAuth(auth) == null) {
            DataAccessException e = new DataAccessException("Error: unauthorized");
            ResponseException r = new ResponseException(401, e.getMessage());
            throw r;
        }
        return gameDAO.listGames();
    }

    public int creategame(String auth, String gameName) throws ResponseException, DataAccessException, SQLException {
        if (authDAO.getAuth(auth) == null) {
            DataAccessException e = new DataAccessException("Error: unauthorized");
            ResponseException r = new ResponseException(401, e.getMessage());
            throw r;
        }
        else if (gameName == null) {
            DataAccessException e = new DataAccessException("Error: bad request");
            ResponseException r = new ResponseException(400, e.getMessage());
            throw r;
        }
        return gameDAO.createGame(gameName);
    }

    public void joingame(String auth, JoinData join) throws ResponseException, DataAccessException, SQLException {
        GameData game = gameDAO.getGame(join.gameID());
        if (game == null || join.playerColor() == null) {
            DataAccessException e = new DataAccessException("Error: bad request");
            ResponseException r = new ResponseException(400, e.getMessage());
            throw r;
        }
        else if (authDAO.getAuth(auth) == null) {
            DataAccessException e = new DataAccessException("Error: unauthorized");
            ResponseException r = new ResponseException(401, e.getMessage());
            throw r;
        }
        else if ((join.playerColor().equals("WHITE") && !Objects.equals(game.whiteUsername(), null)) ||
                (join.playerColor().equals("BLACK") && !Objects.equals(game.blackUsername(), null))) {
            DataAccessException e = new DataAccessException("Error: already taken");
            ResponseException r = new ResponseException(403, e.getMessage());
            throw r;
        }
        String username = authDAO.getUsername(auth);
        gameDAO.updateGame(join.playerColor(), join.gameID(), username);
    }

    public void clearGames() throws ResponseException, DataAccessException, SQLException {
        gameDAO.deleteAllGames();
    }
}
