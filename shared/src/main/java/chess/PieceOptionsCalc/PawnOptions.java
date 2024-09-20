package chess.PieceOptionsCalc;

import chess.ChessGame;
import chess.ChessPosition;
import chess.ChessMove;
import chess.ChessBoard;
import chess.ChessPiece;

import java.util.HashSet;

public class PawnOptions implements PieceOptionsCalc{
    //this is actually harder than the others!
    public static HashSet<ChessMove> calculateOptions(ChessBoard board, ChessPosition position){
        HashSet<ChessMove> moves = HashSet.newHashSet(17);
        int row =position.getRow();
        int column = position.getColumn();
        ChessPiece.PieceType[] promo = new ChessPiece.PieceType[]{null};
        //attack right

        //attack left

        //doublejump first move

        return moves;
    }


}
