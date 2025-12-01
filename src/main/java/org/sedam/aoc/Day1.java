package org.sedam.aoc;

import java.util.List;

public class Day1 extends Day {
	@Override
	public long part1Long(List<String> input) {
		int pos = 50;
		int count = 0;
		for (var mov : input) {
			int dist = Integer.parseInt(mov.substring(1));
			if (mov.startsWith("L")) {
				pos -= dist;
			}
			else {
				pos += dist;
			}
			pos = (pos + 100) % 100;
			if (pos == 0) {
				count++;
			}
		}
		return count;
	}

	@Override
	public long part2Long(List<String> input) {
		int pos = 50;
		int count = 0;
		for (var mov : input) {
			int dist = Integer.parseInt(mov.substring(1));
			if (mov.startsWith("L")) {
				boolean was0 = pos == 0;
				pos -= dist;
				if (pos <= 0) {
					if (!was0) {
						count++;
					}
					count -= pos / 100;
					pos %= 100;
					if (pos < 0) {
						pos += 100;
					}
				}
			}
			else {
				pos += dist;
				count += pos / 100;
				pos %= 100;
			}
		}
		return count;
	}
}
