package org.sedam.aoc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToLongFunction;

import static java.util.stream.Collectors.toMap;

public class Day11 extends Day {
	@Override
	public long part1Long(List<String> input) {
		return new Solver(input).throughAny("you");
	}

	@Override
	public long part2Long(List<String> input) {
		return new Solver(input).throughBoth("svr");
	}

	static class Solver {
		Map<String, List<String>> nodes;

		Map<String, Long> throughDac = new HashMap<>(Map.of("out", 0L));
		Map<String, Long> throughFft = new HashMap<>(Map.of("out", 0L));
		Map<String, Long> throughBoth = new HashMap<>(Map.of("out", 0L));
		Map<String, Long> throughAny = new HashMap<>(Map.of("out", 1L));

		public Solver(List<String> input) {
			nodes = input.stream()
					.collect(toMap(
							s -> s.substring(0, s.indexOf(':')),
							s -> SPACE_PATTERN.splitAsStream(s.substring(s.indexOf(' ') + 1)).toList()
					));
		}

		private long throughBoth(String node) {
			if ("dac".equals(node)) {
				return throughFft(node);
			}
			if ("fft".equals(node)) {
				return throughDac(node);
			}
			return through(throughBoth, this::throughBoth, node);
		}

		private long throughAny(String node) {
			return through(throughAny, this::throughAny, node);
		}

		private long throughDac(String node) {
			if ("dac".equals(node)) {
				return throughAny(node);
			}
			return through(throughDac, this::throughDac, node);
		}

		private long throughFft(String node) {
			if ("fft".equals(node)) {
				return throughAny(node);
			}
			return through(throughFft, this::throughFft, node);
		}

		private long through(Map<String, Long> throughMap, ToLongFunction<String> recursiveFun, String node) {
			var count = throughMap.get(node);
			if (count == null) {
				count = nodes.get(node).stream().mapToLong(recursiveFun).sum();
				throughMap.put(node, count);
			}
			return count;
		}
	}
}
