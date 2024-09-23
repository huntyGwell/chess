package chess.PieceOptions;

import chess.ChessPosition;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessBoard;

import java.util.HashSet;

public interface PieceOptions {

//    static HashSet<ChessMove> getOptions(ChessBoard board, ChessPosition position) {
//        return null; //individual implementation
//    }


    static boolean isValidPosition(ChessPosition position) {
        return (position.getRow() >= 1 && position.getRow() <= 8) &&
                (position.getColumn() >= 1 && position.getColumn() <= 8);
    }

    static HashSet<ChessMove> stateMoves(ChessBoard board, ChessPosition position, int[][] realizableMoves) {
        HashSet<ChessMove> moves = HashSet.newHashSet(8);
        int row = position.getRow();
        int column = position.getColumn();
        ChessGame.TeamColor team = board.getSquaresColor(position);
        for(int[] realizableMove : realizableMoves) {
            ChessPosition newPosition = new ChessPosition(row + realizableMove[1], column + realizableMove[0]);
            if(isValidPosition(newPosition) && board.getSquaresColor(newPosition) != team)
                moves.add(new ChessMove(position, newPosition, null));
        }
        return moves;
    }
    static HashSet<ChessMove> dynamicMoves(ChessBoard board, ChessPosition position, int[][] moveDir,
                                           int row, int column, ChessGame.TeamColor teamColor) {
        HashSet<ChessMove> moves = HashSet.newHashSet(83);
        for (int[] direction : moveDir) {
            boolean open = true;
            int i = 1;
            while (open) {
                ChessPosition newPosition = new ChessPosition(row + direction[1] * i, column + direction[0] * i);
                if (!isValidPosition(newPosition)) {
                    open = false;
                } else if (board.getPiece(newPosition) == null) {
                    moves.add(new ChessMove(position, newPosition, null));
                } else if (board.getSquaresColor(newPosition) != teamColor) {
                    moves.add(new ChessMove(position, newPosition, null));
                    open = false;
                } else if (board.getSquaresColor(newPosition) == teamColor) {
                    open = false;
                }
                else {
                    open= false;
                }
                i++;
            }
        }
        return moves;
    }
}
