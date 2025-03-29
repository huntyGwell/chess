import chess.*;
import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import server.Server;
import server.WebSocketHandler;
import service.GameService;
import service.UserService;
import spark.*;

import java.sql.SQLException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.run(8080);
    }
}