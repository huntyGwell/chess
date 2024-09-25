package chess.PieveMoveCalc;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public interface PieceMoveCalc {

    //static HashSet<ChessMove> getMoves(ChessBoard board, ChessPosition currPosition) {
        //return null;
    //}

    static boolean isValidSquare(ChessPosition position) {
        return (position.getRow() >= 1 && position.getRow() <= 8) &&
                (position.getCol() >= 1 && position.getCol() <= 8);
    }

    static HashSet<ChessMove> staticMoves(ChessPosition position, int[][] potentialMoves, ChessBoard board) {
        HashSet<ChessMove> moves = HashSet.newHashSet(8);
        int x = position.getCol();
        int y = position.getRow();
        ChessGame.TeamColor team = board.getSquaresTeam(position);
        for (int[] nearMove : potentialMoves) {
            ChessPosition possiblePosition = new ChessPosition(y + nearMove[1], x + nearMove[0]);
            if (PieceMoveCalc.isValidSquare(possiblePosition) && board.getSquaresTeam(possiblePosition) != team)
                moves.add(new ChessMove(position, possiblePosition, null));
        }
        return moves;
    }

    static HashSet<ChessMove> dynamicMoves(ChessBoard board, ChessPosition position, int[][] directions, int y, int x, ChessGame.TeamColor team) {
        HashSet<ChessMove> moves = HashSet.newHashSet(27);
        for (int[] direction : directions) {
            boolean blocked = false;
            int i = 1;
            while (!blocked) {
                ChessPosition possiblePosition = new ChessPosition(y + direction[1]*i, x + direction[0]*i);
                if (!PieceMoveCalc.isValidSquare(possiblePosition)) {
                    blocked = true;
                }
                else if (board.getPiece(possiblePosition) == null) {
                    moves.add(new ChessMove(position, possiblePosition, null));
                }
                else if (board.getSquaresTeam(possiblePosition) != team) {
                    moves.add(new ChessMove(position, possiblePosition, null));
                    blocked = true;
                }
                else if (board.getSquaresTeam(possiblePosition) == team) {
                    blocked = true;
                }
                else {
                    blocked = true;
                }
                i++;
            }
        }
        return moves;
    }
}
