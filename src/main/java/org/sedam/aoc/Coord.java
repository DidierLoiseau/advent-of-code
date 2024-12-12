package org.sedam.aoc;

import java.util.stream.IntStream;
import java.util.stream.Stream;

record Coord(int x, int y) {
    public static Stream<Coord> gridStream(int width, int length) {
        return IntStream.range(0, width).boxed()
                .flatMap(x -> IntStream.range(0, length).mapToObj(y -> new Coord(x, y)));
    }

    public boolean isValid(int width, int length) {
        return x >= 0 && y >= 0 && x < width && y < length;
    }

    public Stream<Coord> get4Neighb() {
        return Stream.of(
                new Coord(x - 1, y),
                new Coord(x, y - 1),
                new Coord(x + 1, y),
                new Coord(x, y + 1)
        );
    }
}
