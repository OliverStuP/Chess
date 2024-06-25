public class Bishop extends Piece {
    public Bishop(PieceColour colour, Position position) {
        super(colour, position);
    } 

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] chessBoard) {
        int rowDiff = Math.abs(position.getRow() - newPosition.getRow());
        int columnDiff = Math.abs(position.getColumn() - newPosition.getColumn());

        // Check if diagonal.
        if (rowDiff != columnDiff) {
            return false;
        }

       int rowStep = newPosition.getRow() > position.getRow() ? 1 : -1;
       int columnStep = newPosition.getColumn() > position.getColumn() ? 1 : -1;
       int steps = Math.abs(rowDiff) - 1;

       for (int i = 1; i <= steps; i++) {
            if (chessBoard[position.getRow() + i * rowStep][position.getColumn() + i * columnStep] != null) {
                return false;
            }
       }

       Piece destinationPiece = chessBoard[newPosition.getRow()][newPosition.getColumn()];
       if (destinationPiece == null) {
            return true;
       } else if (destinationPiece.getColour() != this.getColour()) {
            return true;
       }

       return false;
    }
}
