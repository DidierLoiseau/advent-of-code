package org.sedam.aoc;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class Day14 extends Day {
    static final Pattern BOT = Pattern.compile("p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)");
    int width, height;

    @Override
    public long part1Long(List<String> input) {
        if (input.size() < 20) {
            width = 11;
            height = 7;
        } else {
            width = 101;
            height = 103;
        }
        var res = input.stream()
                .map(this::simulateBot)
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        return res.get(1) * res.get(2) * res.get(3) * res.get(4);
    }

    private int simulateBot(String s) {
        var matcher = BOT.matcher(s);
        matcher.matches();
        int x = parseInt(matcher.group(1));
        int y = parseInt(matcher.group(2));
        int vx = parseInt(matcher.group(3));
        int vy = parseInt(matcher.group(4));

        x = Math.floorMod(x + 100 * vx, width);
        y = Math.floorMod(y + 100 * vy, height);
        if (x == width / 2 || y == height / 2) {
            return 0;
        }
        if (x < width / 2) {
            return y < height / 2 ? 1 : 3;
        }
        return y < height / 2 ? 2 : 4;
    }

    record Bot(int x, int y, int vx, int vy) {
        Bot(Matcher matcher) {
            this(
                    parseInt(matcher.group(1)),
                    parseInt(matcher.group(2)),
                    parseInt(matcher.group(3)),
                    parseInt(matcher.group(4))
            );
        }

        Bot move(int width, int height) {
            int x = this.x + vx;
            int y = this.y + vy;
            if (x >= width) {
                x -= width;
            }
            if (y >= height) {
                y -= height;
            }
            if (x < 0) {
                x += width;
            }
            if (y < 0) {
                y += height;
            }
            return new Bot(x, y, vx, vy);
        }
    }

    public static void main(String[] args) {
        new Day14().part2Long(Utils.readLines("day14/input-1.txt"));
    }

    @Override
    public long part2Long(List<String> input) {
        width = 101;
        height = 103;
        var bots = input.stream().map(s -> {
            var matcher = BOT.matcher(s);
            matcher.matches();
            return new Bot(matcher);
        }).toList();

        // alignments at i % 103 = 31 & i % 101 = 72 (sorry dear reader, if you are not me, this probably won’t work for you 😏)
        for (int i = 1; i < 103 * 101; i++) {
            bots = bots.stream().map(b -> b.move(width, height)).toList();
            if (i % 103 == 31 && i % 101 == 72) {
                System.out.println("Iter " + i);
                boolean[][] grid = new boolean[width][height];
                for (Bot bot : bots) {
                    grid[bot.x][bot.y] = true;
                }
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        System.out.print(grid[x][y] ? '+' : ' ');
                    }
                    System.out.println();
                }
                return i;
            }
        }
        return 0;
    }

    @Override
    public boolean hasPart2ExpectedResult() {
        return false;
    }
}
