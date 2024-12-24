import java.util.Random;

public class GameState {
    private boolean[][] mines;
    private boolean[][] revealed;
    private boolean[][] flagged;
    private int rows;
    private int cols;
    private int numMines;

    public GameState(int rows, int cols, int numMines) {
        this.rows = rows;
        this.cols = cols;
        this.numMines = numMines;
        this.mines = new boolean[rows][cols];
        this.revealed = new boolean[rows][cols];
        this.flagged = new boolean[rows][cols];
        generateMines();
    }

    private void generateMines() {
        Random rand = new Random();
        int count = 0;
        while (count < numMines) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);
            if (!mines[r][c]) {
                mines[r][c] = true;
                count++;
            }
        }
    }

    public boolean isMine(int row, int col) {
        return mines[row][col];
    }

    public void reveal(int row, int col) {
        revealed[row][col] = true;
    }
    public int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (isValid(newRow, newCol) && isMine(newRow, newCol)) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isRevealed(int r, int c) {
        return revealed[r][c];
    }

    public boolean isFlagged(int row, int col) {
        return flagged[row][col];
    }

    public boolean isValid(int row, int col) {
        return row >= 0 && col >= 0 && row < rows && col < cols;
    }
    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public boolean isGameWon() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!isRevealed(r, c) && !isMine(r, c)) {
                    return false;
                }
            }
        }
        return true;
    }


}
