package org.sedam.aoc;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

record Coord(int x, int y) implements Comparable<Coord> {
	public Coord(String s) {
		String[] split = s.split(",");
		this(parseInt(split[0]), parseInt(split[1]));
	}

	public static Stream<Coord> gridStream(int width, int length) {
		return IntStream.range(0, width).boxed()
				.flatMap(x -> IntStream.range(0, length).mapToObj(y -> new Coord(x, y)));
	}

	public Coord plus(int dx, int dy) {
		return new Coord(x + dx, y + dy);
	}

	public boolean isValid(int width, int length) {
		return x >= 0 && y >= 0 && x < width && y < length;
	}

	public static Predicate<Coord> isValidPred(int width, int length) {
		return c -> c.isValid(width, length);
	}

	public Stream<Coord> get4Neighb() {
		return Stream.of(
				new Coord(x - 1, y),
				new Coord(x, y - 1),
				new Coord(x + 1, y),
				new Coord(x, y + 1)
		);
	}

	public Stream<Coord> get8Neighb() {
		return Stream.concat(get4Neighb(), Stream.of(
				new Coord(x - 1, y - 1),
				new Coord(x + 1, y - 1),
				new Coord(x + 1, y + 1),
				new Coord(x - 1, y + 1)
		));
	}

	public static Coord getCoord(List<String> input, char c) {
		int x = input.stream().filter(l -> l.contains(c + "")).mapToInt(l -> l.indexOf(c)).findFirst().orElseThrow();
		int y = (int) input.stream().takeWhile(l -> !l.contains(c + "")).count();
		return new Coord(x, y);
	}

	boolean matches(boolean[][] grid) {
		return grid[y()][x()];
	}

	boolean isValidMatching(boolean[][] grid) {
		return isValid(grid.length, grid[0].length)
				&& matches(grid);
	}

	boolean isValidNonMatching(boolean[][] grid) {
		return isValid(grid.length, grid[0].length)
				&& !matches(grid);
	}

	@Override
	public int compareTo(Coord o) {
		if (x < o.x) {
			return -1;
		} else if (x == o.x) {
			if (y < o.y) {
				return -1;
			} else if (y == o.y) {
				return 0;
			}
		}
		return 1;
	}
}
