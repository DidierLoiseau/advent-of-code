package org.sedam.aoc;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Day8 extends Day {
	@Override
	public long part1Long(List<String> input) {
		var coords = input.stream()
				.map(Coord3D::new)
				.toList();
		int numWires = coords.size() == 1000 ? 1000 : 10;
		var wires = new TreeSet<Wire>();
		for (var a : coords) {
			for (var b : coords) {
				if (a.compareTo(b) < 0) {
					wires.add(new Wire(a, b));
					if (wires.size() > numWires) {
						wires.removeLast();
					}
				}
			}
		}
		var circuits = new HashMap<Coord3D, Set<Coord3D>>();
		for (var w : wires) {
			var circuitA = circuits.get(w.a);
			var circuitB = circuits.get(w.b);
			if (circuitA == null && circuitB == null) {
				var circuit = new HashSet<>(List.of(w.a, w.b));
				circuits.put(w.a, circuit);
				circuits.put(w.b, circuit);
			} else if (circuitA == null) {
				circuitB.add(w.a);
				circuits.put(w.a, circuitB);
			} else if (circuitB == null) {
				circuitA.add(w.b);
				circuits.put(w.b, circuitA);
			} else if (circuitA != circuitB) {
				circuitA.addAll(circuitB);
				circuitB.forEach(b -> circuits.put(b, circuitA));
			}
		}
		return circuits.values().stream().distinct()
				.sorted(Comparator.<Set<Coord3D>, Integer>comparing(Set::size).reversed())
				.limit(3)
				.mapToLong(Set::size)
				.reduce(1, (x, y) -> x * y);
	}

	@Override
	public long part2Long(List<String> input) {
		var coords = input.stream()
				.map(Coord3D::new)
				.toList();
		var wires = new TreeSet<Wire>();
		for (var a : coords) {
			for (var b : coords) {
				if (a.compareTo(b) < 0) {
					wires.add(new Wire(a, b));
				}
			}
		}
		var circuits = new HashMap<Coord3D, Set<Coord3D>>();
		var numCircuits = coords.size();
		for (var w : wires) {
			var circuitA = circuits.get(w.a);
			var circuitB = circuits.get(w.b);
			numCircuits--;
			if (circuitA == null && circuitB == null) {
				var circuit = new HashSet<>(List.of(w.a, w.b));
				circuits.put(w.a, circuit);
				circuits.put(w.b, circuit);
			} else if (circuitA == null) {
				circuitB.add(w.a);
				circuits.put(w.a, circuitB);
			} else if (circuitB == null) {
				circuitA.add(w.b);
				circuits.put(w.b, circuitA);
			} else if (circuitA != circuitB) {
				circuitA.addAll(circuitB);
				circuitB.forEach(b -> circuits.put(b, circuitA));
			} else {
				numCircuits++;
			}
			if (numCircuits == 1) {
				return (long) w.a.x() * w.b.x();
			}
		}
		throw new IllegalStateException("Did not reach single circuit!");
	}

	record Wire(Coord3D a, Coord3D b, long squareDist) implements Comparable<Wire> {
		public Wire(Coord3D a, Coord3D b) {
			long xDist = a.x() - b.x();
			long yDist = a.y() - b.y();
			long zDist = a.z() - b.z();
			long squareDist = xDist * xDist + yDist * yDist + zDist * zDist;
			this(a, b, squareDist);
		}

		@Override
		public int compareTo(Wire o) {
			if (squareDist < o.squareDist) {
				return -1;
			} else if (squareDist > o.squareDist) {
				return 1;
			}
			if (a.equals(o.a)) {
				return b.compareTo(o.b);
			}
			return a.compareTo(o.a);
		}
	}
}
