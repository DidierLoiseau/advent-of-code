package org.sedam.aoc;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import static java.lang.Integer.parseInt;

public class Day18 extends Day {
    @Override
    public long part1Long(List<String> input) {
        int width = input.size() < 30 ? 7 : 71;
        int numWalls = input.size() < 30 ? 12 : 1024;
        boolean[][] walls = new boolean[width][width];
        input.stream().limit(numWalls).forEach(s -> {
            String[] sp = s.split(",");
            walls[parseInt(sp[0])][parseInt(sp[1])] = true;
        });
        Coord start = new Coord(0, 0);
        Coord target = new Coord(width - 1, width - 1);

        var nodeCost = new HashMap<Coord, Integer>();
        nodeCost.put(start, 0);

        var queue = new PriorityQueue<Coord>(Comparator.comparing(nodeCost::get));
        queue.offer(start);
        while (true) {
            var node = queue.remove();
            int cost = nodeCost.get(node);
            if (node.equals(target)) {
                return cost;
            }
            updateNeighbours(walls, nodeCost, queue, node, cost);
        }
    }

    private void updateNeighbours(boolean[][] walls, HashMap<Coord, Integer> nodeCost, PriorityQueue<Coord> queue,
                                  Coord curNode, int curCost) {
        curNode.get4Neighb()
                .filter(c -> c.isValid(walls.length, walls.length) && !walls[c.x()][c.y()])
                .filter(r -> !nodeCost.containsKey(r) || nodeCost.get(r) > curCost + 1)
                .forEach(r -> {
                    queue.remove(r);
                    nodeCost.put(r, curCost + 1);
                    queue.add(r);
                });
    }

    @Override
    public String part2(List<String> input) {
        int width = input.size() < 30 ? 7 : 71;
        int numWalls = input.size() < 30 ? 12 : 1024;
        boolean[][] walls = new boolean[width][width];
        input.stream().limit(numWalls).forEach(s -> {
            String[] sp = s.split(",");
            walls[parseInt(sp[0])][parseInt(sp[1])] = true;
        });

        for (int i = numWalls; i < input.size(); i++) {
            String s = input.get(i);
            String[] sp = s.split(",");
            walls[parseInt(sp[0])][parseInt(sp[1])] = true;
            if (!hasExit(walls)) {
                return s;
            }
        }

        return "";
    }

    private boolean hasExit(boolean[][] walls) {
        Coord start = new Coord(0, 0);
        Coord target = new Coord(walls.length - 1, walls.length - 1);
        HashMap<Coord, Integer> nodeCost = new HashMap<>();
        nodeCost.put(start, 0);
        var queue = new PriorityQueue<Coord>(Comparator.comparing(nodeCost::get));
        queue.offer(start);
        while (!queue.isEmpty()) {
            var node = queue.remove();
            int cost = nodeCost.get(node);
            if (node.equals(target)) {
                return true;
            }
            updateNeighbours(walls, nodeCost, queue, node, cost);
        }
        return false;
    }
}
