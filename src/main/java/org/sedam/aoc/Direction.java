package org.sedam.aoc;

import java.util.Arrays;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
enum Direction {
    N('^', false, 0, -1),
    E('>', true, 1, 0),
    S('v', false, 0, 1),
    W('<', true, -1, 0);

    final int symbol;

    final boolean horizontal;

    final int x;

    final int y;

    static Direction from(int c) {
        for (Direction d : values()) {
            if (c == d.symbol) {
                return d;
            }
        }
        throw new IllegalArgumentException("" + c);
    }

    static Stream<Direction> stream() {
        return Arrays.stream(values());
    }
}
