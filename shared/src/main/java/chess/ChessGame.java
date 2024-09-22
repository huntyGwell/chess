package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor colorTurn;
    private ChessBoard board;

    public ChessGame() {
        board = new ChessBoard();
        setTeamTurn(TeamColor.WHITE); //white moves first
    }
    //holy mother this is a big one!
    //I should probably do this sooner rather than later to get it done and debugged

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return colorTurn;
    }
    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        colorTurn = team;
    }
    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK;
        public String toString() {
            return this == WHITE ? "white" : "black";
        }
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        HashSet<ChessMove> moves = (HashSet<ChessMove>) board.getPiece(startPosition).pieceMoves(board, startPosition);
        HashSet<ChessMove> validMoves = HashSet.newHashSet(moves.size());
        for (ChessMove move : moves) {
            ChessPiece temporary = board.getPiece(move.getEndPosition());
            board.addPiece(startPosition, null);
            board.addPiece(move.getEndPosition(), piece); //jinkies i am not sure here (temp)-----fixed!
            if (!isInCheck(piece.getTeamColor())) {
                validMoves.add(move);
            }//otherwise revert
            board.addPiece(move.getEndPosition(), temporary);
            board.addPiece(startPosition, piece); //this throws an error too----fixed ... i think!
        }
        return validMoves;
        }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        boolean isColorTurn = getTeamTurn() == board.getTeamColor(move.getStartPosition());
        Collection<ChessMove> availableMoves = validMoves(move.getStartPosition());
        if (availableMoves == null) {
            throw new InvalidMoveException("no moves available");
        }
        boolean isValidMove = availableMoves.contains(move);
        if (isValidMove && isColorTurn) {
            ChessPiece movePiece = board.getPiece(move.getEndPosition());
            if (move.getPromotionPiece() != null) {
                movePiece = new ChessPiece(movePiece.getTeamColor(), move.getPromotionPiece());
            }
            board.addPiece(move.getStartPosition(), null);
            board.addPiece(move.getEndPosition(), movePiece);
            setTeamTurn(getTeamTurn() == TeamColor.BLACK ? TeamColor.WHITE : TeamColor.BLACK);
            //maybe switch the colors
        }
        else {
            throw new InvalidMoveException(String.format("valid move %b Your turn %b",isValidMove,
                    isColorTurn)); //why does the font on format look like that?
        }//nested if and check if the correct team is up
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingSpot = null;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                ChessPiece piece = board.getPiece(new ChessPosition(y, x));
                if(piece == null) {
                    continue;
                }
                if (piece.getTeamColor() == teamColor && piece.getPieceType() == //consider removing
                        ChessPiece.PieceType.KING) {
                    kingSpot = new ChessPosition(y, x);
                }
            }
        }
        for (int y = 0; y < 8; y++) { //=================refractor the null check
            for (int x = 0; x < 8; x++) {
                ChessPiece piece = board.getPiece(new ChessPosition(y, x));
                if (piece == null || !piece.getTeamColor().equals(teamColor)) {
                    continue;
                }
                for (ChessMove opponentMove : piece.pieceMoves(board, new ChessPosition(y, x))) { //look here
                    if (opponentMove.getEndPosition().equals(kingSpot)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && isInStalemate(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //set gameOver to true??
        for (int i = 1; i <= 8; i++) {
            for (int ii = 1; ii <= 8; ii++) {
                ChessPosition position = new ChessPosition(i, ii);
                ChessPiece piece = board.getPiece(position);
                Collection<ChessMove> moves;// = validMoves(position);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    moves = validMoves(position);
                    if (moves != null && !moves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
    @Override
    public String toString() {
        return "ChessGame{" + "teamTurn=" + colorTurn + ", board=" + board + '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return colorTurn == chessGame.colorTurn && board.equals(chessGame.board);
    }
    @Override
    public int hashCode() {
        return Objects.hash(colorTurn, board);
    }
}
