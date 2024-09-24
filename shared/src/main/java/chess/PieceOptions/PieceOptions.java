package chess.PieceOptions;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public interface PieceOptions {

    static HashSet<ChessMove> getOptions(ChessBoard board, ChessPosition currPosition) {
        return null;
    }

    static boolean isValidPosition(ChessPosition position) {
        return (position.getRow() >= 1 && position.getRow() <= 8) &&
                (position.getColumn() >= 1 && position.getColumn() <= 8);
    }

    // Generate all possible moves relating to current position using the static relative moves
    static HashSet<ChessMove> stateMoves(ChessBoard board,ChessPosition position, int[][] relativeMoves) {
        HashSet<ChessMove> moves = HashSet.newHashSet(8); //8 is the max number of moves of a Knight

        int currX = position.getColumn();
        int currY = position.getRow();

        ChessGame.TeamColor team = board.getSquaresColor(position);
        for (int[] relativeMove : relativeMoves) {
            ChessPosition possiblePosition = new ChessPosition(currY + relativeMove[1], currX + relativeMove[0]);
            if (PieceOptions.isValidPosition(possiblePosition) && board.getSquaresColor(possiblePosition) != team)
                moves.add(new ChessMove(position, possiblePosition, null));
        }
        return moves;
    }

    static HashSet<ChessMove> dynamicMoves(ChessBoard board, ChessPosition currPosition, int[][] moveDirections, int currY, int currX, ChessGame.TeamColor team) {
        HashSet<ChessMove> moves = HashSet.newHashSet(27);
        for (int[] direction : moveDirections) {
            boolean obstructed = false;
            int i = 1;
            while (!obstructed) {
                ChessPosition possiblePosition = new ChessPosition(currY + direction[1]*i, currX + direction[0]*i);
                if (!PieceOptions.isValidPosition(possiblePosition)) {
                    obstructed = true;
                }
                else if (board.getPiece(possiblePosition) == null) {
                    moves.add(new ChessMove(currPosition, possiblePosition, null));
                }
                else if (board.getSquaresColor(possiblePosition) != team) {
                    moves.add(new ChessMove(currPosition, possiblePosition, null));
                    obstructed = true;
                }
                else if (board.getSquaresColor(possiblePosition) == team) {
                    obstructed = true;
                }
                else {
                    obstructed = true;
                }
                i++;
            }
        }
        return moves;
    }

}
