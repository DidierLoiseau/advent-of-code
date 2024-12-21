package org.sedam.aoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import static java.lang.Math.abs;
import static java.util.Map.entry;
import static org.sedam.aoc.Day21.Move.DOWN;
import static org.sedam.aoc.Day21.Move.LEFT;
import static org.sedam.aoc.Day21.Move.PRESS;
import static org.sedam.aoc.Day21.Move.RIGHT;
import static org.sedam.aoc.Day21.Move.UP;

public class Day21 extends Day {

    public static final Coord NUMPAD_A = new Coord(2, 3);

    public static final Coord DIRPAD_A = new Coord(2, 0);

    @RequiredArgsConstructor
    enum Move {
        UP("^"), RIGHT(">"), DOWN("v"), LEFT("<"), PRESS("A");

        final String rep;

        public String toString() {
            return rep;
        }
    }

    record SmartMove(Move m, int times) {
    }

    record CostKey(List<SmartMove> moves, int depth) {
    }

    final Map<Character, Coord> NUMPAD = Map.ofEntries(
            entry('7', new Coord(0, 0)),
            entry('8', new Coord(1, 0)),
            entry('9', new Coord(2, 0)),
            entry('4', new Coord(0, 1)),
            entry('5', new Coord(1, 1)),
            entry('6', new Coord(2, 1)),
            entry('1', new Coord(0, 2)),
            entry('2', new Coord(1, 2)),
            entry('3', new Coord(2, 2)),
            entry('0', new Coord(1, 3)),
            entry('A', NUMPAD_A)
    );

    private long getCost(Map<CostKey, Long> costs, String s, int depth) {
        return getSmartNumpadMoves(s)
                .stream()
                .mapToLong(m -> getCost(costs, m, depth))
                .sum();
    }

    private long getCost(Map<CostKey, Long> costs, List<SmartMove> moves, int depth) {
        if (depth == 0) {
            return moves.stream().mapToLong(m -> m.times).sum() + 1;
        }
        CostKey key = new CostKey(moves, depth);
        if (costs.containsKey(key)) {
            return costs.get(key);
        }
        long cost = getSmartDirpadMoves(moves).stream()
                .mapToLong(higherMoves -> getCost(costs, higherMoves, depth - 1))
                .sum();
        costs.put(key, cost);
        return cost;
    }

    List<List<SmartMove>> getSmartNumpadMoves(String s) {
        var pos = NUMPAD_A;
        var main = new ArrayList<List<SmartMove>>();
        for (char c : s.toCharArray()) {
            var res = new ArrayList<SmartMove>();
            var dest = NUMPAD.get(c);
            // moving left is costly, so prefer to do it first and do up or down on the way back
            if (dest.x() < pos.x() && (pos.y() < 3 || dest.x() > 0)) {
                res.add(new SmartMove(LEFT, pos.x() - dest.x()));
                pos = new Coord(dest.x(), pos.y());
            }
            // moving down is costly (requires down+left), so prefer it first over right
            if (dest.y() > pos.y() && (dest.y() < 3 || pos.x() > 0)) {
                res.add(new SmartMove(DOWN, dest.y() - pos.y()));
                pos = new Coord(pos.x(), dest.y());
            }
            if (dest.y() < pos.y()) {
                res.add(new SmartMove(UP, pos.y() - dest.y()));
            }
            if (dest.x() != pos.x()) {
                res.add(new SmartMove(dest.x() < pos.x() ? LEFT : RIGHT, abs(pos.x() - dest.x())));
            }
            if (dest.y() > pos.y()) {
                res.add(new SmartMove(DOWN, dest.y() - pos.y()));
            }
            pos = dest;
            main.add(res);
        }
        return main;
    }

    final Map<Move, Coord> DIRPAD = Map.of(
            UP, new Coord(1, 0),
            PRESS, DIRPAD_A,
            LEFT, new Coord(0, 1),
            DOWN, new Coord(1, 1),
            RIGHT, new Coord(2, 1)
    );

    List<List<SmartMove>> getSmartDirpadMoves(List<SmartMove> moves) {
        var pos = DIRPAD_A;
        var main = new ArrayList<List<SmartMove>>();
        for (var move : moves) {
            var dest = DIRPAD.get(move.m);
            main.add(getSmartMoves(pos, dest));
            pos = dest;
            if (move.times > 1) {
                for (int i = 1; i < move.times; i++) {
                    main.add(List.of());
                }
            }
        }
        // back to A at the end
        main.add(getSmartMoves(pos, DIRPAD_A));
        return main;
    }

    private static ArrayList<SmartMove> getSmartMoves(Coord pos, Coord dest) {
        var res = new ArrayList<SmartMove>();
        // moving left is costly, so prefer to do it first and do up or down on the way back
        if (dest.x() < pos.x() && (pos.y() != 0 || dest.x() > 0)) {
            res.add(new SmartMove(LEFT, pos.x() - dest.x()));
            pos = new Coord(dest.x(), pos.y());
        }
        if (dest.y() > pos.y()) {
            res.add(new SmartMove(DOWN, dest.y() - pos.y()));
        }
        if (dest.x() != pos.x()) {
            res.add(new SmartMove(dest.x() < pos.x() ? LEFT : RIGHT, abs(pos.x() - dest.x())));
        }
        if (dest.y() < pos.y()) {
            res.add(new SmartMove(UP, pos.y() - dest.y()));
        }
        return res;
    }

    @Override
    public long part1Long(List<String> input) {
        Map<CostKey, Long> costs = new HashMap<>();
        return input.stream()
                .mapToLong(s -> Long.parseLong(s.substring(0, s.length() - 1)) * getCost(costs, s, 2))
                .sum();
    }

    @Override
    public long part2Long(List<String> input) {
        Map<CostKey, Long> costs = new HashMap<>();
        return input.stream()
                .mapToLong(s -> Long.parseLong(s.substring(0, s.length() - 1)) * getCost(costs, s, 25))
                .sum();
    }

    @Override
    public boolean hasPart2ExpectedResult() {
        return false;
    }
}
