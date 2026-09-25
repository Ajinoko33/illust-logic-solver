package org.example;

import org.example.io.CsvReader;
import org.example.io.CsvWriter;
import org.example.solver.Solver;
import org.example.validator.PuzzleValidator;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static final String sourceRowsPath = "rows.csv";
    private static final String sourceColsPath = "cols.csv";
    private static final String destinationDirectoryPath = "out/" + generateDestinationDirectoryPath();

    private static final CsvReader reader = new CsvReader(sourceRowsPath, sourceColsPath);
    private static final CsvWriter writer = new CsvWriter(destinationDirectoryPath);

    private static final PuzzleValidator validator = new PuzzleValidator();

    static void main() {
        // 問題を読み込む
        final var puzzle = reader.read();

        // バリデーションチェック
        validator.validate(puzzle);

        // 解く
        final var solver = new Solver(puzzle);
        final var result = solver.solve();

        // 答えを出力
        writer.write(result);
    }

    private static String generateDestinationDirectoryPath() {
        final ZonedDateTime japanTime = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        return japanTime.format(formatter);
    }
}
