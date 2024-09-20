package chess.PieceOptionsCalc;

import chess.ChessGame;
import chess.ChessPosition;
import chess.ChessMove;
import chess.ChessBoard;

import java.util.HashSet;


public class KnightOptions implements PieceOptionsCalc {
    public static HashSet<ChessMove> calculateOptions(ChessBoard chessBoard, ChessPosition position) {
        int[][] moves = {{-2,1},{-1,2},{1,2},{2,1},{2,-1},{1,-2},{-1,-2},{-2,-1}};
        return PieceOptionsCalc.CreateStateMoves(chessBoard, position, moves);
    }
}
