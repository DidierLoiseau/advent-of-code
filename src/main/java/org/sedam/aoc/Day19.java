package org.sedam.aoc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Day19 extends Day {
    @Override
    public long part1Long(List<String> input) {
        var pat = Pattern.compile("^(?:%s)+$".formatted(input.getFirst().replaceAll(", ", "|")));
        return input.stream()
                .filter(s -> pat.matcher(s).matches())
                .count();
    }

    @Override
    public long part2Long(List<String> input) {
        List<String> stripes = List.of(input.getFirst().split(", "));

        Map<String, Long> matchCount = new HashMap<>();
        matchCount.put("", 1L);

        return input.stream()
                .skip(2)
                .mapToLong(s -> countMatches(stripes, matchCount, s))
                .sum();
    }

    private long countMatches(List<String> stripes, Map<String, Long> matchCount, String s) {
        return stripes.stream()
                .filter(s::startsWith)
                .mapToLong(stripe -> {
                    String substring = s.substring(stripe.length());
                    if (matchCount.containsKey(substring)) {
                        return matchCount.get(substring);
                    }
                    long res = countMatches(stripes, matchCount, substring);
                    matchCount.put(substring, res);
                    return res;
                })
                .sum();
    }
}
