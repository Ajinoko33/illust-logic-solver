package org.example.io;

import org.example.solver.result.Result;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CsvWriter {
    private static final String OUTPUT_FILE_NAME = "result.csv";

    private final String destinationDirectoryPath;

    public CsvWriter(final String destinationDirectoryPath) {
        this.destinationDirectoryPath = destinationDirectoryPath;
    }

    public void write(final Result result) {
        // outputディレクトリを作成
        try {
            Files.createDirectories(Path.of(destinationDirectoryPath));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        // ファイル出力
        export(result);
    }

    private void export(final Result result) {
        final Path outputPath = Path.of(destinationDirectoryPath, OUTPUT_FILE_NAME);
        try (final BufferedWriter bw = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            // 各行を処理
            for (int i = 0; i < result.lines().size(); i++) {
                final var line = result.lines().get(i);

                // 先頭行にはBOMを付与
                if (i == 0) {
                    bw.write("\uFEFF");
                }

                // 各まとまりを書き込む
                for (int j = 0; j < line.size(); j++) {
                    final var chunk = line.get(j);

                    // まとまりが最初でない場合は、カンマを先に書き込む
                    if (j != 0) {
                        bw.write(',');
                    }

                    final int value = switch (chunk.color()) {
                        case WHITE -> 0;
                        case BLACK -> 1;
                    };
                    final var array = new int[chunk.length()];
                    Arrays.fill(array, value);
                    // カンマ区切りで書き込む
                    final String joined = Arrays.stream(array)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(","));
                    bw.write(joined);
                }

                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
