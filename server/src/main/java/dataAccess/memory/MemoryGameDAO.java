package dataAccess.memory;

import dataAccess.DataAccessException;
import model.GameData;
import dataAccess.GameDAO;

import java.util.HashSet;

public class MemoryGameDAO implements GameDAO {

    private HashSet<GameData> gamesData;
    public MemoryGameDAO() {
        gamesData = new HashSet<>();
    }
    @Override
    public HashSet<GameData> getAllGames() {
        return gamesData;
    }
    @Override
    public void makeGame(GameData game) {
        gamesData.add(game);
    }
    @Override
    public GameData getGame(int gameID) {
        for (GameData game: gamesData) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        throw new DataAccessException("Game with ID " + gameID + " not found");
    }
    @Override
    public boolean validateGame(int gameID) {
        for (GameData game: gamesData) {
            if (game.gameID() == gameID) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void updateGame(GameData game) {
        try {
            gamesData.remove(game);
            gamesData.add(game);
        } catch (DataAccessException e) {
            gamesData.add(game);
        }
    }
    @Override
    public void clearGames() {
        gamesData.clear();
    }
}
