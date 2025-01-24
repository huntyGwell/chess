package chess.PieceMoveCalc;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class QueenCalc implements PieceMoveCalc{
    public static HashSet<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        int x = position.getCol();
        int y = position.getRow();
        int[][] moves = {{-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}};

        ChessGame.TeamColor team = board.getSquaresTeam(position);

        return PieceMoveCalc.dynamicMoves(board, position, moves, y, x, team);
    }
}