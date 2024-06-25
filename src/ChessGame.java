import java.util.ArrayList;
import java.util.List;

public class ChessGame {
    private Board board;
    // Ensure that white starts the game.
    private boolean whiteTurn = true;
    // Initialise King status flag.
    private boolean kingDead = false;

    public ChessGame() {
        this.board = new Board();
    }

    public boolean getKingState() {
        return kingDead;
    }

    public boolean makeMove(Position start, Position end) {
        Piece movingPiece = board.getPiece(start.getRow(), start.getColumn());
        if (movingPiece == null || movingPiece.getColour() != (whiteTurn ? PieceColour.WHITE : PieceColour.BLACK)) {
            return false;
        }

        if (movingPiece.isValidMove(end, board.getBoard())) {
            board.movePiece(start, end);
            whiteTurn = !whiteTurn;
            return true;
        }
        return false;
    }

    public boolean inCheck(PieceColour kingColour) {
        Position kingPosition = findKingPosition(kingColour);
        for (int row = 0; row < board.getBoard().length; row++) {
            for (int column = 0; column < board.getBoard()[row].length; column++) {
                Piece piece = board.getPiece(row, column);
                if (piece != null && piece.getColour() != kingColour) {
                    if (!getKingState() && piece.isValidMove(kingPosition, board.getBoard())) {
                            return true;
                    }
                    else if (getKingState()) {
                        isCheckmate(kingColour);
                    }
                }
            }
        }
        return false;
    }

    private Position findKingPosition(PieceColour colour) {
        for (int row = 0; row < board.getBoard().length; row++) {
            for (int column = 0; column < board.getBoard()[row].length; column++) {
                Piece piece = board.getPiece(row, column);
                if (piece instanceof King && piece.getColour() == colour) {
                    return new Position(row, column);
                }
            }
        }
        kingDead = true;
        return null;
    }

