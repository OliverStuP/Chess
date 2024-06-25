public class Rook extends Piece {
    public Rook(PieceColour colour, Position position) {
        super(colour, position);
    } 

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] chessBoard) {
        // Cannot jump over pieces.
        if (position.getRow() == newPosition.getRow()) {
            int columnStart = Math.min(position.getColumn(), newPosition.getColumn()) + 1;
            int columnEnd = Math.max(position.getColumn(), newPosition.getColumn());
            for (int column = columnStart; column < columnEnd; column++) {
                if (chessBoard[position.getRow()][column] != null) {
                    return false;
                }
            }
        } else if (position.getColumn() == newPosition.getColumn()) {
            int rowStart = Math.min(position.getRow(), newPosition.getRow()) + 1;
            int rowEnd = Math.max(position.getRow(), newPosition.getRow());
            for (int row = rowStart; row < rowEnd; row++) {
                if (chessBoard[row][position.getColumn()] != null) {
                    return false;
                }
            }
        } else {
            return false;
        }

        // Check if a capture if possible at the destination.
        Piece destinationPiece = chessBoard[newPosition.getRow()][newPosition.getColumn()];
        if (destinationPiece == null) {
            return true;
        } else if (destinationPiece.getColour() != this.getColour()) {
            return true;
        }

        return false;
    }
}
