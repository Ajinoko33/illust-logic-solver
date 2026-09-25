package org.example.validator;

import org.example.solver.Puzzle;

import java.util.Arrays;

public class PuzzleValidator {
    /**
     * 「行の合計と列の合計が一致すること」をチェック。
     */
    public void validate(final Puzzle puzzle) {
        // 行の合計を算出
        final var rowSum = sum(puzzle.rows());

        // 列の合計を算出
        final var colSum = sum(puzzle.cols());

        // 比較
        if (rowSum != colSum) {
            final var message = String.format("[ERROR] 行の合計と列の合計が一致しません: 行の合計=%d, 列の合計=%d", rowSum, colSum);
            throw new RuntimeException(message);
        }
    }

    private static int sum(final int[][] lines) {
        return Arrays.stream(lines)
                .mapToInt(line -> Arrays.stream(line).sum())
                .sum();
    }
}
