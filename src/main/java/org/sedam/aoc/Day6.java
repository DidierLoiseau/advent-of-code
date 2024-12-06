package org.sedam.aoc;

import java.util.List;

public class Day6 extends Day {
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

        Direction next() {
            return Direction.values()[(ordinal() + 1) % 4];
        }
    }

    @Override
    public String part1(List<String> input) {
        boolean[][] walls = toWallArray(input);
        int x = input.stream().filter(s -> s.contains("^")).mapToInt(s -> s.indexOf('^')).findFirst().orElseThrow();
        int y = (int) input.stream().takeWhile(s -> !s.contains("^")).count();
        var dir = Direction.N;

        boolean[][] visitedPos = new boolean[walls.length][walls[0].length];
        int visited = 0;

        while (isValid(x, y, walls)) {
            if (!visitedPos[y][x]) {
                visitedPos[y][x] = true;
                visited++;
            }
            int nextX = x + dir.x, nextY = y + dir.y;
            if (isValid(nextX, nextY, walls) && walls[nextY][nextX]) {
                dir = dir.next();
            } else {
                x = nextX;
                y = nextY;
            }
        }

        return "" + visited;
    }

    private static boolean isValid(int x, int y, boolean[][] walls) {
        return x >= 0 && y >= 0 && x < walls[0].length && y < walls.length;
    }

    private boolean[][] toWallArray(List<String> input) {
        return input.stream()
                .map(this::toWallArray)
                .toArray(boolean[][]::new);
    }

    private boolean[] toWallArray(String s) {
        var walls = new boolean[s.length()];
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '#') {
                walls[i] = true;
            }
        }
        return walls;
    }

    @Override
    public String part2(List<String> input) {
        return "";
    }
}
