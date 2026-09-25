package org.example.solver.searchqueue;

import org.example.model.Direction;
import org.example.model.Line;

import java.util.*;

/**
 * 次に探索するラインを管理するキュー。
 * 内部では順序付きSetで管理し、挿入された要素が既に存在すれば、新たには追加しない。
 */
public class SearchQueue {
    private final LinkedHashSet<Line> queue;

    public SearchQueue() {
        this.queue = new LinkedHashSet<>();
    }

    public void pushRow(final int index) {
        queue.add(new Line(Direction.ROW, index));
    }

    public void pushCol(final int index) {
        queue.add(new Line(Direction.COL, index));
    }

    public void push(final List<Line> lines) {
        queue.addAll(lines);
    }

    public Optional<Line> pop() {
        if (queue.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(queue.removeFirst());
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
