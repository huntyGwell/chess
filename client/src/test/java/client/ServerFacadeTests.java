package client;

import dataaccess.*;
import exception.ResponseException;
import model.JoinData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.GameService;
import service.UserService;

import java.sql.SQLException;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    static GameDAO gameDAO;
    static AuthDAO authDAO;
    static UserDAO userDAO ;
    static GameService gameService;
    static UserService userService;
    UserData user;
    JoinData join;

    @BeforeAll
    public static void init() {
        server = new Server();
        gameDAO = new MySQLGameDAO();
        authDAO = new MySQLAuthDAO();
        try {
            userDAO = new MySQLUserDAO();
        } catch (ResponseException | SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        gameService = new GameService(gameDAO, authDAO);
        userService = new UserService(userDAO, authDAO);
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearDatabase() throws ResponseException, SQLException, DataAccessException {
        gameService.clearGames();
        userService.clearUsers();
    }

    //register
    @Test
    void registerSuccess() throws Exception {
        user = new UserData("test", "what a good password", "wow@gmail.com");
        Assertions.assertDoesNotThrow(() -> facade.register(user));
    }

    @Test
    void registerFail() {
        user = new UserData("test", "what a good password", "wow@gmail.com");
        Assertions.assertDoesNotThrow(() -> facade.register(user));
        user = new UserData("test", "what a good password", "wow@gmail.com");
        Assertions.assertThrows(ResponseException.class, () -> facade.register(user));
    }

    //login
    @Test
    void loginSuccess() throws Exception {
        user = new UserData("test", "what a good password", "wow@gmail.com");
        Assertions.assertDoesNotThrow(() -> facade.register(user));
        Assertions.assertDoesNotThrow(() -> facade.login(user));
    }

    @Test
    void loginFail() {
        Assertions.assertThrows(ResponseException.class, () -> facade.login(user));
    }

    //logout
    @Test
    void logoutSuccess() throws Exception {
        user = new UserData("test", "what a good password", "wow@gmail.com");
        Assertions.assertDoesNotThrow(() -> facade.register(user));
        Assertions.assertDoesNotThrow(() -> facade.login(user));
        Assertions.assertDoesNotThrow(() -> facade.logout());
    }

    //create game
    @Test
    void createGameSuccess() throws Exception {
        user = new UserData("test", "what a good password", "wow@gmail.com");
        Assertions.assertDoesNotThrow(() -> facade.register(user));
        Assertions.assertDoesNotThrow(() -> facade.login(user));
        Assertions.assertDoesNotThrow(() -> facade.creategame("newGame"));
    }

    @Test
    void createGameFail() {
        Assertions.assertThrows(ResponseException.class, () -> facade.creategame("newGame"));
    }

    //list games
    @Test
    void listGamesSuccess() throws Exception {
        user = new UserData("test", "what a good password", "wow@gmail.com");
        Assertions.assertDoesNotThrow(() -> facade.register(user));
        Assertions.assertDoesNotThrow(() -> facade.login(user));
        Assertions.assertDoesNotThrow(() -> facade.creategame("gg"));
        Assertions.assertDoesNotThrow(() -> facade.listgames());
    }

    @Test
    void listGamesFail() {
        Assertions.assertThrows(ResponseException.class, () -> facade.listgames());
    }

    //join game
    @Test
    void joinSuccess() throws Exception {
        user = new UserData("test", "what a good password", "wow@gmail.com");
        Assertions.assertDoesNotThrow(() -> facade.register(user));
        Assertions.assertDoesNotThrow(() -> facade.login(user));
        int gameID = facade.creategame("gg");
        Assertions.assertDoesNotThrow(() -> facade.listgames());
        join = new JoinData("WHITE", gameID);
        Assertions.assertDoesNotThrow(() -> facade.joingame(join));
    }

    @Test
    void joinFail() {
        join = new JoinData("WHITE", 1000);
        Assertions.assertThrows(ResponseException.class, () -> facade.joingame(join));
    }

    //observe
    @Test
    void observeSuccess() throws Exception { //temporary
        Assertions.assertTrue(true);
    }

    @Test
    void observeFail() { //temporary
        Assertions.assertFalse(false);
    }
}
