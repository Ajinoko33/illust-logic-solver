package org.example.solver.leftalignedcalculator;

import org.example.constant.CellStateConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeftAlignedCalculatorTest {
    @Test
    void calculate_1() {
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
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
        };
        final var calculator = new LeftAlignedCalculator(hints, currentLine);

        /* execute */
        final var result = calculator.calculate();

        /* verify */
        assertTrue(result.existSolution);
        assertArrayEquals(new int[]{
                0,
                -1,
                1,
                1,
                -1,
                2,
                2,
                2,
                -1,
                -1,
        }, result.solution);
    }

    @Test
    void calculate_2() {
        /* setup */
        final int[] hints = {2, 3};
        final int[] currentLine = {
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.WHITE,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
        };
        final var calculator = new LeftAlignedCalculator(hints, currentLine);

        /* execute */
        final var result = calculator.calculate();

        /* verify */
        assertTrue(result.existSolution);
        assertArrayEquals(new int[]{
                0,
                0,
                -1,
                -1,
                1,
                1,
                1,
                -1,
                -1,
        }, result.solution);
    }

    @Test
    void calculate_3() {
        /* setup */
        final int[] hints = {2, 3};
        final int[] currentLine = {
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.WHITE,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.BLACK,
        };
        final var calculator = new LeftAlignedCalculator(hints, currentLine);

        /* execute */
        final var result = calculator.calculate();

        /* verify */
        assertTrue(result.existSolution);
        assertArrayEquals(new int[]{
                0,
                0,
                -1,
                -1,
                -1,
                -1,
                1,
                1,
                1,
        }, result.solution);
    }

    @Test
    void calculate_4() {
        /* setup */
        final int[] hints = {2, 2};
        final int[] currentLine = {
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.BLACK,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.BLACK,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
        };
        final var calculator = new LeftAlignedCalculator(hints, currentLine);

        /* execute */
        final var result = calculator.calculate();

        /* verify */
        assertTrue(result.existSolution);
        assertArrayEquals(new int[]{
                -1,
                -1,
                0,
                0,
                -1,
                1,
                1,
                -1,
                -1,
        }, result.solution);
    }

    @Test
    void calculate_noSolution() {
        /* setup */
        final int[] hints = {2, 2};
        final int[] currentLine = {
                CellStateConstants.UNDETERMINED,
                CellStateConstants.WHITE,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.BLACK,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.UNDETERMINED,
                CellStateConstants.WHITE,
                CellStateConstants.UNDETERMINED,
        };
        final var calculator = new LeftAlignedCalculator(hints, currentLine);

        /* execute */
        final var result = calculator.calculate();

        /* verify */
        assertFalse(result.existSolution);
    }
}
