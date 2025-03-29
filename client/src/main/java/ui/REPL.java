package ui;

import chess.ChessGame;
import client.ServerFacade;
import exception.ResponseException;
import model.*;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import java.util.Map;
import java.util.HashMap;

import static ui.EscapeSequences.*;

public class REPL {
    private UserData userData;
    private JoinData joinData;
    private GameData gameData;
    boolean listed = false;
    public static DrawBoard drawBoard;
    ServerFacade client;
    Map<Integer, Integer> allGames = new HashMap<>();
    List<GameData> allGamesData = new ArrayList<>();
    String color;
    ChessGame game;

    public REPL(String domain) {
        client = new ServerFacade(domain);
        drawBoard = new DrawBoard(game, color);
    }

    public void run() throws ResponseException, IOException, URISyntaxException {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        boolean loggedIn = false;
        Scanner scanner = new Scanner(System.in);

        while (!loggedIn) {
            prelogin(out);
            String line = scanner.nextLine();

            if (Objects.equals(line, "1")) {
                loggedIn = menuRegister(out, scanner);
            } else if (Objects.equals(line, "2")) {
                loggedIn = menuLogin(out, scanner);
            } else if (Objects.equals(line, "3")) {
                out.println("Press 1 to register as a new user.");
                out.println("Press 2 to login as an existing user.");
                out.println("Press 3 to exit the program.\n");
            } else if (Objects.equals(line, "4")) {
                out.println("Goodbye!");
                out.close();
                System.exit(0);
            } else {
                out.println("Invalid selection.\n");
            }
        }
        while (loggedIn) {
            postlogin(out);
            String line = scanner.nextLine();
            if (Objects.equals(line, "1")) {
                loggedIn = false;
                listed = false;
                client.logout();
                run();
            } else if (Objects.equals(line, "2")) {
                out.println("Please enter a name for your game.");
                String name = scanner.nextLine();
                while (name.trim().isEmpty()) {
                    out.println("Please enter a name for your game.");
                    name = scanner.nextLine();
                }
                client.creategame(name);
            } else if (Objects.equals(line, "3")) {
                int num = 1;
                allGamesData = new ArrayList<>();
                HashSet<GameData> gameSet = client.listgames();
                allGamesData.addAll(gameSet);

                for (GameData game : gameSet) {
                    out.println(num + ". " + game.gameName());
                    out.println("\tWhite Player: " + game.whiteUsername());
                    out.println("\tBlack Player: " + game.blackUsername());
                    out.println("\t");
                    allGames.put(num, game.gameID());
                    num++;
                }
                listed = true;
            } else if (Objects.equals(line, "4")) {
                if (!listed) {
                    out.println("\nPlease list the games first.\n");
                    continue;
                }
                joinGame(out, scanner);
            } else if (Objects.equals(line, "5")) {
                if (!listed) {
                    out.println("\nPlease list the games first.\n");
                    continue;
                }
                observeGame(out, scanner);
            } else if (Objects.equals(line, "6")) {
                out.println("Press 1 to logout and return to the previous menu.");
                out.println("Press 2 to create a new game.");
                out.println("Press 3 to list all games.");
                out.println("Press 4 to join a game.");
                out.println("Press 5 to observe a game.\n");
            } else {
                out.println("Invalid selection.\n");
            }
        }
    }

    private void prelogin(PrintStream out) {
        out.println("Welcome! Please enter a number corresponding to one of the ones below.");
        out.println("\t1. Register");
        out.println("\t2. Login");
        out.println("\t3. Help");
        out.println("\t4. Quit");
    }

    private boolean menuRegister (PrintStream out, Scanner scanner) throws IOException {
        out.println("Please enter a username.");
        String username = scanner.nextLine();
        while (username.isEmpty()) {
            out.println("Please enter a username.");
            username = scanner.nextLine();
        }
        out.println("Please enter an email address.");
        String email = scanner.nextLine();
        while (email.isEmpty()) {
            out.println("Please enter an email address.");
            email = scanner.nextLine();
        }
        out.println("Please enter a password.");
        String password = scanner.nextLine();
        while (password.isEmpty()) {
            out.println("Please enter a password.");
            password = scanner.nextLine();
        }
        userData = new UserData(username, password, email);
        try {
            client.register(userData);
            return true;
        } catch (ResponseException e) {
            int status = e.statusCode();
            switch (status) {
                case 400:
                    out.println("Bad request.\n");
                    break;
                case 401:
                    out.println("Unauthorized.\n");
                    break;
                case 403:
                    out.println("Already taken.\n");
                    break;
            }
            return false;
        }
    }

