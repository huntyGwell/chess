package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor colorsMove;
    private ChessBoard board;

    //gameOver Flag bool

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
        return colorsMove;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        colorsMove = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
        //anything else needed to enum??
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
            board.addPiece(move.getStartPosition(), piece); //this throws an error too----fixed ... i think!
        }
        return validMoves;
        }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException { //should this throw an exception??---
                                                    // ---yes it should!! lol
                                                    //lol need to read better!
        boolean isColorsTurn = getTeamTurn() == board.getTeamColor(move.getStartPosition());
        Collection<ChessMove> availableMoves = validMoves(move.getStartPosition());
        if (availableMoves == null) {
            throw new InvalidMoveException("no moves available");
        }
        boolean isValidMove = availableMoves.contains(move);
        if (isValidMove && isColorsTurn) {
            ChessPiece temporary = board.getPiece(move.getEndPosition());
            if (move.getPromotionPiece() != null) {
                temporary = new ChessPiece(temporary.getTeamColor(), move.getPromotionPiece());
            }
            board.addPiece(move.getStartPosition(), null);
            board.addPiece(move.getEndPosition(), temporary);
            setTeamTurn(getTeamTurn() == TeamColor.BLACK ? TeamColor.WHITE : TeamColor.BLACK); // ternary operator
            //maybe switch the colors
        }
        else {
            throw new InvalidMoveException(String.format("valid move! %b now is %b 's turn",isValidMove,
                    isColorsTurn)); //why does the font on format look like that?
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
        for (int i = 0; i < 8; i++) {
            for (int ii = 0; ii < 8; ii++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, ii));
                if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == //consider removing hte null check??
                        ChessPiece.PieceType.KING) {
                    kingSpot = new ChessPosition(i, ii);
                }
            }
        }
        for (int i = 0; i < 8; i++) { //=================refractor the null check
            for (int ii = 0; ii < 8; ii++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, ii));
                if (piece == null || !piece.getTeamColor().equals(teamColor)) {
                    continue;
                }
                for (ChessMove opponentMove : validMoves(kingSpot)) { //look here 

                }
            }
        }
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //needs to be in check and stalemate
        //set gameOver to true
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
    //game over flag??
    //Overrides
}
