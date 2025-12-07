package org.sedam.aoc;

import java.util.Arrays;
import java.util.List;

public class Day7 extends Day {
	@Override
	public long part1Long(List<String> input) {
		var first = input.getFirst();
		boolean[] beams = new boolean[first.length()];
		for (int i = 0; i < first.length(); i++) {
			beams[i] = first.charAt(i) == 'S';
		}

		return input.stream().skip(1)
				.mapToLong(s -> {
					int count = 0;
					for (int i = 0; i < s.length(); i++) {
						if (beams[i] && s.charAt(i) == '^') {
							beams[i] = false;
							beams[i - 1] = true;
							beams[i + 1] = true;
							count++;
						}
					}
					return count;
				})
				.sum();
	}

	@Override
	public long part2Long(List<String> input) {
		var first = input.getFirst();
		long[] beams = new long[first.length()];
		for (int i = 0; i < first.length(); i++) {
			beams[i] = first.charAt(i) == 'S' ? 1 : 0;
		}

		input.stream().skip(1)
				.forEach(s -> {
					for (int i = 0; i < s.length(); i++) {
						if (beams[i] > 0 && s.charAt(i) == '^') {
							beams[i - 1] += beams[i];
							beams[i + 1] += beams[i];
							beams[i] = 0;
						}
					}
				});

		return Arrays.stream(beams).sum();
	}
}
