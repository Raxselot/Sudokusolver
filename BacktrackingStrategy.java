package sudoku;

import java.util.concurrent.atomic.AtomicBoolean;

public class BacktrackingStrategy implements SolverStrategy {
    @Override
    public int[][] solve(int[][] board, AtomicBoolean found) {
        if (backtrack(board, found)) {
            return board;
        }
        return null;
    }

    private boolean backtrack(int[][] board, AtomicBoolean found) {
        if (found.get()) {
            return false;
        }

        int[] empty = findEmpty(board);
        if (empty == null) {
            return true;
        }

        int row = empty[0];
        int col = empty[1];

        for (int num = 1; num <= 9; num++) {
            if (isValid(board, num, row, col)) {
                board[row][col] = num;
                if (backtrack(board, found)) {
                    return true;
                }
                board[row][col] = 0;
            }
        }
        return false;
    }

    private int[] findEmpty(int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private boolean isValid(int[][] board, int num, int row, int col) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }

        int startRow = row - row % 3;
        int startCol = col - col % 3;

        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == num) {
                    return false;
                }
            }
        }

        return true;
    }
}
