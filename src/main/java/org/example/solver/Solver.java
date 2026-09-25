package org.example.solver;

import org.example.constant.CellStateConstants;
import org.example.model.Cell;
import org.example.model.Direction;
import org.example.model.Line;
import org.example.solver.result.Chunk;
import org.example.solver.result.Color;
import org.example.solver.result.Result;
import org.example.solver.searchqueue.SearchQueue;
import org.example.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver {
    /**
     * 行のヒント
     */
    private final int[][] rowHints;
    /**
     * 列のヒント
     */
    private final int[][] colHints;
    /**
     * 0: 未確定
     * 1: 白マス
     * 2: 黒マス
     */
    private final int[][] grid;
    /**
     * 探索キュー
     */
    private final SearchQueue queue;

    public Solver(final Puzzle puzzle) {
        // 全ての行と列を追加したキューを用意
        SearchQueue adding = new SearchQueue();
        for (int i = 0; i < puzzle.rows().length; i++) {
            adding.pushRow(i);
        }
        for (int i = 0; i < puzzle.cols().length; i++) {
            adding.pushCol(i);
        }

        this(puzzle.rows(), puzzle.cols(), new int[puzzle.rows().length][puzzle.cols().length], adding);
    }

    private Solver(final int[][] rowHints, final int[][] colHints, final int[][] grid, final SearchQueue queue) {
        this.rowHints = rowHints;
        this.colHints = colHints;
        this.grid = grid;
        this.queue = queue;
    }

    /**
     * 1. キューが無くなるまで順番に処理
     * 2. if 解けた
     *   then 終了
     *   else
     *     1. どこかを黒と仮定し、子Solverで解けるか試す
     *     2. if 子Solverで解けた
     *       then 終了
     *       else 仮定が誤りなので、背理法により白マスが確定。そのマスの行と列をキューに詰め、最初の1に戻る
     */
    public Result solve() {
        while (true) {
            // キューが無くなるまで順番に処理
            while (true) {
                final var targetLine = queue.pop();
                if (targetLine.isEmpty()) {
                    break;
                }
                solveLine(targetLine.get());
            }

            // 解けていれば終了
            if (isSolved()) {
                return answer();
            }

            // 黒で試したいマスを見つける
            final var blackChallengeCell = findBlackChallengeCell();

            // そのマスを黒にして進める
            try {
                return blackChallenge(blackChallengeCell);
            } catch (final ContradictionException e) {
                // 黒で解けなかったので、白で確定
                grid[blackChallengeCell.rowIndex()][blackChallengeCell.colIndex()] = CellStateConstants.WHITE;

                // 行と列をキューに追加
                queue.pushRow(blackChallengeCell.rowIndex());
                queue.pushCol(blackChallengeCell.colIndex());

                // 解き進める(次のループへ)
            }
        }
    }

    private void solveLine(final Line line) {
        // 該当ラインのヒント・今の状態を抽出
        final int[] currentLine = extractCurrentLine(line);
        final int[] hints = extractLineHints(line);

        // 行を解く
        final var lineSolver = new LineSolver(hints, currentLine);
        final int[] solved = lineSolver.solve();

        // 差分チェック
        final List<Line> changedLines = extractChangedLines(line, currentLine, solved);

        // 差分があれば反映し、キューに追加
        if (!changedLines.isEmpty()) {
            reflectLines(line, solved);
            queue.push(changedLines);
        }
    }

    private int[] extractCurrentLine(final Line line) {
        return switch (line.direction()) {
            case ROW -> grid[line.index()].clone();
            case COL -> {
                int[] result = new int[rowCounts()];
                for (int i = 0; i < rowCounts(); i++) {
                    result[i] = grid[i][line.index()];
                }
                yield result;
            }
        };
    }

    private int[] extractLineHints(final Line line) {
        return switch (line.direction()) {
            case ROW -> rowHints[line.index()];
            case COL -> colHints[line.index()];
        };
    }

    private List<Line> extractChangedLines(final Line line, final int[] before, final int[] after) {
        List<Line> changedLines = new ArrayList<>();

        for (int i = 0; i < before.length; i++) {
            if (before[i] != after[i]) {
                final Line changedLine = switch (line.direction()) {
                    case ROW -> new Line(Direction.COL, i);
                    case COL -> new Line(Direction.ROW, i);
                };
                changedLines.add(changedLine);
            }
        }
        return changedLines;
    }

    private void reflectLines(final Line line, final int[] solved) {
        switch (line.direction()) {
            case ROW -> grid[line.index()] = solved;
            case COL -> {
                for (int i = 0; i < solved.length; i++) {
                    grid[i][line.index()] = solved[i];
                }
            }
        }
    }

    private boolean isSolved() {
        // 未確定マスが無ければ、解けている
        for (int i = 0; i < rowHints.length; i++) {
            for (int j = 0; j < colHints.length; j++) {
                if (grid[i][j] == CellStateConstants.UNDETERMINED) {
                    return false;
                }
            }
        }
        return true;
    }

    private Result answer() {
        // 解答を返す
        List<List<Chunk>> adding = new ArrayList<>();
        for (int i = 0; i < rowHints.length; i++) {
            List<Chunk> chunks = new ArrayList<>();

            int value = grid[i][0];
            int length = 0;
            for (int j = 0; j < colHints.length; j++) {
                if (grid[i][j] == value) {
                    length++;
                    continue;
                }

                // 色が異なった
                chunks.add(new Chunk(value == CellStateConstants.WHITE ? Color.WHITE : Color.BLACK, length));

                value = grid[i][j];
                length = 1;
            }

            // 最後の分も追加
            chunks.add(new Chunk(value == CellStateConstants.WHITE ? Color.WHITE : Color.BLACK, length));

            adding.add(Collections.unmodifiableList(chunks));
        }

        return new Result(Collections.unmodifiableList(adding));
    }

    private Cell findBlackChallengeCell() {
        // 最も上で最も左の未確定マスを探す
        for (int i = 0; i < rowHints.length; i++) {
            for (int j = 0; j < colHints.length; j++) {
                if (grid[i][j] == CellStateConstants.UNDETERMINED) {
                    return new Cell(i, j);
                }
            }
        }

        throw new RuntimeException("未確定マスが見つかりませんでした。");
    }

    private Result blackChallenge(final Cell cell) {
        // gridをクローンし、指定マスを黒にする
        int[][] cloned = Utils.clone(grid);
        cloned[cell.rowIndex()][cell.colIndex()] = CellStateConstants.BLACK;

        // 指定マスの行と列を追加した探索キューを作成
        SearchQueue adding = new SearchQueue();
        adding.pushRow(cell.rowIndex());
        adding.pushCol(cell.colIndex());

        // 子Solverで解けるか試す
        final var childSolver = new Solver(rowHints, colHints, cloned, adding);
        return childSolver.solve();
    }

    private int rowCounts() {
        return rowHints.length;
    }

    private int colCounts() {
        return colHints.length;
    }
}
