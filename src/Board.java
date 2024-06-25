public class Board {
    private Piece[][] chessBoard;

    public Board() {
        // Create 8x8 board.
        this.chessBoard = new Piece[8][8];
        setupPieces();
    }

    private void setupPieces() {
        // Rooks.
        chessBoard[0][0] = new Rook(PieceColour.BLACK, new Position(0, 0));
        chessBoard[0][7] = new Rook(PieceColour.BLACK, new Position(0, 7));
        chessBoard[7][0] = new Rook(PieceColour.WHITE, new Position(7, 0));
        chessBoard[7][7] = new Rook(PieceColour.WHITE, new Position(7, 7));

        // Knights.
        chessBoard[0][1] = new Knight(PieceColour.BLACK, new Position(0, 1));
        chessBoard[0][6] = new Knight(PieceColour.BLACK, new Position(0, 6));
        chessBoard[7][1] = new Knight(PieceColour.WHITE, new Position(7, 1));
        chessBoard[7][6] = new Knight(PieceColour.WHITE, new Position(7, 6));

        // Bishops.
        chessBoard[0][2] = new Bishop(PieceColour.BLACK, new Position(0, 2));
        chessBoard[0][5] = new Bishop(PieceColour.BLACK, new Position(0, 5));
        chessBoard[7][2] = new Bishop(PieceColour.WHITE, new Position(7, 2));
        chessBoard[7][5] = new Bishop(PieceColour.WHITE, new Position(7, 5));

        // Queens.
        chessBoard[0][3] = new Queen(PieceColour.BLACK, new Position(0, 3));
        chessBoard[7][3] = new Queen(PieceColour.WHITE, new Position(7, 3));

        // Kings.
        chessBoard[0][4] = new King(PieceColour.BLACK, new Position(0, 4));
        chessBoard[7][4] = new King(PieceColour.WHITE, new Position(7, 4));

        // Pawns.
        for (int i = 0; i < 8; i++) {
            chessBoard[1][i] = new Pawn(PieceColour.BLACK, new Position(1, i));
            chessBoard[6][i] = new Pawn(PieceColour.WHITE, new Position(6, i));
        }
    }

    public void movePiece (Position start, Position end) {
        if (chessBoard[start.getRow()][start.getColumn()] != null && chessBoard[start.getRow()][start.getColumn()].isValidMove(end, chessBoard)) {
            // Perform move.
            chessBoard[end.getRow()][end.getColumn()] = chessBoard[start.getRow()][start.getColumn()];

            // Update position
            chessBoard[end.getRow()][end.getColumn()].setPosition(end);

            // Clear start position.
            chessBoard[start.getRow()][start.getColumn()] = null;
        }
    }

    public Piece getPiece (int row, int column) {
        return chessBoard[row][column];
    }

    public Piece[][] getBoard() {
        return chessBoard;
    }

    public void setPiece(int row, int column, Piece piece) {
        chessBoard[row][column] = piece;
        if (piece != null) {
            piece.setPosition(new Position(row, column));
        }
    }
}
