package sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Hauptklasse zum ausführen der Programme
 */
public class SudokuSolver {
    private final int[][] board;
    private volatile int[][] solution;
    private final AtomicBoolean found;
    private final List<SolverStrategy> strategies;
    private final ExecutorService executor;

    public SudokuSolver(int[][] board) {
        this.board = deepCopy(board);
        this.found = new AtomicBoolean(false);
        this.strategies = new ArrayList<>();
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        initializeStrategies();
    }

    private void initializeStrategies() {
        strategies.add(new BacktrackingStrategy());
        strategies.add(new ForwardCheckingStrategy());
        strategies.add(new ConstraintPropagationStrategy());
    }

    public int[][] solve() throws InterruptedException, ExecutionException {
        List<Future<int[][]>> futures = new ArrayList<>();

        for (SolverStrategy strategy : strategies) {
            futures.add(executor.submit(() -> {
                if (!found.get()) {
                    int[][] result = strategy.solve(deepCopy(board), found);
                    if (result != null) {
                        setSolution(result);
                        return result;
                    }
                }
                return null;
            }));
        }

        // auf Lösung warten
        for (Future<int[][]> future : futures) {
            try {
                int[][] res = future.get();
                if (res != null) {
                    break;
                }
            } catch (CancellationException e) {

            }
        }

        executor.shutdownNow();
        return solution;
    }

    private synchronized void setSolution(int[][] sol) {
        if (!found.get()) {
            solution = sol;
            found.set(true);
        }
    }

    private int[][] deepCopy(int[][] original) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 9);
        }
        return copy;
    }

    // Testen
    public static void main(String[] args) {
int[][] board = {
    {0,7,0,3,9,0,2,0,5},
    {8,0,0,1,0,3,0,7,5},
    {9,0,5,0,2,0,8,6,0},
    {3,6,0,5,0,9,0,0,7},
    {0,0,7,8,6,1,3,0,0},
    {4,0,5,6,8,0,1,0,0},
    {0,0,0,1,0,8,5,4,2},
    {0,3,0,6,9,0,0,5,8},
    {2,3,0,7,0,0,8,0,5},
};


        SudokuSolver solver = new SudokuSolver(board);
        try {
            int[][] solution = solver.solve();
            if (solution != null) {
                printBoard(solution);
            } else {
                System.out.println("Theres no Solution.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printBoard(int[][] board) {
        for (int i = 0; i < 9; i++) {
            if(i % 3 == 0 && i != 0) {
                System.out.println("------+-------+------");
            }
            for (int j = 0; j < 9; j++) {
                if(j % 3 == 0 && j != 0) {
                    System.out.print("| ");
                }
                System.out.print(board[i][j] == 0 ? ". " : board[i][j] + " ");
            }
            System.out.println();
        }
    }
}
