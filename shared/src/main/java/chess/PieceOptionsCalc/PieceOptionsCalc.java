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
    static HashSet<ChessMove> CreateStateMoves(ChessBoard board, ChessPosition position, int[][] realizableMoves) {
        HashSet<ChessMove> moves = HashSet.newHashSet(8);
        int row = position.getRow();
        int column = position.getColumn();
        ChessGame.TeamColor teamColor = board.getTeamColor(position);
        for(int i = 0; i < realizableMoves.length; i++) {
            ChessPosition newPosition = new ChessPosition(row, column);//modify with realizableMoves
        }
    }
    //direction option
    static HashSet<ChessMove> CreateDirectionMoves(ChessBoard board, ChessPosition position) {
        return null; //this is all wrong
    }

}
