package org.sedam.aoc;

import java.util.List;
import java.util.Map;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Gatherers;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.partitioningBy;

public class Day25 extends Day {
    @Override
    public long part1Long(List<String> input) {
        Map<Boolean, List<int[]>> keyAndLocks = input.stream()
                .filter(s -> !s.isEmpty())
                .gather(Gatherers.windowFixed(7))
                .map(Utils::toWallArray)
                .collect(partitioningBy(w -> w[0][0], mapping(this::toKeying, Collectors.toList())));

        return keyAndLocks.get(true).stream()
                .mapToLong(key -> keyAndLocks.get(false).stream().filter(lock -> fits(key, lock)).count())
                .sum();
    }

    private boolean fits(int[] key, int[] lock) {
        return IntStream.range(0, 5).allMatch(i -> key[i] <= lock[i]);
    }

    private int[] toKeying(boolean[][] walls) {
        return IntStream.range(0, 5)
                .map(keying(walls))
                .toArray();
    }

    private IntUnaryOperator keying(boolean[][] walls) {
        IntUnaryOperator op = i -> (int) IntStream.range(1, 6)
                .filter(j -> walls[j][i])
                .count();
        return walls[0][0] ? op : op.andThen(i -> 5 - i);
    }

    @Override
    public String part2(List<String> input) {
        return "48 stars";
    }
}
