public class Pawn extends Piece {
    public Pawn(PieceColour colour, Position position) {
        super(colour, position);
    } 

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] chessBoard) {
        int forwardDirection = colour == PieceColour.WHITE ? -1 : 1;
        int rowDiff = (newPosition.getRow() - position.getRow()) * forwardDirection;
        int columnDiff = newPosition.getColumn() - position.getColumn();

        // Forward move
        if (columnDiff == 0 && rowDiff == 1 && chessBoard[newPosition.getRow()][newPosition.getColumn()] == null) {
            return true;
        }

        // Two square move.
        boolean isStartingPosition = (colour == PieceColour.WHITE && position.getRow() == 6) || (colour == PieceColour.BLACK && position.getRow() == 1);
        if (columnDiff == 0 && rowDiff == 2 && isStartingPosition && chessBoard[newPosition.getRow()][newPosition.getColumn()] == null) {
            int middleRow = position.getRow() + forwardDirection;
            if (chessBoard[middleRow][position.getColumn()] == null) {
                return true;
            }
        }

        // Diagonal capture.
        if (Math.abs(columnDiff) == 1 && rowDiff == 1 && chessBoard[newPosition.getRow()][newPosition.getColumn()] != null && chessBoard[newPosition.getRow()][newPosition.getColumn()].colour != this.colour) {
            return true;
        }

        return false;
    }
}
