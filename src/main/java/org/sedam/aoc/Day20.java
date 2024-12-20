package org.sedam.aoc;

import java.util.ArrayList;
import java.util.List;

public class Day20 extends Day {
    @Override
    public long part1Long(List<String> input) {
        boolean[][] walls = Utils.toWallArray(input);
        var start = Coord.getCoord(input, 'S');
        var end = Coord.getCoord(input, 'E');
        int minCheat = walls.length > 20 ? 102 : 14;

        int[][] time = new int[walls.length][walls[0].length];
        List<Coord> track = new ArrayList<>(List.of(start));
        var cur = start;
        int curTime = 0;
        while (!cur.equals(end)) {
            cur = cur.get4Neighb()
                    .filter(c -> !walls[c.y()][c.x()] && time[c.y()][c.x()] == 0 && !c.equals(start))
                    .findFirst()
                    .orElseThrow();
            time[cur.y()][cur.x()] = ++curTime;
            track.add(cur);
        }

        return track.stream()
                .flatMap(pos -> Direction.stream()
                        .filter(d -> {
                            var o = pos.jump(d, 2);
                            return o.isValid(walls.length, walls[0].length)
                                    && !walls[o.y()][o.x()]
                                    && time[o.y()][o.x()] - time[pos.y()][pos.x()] >= minCheat;
                        }))
                .count();
    }
}
