package chess.PieceOptions;

import chess.ChessPosition;
import chess.ChessMove;
import chess.ChessBoard;

import java.util.HashSet;


public class KnightOptions implements PieceOptions {
    public static HashSet<ChessMove> getOptions(ChessBoard chessBoard, ChessPosition position) {
        int[][] moves = {{-2,1},{-1,2},{1,2},{2,1},{2,-1},{1,-2},{-1,-2},{-2,-1}};
        return PieceOptions.stateMoves(chessBoard, position, moves);
    }
}
