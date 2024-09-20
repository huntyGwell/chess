package chess.PieceOptionsCalc;

import chess.ChessGame;
import chess.ChessPosition;
import chess.ChessMove;
import chess.ChessBoard;

import java.util.HashSet;

public class QueenOptions implements PieceOptionsCalc{
    public static HashSet<ChessMove> calculateOptions(ChessBoard board, ChessPosition position) {
        int y = position.getRow();
        int x = position.getColumn();
        int[][] directions = {{-1, 1}, {0, 1}, {1, 1}, {1, 0},{1,-1},{0,-1},{-1,-1},{-1,0}};
        ChessGame.TeamColor teamColor = board.getTeamColor(position);
        return PieceOptionsCalc.CreateDirectionMoves(board, position, directions, y, x, teamColor);
    }
}
