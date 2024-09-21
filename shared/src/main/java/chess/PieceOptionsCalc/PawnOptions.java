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
        //Add first move double, if available
        ChessPosition doubleJump = new ChessPosition(row + moveIncrement*2, column);
        if (PieceOptionsCalc.isValidPosition(doubleJump) &&
                ((team == ChessGame.TeamColor.WHITE && currY == 2) || (team == ChessGame.TeamColor.BLACK && currY == 7)) &&
                board.getPiece(doubleForwardPosition) == null &&
                board.getPiece(forwardPosition) == null) {
            moves.add(new ChessMove(position, doubleJump, promotionPiece));
        }
        return moves;
    }
}
