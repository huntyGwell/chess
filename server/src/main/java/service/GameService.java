package service;

import dataAccess.*;
import model.*;
import chess.ChessGame;

import java.util.HashSet;
import java.util.UUID;

public class GameService {
    private final GameDAO gameDAO;
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, UserDAO userDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    /**
     * Create a new game and return its ID.
     */
    public int createGame(GameData game) throws DataAccessException {
        if (game.gameName() == null || game.gameName().isEmpty()) {
            throw new DataAccessException("Bad request: game name required");
        }

        int gameID = gameDAO.makeGame();
        return gameID;
    }

    /**
     * Join a game as a specified color if available.
     */
    public boolean joinGame(GameData game) throws DataAccessException {
        GameData existing = gameDAO.getGame(game.gameID());

        if (existing == null) {
            throw new DataAccessException("Game does not exist");
        }

        // Determine color to join
        if ("WHITE".equalsIgnoreCase(game.whiteUsername())) {
            if (existing.whiteUsername() != null) {
                throw new DataAccessException("White already taken");
            }
            existing = new GameData(
                    existing.gameID(),
                    //game.username(),
                    //game.gameID(),
                    game.whiteUsername(),
                    existing.blackUsername(),
                    existing.gameName(),
                    existing.game()
            );
        } else if ("BLACK".equalsIgnoreCase(game.blackUsername())) {
            if (existing.blackUsername() != null) {
                throw new DataAccessException("Black already taken");
            }
            existing = new GameData(
                    existing.gameID(),
                    existing.whiteUsername(),
                    //game.username(),
                    game.gameName(),
                    existing.gameName(),
                    existing.game()
            );
        } else {
            throw new DataAccessException("Bad request: must specify color");
        }

        gameDAO.updateGame(existing);
        return true;
    }

    /**
     * List all games.
     */
    public HashSet<GameData> listGames() throws DataAccessException {
        return gameDAO.getAllGames();
    }

    /**
     * Update the game (e.g., if player joins or makes a move).
     */
    public void updateGame(GameData game) throws DataAccessException {
        gameDAO.updateGame(game);
    }

    /**
     * Get full game info by ID.
     */
    public GameData getGameInfo(UUID id) throws DataAccessException {
        return gameDAO.getGame(id.hashCode());
    }

    /**
     * Clear all games.
     */
    public void clear() throws DataAccessException {
        gameDAO.clearGames();
    }
}
