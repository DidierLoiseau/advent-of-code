package org.sedam.aoc;

import java.util.List;

public class Day8 extends Day {
    @Override
    public long part1Long(List<String> input) {
        char[][] map = input.stream().map(String::toCharArray).toArray(char[][]::new);
        boolean[][] antinodes = new boolean[map.length][map[0].length];

        int count = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                char freq = map[i][j];
                if (freq != '.') {
                    for (int k = 0; k < map.length; k++) {
                        for (int l = 0; l < map[0].length; l++) {
                            if ((i != k || j != l) && map[k][l] == freq) {
                                int m = 2 * k - i;
                                int n = 2 * l - j;
                                if (m >= 0 && n >= 0 && m < map.length && n < map[0].length && !antinodes[m][n]) {
                                    antinodes[m][n] = true;
                                    count++;
                                }
                            }
                        }
                    }
                }
            }
        }

        return count;
    }

    @Override
    public long part2Long(List<String> input) {
        char[][] map = input.stream().map(String::toCharArray).toArray(char[][]::new);
        boolean[][] antinodes = new boolean[map.length][map[0].length];

        int count = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                char freq = map[i][j];
                if (freq != '.') {
                    for (int k = 0; k < map.length; k++) {
                        for (int l = 0; l < map[0].length; l++) {
                            if ((i != k || j != l) && map[k][l] == freq) {
                                int diffV = k - i;
                                int diffH = l - j;
                                int m = k;
                                int n = l;
                                while (m >= 0 && n >= 0 && m < map.length && n < map[0].length) {
                                    if (!antinodes[m][n]) {
                                        antinodes[m][n] = true;
                                        count++;
                                    }
                                    m += diffV;
                                    n += diffH;
                                }
                            }
                        }
                    }
                }
            }
        }

        return count;
    }
}
