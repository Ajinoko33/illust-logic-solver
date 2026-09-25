package org.example.solver;

import org.example.constant.CellStateConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LineSolverTest {
    @Test
    void solve_1() {
        /* setup */
        final int[] hints = {1, 2, 3};
        final int[] currentLine = {
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED
        };
        final var solver = new LineSolver(hints, currentLine);

        /* execute */
        final var result = solver.solve();

        /* verify */
        final int[] expected = {
                CellStateConstants.BLACK,
                CellStateConstants.WHITE,
                CellStateConstants.BLACK,
                CellStateConstants.BLACK,
                CellStateConstants.WHITE,
                CellStateConstants.BLACK,
                CellStateConstants.BLACK,
                CellStateConstants.BLACK
        };
        assertArrayEquals(expected, result);
    }

    @Test
    void solve_2() {
        /* setup */
        final int[] hints = {1, 1, 3};
        final int[] currentLine = {
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.WHITE,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED
        };
        final var solver = new LineSolver(hints, currentLine);

        /* execute */
        final var result = solver.solve();

        /* verify */
        final int[] expected = {
                CellStateConstants.BLACK,
                CellStateConstants.WHITE,
                CellStateConstants.BLACK,
                CellStateConstants.WHITE,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.BLACK,
                CellStateConstants.BLACK,
                CellStateConstants.UNDETERMINED
        };
        assertArrayEquals(expected, result);
    }
}
