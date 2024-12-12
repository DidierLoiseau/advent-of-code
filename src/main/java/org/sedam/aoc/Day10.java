package org.sedam.aoc;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day10 extends Day {

    @Override
    public long part1Long(List<String> input) {
        int[][] map = input.stream().map(s -> s.chars().map(c -> c - '0').toArray()).toArray(int[][]::new);
        int count = 0;
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y] == 0) {
                    Set<Coord> current = Set.of(new Coord(x, y));
                    for (int h = 1; h < 10; h++) {
                        int finalH = h;
                        current = current.stream()
                                .flatMap(c -> neighboursAtHight(map, c.x(), c.y(), finalH))
                                .collect(Collectors.toSet());
                    }
                    count += current.size();
                }
            }
        }
        return count;
    }

    private Stream<Coord> neighboursAtHight(int[][] map, int x, int y, int h) {
        Stream.Builder<Coord> builder = Stream.builder();
        if (x > 0) {
            builder.add(new Coord(x - 1, y));
        }
        if (y > 0) {
            builder.add(new Coord(x, y - 1));
        }
        if (x < map.length - 1) {
            builder.add(new Coord(x + 1, y));
        }
        if (y < map[0].length - 1) {
            builder.add(new Coord(x, y + 1));
        }
        return builder.build().filter(c -> map[c.x()][c.y()] == h);
    }

    @Override
    public long part2Long(List<String> input) {
        int[][] map = input.stream().map(s -> s.chars().map(c -> c - '0').toArray()).toArray(int[][]::new);
        int count = 0;
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y] == 0) {
                    Map<Coord, Integer> current = Map.of(new Coord(x, y), 1);
                    for (int h = 1; h < 10; h++) {
                        int finalH = h;
                        current = current.entrySet().stream()
                                .flatMap(e -> neighboursAtHight(map, e.getKey().x(), e.getKey().y(), finalH).map(
                                        n -> Map.entry(n, e.getValue())))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));
                    }
                    count += current.values().stream().mapToInt(v -> v).sum();
                }
            }
        }
        return count;
    }
}
