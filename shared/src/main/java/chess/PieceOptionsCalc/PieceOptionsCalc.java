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
    static HashSet<ChessMove> GetOptions(ChessBoard board, ChessPosition currPosition) {
        return null;
    }

    //isValid position
    static boolean isValidPosition(ChessPosition position) {
        return (position.getRow() >= 1 && position.getRow() <= 8) &&
                (position.getColumn() >= 1 && position.getColumn() <= 8);
    }

    //static option
    //direction option

}
