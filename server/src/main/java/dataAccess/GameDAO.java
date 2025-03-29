package dataaccess;

import exception.ResponseException;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

public interface GameDAO {
    int createGame(String gameName) throws DataAccessException, ResponseException, SQLException;

    GameData getGame(int gameId) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException, ResponseException;

    void updateGame(String playerColor, int gameID, String auth) throws DataAccessException, ResponseException, SQLException;

    void deleteAllGames() throws DataAccessException, ResponseException, SQLException;
}
