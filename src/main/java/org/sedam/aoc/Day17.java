package org.sedam.aoc;

import java.util.List;
import java.util.regex.Pattern;

public class Day17 extends Day {
    @Override
    public String part1(List<String> input) {
        int a = extractRegister(input.get(0));
        int b = extractRegister(input.get(1));
        int c = extractRegister(input.get(2));
        int[] prog =
                Pattern.compile(",").splitAsStream(input.get(4).split(": ")[1]).mapToInt(Integer::parseInt).toArray();
        int pointer = 0;
        StringBuilder result = new StringBuilder();

        while (pointer < prog.length) {
            switch (prog[pointer]) {
                case 0:
                    a >>= getCombo(prog[pointer + 1], a, b, c);
                    break;
                case 1:
                    b ^= prog[pointer + 1];
                    break;
                case 2:
                    b = getCombo(prog[pointer + 1], a, b, c) % 8;
                    break;
                case 3:
                    if (a != 0) {
                        pointer = prog[pointer + 1] - 2;
                    }
                    break;
                case 4:
                    b ^= c;
                    break;
                case 5:
                    if (!result.isEmpty()) {
                        result.append(",");
                    }
                    result.append(getCombo(prog[pointer + 1], a, b, c) % 8);
                    break;
                case 6:
                    b = a >> getCombo(prog[pointer + 1], a, b, c);
                    break;
                case 7:
                    c = a >> getCombo(prog[pointer + 1], a, b, c);
                    break;
            }
            pointer += 2;
        }

        return result.toString();
    }

    private int getCombo(int op, int a, int b, int c) {
        return switch (op) {
            case 4 -> a;
            case 5 -> b;
            case 6 -> c;
            case 7 -> throw new IllegalStateException();
            default -> op;
        };
    }

    private int extractRegister(String s) {
        return Integer.parseInt(s.split(": ")[1]);
    }
}
