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
        ChessPiece.PieceType[] promotionPieces = new ChessPiece.PieceType[]{null};

        ChessGame.TeamColor team = board.getTeamColor(position);
        int incrementation = team == ChessGame.TeamColor.WHITE ? 1 : -1; //Black??

        boolean promote = (team == ChessGame.TeamColor.WHITE && row == 7) || (team == ChessGame.TeamColor.BLACK && row == 2);
        if (promote) {
            promotionPieces = new ChessPiece.PieceType[]{ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KNIGHT,
            ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.ROOK};
        }//now forward move
        for (ChessPiece.PieceType promotionPiece : promotionPieces) {
            ChessPosition forwardPosition = new ChessPosition(row + incrementation, column);
            if (PieceOptionsCalc.isValidPosition(forwardPosition) && board.getPiece(forwardPosition) == null) {
                moves.add(new ChessMove(position, forwardPosition, promotionPiece));
            }//doubleJump
            ChessPosition doubleJumpPosition = new ChessPosition(row + incrementation*2, column);
            if (PieceOptionsCalc.isValidPosition(doubleJumpPosition) && board.getPiece(doubleJumpPosition) == null
                            && board.getPiece(forwardPosition) == null &&
                    ((team == ChessGame.TeamColor.BLACK && row == 7) || (team == ChessGame.TeamColor.WHITE && row == 2))) {
                moves.add(new ChessMove(position, doubleJumpPosition, promotionPiece));
            }// now attack right
            ChessPosition attackRight = new ChessPosition(row + incrementation, column + 1);
            if (PieceOptionsCalc.isValidPosition(attackRight) && board.getPiece(attackRight) != null &&
                                board.getSquaresColor(attackRight) != team) {
                moves.add(new ChessMove(position, attackRight, promotionPiece));
            }//now attack left
            ChessPosition attackLeft = new ChessPosition(row + incrementation, column -1);
            if (PieceOptionsCalc.isValidPosition(attackLeft) && board.getPiece(attackLeft) != null &&
                    board.getSquaresColor(attackLeft) != team) {
                moves.add(new ChessMove(position, attackLeft, promotionPiece));
            }
        }
        return moves;
    }
}
