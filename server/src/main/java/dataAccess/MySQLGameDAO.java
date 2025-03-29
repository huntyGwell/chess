package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

public class MySQLGameDAO implements GameDAO {

    @Override
    public int createGame(String gameName) throws DataAccessException, ResponseException, SQLException {
        var statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, chessGame) VALUES (?, ?, ?, ?, ?)";
        int gameID = generateRandomNumber(1, 9999);
        executeUpdate(statement, gameID, null, null, gameName, serializeGame(new ChessGame()));
        return gameID;
    }

    public static int generateRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min; // Generates a random number between min and max
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame FROM game WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    rs.next();
                    var whiteUsername = rs.getString("whiteUsername");
                    var blackUsername = rs.getString("blackUsername");
                    var gameName = rs.getString("gameName");
                    var chessGame = deserializeGame(rs.getString("chessGame"));
                    return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
                }
            }
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException, ResponseException {
        var result = new HashSet<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(getGame(rs.getInt("gameID")));
                    }
                }
            }
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    @Override
    public void updateGame(String playerColor, int gameID, String user) throws DataAccessException, ResponseException, SQLException {
        var statement = "UPDATE game SET whiteUsername=?, blackUsername=?, gameName=?, chessGame=? WHERE gameID=?";
        GameData game = getGame(gameID);
        String whiteUsername = game.whiteUsername();
        String blackUsername = game.blackUsername();
        if (Objects.equals(playerColor, "WHITE") && whiteUsername == null) {
            whiteUsername = user;
        }
        else if (Objects.equals(playerColor, "BLACK") && blackUsername == null) {
            blackUsername = user;
        }
        else {
            throw new SQLException();
        }
        executeUpdate(statement, whiteUsername, blackUsername, game.gameName(), serializeGame(game.chessGame()), gameID);
    }

    @Override
    public void deleteAllGames() throws DataAccessException {
        var statement = "TRUNCATE game";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String serializeGame(ChessGame game) {
        var serializer = new Gson();
        return serializer.toJson(game);
    }

    private ChessGame deserializeGame(String json) {
        return new Gson().fromJson(json, ChessGame.class);
    }

    private void executeUpdate(String statement, Object...params) throws ResponseException, DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                String sql = statement.trim();
                if (sql.toUpperCase().startsWith("INSERT")) {
                    ps.setInt(1, (Integer) params[0]);
                    ps.setObject(2, params[1]);
                    ps.setObject(3, params[2]);
                    ps.setString(4, params[3].toString());
                    ps.setString(5, params[4].toString());
                }
                else if (sql.toUpperCase().startsWith("UPDATE")) {
                    ps.setObject(1, params[0]);
                    ps.setObject(2, params[1]);
                    ps.setString(3, params[2].toString());
                    ps.setString(4, params[3].toString());
                    ps.setInt(5, (Integer) params[4]);
                }
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }
}