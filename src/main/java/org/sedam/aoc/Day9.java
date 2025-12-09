package org.sedam.aoc;

import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class Day9 extends Day {
	@Override
	public long part1Long(List<String> input) {
		List<Coord> coords = input.stream()
				.map(Coord::new)
				.toList();
		return coords.stream()
				.flatMapToLong(a -> coords.stream().mapToLong(b -> (abs(a.x() - b.x()) + 1L) * (abs(a.y() - b.y()) + 1)))
				.max()
				.orElseThrow();
	}

	@Override
	public long part2Long(List<String> input) {
		List<Coord> coords = input.stream()
				.map(Coord::new)
				.toList();
		NavigableMap<Integer, NavigableMap<Integer, Integer>> verticalSegments = new TreeMap<>();
		NavigableMap<Integer, NavigableMap<Integer, Integer>> horizontalSegments = new TreeMap<>();
		Stream.concat(Stream.of(coords.getLast()), coords.stream())
				.gather(Gatherers.windowSliding(2))
				.forEach(pair -> {
					var a = pair.getFirst();
					var b = pair.getLast();
					if (a.x() == b.x()) {
						verticalSegments.computeIfAbsent(a.x(), _ -> new TreeMap<>())
								.put(min(a.y(), b.y()), max(a.y(), b.y()));
					} else {
						horizontalSegments.computeIfAbsent(a.y(), _ -> new TreeMap<>())
								.put(min(a.x(), b.x()), max(a.x(), b.x()));
					}
				});

		return coords.stream()
				.flatMapToLong(a -> coords.stream()
						.filter(b -> a.compareTo(b) < 0 && noIntersection(a, b, verticalSegments, horizontalSegments))
						.mapToLong(b -> (abs(a.x() - b.x()) + 1L) * (abs(a.y() - b.y()) + 1)))
				.max()
				.orElseThrow();
	}

	private boolean noIntersection(Coord a, Coord b,
								   NavigableMap<Integer, NavigableMap<Integer, Integer>> verticalSegments,
								   NavigableMap<Integer, NavigableMap<Integer, Integer>> horizontalSegments) {
		var minX = min(a.x(), b.x());
		var minY = min(a.y(), b.y());
		var maxX = max(a.x(), b.x());
		var maxY = max(a.y(), b.y());

		// ok I know I don’t check it is actually inside, but this worked!
		return verticalSegments.subMap(minX, false, maxX, false)
					   .values().stream()
					   .noneMatch(map -> intersects(map, minY, maxY))
			   && horizontalSegments.subMap(minY, false, maxY, false)
					   .values().stream()
					   .noneMatch(map -> intersects(map, minX, maxX));

	}

	private boolean intersects(NavigableMap<Integer, Integer> map, int min, int max) {
		var entry = map.floorEntry(max - 1);
		return entry != null && entry.getValue() > min;
	}
}
