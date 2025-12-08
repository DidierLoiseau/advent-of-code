package org.sedam.aoc;

public record Coord3D(int x, int y, int z) implements Comparable<Coord3D> {
	public Coord3D(String s) {
		var split = s.split(",");
		var x = Integer.parseInt(split[0]);
		var y = Integer.parseInt(split[1]);
		var z = Integer.parseInt(split[2]);
		this(x, y, z);
	}

	@Override
	public int compareTo(Coord3D o) {
		if (x < o.x) {
			return -1;
		} else if (x == o.x) {
			if (y < o.y) {
				return -1;
			} else if (y == o.y) {
				if (z < o.z) {
					return -1;
				} else if (z == o.z) {
					return 0;
				}
			}
		}
		return 1;
	}
}
