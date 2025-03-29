package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public class MemoryGameDAO implements GameDAO {
    final private HashSet<GameData> games = new HashSet<>();

    public int createGame(String gameName) {
        int min = 1;
        int max = 9999;
        int id = generateRandomNumber(min, max);
        while (getGame(id) != null) {
            id = generateRandomNumber(min, max);
        }
        GameData game = new GameData(id, null, null, gameName, new ChessGame());
        games.add(game);
        return id;
    }

    public static int generateRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min; // Generates a random number between min and max
    }

    public GameData getGame(int gameId) {
        for (GameData game : games) {
            if (game.gameID() == gameId) {
                return game;
            }
        }
        return null;
    }

    public Collection<GameData> listGames() {
        return games;
    }

    public void updateGame(String playerColor, int gameID, String user) {
        GameData game = getGame(gameID);
        GameData newGame;
        if (playerColor.equals("WHITE")) {
            newGame = new GameData(gameID, user, game.blackUsername(), game.gameName(), game.chessGame());
        }
        else {
            newGame = new GameData(gameID, game.whiteUsername(), user, game.gameName(), game.chessGame());
        }
        games.remove(game);
        games.add(newGame);
    }

    public void deleteAllGames() {
        games.clear();
    }
}
