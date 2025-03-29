package server;

import dataaccess.*;
import exception.ResponseException;
import service.GameService;
import service.UserService;
import spark.*;
import spark.route.Routes;

import java.sql.SQLException;

public class Server {
    UserDAO userDAO;
    GameDAO gameDAO;
    AuthDAO authDAO;

    UserService userService;
    GameService gameService;

    UserHandler userHandler;
    GameHandler gameHandler;

    public Server() {

        try {
            userDAO = new MySQLUserDAO();
            gameDAO = new MySQLGameDAO();
            authDAO = new MySQLAuthDAO();
        } catch (ResponseException | SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }

        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);

        userHandler = new UserHandler(userService);
        gameHandler = new GameHandler(gameService);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/connect", WebSocketHandler.class);

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearAll);

        Spark.post("/user", userHandler::register);
        Spark.post("/session", userHandler::login);
        Spark.delete("/session", userHandler::logout);

        Spark.get("/game", gameHandler::listgames);
        Spark.post("/game", gameHandler::creategame);
        Spark.put("/game", gameHandler::joingame);

        Spark.exception(ResponseException.class, this::exceptionHandler);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.statusCode());
    }

    public Object clearAll(Request req, Response res) throws ResponseException, DataAccessException, SQLException {
        userService.clearUsers();
        gameService.clearGames();
        res.status(200);
        return "{}";
    }
}
