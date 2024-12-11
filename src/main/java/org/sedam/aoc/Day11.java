package org.sedam.aoc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Day11 extends Day {
    record CacheKey(String s, int iter) {
    }

    Map<CacheKey, Long> cache = new HashMap<>();

    @Override
    public long part1Long(List<String> input) {
        return Pattern.compile(" ").splitAsStream(input.getFirst())
                .mapToLong(s -> compute(s, 25))
                .sum();
    }

    private long compute(String s, int i) {
        if (i == 0) {
            return 1;
        }
        var key = new CacheKey(s, i);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        long result = compute0(s, i);
        cache.put(key, result);
        return result;
    }

    private long compute0(String s, int i) {
        if (s.equals("0")) {
            return compute("1", i - 1);
        }
        if (s.length() % 2 == 0) {
            return compute(s.substring(0, s.length() / 2), i - 1) + compute(
                    Long.valueOf(s.substring(s.length() / 2)).toString(), i - 1);
        }
        return compute(Long.toString(Long.parseLong(s) * 2024), i - 1);
    }

    @Override
    public long part2Long(List<String> input) {
        return Pattern.compile(" ").splitAsStream(input.getFirst())
                .mapToLong(s -> compute(s, 75))
                .sum();
    }

    @Override
    public boolean hasPart2ExpectedResult() {
        return false;
    }
}
