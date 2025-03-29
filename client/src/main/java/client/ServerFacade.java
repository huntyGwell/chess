package client;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.JoinData;
import model.ListData;
import model.UserData;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;

public class ServerFacade {
    private HttpCommunicator httpCommunicator;
    private String auth;
    private int gameID;
    private WebSocketCommunicator webSocketCommunicator;

    public ServerFacade(String domain) {
        httpCommunicator = new HttpCommunicator(this, domain);
        try {
            webSocketCommunicator = new WebSocketCommunicator(domain);
        }
        catch (Exception e) {
            System.out.println("Failed to make connection with server");
        }
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getAuth() {
        return auth;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

    public void register(UserData user) throws ResponseException, IOException {
        var path = "/user";
        httpCommunicator.register("POST", path, user);
    }

    public void login(UserData user) throws ResponseException, URISyntaxException, IOException {
        var path = "/session";
        httpCommunicator.login("POST", path, user);
    }

    public void logout() throws ResponseException, IOException {
        var path = "/session";
        httpCommunicator.logout("DELETE", path);
    }

    public HashSet<GameData> listgames() throws ResponseException, IOException {
        var path = "/game";
        return httpCommunicator.listgames("GET", path);
    }

    public int creategame(String name) throws ResponseException, IOException {
        var path = "/game";
        httpCommunicator.creategame("POST", path, name);
        return gameID;
    }

    public void joingame(JoinData join) throws ResponseException, IOException {
        var path = "/game";
        httpCommunicator.joingame("PUT", path, join);
    }

    public void sendCommand(UserGameCommand command, ChessGame game, String color) {
        String message = new Gson().toJson(command);
        webSocketCommunicator.setGameAndColor(game, color);
        webSocketCommunicator.sendMessage(message);
    }

    public void connPerson(ChessGame game, String color) {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, getAuth(), getGameID());
        sendCommand(command, game, color);
    }

}
