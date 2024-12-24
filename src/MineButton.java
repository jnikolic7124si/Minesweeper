import javax.swing.*;

public class MineButton extends JButton {
    private int row;
    private int col;

    public MineButton(int row, int col) {
        this.row = row;
        this.col = col;
        this.setText("");
        this.setFocusPainted(false);
        this.setPreferredSize(new java.awt.Dimension(50, 50));
    }
}
