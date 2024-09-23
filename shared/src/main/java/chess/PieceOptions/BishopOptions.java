package chess.PieceOptions;

import chess.ChessGame;
import chess.ChessPosition;
import chess.ChessMove;
import chess.ChessBoard;

import java.util.HashSet;

public class BishopOptions implements PieceOptions {
    public static HashSet<ChessMove> getOptions(ChessBoard board, ChessPosition position) {
        int y = position.getRow();
        int x = position.getColumn();
        int[][] directions = {{-1, 1}, {1, 1}, {1, -1}, {-1, -1}};
        ChessGame.TeamColor teamColor = board.getSquaresColor(position);
        return PieceOptions.dynamicMoves(board, position, directions, y, x, teamColor);
    }
}
