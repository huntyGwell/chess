package chess.PieceMoveCalc;

import chess.*;

import java.util.HashSet;

public class PawnCalc implements PieceMoveCalc{
    public static HashSet<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> moves = HashSet.newHashSet(16);
        int col = position.getColumn();
        int row = position.getRow();
        ChessPiece.PieceType[] promotionPieces = new ChessPiece.PieceType[]{null};

        ChessGame.TeamColor team = board.getSquaresTeam(position);
        int increment = team == ChessGame.TeamColor.WHITE ? 1 : -1;

        boolean promote = (team == ChessGame.TeamColor.WHITE && row == 7) || (team == ChessGame.TeamColor.BLACK && row == 2);
        if (promote) {
            promotionPieces = new ChessPiece.PieceType[]{ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN};
        }
        for (ChessPiece.PieceType promotionPiece : promotionPieces) {
            ChessPosition forwardPosition = new ChessPosition(row + increment, col);
            if (PieceMoveCalc.isValidSquare(forwardPosition) && board.getPiece(forwardPosition) == null) {
                moves.add(new ChessMove(position, forwardPosition, promotionPiece));
            }
            ChessPosition attackLeft = new ChessPosition(row + increment, col-1);
            if (PieceMoveCalc.isValidSquare(attackLeft) &&
                    board.getPiece(attackLeft) != null &&
                    board.getSquaresTeam(attackLeft) != team) {
                moves.add(new ChessMove(position, attackLeft, promotionPiece));
            }
            ChessPosition attackRight = new ChessPosition(row + increment, col+1);
            if (PieceMoveCalc.isValidSquare(attackRight) &&
                    board.getPiece(attackRight) != null &&
                    board.getSquaresTeam(attackRight) != team) {
                moves.add(new ChessMove(position, attackRight, promotionPiece));
            }
            ChessPosition doubleJump = new ChessPosition(row + increment*2, col);
            if (PieceMoveCalc.isValidSquare(doubleJump) &&
                    ((team == ChessGame.TeamColor.WHITE && row == 2) || (team == ChessGame.TeamColor.BLACK && row == 7)) &&
                    board.getPiece(doubleJump) == null &&
                    board.getPiece(forwardPosition) == null) {
                moves.add(new ChessMove(position, doubleJump, promotionPiece));
            }

        }
        return moves;
    }
}