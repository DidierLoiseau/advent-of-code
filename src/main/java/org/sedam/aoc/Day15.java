package org.sedam.aoc;

import java.util.List;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;

import static org.sedam.aoc.Day15.ObjectType.BOX;
import static org.sedam.aoc.Day15.ObjectType.BOXRIGHT;
import static org.sedam.aoc.Day15.ObjectType.EMPTY;

public class Day15 extends Day {
    @RequiredArgsConstructor
    enum ObjectType {
        WALL('#'), BOX('O'), EMPTY('.'), BOT('@'), BOXRIGHT('R');

        final int symbol;

        static ObjectType from(int c) {
            for (ObjectType t : values()) {
                if (c == t.symbol) {
                    return t;
                }
            }
            throw new IllegalArgumentException("" + c);
        }

        Stream<ObjectType> makeWide() {
            return Stream.of(this, switch (this) {
                case WALL -> WALL;
                case BOX -> BOXRIGHT;
                default -> EMPTY;
            });
        }
    }

    @Override
    public long part1Long(List<String> input) {
        var grid = input.stream()
                .takeWhile(l -> !l.isEmpty())
                .map(s -> s.chars().mapToObj(ObjectType::from).toArray(ObjectType[]::new))
                .toArray(ObjectType[][]::new);
        int botX = input.stream().filter(l -> l.contains("@")).mapToInt(l -> l.indexOf('@')).findFirst().orElseThrow();
        int botY = (int) input.stream().takeWhile(l -> !l.contains("@")).count();
        grid[botY][botX] = EMPTY;

        var instructions = input.stream().dropWhile(s -> !s.isEmpty())
                .filter(s1 -> !s1.isEmpty())
                .flatMap(s -> s.chars().mapToObj(Direction::from))
                .toList();

        for (var d : instructions) {
            int pushToX = botX + d.x, pushToY = botY + d.y;
            while (grid[pushToY][pushToX] == BOX) {
                pushToX += d.x;
                pushToY += d.y;
            }
            if (grid[pushToY][pushToX] == EMPTY) {
                botX += d.x;
                botY += d.y;
                grid[pushToY][pushToX] = grid[botY][botX];
                grid[botY][botX] = EMPTY;
            }
        }

        int sum = 0;
        for (int x = 0; x < grid[0].length; x++) {
            for (int y = 0; y < grid.length; y++) {
                if (grid[y][x] == BOX) {
                    sum += 100 * y + x;
                }
            }
        }

        return sum;
    }

    private void printState(Direction d, ObjectType[][] grid, int botX, int botY) {
        System.out.println("Move " + (char) d.symbol);
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (x == botX && y == botY) {
                    System.out.print('@');
                } else {
                    System.out.print((char) grid[y][x].symbol);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public long part2Long(List<String> input) {
        var grid = input.stream()
                .takeWhile(l -> !l.isEmpty())
                .map(s -> s.chars().mapToObj(ObjectType::from).flatMap(ObjectType::makeWide).toArray(ObjectType[]::new))
                .toArray(ObjectType[][]::new);
        int botX =
                2 * input.stream().filter(l -> l.contains("@")).mapToInt(l -> l.indexOf('@')).findFirst().orElseThrow();
        int botY = (int) input.stream().takeWhile(l -> !l.contains("@")).count();
        grid[botY][botX] = EMPTY;

        var instructions = input.stream().dropWhile(s -> !s.isEmpty())
                .filter(s1 -> !s1.isEmpty())
                .flatMap(s -> s.chars().mapToObj(Direction::from))
                .toList();

        for (var d : instructions) {
            if (canMove(grid, botX, botY, d)) {
                botX += d.x;
                botY += d.y;
                pushBoxes(grid, botX, botY, d);
                grid[botY][botX] = EMPTY;
            }
        }

        int sum = 0;
        for (int x = 0; x < grid[0].length; x++) {
            for (int y = 0; y < grid.length; y++) {
                if (grid[y][x] == BOX) {
                    sum += 100 * y + x;
                }
            }
        }

        return sum;
    }

    private boolean canMove(ObjectType[][] grid, int x, int y, Direction d) {
        if (d.horizontal) {
            int pushToX = x + d.x;
            return switch (grid[y][pushToX]) {
                case EMPTY -> true;
                case WALL -> false;
                default -> canMove(grid, pushToX, y, d);
            };
        }
        int pushToY = y + d.y;
        return switch (grid[pushToY][x]) {
            case EMPTY -> true;
            case BOX -> canMove(grid, x, pushToY, d) && canMove(grid, x + 1, pushToY, d);
            case BOXRIGHT -> canMove(grid, x, pushToY, d) && canMove(grid, x - 1, pushToY, d);
            default -> false;
        };
    }

    private void pushBoxes(ObjectType[][] grid, int x, int y, Direction d) {
        if (grid[y][x] == EMPTY) {
            return;
        }
        if (d.horizontal) {
            int pushToX = x + d.x;
            switch (grid[y][x]) {
                case BOX, BOXRIGHT:
                    pushBoxes(grid, pushToX, y, d);
                    grid[y][pushToX] = grid[y][x];
                    return;
                case WALL:
                    throw new IllegalStateException("pushing into wall!");
            }
        } else {
            int pushToY = y + d.y;
            switch (grid[y][x]) {
                case BOX:
                    pushBoxes(grid, x, pushToY, d);
                    pushBoxes(grid, x + 1, pushToY, d);
                    grid[pushToY][x] = BOX;
                    grid[pushToY][x + 1] = BOXRIGHT;
                    grid[y][x] = EMPTY;
                    grid[y][x + 1] = EMPTY;
                    return;
                case BOXRIGHT:
                    pushBoxes(grid, x - 1, y, d);
                    return;
                case WALL:
                    throw new IllegalStateException("pushing into wall!");
            }
        }
    }
}
