package org.example.solver.leftalignedcalculator;

public class Result {
    /**
     * 白マスを表す値。
     */
    public static final int WHITE = -1;

    /**
     * 解があるか。
     */
    public final boolean existSolution;
    /**
     * 解。黒マスは、対応するヒントのインデックス番号が入る。
     */
    public final int[] solution;

    private Result(final boolean existSolution, final int[] solution) {
        this.existSolution = existSolution;
        this.solution = solution;
    }

    public static Result solution(final int[] solution) {
        return new Result(true, solution);
    }

    public static Result noSolution() {
        return new Result(false, new int[0]);
    }
}