    public boolean isCheckmate(PieceColour kingColour) {
        if (getKingState()) {
            return true;
        }
        if (!inCheck(kingColour)) {
            return false;
        }

        Position kingPosition = findKingPosition(kingColour);
        if (kingPosition == null) {
            return true;
        }
        King king = (King) board.getPiece(kingPosition.getRow(), kingPosition.getColumn());

        // Find a move that saves the king.
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                if (rowOffset == 0 && columnOffset == 0) {
                    continue;
                }
                Position newPosition = new Position(kingPosition.getRow() + rowOffset, kingPosition.getColumn() + columnOffset);

                // Check if next move is legal and does not result in a check.
                if (isPositionOnBoard(newPosition) && king.isValidMove(newPosition, board.getBoard()) && !inCheckAfter(kingColour, kingPosition, newPosition)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isPositionOnBoard(Position position) {
        return position.getRow() >= 0 && position.getRow() < board.getBoard().length && position.getColumn() >= 0 && position.getColumn() < board.getBoard()[0].length;
    }

    private boolean inCheckAfter(PieceColour kingColour, Position current, Position destination) {
        // Simulate the move.
        Piece temp = board.getPiece(destination.getRow(), destination.getColumn());
        board.setPiece(destination.getRow(), destination.getColumn(), board.getPiece(current.getRow(), current.getColumn()));
        board.setPiece(current.getRow(), current.getColumn(), null);

        boolean checkPos = inCheck(kingColour);

        // Undo the simulation.
        board.setPiece(current.getRow(), current.getColumn(), board.getPiece(destination.getRow(), destination.getColumn()));
        board.setPiece(destination.getRow(), destination.getColumn(), temp);

        return checkPos;
    }

    public Board getBoard() {
        return this.board;
    }

    private Position selectedPosition;

    public boolean handleSquareSelection(int row, int column) {
        if (selectedPosition == null) {
            Piece selectedPiece = board.getPiece(row, column);
            if (selectedPiece != null && selectedPiece.getColour() == (whiteTurn ? PieceColour.WHITE : PieceColour.BLACK)) {
                selectedPosition = new Position(row, column);
                return false;
            } 
        } else {
            boolean moveMade = makeMove(selectedPosition, new Position(row, column));
            selectedPosition = null;
            return moveMade;
        }
        return false;
    }

    public PieceColour getCurrentPlayerColour() {
        return whiteTurn ? PieceColour.WHITE : PieceColour.BLACK;
    }

    public void resetGame() {
        this.board = new Board();
        this.whiteTurn = true;
        kingDead = false;
    }

    public boolean isPieceSelected() {
        return selectedPosition != null;
    }

    public List<Position> getLegalMoves(Position position) {
        Piece selectedPiece = board.getPiece(position.getRow(), position.getColumn());
        if (selectedPiece == null) {
            return new ArrayList<>();
        }
        List<Position> legalMoves = new ArrayList<>();
        switch (selectedPiece.getClass().getSimpleName()) {
            case "Pawn": addPawnMoves(position, selectedPiece.getColour(), legalMoves);
                break;
            case "Rook": addLineMoves(position, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}, legalMoves);
                break;
            case "Knight": addSingleMoves(position, new int[][] {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {-1, 2}, {1, -2}, {-1, -2}}, legalMoves);
                break;
            case "Bishop": addLineMoves(position, new int[][]{{1, 1}, {-1, -1}, {1, -1}, {-1, 1}}, legalMoves);
                break;
            case "Queen": addLineMoves(position, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}}, legalMoves);
                break;
            case "King": addSingleMoves(position, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}}, legalMoves);
                break;
        }
        return legalMoves;
    }

    private void addPawnMoves(Position position, PieceColour colour, List<Position> legalMoves) {
        int direction = colour == PieceColour.WHITE ? -1 : 1;
        // Single move.
        Position newPosition = new Position(position.getRow() + direction, position.getColumn());
        if (isPositionOnBoard(newPosition) && board.getPiece(newPosition.getRow(), newPosition.getColumn()) == null) {
            legalMoves.add(newPosition);
        }
        // Double move from starting point.
        if ((colour == PieceColour.WHITE && position.getRow() == 6) || (colour == PieceColour.BLACK && position.getRow() == 1)) {
            newPosition = new Position(position.getRow() + 2 * direction, position.getColumn());
            Position interPosition = new Position(position.getRow() + direction, position.getColumn());
            if (isPositionOnBoard(newPosition) && board.getPiece(newPosition.getRow(), newPosition.getColumn()) == null && board.getPiece(interPosition.getRow(), interPosition.getColumn()) == null) {
                legalMoves.add(newPosition);
            }
        }
        // Captures.
        int[] captureColumns = {position.getColumn() - 1, position.getColumn() + 1};
        for (int column : captureColumns) {
            newPosition = new Position(position.getRow() + direction, column);
            if (isPositionOnBoard(newPosition) && board.getPiece(newPosition.getRow(), newPosition.getColumn()) != null && board.getPiece(newPosition.getRow(), newPosition.getColumn()).getColour() != colour) {
                legalMoves.add(newPosition);
            }
        }
    }

    private void addLineMoves(Position position, int[][] directions, List<Position> legalMoves) {
        for (int[] dir : directions) {
            Position newPosition = new Position(position.getRow() + dir[0], position.getColumn() + dir[1]);
            while (isPositionOnBoard(newPosition)) {
                if (board.getPiece(newPosition.getRow(), newPosition.getColumn()) == null) {
                    legalMoves.add(new Position(newPosition.getRow(), newPosition.getColumn()));
                    newPosition = new Position(newPosition.getRow() + dir[0], newPosition.getColumn() + dir[1]);
                }
                else {
                    if (board.getPiece(newPosition.getRow(), newPosition.getColumn()).getColour() != board.getPiece(position.getRow(), position.getColumn()).getColour()) {
                        legalMoves.add(newPosition);                        
                    }
                    break;
                }
            }
        }
    }

    private void addSingleMoves(Position position, int[][] moves, List<Position> legalMoves) {
        for (int[] move : moves) {
            Position newPosition = new Position(position.getRow() + move[0], position.getColumn() + move[1]);
            if (isPositionOnBoard(newPosition) && (board.getPiece(newPosition.getRow(), newPosition.getColumn()) == null || board.getPiece(newPosition.getRow(), newPosition.getColumn()).getColour() != board.getPiece(position.getRow(), position.getColumn()).getColour())) {
                legalMoves.add(newPosition);
            }
        }
    }

    // Add castle.

    /* private boolean detectStalemate() {

        return false;
    } */

    // Add en passant.

    // Add A.I.
}
