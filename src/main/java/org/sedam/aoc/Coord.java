package org.sedam.aoc;

import java.util.List;
import java.util.function.Predicate;
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

    public static Predicate<Coord> isValidPred(int width, int length) {
        return c -> c.isValid(width, length);
    }

    public Stream<Coord> get4Neighb() {
        return Stream.of(
                new Coord(x - 1, y),
                new Coord(x, y - 1),
                new Coord(x + 1, y),
                new Coord(x, y + 1)
        );
    }

    public Coord jump(Direction d, int dist) {
        return new Coord(x + d.x * dist, y + d.y * dist);
    }

    public static Coord getCoord(List<String> input, char c) {
        int x = input.stream().filter(l -> l.contains(c + "")).mapToInt(l -> l.indexOf(c)).findFirst().orElseThrow();
        int y = (int) input.stream().takeWhile(l -> !l.contains(c + "")).count();
        return new Coord(x, y);
    }
}
