import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MinesweeperGUI {
    private static GameState gameState;
    private static MineButton[][] mineButtons;
    private int rows = 10;
    private int cols = 10;

    public MinesweeperGUI() {
        gameState = new GameState(rows, cols, 15);
        mineButtons = new MineButton[rows][cols];
        initializeGUI();
    }

    private void initializeGUI() {
        JFrame frame = new JFrame("Minesweeper");
        frame.setLayout(new GridLayout(rows, cols));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                mineButtons[r][c] = new MineButton(r, c);
                final int row = r;
                final int col = c;

                mineButtons[r][c].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        onButtonClicked(row, col);
                    }
                });
                frame.add(mineButtons[r][c]);
            }
        }

        frame.setVisible(true);
    }
    public static void updateButton(int row, int col) {
        MineButton btn = mineButtons[row][col];
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        if (!gameState.isMine(row, col)) {
            int adjacentMines = gameState.countAdjacentMines(row, col);
            if (adjacentMines == 0) {
                btn.setText("");
            } else {
                btn.setText(String.valueOf(adjacentMines));
            }
        } else {
            btn.setText("💣");
        }

        btn.setEnabled(false);
        btn.setBackground(Color.GREEN);
    }

    private void autoOpenAdjacent(int row, int col) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;

                if (gameState.isValid(newRow, newCol) && !gameState.isRevealed(newRow, newCol)) {
                    gameState.reveal(newRow, newCol);
                    updateButton(newRow, newCol);

                    int adjacentMines = gameState.countAdjacentMines(newRow, newCol);
                    if (adjacentMines == 0) {
                        autoOpenAdjacent(newRow, newCol);
                    }
                }
            }
        }
    }

    private void revealAllMines() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (gameState.isMine(r, c)) {
                    mineButtons[r][c].setText("💣");
                    mineButtons[r][c].setBackground(Color.RED);
                }
            }
        }
    }
    public void startAutoPlay() {
        MinesweeperBot bot = new MinesweeperBot(gameState,this);
        bot.start();
    }
    private void onButtonClicked(int row, int col) {
        if (gameState.isRevealed(row, col) || gameState.isFlagged(row, col)) {
            return;
        }
        gameState.reveal(row, col);
        updateButton(row, col);
        if (gameState.isMine(row, col)) {
            revealAllMines();
            JOptionPane.showMessageDialog(null, "Game Over! Mina na poziciji " + row + ", " + col);
            System.exit(0);
        } else {
            int adjacentMines = gameState.countAdjacentMines(row, col);
            if (adjacentMines == 0) {
                autoOpenAdjacent(row, col);
            }
        }
    }

}
