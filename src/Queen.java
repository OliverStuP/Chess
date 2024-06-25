public class Queen extends Piece {
    public Queen(PieceColour colour, Position position) {
        super(colour, position);
    } 

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] chessBoard) {
        if (newPosition.equals(this.position)) {
            return false;
        }

        int rowDiff = Math.abs(this.position.getRow() - newPosition.getRow());
        int columnDiff = Math.abs(this.position.getColumn() - newPosition.getColumn());

        // Check straight movement.
        boolean straightLine = this.position.getRow() == newPosition.getRow() || this.position.getColumn() == newPosition.getColumn();

        // Check diagonal movement.
        boolean diagonal = rowDiff == columnDiff;

        if (!straightLine && !diagonal) {
            return false;
        }

        // Calculate movement direction.
        int rowDirection = Integer.compare(newPosition.getRow(), this.position.getRow());
        int columnDirection = Integer.compare(newPosition.getColumn(), this.position.getColumn());

        // Check for pieces in path.
        int currentRow = this.position.getRow() + rowDirection;
        int currentColumn = this.position.getColumn() + columnDirection;
        while (currentRow != newPosition.getRow() || currentColumn != newPosition.getColumn()) {
            if (chessBoard[currentRow][currentColumn] != null) {
                return false;
            }
            currentRow += rowDirection;
            currentColumn += columnDirection;
        }

        // Check if square is empty or contains an opposing piece.
        Piece targetPiece = chessBoard[newPosition.getRow()][newPosition.getColumn()];
        return targetPiece == null || targetPiece.getColour() != this.getColour();
    }
}
