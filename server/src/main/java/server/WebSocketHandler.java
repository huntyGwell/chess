package server;

import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebSocketHandler {

    private final ConcurrentHashMap<Session, Integer> connections = new ConcurrentHashMap<>();
    private static final WebSocketHandler wsHandler;

    static {
        try {
            wsHandler = new WebSocketHandler();
        } catch (ResponseException | SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private MySQLAuthDAO authDAO;
    private MySQLGameDAO gameDAO;
    private MySQLUserDAO userDAO;


    public WebSocketHandler() throws ResponseException, SQLException, DataAccessException {
        this.authDAO = new MySQLAuthDAO();
        this.gameDAO = new MySQLGameDAO();
        this.userDAO = new MySQLUserDAO();
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        connections.put(session, 0);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        connections.remove(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException {
        System.out.printf("Received: %s\n", message);

        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        AuthData authData = null;
        GameData gameData = null;

        try{
            authData = authDAO.getAuth(command.getAuthToken());

            if (authData == null) {
                return;
            }

            gameData = gameDAO.getGame(command.getGameID());

            if (gameData == null) {
                return;
            }
        } catch (DataAccessException e) {

        }

        switch (command.getCommandType()) {
            case CONNECT -> conn(session, Objects.requireNonNull(authData).username(), command, gameData);
            case MAKE_MOVE -> makeMove(session, Objects.requireNonNull(authData).username(), command, gameData);
            case LEAVE -> leave(session, Objects.requireNonNull(authData).username(), command, gameData);
            case RESIGN -> resign(session, Objects.requireNonNull(authData).username(), command, gameData);
        }

    }

    //join as an observer or a player
    private void conn(Session session, String username, UserGameCommand command, GameData gameData) {
        connections.replace(session, command.getGameID());
        String s;
        String color;

        try {
            if (!username.equals(gameData.blackUsername()) && !username.equals(gameData.whiteUsername())) {
                s = username + " has joined the game as an observer";

            } else {
                if (username.equals(gameData.blackUsername())) {
                    color = "black";
                } else {
                    color = "white";
                }
                s = username + " has joined the game as " + color + ".";
            }
            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, s);
            broadcast(session, notification);

            ServerMessage load = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, s);
            sendMessage(session, load);

        } catch (IOException e) {
            //figure out later
        }
    }

    private void makeMove(Session session, String username, UserGameCommand command, GameData gameData) {}

    private void leave(Session session, String username, UserGameCommand command, GameData gameData) {}

    private void resign(Session session, String username, UserGameCommand command, GameData gameData) {}

    // Send notification to all clients on curr game except currSession
    public void broadcast(Session currSession, ServerMessage message) throws IOException {
        broadcast(currSession, message, false);
    }

    // Send notification to all clients on curr game
    public void broadcast(Session currSession, ServerMessage message, boolean self) throws IOException {
        System.out.printf("Broadcasting (toSelf: %s): %s%n", self, new Gson().toJson(message));
        for (Session session : connections.keySet()) {
            boolean inGame = connections.get(session) != 0;
            boolean sameGame = connections.get(session).equals(connections.get(currSession));
            boolean isSelf = session == currSession;
            if ((self || !isSelf) && inGame && sameGame) {
                sendMessage(session, message);
            }
        }
    }

    public void sendMessage(Session session, ServerMessage message) throws IOException {
        session.getRemote().sendString(new Gson().toJson(message));
    }
}
