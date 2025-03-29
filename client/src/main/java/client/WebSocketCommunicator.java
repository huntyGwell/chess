package client;

import chess.ChessGame;
import com.google.gson.Gson;
import ui.DrawBoard;
import ui.REPL;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import static ui.EscapeSequences.ERASE_LINE;

public class WebSocketCommunicator extends Endpoint {

    Session session;
    DrawBoard drawBoard;
    private ChessGame game;
    private String color;

    public WebSocketCommunicator(String domain) throws Exception {
        try {
            URI uri = new URI("ws://" + domain + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    messageHandler(message);
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception();
        }

    }

    protected void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

    protected void setGameAndColor(ChessGame game, String color) {
        this.game = game;
        this.color = color;
    }

    private void messageHandler(String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        ServerMessage.ServerMessageType type = serverMessage.getServerMessageType();

        if (Objects.equals(type.toString(), "NOTIFICATION")) {
            notify(message);
        }
        else if (Objects.equals(type.toString(), "ERROR")) {
            error(message);
        }
        else if (Objects.equals(type.toString(), "LOAD_GAME")) {
            load(message);
        }
    }

    private void error(String message) {}

    private void load(String message) {
        ServerMessage loadGame = new Gson().fromJson(message, ServerMessage.class);
        System.out.print(ERASE_LINE + "\r\n");
        drawBoard = new DrawBoard(game, color);
    }

    private void notify(String message) {
        System.out.print(ERASE_LINE + '\r');
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}
