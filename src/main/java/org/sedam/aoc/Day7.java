package org.sedam.aoc;

import java.util.List;

import static java.lang.Long.parseLong;

public class Day7 extends Day {
    @Override
    public long part1Long(List<String> input) {
        return input.stream().mapToLong(s -> processLine(s, false)).sum();
    }

    private long processLine(String s, boolean allowConcat) {
        var split = s.split(": ");
        long testValue = parseLong(split[0]);
        long[] numbers = toLongArray(split[1]);
        if (solvable(numbers, numbers.length - 1, testValue, allowConcat)) {
            return testValue;
        }
        return 0;
    }

    private boolean solvable(long[] numbers, int i, long testValue, boolean allowConcat) {
        long number = numbers[i];
        if (i == 0) {
            return number == testValue;
        }
        if (allowConcat) {
            // we know our input :)
            if (number < 10) {
                if (testValue % 10 == number && solvable(numbers, i - 1, testValue / 10, true)) {
                    return true;
                }
            } else if (number < 100) {
                if (testValue % 100 == number && solvable(numbers, i - 1, testValue / 100, true)) {
                    return true;
                }
            } else if (testValue % 1000 == number && solvable(numbers, i - 1, testValue / 1000, true)) {
                return true;
            }
        }
        if (testValue % number == 0 && solvable(numbers, i - 1, testValue / number, allowConcat)) {
            return true;
        }
        return testValue > number && solvable(numbers, i - 1, testValue - number, allowConcat);
    }

    @Override
    public long part2Long(List<String> input) {
        return input.stream().mapToLong(s -> processLine(s, true)).sum();
    }
}
