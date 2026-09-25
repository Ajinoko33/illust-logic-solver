package org.example.solver.leftalignedcalculator;

import org.example.constant.CellStateConstants;

import java.util.Arrays;

/**
 * 1ラインについて、ヒント・現在の状態を満たし、各黒マスが最も左になる配置(左配置)を見つける。
 */
public class LeftAlignedCalculator {
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

    public LeftAlignedCalculator(final int[] hints, final int[] currentLine) {
        this.hints = hints;
        this.currentLine = currentLine;
        this.length = currentLine.length;
    }

    public Result calculate() {
        /* 前計算 */
        // 自ヒント含めた最小必要長
        int[] needLength = new int[hints.length];
        needLength[hints.length - 1] = hints[hints.length - 1];
        for (int i = hints.length - 2; i >= 0; i--) {
            needLength[i] = needLength[i + 1] + 1 + hints[i];
        }

        // 白マスの個数を累積和(区間内の白マス検知用)
        int[] whiteCellSum = new int[length + 1];
        for (int i = 0; i < length; i++) {
            whiteCellSum[i + 1] = whiteCellSum[i];
            if (currentLine[i] == CellStateConstants.WHITE) {
                whiteCellSum[i + 1]++;
            }
        }

        // 次の黒マス位置(見つからなければ-1)
        int[] nextBlackIndex = new int[length];
        nextBlackIndex[length - 1] = -1;
        for (int i = length - 2; i >= 0; i--) {
            if (currentLine[i + 1] == CellStateConstants.BLACK) {
                nextBlackIndex[i] = i + 1;
            } else {
                nextBlackIndex[i] = nextBlackIndex[i + 1];
            }
        }

        /* 後ろからDPで解く */
        // dp[i][p] := ヒントiを位置pから始めると、その後すべてのヒントが、現在のライン状態を満たして配置可能か
        boolean[][] dp = new boolean[hints.length + 1][length];
        // 初期状態として、dp[hints.length]は、最終ヒントに0があるイメージで埋めておく
        for (int p = length - 1; p >= 0; p--) {
            // 最終ヒントが0 = 位置pを白マスにする

            // 後ろから辿って、黒マスが出現するまで配置可能
            if (currentLine[p] == CellStateConstants.BLACK) {
                break;
            }
            dp[hints.length][p] = true;
        }

        // dpのtrueの個数を累積和(区間内のtrue検知用)
        int[][] dpSum = new int[hints.length + 1][length + 1];
        // 初期状態の分を計算
        for (int p = 0; p < length; p++) {
            dpSum[hints.length][p + 1] = dpSum[hints.length][p];
            if (dp[hints.length][p]) {
                dpSum[hints.length][p + 1]++;
            }
        }

        // 後ろからDP
        for (int i = hints.length - 1; i >= 0; i--) {
            for (int p = length - needLength[i]; p >= 0; p--) {
                // 自ヒント含めた最小必要長が収まる範囲で更新

                // 自ヒントの区間内に白マスが無いこと
                if (whiteCellSum[p + hints[i]] - whiteCellSum[p] > 0) {
                    continue;
                }

                // 自ヒントの直後が黒マスではないこと
                if (p + hints[i] < length && currentLine[p + hints[i]] == CellStateConstants.BLACK) {
                    continue;
                }

                // 自ヒント直後の白マスの次以降から、次の黒マスor末尾までの間で、次のヒントについて配置可能であること
                if (p + hints[i] + 1 < length) {
                    final int blackIndex = nextBlackIndex[p + hints[i]];
                    if (blackIndex == -1) {
                        // 末尾まで黒マスが無い場合
                        if (dpSum[i + 1][length] - dpSum[i + 1][p + hints[i] + 1] == 0) {
                            continue;
                        }
                    } else {
                        // 末尾までに黒マスがある場合
                        if (dpSum[i + 1][nextBlackIndex[p + hints[i]] + 1] - dpSum[i + 1][p + hints[i] + 1] == 0) {
                            continue;
                        }
                    }
                }

                dp[i][p] = true;
            }

            for (int p = 0; p < length; p++) {
                dpSum[i][p + 1] = dpSum[i][p];
                if (dp[i][p]) {
                    dpSum[i][p + 1]++;
                }
            }
        }

        /* DPテーブルから復元 */
        // 解があるかチェック
        if (!existSolution(dp)) {
            return Result.noSolution();
        }

        // 復元
        int[] result = new int[length];
        Arrays.fill(result, Result.WHITE);
        int hintIndex = 0;
        int cellIndex = 0;
        while (hintIndex < hints.length) {
            while (!dp[hintIndex][cellIndex]) {
                cellIndex++;
            }

            // 自ヒントが配置可能な開始位置を発見。ヒントのインデックスをメモ
            final int afterBlackIndex = cellIndex + hints[hintIndex];
            while (cellIndex < afterBlackIndex) {
                result[cellIndex] = hintIndex;
                cellIndex++;
            }

            // 自ヒント直後にマスがあるならば、必ず白マスなので、1つ進める
            if (cellIndex < length) {
                cellIndex++;
            }

            // 次のヒントへ
            hintIndex++;
        }

        return Result.solution(result);
    }

    private boolean existSolution(final boolean[][] dp) {
        // DPした結果、最初のヒントが配置可能であれば解あり
        for (int p = 0; p < dp[0].length; p++) {
            if (dp[0][p]) {
                return true;
            }
        }
        return false;
    }
}