    private boolean menuLogin (PrintStream out, Scanner scanner) throws ResponseException, URISyntaxException, IOException {
        out.println("Please enter your username.");
        String username = scanner.nextLine();
        out.println("Please enter your password.");
        String password = scanner.nextLine();
        userData = new UserData(username, password, "");
        try {
            client.login(userData);
        } catch (ResponseException e) {
            int status = e.statusCode();
            if (status == 401) {
                out.println("Unauthorized username or password.\n");
                return false;
            }
        }
        return true;
    }

    private void postlogin(PrintStream out) {
        out.println("Please enter a number corresponding to one of the ones below.");
        out.println("\t1. Logout");
        out.println("\t2. Create Game");
        out.println("\t3. List Games");
        out.println("\t4. Play Game");
        out.println("\t5. Observe Game");
        out.println("\t6. Help");
    }

    private void joinGame(PrintStream out, Scanner scanner) throws ResponseException, IOException {
        out.println("Please enter the number of the game you wish to join.");
        String number = scanner.nextLine();
        boolean isNumeric;
        while (true) {
            isNumeric = checkNonNumeric(number);
            if (!isNumeric) {
                out.println("Invalid game number.\nPlease enter the number of the game you wish to join.");
                number = scanner.nextLine();
                continue;
            }
            if (!allGames.containsKey(Integer.parseInt(number))) {
                out.println("Invalid game number.\nPlease enter the number of the game you wish to join.");
                number = scanner.nextLine();
                continue;
            }
            break;
        }
        out.println("What color do you wish to be? Enter WHITE or BLACK.");
        String color = scanner.nextLine().toUpperCase();
        while (true) {
            if (Objects.equals(color, "WHITE") || Objects.equals(color, "BLACK")) {
                break;
            }
            out.println("Invalid color. Enter WHITE or BLACK.");
            color = scanner.nextLine();
        }
        joinData = new JoinData(color, allGames.get(Integer.parseInt(number)));
        //get game
        gameData = allGamesData.get(Integer.parseInt(number));
        try {
            client.joingame(joinData);
            client.connPerson(game, color);
            gameMenuREPL(out, scanner, color);
        } catch (ResponseException e) {
            int status = e.statusCode();
            switch (status) {
                case 400:
                    out.println("Bad request.\n");
                    break;
                case 401:
                    out.println("Unauthorized username or password.\n");
                    break;
                case 403:
                    out.println("Already taken.\n");
                    break;
            }
        }
    }

    private void observeGame(PrintStream out, Scanner scanner) {
        out.println("Please enter the number of the game you wish to observe.");
        String number = scanner.nextLine();
        boolean isNumeric = true;
        while (true) {
            isNumeric = checkNonNumeric(number);
            if (!isNumeric) {
                out.println("Invalid game number.\nPlease enter the number of the game you wish to observe.");
                number = scanner.nextLine();
                isNumeric = true;
                continue;
            }
            if (!allGames.containsKey(Integer.parseInt(number))) {
                out.println("Invalid game number.\nPlease enter the number of the game you wish to observe.");
                number = scanner.nextLine();
                continue;
            }
            new DrawBoard(new ChessGame(), "white").drawBoard();;
            break;
        }
    }

    public boolean checkNonNumeric(String number) {
        for (int i = 0; i < number.length(); i++) {
            if (!Character.isDigit(number.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public void gameMenu(PrintStream out) {
        out.println("\t1. Redraw Board");
        out.println("\t2. Highlight Valid Moves");
        out.println("\t3. Make a Move");
        out.println("\t4. Leave");
        out.println("\t5. Resign");
        out.println("\t6. Help\n");
    }

    private void gameMenuREPL(PrintStream out, Scanner scanner, String color) throws ResponseException {
        gameMenu(out);
        boolean inGame = true;

        while (inGame) {
            String line = scanner.nextLine();
            if (line.equals("1")) {
                new DrawBoard(new ChessGame(), color).drawBoard();
            }
            else if (line.equals("2")) {
                //redraw the board with the valid moves for specific piece
            }
            else if (line.equals("3")) {
                //(piece to move) / (where to move them)
            }
            else if (line.equals("4")) {
                inGame = false;
                //set player (white/black) as null
            }
            else if (line.equals("5")) {
                //set game finished condition somewhere
            }
            else if (line.equals("6")) {
                out.println("Press 1 to redraw the board.");
                out.println("Press 2 to highlight moves you can make.");
                out.println("Press 3 to make a move.");
                out.println("Press 4 to leave the game and open a spot for a new player.");
                out.println("Press 5 to resign and lose the game.\n");
            }
        }
    }
}
