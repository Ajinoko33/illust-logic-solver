package org.example.solver;

import org.example.constant.CellStateConstants;
import org.example.solver.leftalignedcalculator.LeftAlignedCalculator;
import org.example.solver.leftalignedcalculator.Result;
import org.example.util.Utils;

public class LineSolver {
    /**
     * ラインのヒント
     */
    private final int[] hints;
    /**
     * 現在のライン状態
     */
    private final int[] currentLine;
    /**
     * ラインの長さ
     */
    private final int length;
    /**
     * 結果の状態
     */
    private final int[] result;

    public LineSolver(final int[] hints, final int[] currentLine) {
        this.hints = hints;
        this.currentLine = currentLine;
        this.length = currentLine.length;
        this.result = currentLine.clone();
    }

    public int[] solve() {
        /* 必ず黒のマスを見つける */
        // ヒント・現在のライン状態を満たし、各黒マスが最も左になる配置(左配置)を見つける
        final var leftAlignedState = calculateLeftAlignedState();
        if (!leftAlignedState.existSolution) {
            throw new ContradictionException("現在のライン状態から、ヒントを満たす解がありません。");
        }

        // 同様に、ヒント・現在のライン状態を満たし、各黒マスが最も右になる配置(右配置)を見つける
        final var rightAlignedState = calculateRightAlignedState();
        if (!rightAlignedState.existSolution) {
            throw new ContradictionException("現在のライン状態から、ヒントを満たす解がありません。");
        }

        // 左配置と右配置で、同じ連続黒マスで重複するマスは必ず黒
        reflectOverlappedBlackCells(leftAlignedState.solution, rightAlignedState.solution);

        /* 必ず白のマスを見つける */
        // 左配置と右配置で完全一致したものの両隣マスは必ず白
        reflectFixedBothSideWhiteCells(leftAlignedState.solution, rightAlignedState.solution);

        // 左配置で、最初の黒マスより左側は必ず白
        reflectLeadingWhiteCells(leftAlignedState.solution);

        // 右配置で、最後の黒マスより右側は必ず白
        reflectTailingWhiteCells(rightAlignedState.solution);

        return result;
    }

    private Result calculateLeftAlignedState() {
        return new LeftAlignedCalculator(hints, currentLine).calculate();
    }

    private Result calculateRightAlignedState() {
        // ヒント・現在の状態を左右反転して計算
        final var reversedHints = Utils.toReversed(hints);
        final var reversedCurrentLine = Utils.toReversed(currentLine);

        var currentResult = new LeftAlignedCalculator(reversedHints, reversedCurrentLine).calculate();

        // 解があれば、計算結果を再度、左右反転
        if (currentResult.existSolution) {
            var reversedCurrentResult = Utils.toReversed(currentResult.solution);

            // 解のインデックスも反転
            for (int i = 0; i < reversedCurrentResult.length; i++) {
                if (reversedCurrentResult[i] != Result.WHITE) {
                    reversedCurrentResult[i] = hints.length - 1 - reversedCurrentResult[i];
                }
            }

            return Result.solution(reversedCurrentResult);
        } else {
            return currentResult;
        }
    }

    private void reflectOverlappedBlackCells(final int[] leftAlignedState, final int[] rightAlignedState) {
        for (int i = 0; i < length; i++) {
            if (leftAlignedState[i] >= 0 && leftAlignedState[i] == rightAlignedState[i]) {
                result[i] = CellStateConstants.BLACK;
            }
        }
    }

    private void reflectFixedBothSideWhiteCells(final int[] leftAlignedState, final int[] rightAlignedState) {
        for (int i = 0; i < length; i++) {
            if (leftAlignedState[i] == Result.WHITE) {
                continue;
            }

            // 黒マスを発見
            // 完全一致でなければcontinue
            if (leftAlignedState[i] != rightAlignedState[i]) {
                i += hints[leftAlignedState[i]];
                continue;
            }

            // 完全一致なので、両隣マスは必ず白
            if (i - 1 >= 0) {
                result[i - 1] = CellStateConstants.WHITE;
            }
            if (i + hints[leftAlignedState[i]] < length) {
                result[i + hints[leftAlignedState[i]]] = CellStateConstants.WHITE;
            }

            i += hints[leftAlignedState[i]];
        }
    }

    private void reflectLeadingWhiteCells(final int[] leftAlignedState) {
        for (int i = 0; i < length; i++) {
            if (leftAlignedState[i] != Result.WHITE) {
                break;
            }

            result[i] = CellStateConstants.WHITE;
        }
    }

    private void reflectTailingWhiteCells(final int[] rightAlignedState) {
        for (int i = length - 1; i >= 0; i--) {
            if (rightAlignedState[i] != Result.WHITE) {
                break;
            }

            result[i] = CellStateConstants.WHITE;
        }
    }
}
