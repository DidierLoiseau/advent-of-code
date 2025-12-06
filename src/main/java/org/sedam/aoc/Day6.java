package org.sedam.aoc;

import java.util.Arrays;
import java.util.List;
import java.util.function.ToLongBiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day6 extends Day {

	public static final Pattern SPACES_PATTERN = Pattern.compile(" +");

	@Override
	public long part1Long(List<String> input) {
		ToLongBiFunction<Long, Long>[] ops = parseOps(input);

		return input.stream()
				.map(String::trim)
				.filter(s -> !s.startsWith("*"))
				.map(s -> SPACES_PATTERN.splitAsStream(s).mapToLong(Long::parseLong).toArray())
				.reduce((l1, l2) -> {
					for (int i = 0; i < ops.length; i++) {
						l1[i] = ops[i].applyAsLong(l1[i], l2[i]);
					}
					return l1;
				})
				.stream()
				.flatMapToLong(Arrays::stream)
				.sum();
	}

	@Override
	public long part2Long(List<String> input) {
		ToLongBiFunction<Long, Long>[] ops = parseOps(input);
		var identities = identities(input);
		var matcher = Pattern.compile("[*+]").matcher(input.getLast());
		int[] colPos = Stream.iterate(matcher, Matcher::find, m -> m)
				.mapToInt(Matcher::start)
				.toArray();
		int maxLength = input.stream().mapToInt(String::length).max().orElseThrow();

		long result = 0;
		char[] num = new char[input.size() -1];
		for (int i = 0; i < colPos.length; i++) {
			long subRes = identities[i];
			if (i < colPos.length - 1) {
				for (int j = colPos[i]; j < colPos[i + 1] - 1; j++) {
					for (int k = 0; k < input.size() - 1; k++) {
						num[k] = input.get(k).charAt(j);
					}
					var parsedNum = Long.parseLong(new String(num).trim());
					subRes = ops[i].applyAsLong(subRes, parsedNum);
				}
			} else {
				for (int j = colPos[i]; j < maxLength; j++) {
					for (int k = 0; k < input.size() - 1; k++) {
						var line = input.get(k);
						num[k] = line.length() > j ? line.charAt(j) : ' ';
					}
					var parsedNum = Long.parseLong(new String(num).trim());
					subRes = ops[i].applyAsLong(subRes, parsedNum);
				}
			}
			result += subRes;
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private ToLongBiFunction<Long, Long>[] parseOps(List<String> input) {
		return SPACES_PATTERN.splitAsStream(input.getLast().trim())
				.map(s -> {
					if (s.equals("+")) {
						return (ToLongBiFunction<Long, Long>) Math::addExact;
					} else {
						return (ToLongBiFunction<Long, Long>) Math::multiplyExact;
					}
				})
				.toArray(ToLongBiFunction[]::new);
	}

	private long[] identities(List<String> input) {
		return SPACES_PATTERN.splitAsStream(input.getLast().trim())
				.mapToLong(s -> {
					if (s.equals("+")) {
						return 0;
					} else {
						return 1;
					}
				})
				.toArray();
	}
}
