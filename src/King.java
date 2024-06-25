public class King extends Piece {
    public King(PieceColour colour, Position position) {
        super(colour, position);
    } 

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] chessBoard) {
        int rowDiff = Math.abs(this.position.getRow() - newPosition.getRow());
        int columnDiff = Math.abs(this.position.getColumn() - newPosition.getColumn());

        if (!(rowDiff <= 1 && columnDiff <= 1 && !(rowDiff == 0 && columnDiff == 0))) {
            return false;
        }
        // Check if square is empty or contains an opposing piece.
        Piece targetPiece = chessBoard[newPosition.getRow()][newPosition.getColumn()];
        return targetPiece == null || targetPiece.getColour() != this.getColour();
    }
}
