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
    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        board = new ChessBoard();
        setTeamTurn(TeamColor.WHITE);
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK;
        public String toString() {return this == WHITE ? "White" : "Black";}
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece currentPiece = board.getPiece(startPosition);
        if(currentPiece == null) {
            return null;
        }
        HashSet<ChessMove> possibleMoves = (HashSet<ChessMove>)
                board.getPiece(startPosition).pieceMoves(board, startPosition);
        HashSet<ChessMove> validMoves = HashSet.newHashSet(possibleMoves.size());
        for (ChessMove move : possibleMoves) {
            ChessPiece temp = board.getPiece(move.getEndPosition());
            board.addPiece(startPosition, null);
            board.addPiece(move.getEndPosition(), currentPiece);
            if(!isInCheck(currentPiece.getTeamColor())) {
                validMoves.add(move);
            }
            board.addPiece(move.getEndPosition(), temp);
            board.addPiece(move.getStartPosition(), currentPiece);
        }
        return validMoves;
    }
    // is next king move valid
    private boolean isNextMoveValid(TeamColor teamColor) {
        ChessPosition kingPos = findKing(teamColor);
        //get all possible moves valid moves. see if any of those valid move put king in check
        Collection<ChessMove> hypotheticalMoves = validMoves(kingPos);
        for(ChessMove move : hypotheticalMoves) {
            //testing the move
            ChessPiece tempKing = board.getPiece(move.getEndPosition());
            board.addPiece(move.getStartPosition(), null);
            board.addPiece(move.getEndPosition(), board.getPiece(kingPos));
            boolean isSafe = isInCheck(teamColor);//checking
            //undoing the move
            board.addPiece(move.getEndPosition(), tempKing);
            board.addPiece(move.getStartPosition(), board.getPiece(kingPos));
            if(isSafe) {
                return true;
            }
        }
        return false;
    }
    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */

    public void makeMove(ChessMove move) throws InvalidMoveException {
        boolean isTeamTurn = getTeamTurn() == board.getSquaresTeam(move.getStartPosition());
        Collection<ChessMove> goodMoves = validMoves(move.getStartPosition());
        if (goodMoves == null) {
            throw new InvalidMoveException("No valid moves");//-------exiting here
        }//kay so it should not be exiting when it is. ... need some debug on this
        boolean isValid = goodMoves.contains(move);
        if (isValid && isTeamTurn) {
            ChessPiece movePiece = board.getPiece(move.getStartPosition());
            if (move.getPromotionPiece() != null) {
                movePiece = new ChessPiece(movePiece.getTeamColor(), move.getPromotionPiece());
            }
            board.addPiece(move.getStartPosition(), null);
            board.addPiece(move.getEndPosition(), movePiece);
            setTeamTurn(getTeamTurn() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
        }
        else {
            throw new InvalidMoveException("valid move, next turn");//format output
        }
    }

    //===============================//
    //make move helper

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    //double check here... as i debug i see problems coming from here.
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor);
        if(kingPosition == null) {
            return false;
        }
        for (int y = 1; y <= 8; y++) {
            for (int x = 1; x <= 8; x++) {
                ChessPiece currentPiece = board.getPiece(new ChessPosition(y, x));
                if (currentPiece == null || currentPiece.getTeamColor() == teamColor) {
                    continue;
                }
                for (ChessMove attack : currentPiece.pieceMoves(board, new ChessPosition(y, x))) {
                    if (attack.getEndPosition().equals(kingPosition)) {
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
        //because stalemate checks for isInCheck this won't work
        //need to tune this up
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    //double check here... as i debug i see problems coming from here.
    //==================================================================def an issue here
    public boolean isInStalemate(TeamColor teamColor) {
        if (kingCannotMove(teamColor))
            return false;
        if (isNextMoveValid(teamColor))//-----------------------------------------
            return true;//--------------------------------------------------------
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if(piece != null && piece.getTeamColor() == teamColor) {
                    if (!validMoves(position).isEmpty()) {
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
    //can king move checker
    public boolean kingCannotMove(TeamColor teamColor) {
        ChessPosition kingPos = findKing(teamColor);
        if (kingPos == null) {
            return false;
        }
        Collection<ChessMove> possibleMoves = validMoves(kingPos);
        for (ChessMove move : possibleMoves) {
            if (move.getEndPosition().equals(kingPos)) {
                return validMoves(move.getStartPosition()).isEmpty();//worry
            }
        }
        if (possibleMoves.isEmpty()) {
            return false;
        }
        return true;
    }
    private ChessPosition findKing(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor &&
                        piece.getPieceType() == ChessPiece.PieceType.KING) {
                    return new ChessPosition(row, col);
                }
            }
        }
        return null;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", teamTurn=" + teamTurn +
                '}';
    }
}
