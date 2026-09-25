package org.example.solver.result;

import java.util.List;

public record Result(
        List<List<Chunk>> lines
) {
}
