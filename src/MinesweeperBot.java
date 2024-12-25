import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MinesweeperBot {
    private GameState gameState;
    private MinesweeperGUI gui;

    public MinesweeperBot(GameState gameState, MinesweeperGUI gui) {
        this.gameState = gameState;
        this.gui = gui;
    }

    public void start() {
        autoPlay();
    }

    public void autoPlay() {
        while (true) {
            boolean madeMove = false;
            madeMove = directDeduction();
            if (!madeMove) {
                backtrack();
            }
            if (gameState.isGameWon()) {
                System.out.println("Igra je završena, bot je pobedio!");
                JOptionPane.showMessageDialog(null, "Pobeda! Bot je rešio igru!");
                break;
            }
        }
    }
    public boolean directDeduction() {
        boolean madeMove = false;

        for (int r = 0; r < gameState.getRows(); r++) {
            for (int c = 0; c < gameState.getCols(); c++) {
                if (!gameState.isRevealed(r, c) && !gameState.isFlagged(r, c)) {
                    int adjacentMines = gameState.countAdjacentMines(r, c);

                    if (adjacentMines == 0) {
                        autoOpenAdjacent(r, c);
                        madeMove = true;
                    } else if (adjacentMines > 0) {
                        int hiddenNeighbors = 0;
                        int flaggedNeighbors = 0;

                        for (int i = r - 1; i <= r + 1; i++) {
                            for (int j = c - 1; j <= c + 1; j++) {
                                if (i >= 0 && i < gameState.getRows() && j >= 0 && j < gameState.getCols()) {
                                    if (!gameState.isRevealed(i, j) && !gameState.isFlagged(i, j)) {
                                        hiddenNeighbors++;
                                    } else if (gameState.isFlagged(i, j)) {
                                        flaggedNeighbors++;
                                    }
                                }
                            }
                        }
                        if (hiddenNeighbors == adjacentMines - flaggedNeighbors) {
                            flagAdjacent(r, c);
                            madeMove = true;
                        } else if (adjacentMines == flaggedNeighbors) {
                            autoOpenAdjacent(r, c);
                            madeMove = true;
                        }
                    }
                }
            }
        }
        return madeMove;
    }


    private void autoOpenAdjacent(int row, int col) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;

                if (gameState.isValid(newRow, newCol) && !gameState.isRevealed(newRow, newCol)) {
                    if (gameState.isMine(newRow, newCol)) {
                        continue;
                    }

                    gameState.reveal(newRow, newCol);
                    gui.updateButton(newRow, newCol);

                    int adjacentMines = gameState.countAdjacentMines(newRow, newCol);
                    if (adjacentMines == 0) {
                        autoOpenAdjacent(newRow, newCol);
                    }

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private void backtrack() {
        System.out.println("Backtracking...");

        List<int[]> safeMoves = new ArrayList<>();
        List<int[]> flaggedMoves = new ArrayList<>();
        for (int r = 0; r < gameState.getRows(); r++) {
            for (int c = 0; c < gameState.getCols(); c++) {
                if (!gameState.isRevealed(r, c) && !gameState.isFlagged(r, c)) {
                    if (gameState.isMine(r, c)) {
                        continue;
                    }

                    int adjacentMines = gameState.countAdjacentMines(r, c);
                    if (adjacentMines == 0) {
                        safeMoves.add(new int[]{r, c});
                    } else if (adjacentMines > 0) {
                        flaggedMoves.add(new int[]{r, c});
                    }
                }
            }
        }
        if (!safeMoves.isEmpty()) {
            int[] move = safeMoves.get(0);
            gameState.reveal(move[0], move[1]);
            gui.updateButton(move[0], move[1]);
            System.out.println("Revealed safe move at (" + move[0] + ", " + move[1] + ")");

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!flaggedMoves.isEmpty()) {
            for (int[] move : flaggedMoves) {
                gameState.flag(move[0], move[1]);
                gui.updateButton(move[0], move[1]);
                System.out.println("Flagged mine at (" + move[0] + ", " + move[1] + ")");
            }
        }
    }

    private void flagAdjacent(int r, int c) {
        for (int i = r - 1; i <= r + 1; i++) {
            for (int j = c - 1; j <= c + 1; j++) {
                if (i >= 0 && i < gameState.getRows() && j >= 0 && j < gameState.getCols()) {
                    if (!gameState.isRevealed(i, j) && !gameState.isFlagged(i, j)) {
                        gameState.flag(i, j);
                        gui.updateButton(i, j);
                    }
                }
            }
        }
    }
    }

