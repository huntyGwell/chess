package chess.PieveMoveCalc;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class BishopCalc implements PieceMoveCalc{

    public static HashSet<ChessMove> getMoves(ChessBoard board, ChessPosition position) {
        int col = position.getCol();
        int row = position.getRow();
        int[][] moves = {{-1, 1}, {1, 1}, {1, -1}, {-1, -1}};
        ChessGame.TeamColor team = board.getSquaresTeam(position);

        return PieceMoveCalc.dynamicMoves(board, position, moves, row, col, team);
    }
}
