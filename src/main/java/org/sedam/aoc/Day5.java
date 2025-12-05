package org.sedam.aoc;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Day5 extends Day {
	@Override
	public long part1Long(List<String> input) {
		var ranges = parseRanges(input);
		return input.stream().dropWhile(s -> !s.isEmpty())
				.filter(s -> !s.isEmpty())
				.mapToLong(Long::parseLong)
				.filter(l -> {
					var entry = ranges.floorEntry(l);
					return entry != null && l <= entry.getValue();
				})
				.count();
	}

	@Override
	public long part2Long(List<String> input) {
		var ranges = parseRanges(input);
		return ranges.entrySet().stream()
				.mapToLong(e -> e.getValue() - e.getKey() + 1)
				.sum();
	}

	private TreeMap<Long, Long> parseRanges(List<String> input) {
		var ranges = input.stream()
				.takeWhile(s -> !s.isEmpty())
				.map(s -> s.split("-"))
				.collect(Collectors.toMap(
						s -> Long.parseLong(s[0]),
						s -> Long.parseLong(s[1]),
						Math::max,
						TreeMap::new));
		// merge overlaps
		var entry = ranges.firstEntry();
		while (entry != null) {
			var key = entry.getKey();
			var value = entry.getValue();
			var next = ranges.higherEntry(key);
			while (next != null && next.getKey() <= value) {
				value = Math.max(value, next.getValue());
				ranges.put(key, value);
				ranges.remove(next.getKey());
				next = ranges.higherEntry(key);
			}
			entry = next;
		}
		return ranges;
	}

}
