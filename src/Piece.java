public abstract class Piece {
    protected Position position;
    protected PieceColour colour;

    public Piece (PieceColour colour, Position position) {
        this.colour = colour;
        this.position = position;
    }

    public PieceColour getColour() {
        return colour;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public abstract boolean isValidMove(Position newPosition, Piece[][] board);
}
