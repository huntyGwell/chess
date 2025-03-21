package dataAccess;

import model.GameData;

import java.util.HashSet;

public interface GameDAO {
    HashSet<GameData> getAllGames();
    void makeGame(GameData game); ////throws DataAccessException
    void updateGame(GameData game); ////throws DataAccessException
    GameData getGame(int gameId); ////throws DataAccessException
    boolean validateGame(int gameId);
    void clearGames();
}
