package org.sedam.aoc;

import java.util.List;

public class Day3 extends Day {
	@Override
	public long part1Long(List<String> input) {
		return input.stream()
				.mapToLong(this::maxJoltage)
				.sum();
	}

	private long maxJoltage(String s) {
		var bytes = s.getBytes();
		byte first = bytes[0], second = bytes[1];
		for (int i = 1; i < bytes.length - 1; i++) {
			if (bytes[i] > first) {
				first = bytes[i];
				second = bytes[i + 1];
			} else if (bytes[i] > second) {
				second = bytes[i];
			}
		}
		if (bytes[bytes.length - 1] > second) {
			second = bytes[bytes.length - 1];
		}
		return Integer.parseInt(new String(new byte[]{first, second}));
	}

	@Override
	public long part2Long(List<String> input) {
		return input.stream()
				.mapToLong(this::max12Joltage)
				.sum();
	}

	private long max12Joltage(String s) {
		var bytes = s.getBytes();
		byte[] result = new byte[12];
		int curPos = 0;
		for (int i = 0; i < 12; i++) {
			curPos = findMaxPos(bytes, curPos, bytes.length - 12 + i);
			result[i] = bytes[curPos];
			curPos++;
		}
		return Long.parseLong(new String(result));
	}

	private int findMaxPos(byte[] bytes, int startIncl, int endIncl) {
		int max = bytes[startIncl], maxPos = startIncl;
		for (int i = startIncl + 1; i <= endIncl; i++) {
			if (bytes[i] > max) {
				max = bytes[i];
				maxPos = i;
			}
		}
		return maxPos;
	}
}
