package org.sedam.aoc;

import java.util.List;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class Day3 extends Day {
	public String part1(List<String> input) {
		var pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
		return "" + input.stream()
				.flatMap(s -> pattern.matcher(s).results())
				.mapToInt(m -> parseInt(m.group(1)) * parseInt(m.group(2)))
				.sum();
	}

	public String part2(List<String> input) {
		var pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
		var filtered = String.join("", input)
				.replaceAll("don't\\(\\).*?do\\(\\)", "");
		System.out.println(filtered);
		return "" + pattern.matcher(filtered)
				.results()
				.mapToInt(m -> parseInt(m.group(1)) * parseInt(m.group(2)))
				.sum();
	}

}
