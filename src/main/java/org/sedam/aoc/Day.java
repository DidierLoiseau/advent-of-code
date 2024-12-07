package org.sedam.aoc;

import java.util.List;
import java.util.regex.Pattern;

public class Day {
    protected static final Pattern SPACE_PATTERN = Pattern.compile(" ");

    public String part1(List<String> input) {
        return "" + part1Long(input);
    }

    public long part1Long(List<String> input) {
        throw new UnsupportedOperationException("Not implemented");
    }

    protected int[] toIntArray(String s) {
        return SPACE_PATTERN.splitAsStream(s).mapToInt(Integer::parseInt).toArray();
    }

    protected long[] toLongArray(String s) {
        return SPACE_PATTERN.splitAsStream(s).mapToLong(Long::parseLong).toArray();
    }

    public String part2(List<String> input) {
        return "" + part2Long(input);
    }

    public long part2Long(List<String> input) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
