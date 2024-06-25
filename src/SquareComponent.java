import javax.swing.*;
import java.awt.*;

public class SquareComponent extends JButton {
    private int row;
    private int column;

    public SquareComponent(int row, int column) {
        this.row = row;
        this.column = column;
        initButton();
    }

    private void initButton() {
        // Set button size.
        setPreferredSize(new Dimension(64, 64));

        // Set background colour.
        if ((row + column) % 2 == 0) {
            setBackground(new Color(238, 238, 210));
        } else {
            setBackground(new Color(118, 150, 86));
        }

        // Center text.
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);

        // Adjust font.
        setFont(new Font("Dejavu Sans", Font.PLAIN, 34));
    }

    public void setPieceSymbol (String symbol) {
        this.setText(symbol);
    }

    public void clearPieceSymbol() {
        this.setText("");
    }
}
