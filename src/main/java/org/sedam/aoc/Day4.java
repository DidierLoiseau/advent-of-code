package org.sedam.aoc;

import java.util.List;

public class Day4 extends Day {
    final char[] XMAS = "XMAS".toCharArray();

    final char[] XMAS_BACK = "SAMX".toCharArray();

    @Override
    public String part1(List<String> input) {
        return part1(input, 1, 0)
                + part1(input, 0, 1)
                + part1(input, 1, 1)
                + part1(input, 1, -1)
                + "";
    }

    public int part1(List<String> input, int xInc, int yInc) {
        int lines = input.size();
        int cols = input.getFirst().length();
        int count = 0;
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < cols; j++) {
                if (part1FromPos(input, xInc, yInc, i, j, XMAS)) {
                    count++;
                }
                if (part1FromPos(input, xInc, yInc, i, j, XMAS_BACK)) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean part1FromPos(List<String> input, int xInc, int yInc, int i, int j, char[] search) {
        for (char c : search) {
            if (j < 0 || i < 0 || i >= input.size()) {
                return false;
            }
            String line = input.get(i);
            if (j >= line.length()) {
                return false;
            }
            if (line.charAt(j) != c) {
                return false;
            }
            i += yInc;
            j += xInc;
        }
        return true;
    }

    @Override
    public String part2(List<String> input) {
        int lines = input.size() - 2;
        int cols = input.getFirst().length() - 2;
        int count = 0;
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < cols; j++) {
                if (part2FromPos(input, i, j)) {
                    count++;
                }
            }
        }
        return count + "";
    }

    private boolean part2FromPos(List<String> input, int i, int j) {
        return input.get(i + 1).charAt(j + 1) == 'A'
               && (part2FromPos(input, i, j, 'M', 'S', 'M', 'S')
                   || part2FromPos(input, i, j, 'M', 'M', 'S', 'S')
                   || part2FromPos(input, i, j, 'S', 'M', 'S', 'M')
                   || part2FromPos(input, i, j, 'S', 'S', 'M', 'M'));
    }

    private boolean part2FromPos(List<String> input, int i, int j, char topLeft, char topRight, char bottomLeft, char bottomRight) {
        return input.get(i).charAt(j) == topLeft
                && input.get(i).charAt(j + 2) == topRight
                && input.get(i + 2).charAt(j) == bottomLeft
                && input.get(i + 2).charAt(j + 2) == bottomRight;
    }
}
