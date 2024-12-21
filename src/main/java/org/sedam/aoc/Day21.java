package org.sedam.aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import static java.lang.Math.abs;
import static java.util.Map.entry;
import static java.util.stream.Collectors.joining;
import static org.sedam.aoc.Day21.Move.DOWN;
import static org.sedam.aoc.Day21.Move.LEFT;
import static org.sedam.aoc.Day21.Move.PRESS;
import static org.sedam.aoc.Day21.Move.RIGHT;
import static org.sedam.aoc.Day21.Move.UP;

public class Day21 extends Day {
    @RequiredArgsConstructor
    enum Move {
        UP("^"), RIGHT(">"), DOWN("v"), LEFT("<"), PRESS("A");

        final String rep;

        public String toString() {
            return rep;
        }
    }

    void repeat(Move m, int times, Consumer<Move> c) {
        Stream.generate(() -> m).limit(times).forEach(c);
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
            entry('A', new Coord(2, 3))
    );

    List<Move> getNumpadMoves(String s) {
        var pos = new Coord(2, 3);
        var res = new ArrayList<Move>();
        for (char c : s.toCharArray()) {
            var dest = NUMPAD.get(c);
            // moving left is costly, so prefer to do it first and do up or right on the way back
            if (dest.x() < pos.x() && (pos.y() < 3 || dest.x() > 0)) {
                repeat(LEFT, pos.x() - dest.x(), res::add);
                pos = new Coord(dest.x(), pos.y());
            }
            // moving down is costly (requires down+left), so prefer it over right
            if (dest.y() > pos.y() && (dest.y() < 3 || pos.x() > 0)) {
                repeat(DOWN, dest.y() - pos.y(), res::add);
                pos = new Coord(pos.x(), dest.y());
            }
            if (dest.y() < pos.y()) {
                repeat(UP, pos.y() - dest.y(), res::add);
            }
            if (dest.x() != pos.x()) {
                repeat(dest.x() < pos.x() ? LEFT : RIGHT, abs(pos.x() - dest.x()), res::add);
            }
            if (dest.y() > pos.y()) {
                repeat(DOWN, dest.y() - pos.y(), res::add);
            }
            res.add(PRESS);
            pos = dest;
        }
        return res;
    }

    final Map<Move, Coord> DIRPAD = Map.of(
            UP, new Coord(1, 0),
            PRESS, new Coord(2, 0),
            LEFT, new Coord(0, 1),
            DOWN, new Coord(1, 1),
            RIGHT, new Coord(2, 1)
    );

    List<Move> getDirpadMoves(List<Move> moves) {
        var pos = new Coord(2, 0);
        var res = new ArrayList<Move>();
        for (var move : moves) {
            var dest = DIRPAD.get(move);
            // moving left is costly, so prefer to do it first and do up or right on the way back
            if (dest.x() < pos.x() && (pos.y() != 0 || dest.x() > 0)) {
                repeat(LEFT, pos.x() - dest.x(), res::add);
                pos = new Coord(dest.x(), pos.y());
            }
            if (dest.y() > pos.y()) {
                repeat(DOWN, dest.y() - pos.y(), res::add);
            }
            if (dest.x() != pos.x()) {
                repeat(dest.x() < pos.x() ? LEFT : RIGHT, abs(pos.x() - dest.x()), res::add);
            }
            if (dest.y() < pos.y()) {
                repeat(UP, pos.y() - dest.y(), res::add);
            }
            res.add(PRESS);
            pos = dest;
        }
        return res;
    }

    @Override
    public long part1Long(List<String> input) {
        return input.stream()
                .mapToInt(s -> {
                    var numpadMoves = getNumpadMoves(s);
                    var dirpad1Moves = getDirpadMoves(numpadMoves);
                    var dirpad2Moves = getDirpadMoves(dirpad1Moves);
                    return Integer.parseInt(s.substring(0, s.length() - 1)) * dirpad2Moves.size();
                })
                .sum();
    }
}
