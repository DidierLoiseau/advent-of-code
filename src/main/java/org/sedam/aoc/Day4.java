package org.sedam.aoc;

import java.util.List;

public class Day4 extends Day {
	@Override
	public long part1Long(List<String> input) {
		var rolls = Utils.toBoolArray(input, '@');
		int count = 0;
		for (int x = 0; x < rolls[0].length; x++) {
			for (int y = 0; y < rolls.length; y++) {
				if (rolls[y][x]) {
					var coord = new Coord(x, y);
					if (coord.get8Neighb().filter(c -> c.isValidMatching(rolls)).count() < 4) {
						count++;
					}
				}
			}
		}
		return count;
	}
	@Override
	public long part2Long(List<String> input) {
		var rolls = Utils.toBoolArray(input, '@');
		int count = 0;
		boolean rollRemoved;
		do {
			rollRemoved = false;
			for (int x = 0; x < rolls[0].length; x++) {
				for (int y = 0; y < rolls.length; y++) {
					if (rolls[y][x]) {
						var coord = new Coord(x, y);
						if (coord.get8Neighb().filter(c -> c.isValidMatching(rolls)).count() < 4) {
							count++;
							rollRemoved = true;
							rolls[y][x] = false;
						}
					}
				}
			}
		} while (rollRemoved);
		return count;
	}
}
