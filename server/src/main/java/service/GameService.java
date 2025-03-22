package service;

import dataAccess.*;
import model.*;
import chess.ChessGame;
import chess.ChessBoard;

public class GameService {
    GameDAO gameDAO;
    UserDAO userDAO;
    AuthDAO authDAO;

    public GameService(GameDAO gameDAO, UserDAO userDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public void clear() {
        gameDAO.clearGames();
    }
}
