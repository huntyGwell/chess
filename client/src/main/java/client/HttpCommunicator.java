package client;

import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.JoinData;
import model.ListData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class HttpCommunicator {
    private final String domain;
    private final ServerFacade serverFacade;

    public HttpCommunicator(ServerFacade facade, String domain) {
        this.domain = domain;
        this.serverFacade = facade;
    }

    public void register(String action, String path, UserData user) throws ResponseException, IOException {
        var result = this.makeRequest(action, path, user, Map.class);
        serverFacade.setAuth((String) result.get("authToken"));
    }

    public void login(String action, String path, UserData user) throws ResponseException, URISyntaxException, IOException {
        var result = this.makeRequest(action, path, user, Map.class);
        serverFacade.setAuth((String) result.get("authToken"));
    }

    public void logout(String action, String path) throws ResponseException, IOException {
        this.makeRequest(action, path, null, Map.class);
        serverFacade.setAuth(null);
    }

    public void creategame(String action, String path, String gameName) throws ResponseException, IOException {
        Map requestData = Map.of("gameName", gameName);
        var result = this.makeRequest(action, path, requestData, Map.class);
        Number numID = (Number) result.get("gameID");
        int id = numID.intValue();
        serverFacade.setGameID(id);
    }

    public HashSet<GameData> listgames(String action, String path) throws ResponseException, IOException {
        String games = this.makeRequest(action, path, null, null);
        var json = new Gson().fromJson(games, ListData.class);
        return json.games();
    }

    public void joingame(String action, String path, JoinData join) throws ResponseException, IOException {
        Map requestData = Map.of("playerColor", join.playerColor(), "gameID", join.gameID());
        this.makeRequest(action, path, requestData, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException, IOException {
        String result;
        Map mapResult;
        HttpURLConnection http = null;
        try {
            URL url = (new URI("http://" + domain + path)).toURL();
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);

            if (serverFacade.getAuth() != null) {
                http.addRequestProperty("Authorization", serverFacade.getAuth());
            }

            if (request != null) {
                http.setDoOutput(true);
                writeBody(request, http);
            }

            http.connect();
            throwIfNotSuccessful(http);

            if (method.equals("GET")) {
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    result = reader(inputStreamReader);
                    return (T) result;
                }
            }

            else {
                try (InputStream body = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(body);
                    mapResult = new Gson().fromJson(inputStreamReader, Map.class);
                    return (T) mapResult;
                }
            }
        } catch (Exception ex) {
            if (http != null) {
                throw new ResponseException(http.getResponseCode(), ex.getMessage());
            }
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private String reader(InputStreamReader reader) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (int ch; (ch = reader.read()) != -1; ) {
                stringBuilder.append((char) ch);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            return "";
        }

    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        var message = http.getResponseMessage();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, message);
        }
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
