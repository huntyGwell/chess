package chess.PieceOptions;

import chess.ChessPosition;
import chess.ChessMove;
import chess.ChessBoard;

import java.util.HashSet;

public class KingOptions implements PieceOptions {
    public static HashSet<ChessMove> getOptions(ChessBoard board, ChessPosition position) {
        int[][] moves = {{-1,1},{0,1},{1,1},{1,0},{1,-1},{0,-1},{-1,-1},{-1,0}};
        return PieceOptions.stateMoves(board, position, moves);
    }
}
