package sudoku;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;


public class ConstraintPropagationStrategy implements SolverStrategy {
    @Override
    public int[][] solve(int[][] board, AtomicBoolean found) {
        if (constraintPropagation(board, found)) {
            return board;
        }
        return null;
    }

    private boolean constraintPropagation(int[][] board, AtomicBoolean found) {
        if (found.get()) {
            return false;
        }

        boolean progress = true;
        while (progress) {
            progress = false;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (board[i][j] == 0) {
                        Set<Integer> candidates = getCandidates(board, i, j);
                        if (candidates.isEmpty()) {
                            return false;
                        } else if (candidates.size() == 1) {
                            board[i][j] = candidates.iterator().next();
                            progress = true;
                        }
                    }
                }
            }
        }

        int[] empty = findEmpty(board);
        if (empty == null) {
            return true;
        }

        int row = empty[0];
        int col = empty[1];

        for (int num : getCandidates(board, row, col)) {
            if (isValid(board, num, row, col)) {
                board[row][col] = num;
                if (constraintPropagation(board, found)) {
                    return true;
                }
                board[row][col] = 0;
            }
        }

        return false;
    }

    private Set<Integer> getCandidates(int[][] board, int row, int col) {
        Set<Integer> candidates = new HashSet<>();
        for (int num = 1; num <= 9; num++) {
            if (isValid(board, num, row, col)) {
                candidates.add(num);
            }
        }
        return candidates;
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
