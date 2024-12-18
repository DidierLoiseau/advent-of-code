package org.sedam.aoc;

import java.util.List;
import java.util.regex.Pattern;

public class Day17 extends Day {
    @Override
    public String part1(List<String> input) {
        long a = extractRegister(input.get(0));
        long b = extractRegister(input.get(1));
        long c = extractRegister(input.get(2));
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

    private long getCombo(int op, long a, long b, long c) {
        return switch (op) {
            case 4 -> a;
            case 5 -> b;
            case 6 -> c;
            case 7 -> throw new IllegalStateException();
            default -> op;
        };
    }

    private long extractRegister(String s) {
        return Long.parseLong(s.split(": ")[1]);
    }

    @Override
    public long part2Long(List<String> input) {
        int[] prog =
                Pattern.compile(",").splitAsStream(input.get(4).split(": ")[1]).mapToInt(Integer::parseInt).toArray();
        assert prog[prog.length - 4] == 5; // penultimate instr is out
        assert prog[prog.length - 2] == 3; // ultimate instr is jnz
        assert prog[prog.length - 1] == 0; // jnz 0

        return solveRecursively(prog, 0, prog.length - 1);
    }

    private long solveRecursively(int[] prog, long a, int i) {
        if (i < 0) {
            return a;
        }
        a <<= 3;
        for (int aSuffix = 0; aSuffix < 8; aSuffix++) {
            long aTemp = execute(prog, i, a | aSuffix);
            if (aTemp > 0) {
                return aTemp;
            }
        }
        return -1;
    }

    private long execute(int[] prog, int i, long aOrig) {
        long a = aOrig, b = 0, c = 0;
        for (int ptr = 0; ptr < prog.length; ptr += 2) {
            int arg = prog[ptr + 1];
            switch (prog[ptr]) {
                case 0:
                    assert arg == 3;
                    a >>= arg;
                    break;
                case 1:
                    b ^= arg;
                    break;
                case 2:
                    b = getCombo(arg, a, b, c) % 8;
                    break;
                case 3:
                    throw new IllegalStateException("Can’t jump while solving at " + i
                            + " – ptr: " + ptr + " - a: " + a);
                case 4:
                    b ^= c;
                    break;
                case 5:
                    long v = getCombo(arg, a, b, c);
                    return (v % 8) != prog[i] ? -1 : solveRecursively(prog, aOrig, i - 1);
                case 6:
                    b = a >> getCombo(arg, a, b, c);
                    break;
                case 7:
                    c = a >> getCombo(arg, a, b, c);
                    break;
            }
        }
        throw new IllegalStateException("Reached end of program at iter " + i + " a: " + a);
    }
}
