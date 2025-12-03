package org.sedam.aoc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Day2 extends Day {
	@Override
	public long part1Long(List<String> input) {
		return Pattern.compile(",").splitAsStream(input.getFirst())
				.mapToLong(this::sumPalindromesInRange)
				.sum();
	}

	private long sumPalindromesInRange(String s) {
		var dash = s.indexOf('-');
		var rangeEnd = s.substring(dash + 1);
		if (rangeEnd.length() < dash || rangeEnd.length() > dash + 1) {
			throw new IllegalArgumentException("can’t handle more than 1 digit length difference: " + s);
		}
		int start, max;
		long mult;
		if (dash % 2 == 0) {
			start = determineFirstPossiblePalindrome(s, dash, true);
			mult = Math.powExact(10L, dash / 2);
			if (rangeEnd.length() == dash) {
				// same number of digits, so we need to stop at that number
				max = determineFirstPossiblePalindrome(rangeEnd, dash, false);
			} else {
				// 1 more digit – stop at next power of 10
				max = (int) mult;
			}
		} else {
			start = Math.powExact(10, dash / 2);
			mult = start * 10L;
			if (rangeEnd.length() == dash) {
				// range end would be lower than start
				return 0;
			} else {
				max = determineFirstPossiblePalindrome(rangeEnd, dash + 1, false);
			}
		}

		long result = 0;
		for (int i = start; i < max; i++) {
			var palindrome = i * mult + i;
			result += palindrome;
		}

		return result;
	}

	private int determineFirstPossiblePalindrome(String s, int endPos, boolean includeS) {
		int start;
		var left = Integer.parseInt(s.substring(0, endPos / 2));
		var right = Integer.parseInt(s.substring(endPos / 2, endPos));
		if (left < right || (!includeS && left == right)) {
			start = left + 1;
		} else {
			start = left;
		}
		return start;
	}

	@Override
	public long part2Long(List<String> input) {
		return Pattern.compile(",").splitAsStream(input.getFirst())
				.mapToLong(this::sumRepetitionsInRange)
				.sum();
	}

	private long sumRepetitionsInRange(String s) {
		var startLen = s.indexOf('-');
		var rangeEnd = s.substring(startLen + 1);
		if (rangeEnd.length() < startLen || rangeEnd.length() > startLen + 1) {
			throw new IllegalArgumentException("can’t handle more than 1 digit length difference: " + s);
		}
		long start = Long.parseLong(s.substring(0, startLen));
		long end = Long.parseLong(s.substring(startLen + 1));
		long nextPow10 = Math.powExact(10L, startLen);
		return LongStream.rangeClosed(start, end)
				.flatMap(i ->
						getPatterns(i < nextPow10 ? startLen : startLen + 1)
								.filter(l -> i % l == 0)
								.map(_ -> i)
								.limit(1)
				)
				.sum();
	}

	private final Map<Integer, long[]> patterns = new HashMap<>();

	private LongStream getPatterns(int len) {
		var patternForLen = patterns.computeIfAbsent(len, _ -> IntStream.rangeClosed(1, len / 2)
				.filter(i -> len % i == 0)
				.mapToLong(i -> buildPattern(i, len / i))
				.toArray());
		return Arrays.stream(patternForLen);
	}

	private long buildPattern(int numDigits, int numRepeats) {
		long pattern = 1;
		int mult = Math.powExact(10, numDigits);
		for (int j = 1; j < numRepeats; j++) {
			pattern *= mult;
			pattern++;
		}
		return pattern;
	}
}
