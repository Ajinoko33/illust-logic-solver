package org.example.io;

import org.example.solver.Puzzle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CsvReader {
    private final String sourceRowsPath;
    private final String sourceColsPath;

    public CsvReader(final String sourceRowsPath, final String sourceColsPath) {
        this.sourceRowsPath = sourceRowsPath;
        this.sourceColsPath = sourceColsPath;
    }

    public Puzzle read() {
        // 行の読み込み
        final var rows = readCsv(sourceRowsPath);

        // 列の読み込み
        final var cols = readCsv(sourceColsPath);

        return new Puzzle(rows, cols);
    }

    private int[][] readCsv(final String sourcePath) {
        // 読み込み
        try (final InputStream is = getClass().getClassLoader().getResourceAsStream(sourcePath);
             final BufferedReader reader = new BufferedReader(
                     new InputStreamReader(is, StandardCharsets.UTF_8)
             )) {
            // データ
            return reader.lines().map(line -> readLine(line, sourcePath)).toList().toArray(new int[0][]);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String removeBOM(final String input) {
        if (input.charAt(0) == '\uFEFF') {
            return input.substring(1);
        } else {
            return input;
        }
    }

    private static int[] readLine(final String line, final String sourcePath) {
        // BOMがあれば除去
        final String cleanedLine = removeBOM(line);
        final var splitLine = cleanedLine.split(",");

        // カンマで区切って整数に変換
        int[] result = new int[splitLine.length];
        for (int i = 0; i < splitLine.length; i++) {
            result[i] = readCell(splitLine[i], sourcePath, i);
        }

        return result;
    }

    private static Integer readCell(final String cell, final String sourcePath, final int cellIndex) {
        try {
            return Integer.parseInt(cell);
        } catch (final NumberFormatException e) {
            final String message = String.format("[ERROR] %s の %d 列目の値が整数ではありません: \"%s\"\n", sourcePath, cellIndex + 1, cell);
            throw new RuntimeException(message, e);
        }
    }
}
