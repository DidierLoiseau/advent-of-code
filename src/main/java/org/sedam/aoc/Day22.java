package org.sedam.aoc;

import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

import static java.lang.Long.parseLong;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Gatherers.windowSliding;

public class Day22 extends Day {
    @Override
    public long part1Long(List<String> input) {
        return input.stream()
                .mapToLong(s -> {
                    long num = parseLong(s);
                    for (int i = 0; i < 2000; i++) {
                        num ^= num * 64;
                        num %= 16777216;
                        num ^= num / 32;
                        num %= 16777216; // useless?
                        num ^= num * 2048;
                        num %= 16777216;
                    }
                    return num;
                })
                .sum();
    }

    @Override
    public long part2Long(List<String> input) {
        return input.stream()
                .flatMap(s ->
                        LongStream.iterate(parseLong(s), num -> {
                                    num ^= num * 64;
                                    num %= 16777216;
                                    num ^= num / 32;
                                    num %= 16777216; // useless?
                                    num ^= num * 2048;
                                    num %= 16777216;
                                    return num;
                                })
                                .limit(2000)
                                .map(num -> num % 10)
                                .boxed()
                                .gather(windowSliding(5))
                                .collect(toMap(
                                        fivePrices -> fivePrices.stream()
                                                .gather(windowSliding(2))
                                                .map(twoPrices -> twoPrices.get(1) - twoPrices.getFirst())
                                                .toList(),
                                        fivePrices -> fivePrices.get(4),
                                        (a, _) -> a))
                                .entrySet().stream()
                )
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Long::sum
                ))
                .values()
                .stream()
                .mapToLong(l -> l)
                .max()
                .orElseThrow();
    }
}
