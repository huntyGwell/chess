package chess.PieceOptionsCalc;

import chess.ChessGame;
import chess.ChessPosition;
import chess.ChessMove;
import chess.ChessBoard;

import java.util.HashSet;

public class KingOptions implements PieceOptionsCalc {
    public static HashSet<ChessMove> calculateOptions(ChessBoard board, ChessPosition position) {
        int[][] moves = {{-1,1},{0,1},{1,1},{1,0},{1,-1},{0,-1},{-1,-1},{-1,0}};
        return PieceOptionsCalc.CreateStateMoves(board, position, moves);
    }
}
