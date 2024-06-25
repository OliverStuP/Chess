public class Knight extends Piece {
    public Knight(PieceColour colour, Position position) {
        super(colour, position);
    } 

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] chessBoard) {
        if (newPosition.equals(this.position)) {
            return false;
        }

        int rowDiff = Math.abs(this.position.getRow() - newPosition.getRow());
        int columnDiff = Math.abs(this.position.getColumn() - newPosition.getColumn());

        // Check if movement is in an L shape.
        boolean isLMove = (rowDiff == 2 && columnDiff == 1) || (rowDiff == 1 && columnDiff == 2);

        if (!isLMove) {
            return false;
        }

        // Check if square is empty or contains an opposing piece.
        Piece targetPiece = chessBoard[newPosition.getRow()][newPosition.getColumn()];
        if (targetPiece == null) {
            return true;
        } else {
            return targetPiece.getColour() != this.getColour();
        }
    }
}
