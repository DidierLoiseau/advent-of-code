package org.sedam.aoc;

import java.util.List;

public class Day22 extends Day {
    @Override
    public long part1Long(List<String> input) {
        return input.stream()
                .mapToLong(s -> {
                    long num = Long.parseLong(s);
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
}
