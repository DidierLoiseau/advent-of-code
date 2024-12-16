package org.sedam.aoc;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Stream;

import static org.sedam.aoc.Day16.Direction.E;

public class Day16 extends Day {
    enum Direction {
        N(0, -1),
        E(1, 0),
        S(0, 1),
        W(-1, 0);

        final int x;

        final int y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Stream<Direction> rotations() {
            return Stream.of(Direction.values()[(ordinal() + 1) % 4], Direction.values()[(ordinal() + 3) % 4]);
        }
    }

    record Node(Coord coord, Direction dir) {
        Node move() {
            return new Node(new Coord(coord.x() + dir.x, coord.y() + dir.y), dir);
        }

        Stream<Node> rotations() {
            return dir.rotations().map(d -> new Node(coord, d));
        }
    }

    @Override
    public long part1Long(List<String> input) {
        boolean[][] walls = Utils.toWallArray(input);
        Coord start = new Coord(1, walls.length - 2);
        Coord target = new Coord(walls[0].length - 2, 1);
        var nodeCost = new HashMap<Node, Integer>();
        Node startNode = new Node(start, E);
        nodeCost.put(startNode, 0);

        var queue = new PriorityQueue<Node>(Comparator.comparing(nodeCost::get));
        queue.offer(startNode);
        while (true) {
            var node = queue.remove();
            int cost = nodeCost.get(node);
            if (node.coord.equals(target)) {
                return cost;
            }
            var moved = node.move();
            if (!walls[moved.coord.y()][moved.coord.x()]) {
                var existing = nodeCost.get(moved);
                if (existing == null || existing > cost + 1) {
                    queue.remove(moved);
                    nodeCost.put(moved, cost + 1);
                    queue.add(moved);
                }
            }
            node.rotations().filter(r -> !nodeCost.containsKey(r) || nodeCost.get(r) > cost + 1000)
                    .forEach(r -> {
                        queue.remove(r);
                        nodeCost.put(r, cost + 1000);
                        queue.add(r);
                    });
        }
    }
}
