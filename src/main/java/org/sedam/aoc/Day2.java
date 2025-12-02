package org.sedam.aoc;

import java.util.List;
import java.util.regex.Pattern;

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
}
