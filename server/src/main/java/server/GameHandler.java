package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.ResponseException;
import model.GameData;
import model.JoinData;
import service.GameService;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.*;

import static server.UserHandler.convertExceptionToJson;

public class GameHandler {

    GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object listgames(Request req, Response res) throws ResponseException, DataAccessException {
        String token = req.headers("authorization");
        Collection<GameData> games;
        try {
           games = gameService.listgames(token);
        } catch (ResponseException e) {
            res.status(e.statusCode());
            return convertExceptionToJson(e);
        }
        res.status(200);
        // Wrapper class for the expected JSON structure
        Map<String, Object> responseMap = new HashMap<>();
        List<Map<String, Object>> gameList = new ArrayList<>();

        for (GameData game : games) {
            Map<String, Object> gameMap = new HashMap<>();
            gameMap.put("gameID", game.gameID()); // Assuming GameData has this method
            gameMap.put("whiteUsername", game.whiteUsername()); // Add logic if needed
            gameMap.put("blackUsername", game.blackUsername()); // Add logic if needed
            gameMap.put("gameName", game.gameName()); // Assuming GameData has this method
            gameList.add(gameMap);
        }
        responseMap.put("games", gameList);

        res.type("application/json");
        return new Gson().toJson(responseMap);
    }

    public Object creategame(Request req, Response res) throws DataAccessException {
        var game = new Gson().fromJson(req.body(), GameData.class);
        String token = req.headers("authorization");
        int gameID;
        try {
            gameID = gameService.creategame(token, game.gameName());
        } catch (ResponseException e) {
            res.status(e.statusCode());
            return convertExceptionToJson(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        res.status(200);
        Map<String, Integer> response = new HashMap<>();
        response.put("gameID", gameID);
        return new Gson().toJson(response);
    }

    public Object joingame(Request req, Response res) throws DataAccessException {
        var join = new Gson().fromJson(req.body(), JoinData.class);
        String token = req.headers("authorization");
        try {
            gameService.joingame(token, join);
        } catch (ResponseException e) {
            res.status(e.statusCode());
            return convertExceptionToJson(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        res.status(200);
        return "{}";
    }
}
