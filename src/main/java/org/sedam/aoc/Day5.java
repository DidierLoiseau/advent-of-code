package org.sedam.aoc;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

@SuppressWarnings("unused")
public class Day5 extends Day {
    @Override
    public String part1(List<String> input) {
        Map<Integer, Set<Integer>> order = parseOrder(input);

        return "" + getPagesStream(input)
                .filter(ints -> satisfiesOrder(ints, order))
                .mapToInt(ints -> ints[ints.length / 2])
                .sum();
    }

    private static Stream<int[]> getPagesStream(List<String> input) {
        Pattern separator = Pattern.compile(",");
        return input.stream().dropWhile(s -> !s.isEmpty()).filter(s -> !s.isEmpty())
                .map(s -> separator.splitAsStream(s).mapToInt(Integer::parseInt).toArray());
    }

    private static Map<Integer, Set<Integer>> parseOrder(List<String> input) {
        return input.stream().takeWhile(s -> !s.isEmpty())
                .map(s -> s.split("\\|"))
                .collect(groupingBy(s -> parseInt(s[0]), Collectors.mapping(s -> parseInt(s[1]), toSet())));
    }

    private static boolean satisfiesOrder(int[] ints, Map<Integer, Set<Integer>> order) {
        for (int i = 1; i < ints.length; i++) {
            var pageOrder = order.get(ints[i]);
            if (pageOrder != null) {
                for (int j = 0; j < i; j++) {
                    if (pageOrder.contains(ints[j])) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String part2(List<String> input) {
        Map<Integer, Set<Integer>> order = parseOrder(input);

        return "" + getPagesStream(input)
                .filter(pages -> !satisfiesOrder(pages, order))
                .map(pages -> sort(pages, order))
                .mapToInt(ints -> ints[ints.length / 2])
                .sum();
    }

    private int[] sort(int[] ints, Map<Integer, Set<Integer>> order) {
        for (int i = 1; i < ints.length; i++) {
            int page = ints[i];
            var pageOrder = order.get(page);
            if (pageOrder != null) {
                for (int j = 0; j < i; j++) {
                    if (pageOrder.contains(ints[j])) {
                        ints[i] = ints[j];
                        ints[j] = page;
                        return sort(ints, order);
                    }
                }
            }
        }
        return ints;
    }
}
