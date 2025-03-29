import chess.*;
import client.ServerFacade;
import exception.ResponseException;
import ui.DrawBoard;
import ui.REPL;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws ResponseException, IOException, URISyntaxException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        var domain = "localhost:8080";
        if (args.length == 1) {
            domain = args[0];
        }

        new REPL(domain).run();
    }
}