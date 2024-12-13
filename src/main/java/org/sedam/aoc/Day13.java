package org.sedam.aoc;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Gatherers;

import static java.lang.Integer.parseInt;
import static java.lang.Math.min;

public class Day13 extends Day {
    private static final Pattern BUTTON = Pattern.compile("Button [AB]: X\\+(\\d+), Y\\+(\\d+)");

    private static final Pattern PRIZE = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");

    @Override
    public long part1Long(List<String> input) {
        return input.stream().gather(Gatherers.windowFixed(4))
                .mapToInt(this::computeCost)
                .sum();
    }

    private int computeCost(List<String> machine) {
        Matcher buttonA = BUTTON.matcher(machine.get(0));
        Matcher buttonB = BUTTON.matcher(machine.get(1));
        Matcher prize = PRIZE.matcher(machine.get(2));
        if (!buttonA.matches() || !buttonB.matches() || !prize.matches()) {
            throw new IllegalArgumentException("Failed to match " + machine);
        }

        int ax, ay, bx, by, px, py, maxA, bestCost = 0;
        ax = parseInt(buttonA.group(1));
        ay = parseInt(buttonA.group(2));
        bx = parseInt(buttonB.group(1));
        by = parseInt(buttonB.group(2));
        px = parseInt(prize.group(1));
        py = parseInt(prize.group(2));

        maxA = min(min(px / ax, py / ay), 100);

        for (int a = maxA; a >= 0; a--) {
            int x = ax * a, y = ay * a;
            int b = (px - x) / bx;
            if (b > 100) {
                return bestCost;
            }
            x += b * bx;
            y += b * by;
            if (x == px && y == py) {
                int cost = a * 3 + b;
                if (bestCost == 0 || bestCost > cost) {
                    bestCost = cost;
                }
            }
        }

        return bestCost;
    }

    @Override
    public long part2Long(List<String> input) {
        return input.stream().gather(Gatherers.windowFixed(4))
                .mapToLong(this::computeCost2)
                .sum();
    }

    private long computeCost2(List<String> machine) {
        Matcher buttonA = BUTTON.matcher(machine.get(0));
        Matcher buttonB = BUTTON.matcher(machine.get(1));
        Matcher prize = PRIZE.matcher(machine.get(2));
        if (!buttonA.matches() || !buttonB.matches() || !prize.matches()) {
            throw new IllegalArgumentException("Failed to match " + machine);
        }

        long ax, ay, bx, by, px, py;
        ax = parseInt(buttonA.group(1));
        ay = parseInt(buttonA.group(2));
        bx = parseInt(buttonB.group(1));
        by = parseInt(buttonB.group(2));
        px = parseInt(prize.group(1)) + 10000000000000L;
        py = parseInt(prize.group(2)) + 10000000000000L;
        long det = ax * by - bx * ay;
        long a = (by * px - bx * py) / det;
        long b = (ax * py - ay * px) / det;
        return a * ax + b * bx == px && a * ay + b * by == py ? 3 * a + b : 0;
    }

    @Override
    public boolean hasPart2ExpectedResult() {
        return false;
    }
}
