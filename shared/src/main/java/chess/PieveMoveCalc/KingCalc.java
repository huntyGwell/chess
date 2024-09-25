package chess.PieveMoveCalc;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;
public class KingCalc implements PieceMoveCalc {

    public static HashSet<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        int[][] moves = {{-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}};
        return PieceMoveCalc.staticMoves(position, moves, board);
    }
}
