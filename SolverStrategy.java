package sudoku;

import java.util.concurrent.atomic.AtomicBoolean;

public interface SolverStrategy {

    int[][] solve(int[][] board, AtomicBoolean found);
}
