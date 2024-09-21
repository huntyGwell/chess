package chess.PieceOptionsCalc;

//imports
//will need to import the chess.* and maybe some kind of hashing if not already included..

import chess.ChessPosition;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessBoard;

import java.util.HashSet;

public interface PieceOptionsCalc {
    //get Options
        //return null (implement in the Options * classes)
    static HashSet<ChessMove> GetOptions(ChessBoard board, ChessPosition Position) {
        return null; //I want to spend more time understanding java
    }

    //isValid position
    static boolean isValidPosition(ChessPosition position) {
        return (position.getRow() >= 1 && position.getRow() <= 8) &&
                (position.getColumn() >= 1 && position.getColumn() <= 8);
    }

    //static option
    static HashSet<ChessMove> straightMoves(ChessBoard board, ChessPosition position, int[][] realizableMoves) {
        HashSet<ChessMove> moves = HashSet.newHashSet(8);
        int row = position.getRow();
        int column = position.getColumn();
        ChessGame.TeamColor teamColor = board.getTeamColor(position);
        for(int[] realizableMove : realizableMoves) {
            ChessPosition newPosition = new ChessPosition(row + realizableMove[1], column + realizableMove[0]);//modify with realizableMoves
            if(isValidPosition(newPosition) && board.getSquaresColor(newPosition) != teamColor) {
                moves.add(new ChessMove(position, newPosition, null));
            }
        }
        return moves;
    }
    //direction option
    static HashSet<ChessMove> crossMoves(ChessBoard board, ChessPosition position, int[][] moveVec,
                                         int row, int column, ChessGame.TeamColor teamColor) {
        HashSet<ChessMove> moves = HashSet.newHashSet(83);
        for (int[] direction : moveVec) {
            boolean closed = false;
            int i = 1;
            while (!closed) {
                ChessPosition newPosition = new ChessPosition(row + direction[1] * i, column + direction[0] * i);
                if (!isValidPosition(newPosition)) {
                    closed = true;
                } else if (board.getPiece(newPosition) == null) {
                    moves.add(new ChessMove(position, newPosition, null));
                } else if (board.getSquaresColor(newPosition) != teamColor) {
                    moves.add(new ChessMove(position, newPosition, null));
                    closed = true;
                } else if (board.getSquaresColor(newPosition) == teamColor) {
                    closed = true;
                }
                else {
                    closed= true;
                }
                i++;
            }
        }
        return moves;
    }
}
