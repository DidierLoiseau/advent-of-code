package org.sedam.aoc;

import java.util.*;
import java.util.stream.Stream;

import static org.sedam.aoc.Day16.Direction.E;
import static org.sedam.aoc.Day16.Direction.N;

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

        Node moveBack() {
            return new Node(new Coord(coord.x() - dir.x, coord.y() - dir.y), dir);
        }

        Stream<Node> rotations() {
            return dir.rotations().map(d -> new Node(coord, d));
        }

        public Node withDirection(Direction direction) {
            return new Node(coord, direction);
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
            updateNeighbours(walls, nodeCost, queue, node, cost);
        }
    }

    @Override
    public long part2Long(List<String> input) {
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
                nodeCost.put(node.withDirection(E), cost);
                nodeCost.put(node.withDirection(N), cost);
                break;
            }
            updateNeighbours(walls, nodeCost, queue, node, cost);
        }

        boolean[][] inBestPath = new boolean[walls.length][walls[0].length];
        inBestPath[target.y()][target.x()] = true;
        int bestPathCount = 1;
        Stack<Node> toExplore = new Stack<>();
        toExplore.push(new Node(target, E));
        toExplore.push(new Node(target, N));
        Set<Node> visited = new HashSet<>();
        while (!toExplore.isEmpty()) {
            var node = toExplore.pop();
            if (!visited.add(node)) {
                continue;
            }
            int cost = nodeCost.get(node);
            var prev = node.moveBack();
            if (nodeCost.containsKey(prev) && nodeCost.get(prev) == cost - 1) {
                toExplore.push(prev);
                if (!inBestPath[prev.coord.y()][prev.coord.x()]) {
                    inBestPath[prev.coord.y()][prev.coord.x()] = true;
                    bestPathCount++;
                }
            }
            bestPathCount += node.rotations().filter(r -> nodeCost.containsKey(r) && nodeCost.get(r) == cost - 1000)
                    .mapToInt(r -> {
                        toExplore.push(r);
                        if (!inBestPath[r.coord.y()][r.coord.x()]) {
                            inBestPath[r.coord.y()][r.coord.x()] = true;
                            return 1;
                        }
                        return 0;
                    })
                    .sum();
        }

        return bestPathCount;
    }

    private void updateNeighbours(boolean[][] walls, HashMap<Node, Integer> nodeCost, PriorityQueue<Node> queue, Node curNode, int curCost) {
        var moved = curNode.move();
        if (!walls[moved.coord.y()][moved.coord.x()]) {
            var existing = nodeCost.get(moved);
            if (existing == null || existing > curCost + 1) {
                queue.remove(moved);
                nodeCost.put(moved, curCost + 1);
                queue.add(moved);
            }
        }
        curNode.rotations().filter(r -> !nodeCost.containsKey(r) || nodeCost.get(r) > curCost + 1000)
                .forEach(r -> {
                    queue.remove(r);
                    nodeCost.put(r, curCost + 1000);
                    queue.add(r);
                });
    }
}
