package org.example.solver.searchqueue;

import org.example.model.Direction;
import org.example.model.Line;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SearchQueueTest {
    private final SearchQueue queue = new SearchQueue();

    @Test
    void pushRow() {
        /* setup */
        assertTrue(queue.isEmpty());

        /* execute */
        queue.pushRow(1);

        /* verify */
        final var element = queue.pop();
        final var expected = Optional.of(new Line(Direction.ROW,1));
        assertEquals(expected, element);
    }

    @Test
    void pushCol() {
        /* setup */
        assertTrue(queue.isEmpty());

        /* execute */
        queue.pushCol(1);

        /* verify */
        final var element = queue.pop();
        final var expected = Optional.of(new Line(Direction.COL,1));
        assertEquals(expected, element);
    }

    @Test
    void push_duplicated() {
        /* setup */
        queue.pushRow(1);
        queue.pushRow(2);
        queue.pushRow(3);

        /* execute */
        queue.pushRow(2);

        /* verify */
        final var element1 = queue.pop();
        final var expected1 = Optional.of(new Line(Direction.ROW,1));
        assertEquals(expected1, element1);

        final var element2 = queue.pop();
        final var expected2 = Optional.of(new Line(Direction.ROW,2));
        assertEquals(expected2, element2);

        final var element3 = queue.pop();
        final var expected3 = Optional.of(new Line(Direction.ROW,3));
        assertEquals(expected3, element3);

        assertTrue(queue.isEmpty());
    }

    @Test
    void push() {
        /* setup */
        assertTrue(queue.isEmpty());
        final var list = List.of(
                new Line(Direction.ROW, 1),
                new Line(Direction.ROW, 2),
                new Line(Direction.ROW, 3)
        );

        /* execute */
        queue.push(list);

        /* verify */
        final var element1 = queue.pop();
        final var expected1 = Optional.of(new Line(Direction.ROW,1));
        assertEquals(expected1, element1);

        final var element2 = queue.pop();
        final var expected2 = Optional.of(new Line(Direction.ROW,2));
        assertEquals(expected2, element2);

        final var element3 = queue.pop();
        final var expected3 = Optional.of(new Line(Direction.ROW,3));
        assertEquals(expected3, element3);
    }

    @Test
    void pop_empty() {
        /* setup */
        assertTrue(queue.isEmpty());

        /* execute & verify */
        final var result = queue.pop();
        final var expected = Optional.empty();
        assertEquals(expected, result);
    }

    @Test
    void pop_notEmpty() {
        /* setup */
        assertTrue(queue.isEmpty());
        queue.pushRow(1);

        /* execute & verify */
        final var result = queue.pop();
        final var expected = Optional.of(new Line(Direction.ROW,1));
        assertEquals(expected, result);
    }

    @Test
    void isEmpty() {
        /* execute & verify */
        assertTrue(queue.isEmpty());
    }
}
