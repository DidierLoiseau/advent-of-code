package org.sedam.aoc;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Day20 extends Day {
    @Override
    public long part1Long(List<String> input) {
        boolean[][] walls = Utils.toWallArray(input);
        var start = Coord.getCoord(input, 'S');
        var end = Coord.getCoord(input, 'E');
        int minCheat = walls.length > 20 ? 100 : 12;

        return countCheats(walls, start, end, minCheat, 2);
    }

    @Override
    public long part2Long(List<String> input) {
        boolean[][] walls = Utils.toWallArray(input);
        var start = Coord.getCoord(input, 'S');
        var end = Coord.getCoord(input, 'E');
        int minSparedTime = walls.length > 20 ? 100 : 70;

        return countCheats(walls, start, end, minSparedTime, 20);
    }

    private long countCheats(boolean[][] walls, Coord start, Coord end, int minSparedTime, int maxCheatLen) {
        int[][] time = new int[walls.length][walls[0].length];
        List<Coord> track = new ArrayList<>(List.of(start));
        var cur = start;
        int curTime = 0;
        while (!cur.equals(end)) {
            cur = cur.get4Neighb()
                    .filter(c -> !c.matches(walls) && time[c.y()][c.x()] == 0 && !c.equals(start))
                    .findFirst()
                    .orElseThrow();
            time[cur.y()][cur.x()] = ++curTime;
            track.add(cur);
        }

        return track.stream()
                .limit(track.size() - minSparedTime - 2)
                .mapToLong(pos -> countCheats(time, pos, minSparedTime, maxCheatLen))
                .sum();
    }

    private long countCheats(int[][] time, Coord pos, int minSparedTime, int maxCheatLen) {
        long count = 0;
        int width = time[0].length;
        int height = time.length;
        int startTime = time[pos.y()][pos.x()];
        for (int x = -maxCheatLen; x <= maxCheatLen; x++) {
            for (int y = -maxCheatLen; y <= maxCheatLen; y++) {
                int dist = abs(x) + abs(y);
                if (dist <= maxCheatLen) {
                    Coord c = pos.plus(x, y);
                    if (c.isValid(width, height)
                            // if it’s a wall, time will be 0, so value will be negative
                            && time[c.y()][c.x()] - startTime - dist >= minSparedTime) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
