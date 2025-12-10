package org.sedam.aoc;

import lombok.SneakyThrows;
import org.ssclab.pl.milp.Constraint;
import org.ssclab.pl.milp.GoalType;
import org.ssclab.pl.milp.LP;
import org.ssclab.pl.milp.LinearObjectiveFunction;
import org.ssclab.pl.milp.MILP;
import org.ssclab.pl.milp.SolutionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;
import static org.ssclab.pl.milp.ConsType.EQ;
import static org.ssclab.pl.milp.ConsType.GE;
import static org.ssclab.pl.milp.ConsType.INT;

public class Day10 extends Day {
	private static final Pattern PATTERN = Pattern.compile("\\[([#.]+)] (\\(.+\\)) \\{(.+)}");

	@Override
	public long part1Long(List<String> input) {
		return input.stream().mapToLong(this::part1).sum();
	}

	private long part1(String s) {
		var matcher = PATTERN.matcher(s);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Could not match " + s);
		}
		var lights = Utils.toBoolArray(matcher.group(1), '#');
		var switches = SPACE_PATTERN.splitAsStream(matcher.group(2))
				.map(p -> Arrays.stream(p.substring(1, p.length() - 1).split(",")).mapToInt(Integer::parseInt).toArray())
				.toList();
		var usedSwitches = new boolean[switches.size()];
		for (int i = 1; i < switches.size(); i++) {
			if (hasSolution(lights, switches, usedSwitches, i)) {
				return i;
			}
		}
		throw new IllegalStateException("Could not find solution for " + s);
	}

	private boolean hasSolution(boolean[] lights, List<int[]> switches, boolean[] usedSwitches, int n) {
		if (n == 0) {
			return IntStream.range(0, lights.length).noneMatch(i -> lights[i]);
		}
		return IntStream.range(0, switches.size())
				.filter(i -> !usedSwitches[i] && atLeastOneMatch(lights, switches.get(i)))
				.anyMatch(i -> pressAndSearch(lights, switches, i, usedSwitches, n - 1));
	}

	private boolean atLeastOneMatch(boolean[] lights, int[] sw) {
		for (int light : sw) {
			if (lights[light]) {
				return true;
			}
		}
		return false;
	}

	private boolean pressAndSearch(boolean[] lights, List<int[]> switches, int swn, boolean[] usedSwitches, int n) {
		usedSwitches[swn] = true;
		for (int light : switches.get(swn)) {
			lights[light] = !lights[light];
		}
		if (hasSolution(lights, switches, usedSwitches, n)) {
			return true;
		}
		// revert our changes
		usedSwitches[swn] = false;
		for (int light : switches.get(swn)) {
			lights[light] = !lights[light];
		}
		return false;
	}

	@Override
	public long part2Long(List<String> input) {
		IO.println();
		return input.stream().mapToLong(this::part2).sum();
	}

	@SneakyThrows
	private long part2(String s) {
		IO.print("Processing " + s);
		var matcher = PATTERN.matcher(s);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Could not match " + s);
		}
		var switches = SPACE_PATTERN.splitAsStream(matcher.group(2))
				.map(p -> Arrays.stream(p.substring(1, p.length() - 1).split(","))
						.map(Integer::valueOf)
						.collect(toSet()))
				.toList();
		var counters = Arrays.stream(matcher.group(3).split(","))
				.mapToInt(Integer::parseInt)
				.toArray();
		// variables = number of button presses
		var allVars = DoubleStream.generate(() -> 1).limit(switches.size()).toArray();

		var constraints = new ArrayList<Constraint>();
		// all integer
		constraints.add(new Constraint(allVars, INT, LP.NaN));
		// all ⩾ 0
		for (int i = 0; i < switches.size(); i++) {
			var aj = new double[switches.size()];
			aj[i] = 1;
			constraints.add(new Constraint(aj, GE, 0));
		}
		// button presses equal target counter
		for (int counter = 0; counter < counters.length; counter++) {
			int finalCounter = counter;
			var aj = switches.stream()
					.mapToDouble(sw -> sw.contains(finalCounter) ? 1 : 0)
					.toArray();
			constraints.add(new Constraint(aj, EQ, counters[counter]));
		}

		// minimize sum (button presses)
		var f = new LinearObjectiveFunction(allVars, GoalType.MIN);

		var milp = new MILP(f, constraints);
		var solType = milp.resolve();
		if (solType == SolutionType.OPTIMAL) {
			var optimumValue = milp.getSolution().getOptimumValue();
			IO.println(": " + optimumValue);
			return (long) optimumValue;
		} else {
			throw new IllegalStateException("Could not solve " + s + ": " + solType);
		}
	}
}
