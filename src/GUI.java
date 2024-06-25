import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GUI extends JFrame {
    private final SquareComponent[][] squares = new SquareComponent[8][8];
    private final ChessGame game = new ChessGame();

    private final Map<Class<? extends Piece>, String> blackPieceUnicodeMap = new HashMap<>() {
        {
            put(Pawn.class, "\u265F");
            put(Rook.class, "\u265C");
            put(Knight.class, "\u265E");
            put(Bishop.class, "\u265D");
            put(Queen.class, "\u265B");
            put(King.class, "\u265A");
        }
    };

    private final Map<Class<? extends Piece>, String> whitePieceUnicodeMap = new HashMap<>() {
        {
            put(Pawn.class, "\u2659");
            put(Rook.class, "\u2656");
            put(Knight.class, "\u2658");
            put(Bishop.class, "\u2657");
            put(Queen.class, "\u2655");
            put(King.class, "\u2654");
        }
    };

    public GUI() {
        setTitle("Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));
        initialiseBoard();
        resetOption();
        pack();
        setVisible(true);
        setResizable(false);
    }

    private void initialiseBoard() {
        for (int row = 0; row < squares.length; row++) {
            for (int column = 0; column < squares[row].length; column++) {
                final int lastRow = row;
                final int lastColumn = column;
                SquareComponent square = new SquareComponent(row, column);
                square.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent event) {
                        handleSquareClick(lastRow, lastColumn);
                    }
                });
                add(square);
                squares[row][column] = square;
            }
        }
        refreshBoard();
    }

    private void refreshBoard() {
        Board board = game.getBoard();
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                Piece piece = board.getPiece(row, column);
                if (piece != null) {
                    // Determine what Unicode map should be used based on colour.
                    Color colour = (piece.getColour() == PieceColour.WHITE) ? Color.WHITE : Color.BLACK;
                    String symbol = whitePieceUnicodeMap.get(piece.getClass());
                    if (colour == Color.BLACK) {
                        symbol = blackPieceUnicodeMap.get(piece.getClass());
                    }
                    squares[row][column].setPieceSymbol(symbol);
                }
                else {
                    squares[row][column].clearPieceSymbol();
                }
            }
        }
    }

    private void handleSquareClick(int row, int column) {
        boolean moveResult = game.handleSquareSelection(row, column);
        clearHighlights();
        if (moveResult) {
            refreshBoard();
            checkGameState();
            checkGameOver();
        } else if (game.isPieceSelected()) {
            highlightLegalMoves(new Position(row, column));
        }
        refreshBoard();
    }

    private void checkGameState() {
        promotePawn();
        PieceColour currentPlayer = game.getCurrentPlayerColour();
        String checkPiece = "White";
        if (game.getCurrentPlayerColour() == PieceColour.BLACK) {
            checkPiece = "Black";
        }
        boolean checkPos = game.inCheck(currentPlayer);
        if (checkPos) {
            JOptionPane.showMessageDialog(this, checkPiece + " is in check.");
        }
    }

    private void highlightLegalMoves (Position position) {
        List<Position> legalMoves = game.getLegalMoves(position);
        for (Position move : legalMoves) {
            squares[move.getRow()][move.getColumn()].setBackground(new Color(186, 202, 68));
        }
    }

    private void clearHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                squares[row][column].setBackground((row + column) % 2 == 0 ? new Color(238, 238, 210) : (new Color(118, 150, 86)));
            }
        }
    }

    private void checkGameOver() {
        if (game.isCheckmate(game.getCurrentPlayerColour())) {
           gameOver(game.getCurrentPlayerColour());
        }
    }

    private void gameOver(PieceColour colour) {
        String winner = "White";
        if (colour == PieceColour.WHITE) {
            winner = "Black";
        }
        int response = JOptionPane.showConfirmDialog(this, "Checkmate! " + winner + " wins. Play again?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            System.exit(0);
        }
    }

    private void resetGame() {
        game.resetGame();
        refreshBoard();
    }

    private void resetOption() {
        // Add themes.
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Menu");
        JMenuItem reset = new JMenuItem("Reset");
        reset.addActionListener(e -> resetGame());
        gameMenu.add(reset);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }

    private void promotePawn() {
        PieceColour currentPlayer = game.getCurrentPlayerColour();
        PieceColour upColour = game.getCurrentPlayerColour();
        if (currentPlayer == PieceColour.WHITE) {
            upColour = PieceColour.BLACK;
        }
        else {
            upColour = PieceColour.WHITE;
        }
        Board board = game.getBoard();
        // Get row the opposite player can promote a pawn in.
        int row = 0;
        if (currentPlayer == PieceColour.WHITE) {
            row = 7;
        }
        for (int column = 0; column < squares[row].length; column++) {
            Piece piece = board.getPiece(row, column);
            // Check if the opposite player can promote a pawn.
            if (piece instanceof Pawn && piece.getColour() != currentPlayer) {
                JPanel promotion = new JPanel();
                JLabel label = new JLabel();
                label.setText("What piece will you promote your pawn to?");
                JRadioButton queen = new JRadioButton("\u265B");
                JRadioButton bishop = new JRadioButton("\u265D");
                JRadioButton knight = new JRadioButton("\u265E");
                JRadioButton rook = new JRadioButton("\u265C");
                promotion.setLayout(new BoxLayout(promotion, BoxLayout.Y_AXIS));
                promotion.add(label);
                promotion.add(queen);
                promotion.add(bishop);
                promotion.add(knight);
                promotion.add(rook);
                JOptionPane.showMessageDialog(null, promotion);
                if (queen.isSelected()) {
                    board.setPiece(row, column, new Queen(upColour, null));;
                }
                if (bishop.isSelected()) {
                    board.setPiece(row, column, new Bishop(upColour, null));;
                }
                if (knight.isSelected()) {
                    board.setPiece(row, column, new Knight(upColour, null));;
                }
                if (rook.isSelected()) {
                    board.setPiece(row, column, new Rook(upColour, null));;
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
