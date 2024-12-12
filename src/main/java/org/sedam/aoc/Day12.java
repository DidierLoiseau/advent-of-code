package org.sedam.aoc;

import java.util.List;
import java.util.Stack;

public class Day12 extends Day {

    @Override
    public long part1Long(List<String> input) {
        char[][] plots = toChars(input);
        boolean[][] visited = new boolean[plots.length][plots[0].length];

        return Coord.gridStream(plots.length, plots[0].length)
                .filter(c -> !visited[c.x()][c.y()])
                .mapToLong(c -> regionCost(plots, c, visited))
                .sum();
    }

    private long regionCost(char[][] plots, Coord c, boolean[][] visited) {
        char type = plots[c.x()][c.y()];
        Stack<Coord> toVisit = new Stack<>();
        toVisit.add(c);
        int size = 0;
        int fences = 0;
        int maxX = plots.length;
        int maxY = plots[0].length;
        while (!toVisit.isEmpty()) {
            Coord cur = toVisit.pop();
            if (!visited[cur.x()][cur.y()]) {
                visited[cur.x()][cur.y()] = true;
                size++;
                cur.get4Neighb().filter(
                        n -> n.isValid(maxX, maxY) && !visited[n.x()][n.y()] && plots[n.x()][n.y()] == type).forEach(
                        toVisit::add);
                fences += cur.get4Neighb().filter(n -> !n.isValid(maxX, maxY) || plots[n.x()][n.y()] != type).count();
            }
        }
        return (long) size * fences;
    }

    @Override
    public long part2Long(List<String> input) {
        char[][] plots = toChars(input);
        boolean[][] visited = new boolean[plots.length][plots[0].length];

        return Coord.gridStream(plots.length, plots[0].length)
                .filter(c -> !visited[c.x()][c.y()])
                .mapToLong(c -> regionCost2(plots, c, visited))
                .sum();
    }

    private long regionCost2(char[][] plots, Coord c, boolean[][] visited) {
        int width = plots.length;
        int height = plots[0].length;
        char type = plots[c.x()][c.y()];
        boolean[][] inRegion = new boolean[width][height];
        Stack<Coord> toVisit = new Stack<>();
        toVisit.add(c);
        int size = 0;
        while (!toVisit.isEmpty()) {
            Coord cur = toVisit.pop();
            if (!visited[cur.x()][cur.y()]) {
                visited[cur.x()][cur.y()] = true;
                inRegion[cur.x()][cur.y()] = true;
                size++;
                cur.get4Neighb().filter(
                        n -> n.isValid(width, height) && !visited[n.x()][n.y()] && plots[n.x()][n.y()] == type).forEach(
                        toVisit::add);
            }
        }
        int sides = 0;
        boolean aboveInReg = false, belowInReg = false;
        for (int y = 0; y < height; y++) {
            boolean prevInReg = false;
            for (int x = 0; x < width; x++) {
                if (inRegion[x][y]) {
                    // TODO
                }
            }
        }
        return size * sides;
    }
}
