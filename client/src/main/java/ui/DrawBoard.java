package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class DrawBoard {

    ChessGame game;
    String color;

    public DrawBoard(ChessGame game, String color) {
        this.game = game;
        this.color = color;
    }

    public void drawBoard() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        if (!color.equalsIgnoreCase("WHITE")) {
            drawHeader(out, false);

            for (int row = 0; row < 8; row++) {
                drawRow(out, row, false);
            }

            drawHeader(out, false);
        }
        else {
            //reverse
            int row = 7;

            drawHeader(out, true);

            while (row >= 0) {
                drawRow(out, row, true);
                row--;
            }

            drawHeader(out, true);
        }

        out.print(RESET_TEXT_COLOR);
    }

    private void drawHeader(PrintStream out, boolean reverse) {
        out.print(SET_BG_COLOR_DARK_GREY);

        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
        if (!reverse) {
            headers = new String[]{"h", "g", "f", "e", "d", "c", "b", "a"};
        }

        for (int boardCol = 0; boardCol < 8; ++boardCol) {
            if (boardCol == 0) {
                out.print("   ".repeat(1));
            }

            out.print(" ".repeat(1));
            out.print(SET_BG_COLOR_DARK_GREY);
            out.print(SET_TEXT_COLOR_LIGHT_GREY);

            out.print(headers[boardCol]);

            out.print(SET_BG_COLOR_DARK_GREY);
            out.print(" ".repeat(1));

            if (boardCol == 7) {
                out.print("   ".repeat(1));
                out.print(RESET_BG_COLOR);
            }
        }

        out.println();
    }

    private void drawRow(PrintStream out, int row ,boolean reverse) {
        out.print(SET_BG_COLOR_DARK_GREY);

        out.print(" ".repeat(1));
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);

        out.print(row+1);

        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(" ".repeat(1));

        //printing the actual board
        int boardCol;
        if (!reverse) {
            boardCol = 7;
        }
        else {
            boardCol = 0;
        }

        while (boardCol < 8 && boardCol >= 0) {
            ChessPosition pos = new ChessPosition(row+1, boardCol+1);
            ChessPiece piece = game.getBoard().getPiece(pos);

            if (row % 2 == 0) { //even row
                if (boardCol % 2 == 0) {
                    whiteSquare(out, piece);
                }
                else {
                    blackSquare(out, piece);
                }

            }
            else { //odd row
                if (boardCol % 2 == 0) {
                    blackSquare(out, piece);
                }
                else {
                    whiteSquare(out, piece);
                }
            }

            if (!reverse) {
                boardCol--;
            }
            else {
                boardCol++;
            }
        }

        out.print(SET_BG_COLOR_DARK_GREY);

        out.print(" ".repeat(1));
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);

        out.print(row+1);

        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(" ".repeat(1));

        out.print(RESET_BG_COLOR);

        out.println();
    }

    private void whiteSquare(PrintStream out, ChessPiece piece) {
        //out.print("   ".repeat(1));
        out.print(SET_BG_COLOR_DARK_GREEN); // "white" background
        if (piece != null) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) { // white player
                out.print(SET_TEXT_COLOR_WHITE);
            }
            else { // black player
                out.print(SET_TEXT_COLOR_BLACK);
            }
        }

        if (piece != null) {
            printPiece(out, piece.getPieceType());
        }
        else {
            out.print("   ".repeat(1));
        }

        out.print(SET_BG_COLOR_DARK_GREEN);
        //out.print("   ".repeat(1));
    }

    private void blackSquare(PrintStream out, ChessPiece piece) {
        //out.print("   ".repeat(1));
        out.print(SET_BG_COLOR_LIGHT_GREY); // "white" background
        if (piece != null) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) { // white player
                out.print(SET_TEXT_COLOR_WHITE);
            }
            else { // black player
                out.print(SET_TEXT_COLOR_BLACK);
            }
        }

        if (piece != null) {
            printPiece(out, piece.getPieceType());
        }
        else {
            out.print("   ".repeat(1));
        }

        out.print(SET_BG_COLOR_LIGHT_GREY);
        //out.print("   ".repeat(1));
    }

    private void printPiece(PrintStream out, ChessPiece.PieceType pieceType) {
        if (pieceType == ChessPiece.PieceType.KING) {
            out.print(" K ");
        }
        else if (pieceType == ChessPiece.PieceType.QUEEN) {
            out.print(" Q ");
        }
        else if (pieceType == ChessPiece.PieceType.BISHOP) {
            out.print(" B ");
        }
        else if (pieceType == ChessPiece.PieceType.KNIGHT) {
            out.print(" N ");
        }
        else if (pieceType == ChessPiece.PieceType.ROOK) {
            out.print(" R ");
        }
        else if (pieceType == ChessPiece.PieceType.PAWN) {
            out.print(" P ");
        }
    }
}
