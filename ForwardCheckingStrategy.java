package sudoku;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;


public class ForwardCheckingStrategy implements SolverStrategy {
    @Override
    public int[][] solve(int[][] board, AtomicBoolean found) {
        Set<Integer>[][] possibilities = initializePossibilities(board);
        if (forwardCheck(board, possibilities, found)) {
            return board;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Set<Integer>[][] initializePossibilities(int[][] board) {
        Set<Integer>[][] possibilities = new HashSet[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                possibilities[i][j] = new HashSet<>();
                if (board[i][j] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(board, num, i, j)) {
                            possibilities[i][j].add(num);
                        }
                    }
                }
            }
        }
        return possibilities;
    }

    @SuppressWarnings("unchecked")
    private Set<Integer>[][] copyPossibilities(Set<Integer>[][] original) {
        Set<Integer>[][] copy = new HashSet[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                copy[i][j] = new HashSet<>(original[i][j]);
            }
        }
        return copy;
    }

    private boolean forwardCheck(int[][] board, Set<Integer>[][] possibilities, AtomicBoolean found) {
        if (found.get()) {
            return false;
        }

        int[] empty = findMostConstrainedCell(board, possibilities);
        if (empty == null) {
            return true;
        }

        int row = empty[0];
        int col = empty[1];

        for (int num : possibilities[row][col]) {
            if (isValid(board, num, row, col)) {
                board[row][col] = num;
                Set<Integer>[][] newPossibilities = copyPossibilities(possibilities);
                updatePossibilities(newPossibilities, row, col, num);

                if (forwardCheck(board, newPossibilities, found)) {
                    return true;
                }

                board[row][col] = 0;
            }
        }

        return false;
    }

    private void updatePossibilities(Set<Integer>[][] possibilities, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            possibilities[row][i].remove(num);
            possibilities[i][col].remove(num);
        }

        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                possibilities[i][j].remove(num);
            }
        }
    }

    private int[] findMostConstrainedCell(int[][] board, Set<Integer>[][] possibilities) {
        int min = Integer.MAX_VALUE;
        int[] cell = null;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0 && possibilities[i][j].size() > 0) {
                    if (possibilities[i][j].size() < min) {
                        min = possibilities[i][j].size();
                        cell = new int[]{i, j};
                    }
                }
            }
        }
        return cell;
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
