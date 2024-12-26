package org.sedam.aoc;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class Day24 extends Day {
    public static final String[] XS = IntStream.range(0, 45).mapToObj("x%02d"::formatted).toArray(String[]::new);

    public static final String[] YS = IntStream.range(0, 45).mapToObj("y%02d"::formatted).toArray(String[]::new);

    public static final String[] ZS = IntStream.range(0, 46).mapToObj("z%02d"::formatted).toArray(String[]::new);

    interface Gate {
        boolean eval(Map<String, Gate> gates);
    }

    @Override
    public long part1Long(List<String> input) {
        Map<String, Gate> gates = input.stream()
                .filter(s -> !s.isEmpty())
                .map(s -> {
                    if (s.contains(":")) {
                        var split = s.split(": ");
                        boolean value = split[1].equals("1");
                        return Map.entry(split[0], (Gate) _ -> value);
                    } else {
                        var split = s.split(" ");
                        String in1 = split[0], in2 = split[2];
                        Gate g = switch (split[1]) {
                            case "OR" -> gts -> gts.get(in1).eval(gts) || gts.get(in2).eval(gts);
                            case "AND" -> gts -> gts.get(in1).eval(gts) && gts.get(in2).eval(gts);
                            case "XOR" -> gts -> gts.get(in1).eval(gts) ^ gts.get(in2).eval(gts);
                            default -> throw new IllegalArgumentException(split[1]);
                        };
                        return Map.entry(split[4], g);
                    }
                })
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        long res = 0;
        for (int i = 0; i < 64; i++) {
            long pos = 1L << i;
            String key = i < 10 ? "z0" + i : "z" + i;
            var gate = gates.get(key);
            if (gate == null) {
                break;
            }
            if (gate.eval(gates)) {
                res |= pos;
            }
        }

        return res;
    }

    @Override
    public String part2(List<String> input) {
        Map<String, Gate> gates = input.stream()
                .filter(s -> !s.isEmpty())
                .map(s -> {
                    if (s.contains(":")) {
                        var split = s.split(": ");
                        return Map.entry(split[0], (Gate) _ -> false);
                    } else {
                        var split = s.split(" ");
                        String in1 = split[0], in2 = split[2];
                        Gate g = switch (split[1]) {
                            case "OR" -> gts -> gts.get(in1).eval(gts) || gts.get(in2).eval(gts);
                            case "AND" -> gts -> gts.get(in1).eval(gts) && gts.get(in2).eval(gts);
                            case "XOR" -> gts -> gts.get(in1).eval(gts) ^ gts.get(in2).eval(gts);
                            default -> throw new IllegalArgumentException(split[1]);
                        };
                        return Map.entry(split[4], g);
                    }
                })
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        List<String> vars =
                gates.keySet().stream()
                        .filter(s -> !s.startsWith("x") && !s.startsWith("y"))
                        .toList();
        var swaps = fix(vars, gates, new HashSet<>(), 0);
        return swaps.stream().sorted().collect(joining(","));
    }

    private Set<String> fix(List<String> vars, Map<String, Gate> gates, Set<String> swaps, int i) {
        if (i == 45) {
            return swaps;
        }

        if (simpleCheck(gates, i)) {
            return fix(vars, gates, swaps, i + 1);
        }

        if (swaps.size() >= 8) {
            // can’t swap more!
            return Set.of();
        }

        System.out.println("Trying to fix z" + i);

        // try swapping zi first
        String zi = "z" + i;
        return Stream.concat(Stream.of(zi), vars.stream().filter(v -> !v.equals(zi)))
                .filter(v -> !swaps.contains(v))
                .<Set<String>>mapMulti((v1, c) ->
                        vars.stream()
                                .filter(v2 -> !v2.equals(zi) && (v1.equals(zi) || v2.compareTo(v1) > 0)
                                        && !swaps.contains(v2))
                                .forEach(v2 -> {
                                    Gate origV1 = gates.get(v1);
                                    Gate origV2 = gates.get(v2);
                                    gates.put(v1, origV2);
                                    gates.put(v2, origV1);
                                    try {
                                        if (prefixCheck(gates, i < 45 ? i < 44 ? i + 2 : i + 1 : i)) {
                                            System.out.printf("Swapping %s and %s seems to work%n", v1, v2);
                                            swaps.add(v1);
                                            swaps.add(v2);
                                            var res = fix(vars, gates, swaps, i + 1);
                                            if (!res.isEmpty()) {
                                                c.accept(res);
                                            }
                                        }
                                    } catch (StackOverflowError e) {
                                        // oupsie, we created a loop! (dirty, but hey! It works!)
                                    }
                                    gates.put(v1, origV1);
                                    gates.put(v2, origV2);
                                }))
                .findFirst()
                .orElse(Set.of());
    }

    private boolean simpleCheck(Map<String, Gate> gates, int i) {
        String x = XS[i], y = YS[i];
        try {
            // check x_i = 1 + y_i = 0 => z_i = 1
            gates.put(x, _ -> true);
            for (int j = 0; j <= 45; j++) {
                boolean eval = gates.get(ZS[j]).eval(gates);
                if (eval != (i == j)) {
                    return false;
                }
            }
        } finally {
            gates.put(x, _ -> false);
        }

        // check x_i = 0 + y_i = 1 => z_i = 1
        try {
            gates.put(y, _ -> true);
            for (int j = 0; j <= 45; j++) {
                boolean eval = gates.get(ZS[j]).eval(gates);
                if (eval != (i == j)) {
                    return false;
                }
            }
        } finally {
            gates.put(y, _ -> false);
        }

        if (i > 0) {
            // check x_(i-1) = 1 + y_(i-1) = 1 => z_i = 1
            String xm1 = XS[i - 1], ym1 = YS[i - 1];
            try {
                gates.put(xm1, _ -> true);
                gates.put(ym1, _ -> true);
                for (int j = 0; j <= 45; j++) {
                    boolean eval = gates.get(ZS[j]).eval(gates);
                    if (eval != (i == j)) {
                        return false;
                    }
                }
            } finally {
                gates.put(xm1, _ -> false);
                gates.put(ym1, _ -> false);
            }

            if (i > 1) {
                String xm2 = XS[i - 2], ym2 = YS[i - 2];
                try {
                    gates.put(xm2, _ -> true);
                    gates.put(ym2, _ -> true);

                    // check x_(i-2) = 1 + y_(i-2) = 1 + x_(i-1) = 1 => z_i = 1
                    try {
                        gates.put(xm1, _ -> true);
                        for (int j = 0; j <= 45; j++) {
                            boolean eval = gates.get(ZS[j]).eval(gates);
                            if (eval != (i == j)) {
                                return false;
                            }
                        }
                    } finally {
                        gates.put(xm1, _ -> false);
                    }

                    // check x_(i-2) = 1 + y_(i-2) = 1 + y_(i-1) = 1 => z_i = 1
                    try {
                        gates.put(ym1, _ -> true);
                        for (int j = 2; j <= 45; j++) {
                            boolean eval = gates.get(ZS[j]).eval(gates);
                            if (eval != (i == j)) {
                                return false;
                            }
                        }
                    } finally {
                        gates.put(ym1, _ -> false);
                    }
                } finally {
                    gates.put(xm2, _ -> false);
                    gates.put(ym2, _ -> false);
                }
            }
        }
        return true;
    }

    private boolean prefixCheck(Map<String, Gate> gates, int upTo) {
        for (int i = 0; i <= upTo; i++) {
            if (!simpleCheck(gates, i)) {
                return false;
            }
        }
        return true;
    }

    private static String manualApproach(Map<String, Gate> gates) {
        var toSwap = Map.of(
                "z15", "qnw",
                "z20", "cqr"
        );
        toSwap.forEach((a, b) -> {
            var tmp = gates.get(a);
            gates.put(a, gates.get(b));
            gates.put(b, tmp);
        });
        Set<String> swaps =
                toSwap.entrySet().stream().flatMap(e -> Stream.of(e.getKey(), e.getValue())).collect(toSet());
        for (int i = 0; i < 45; i++) {
            String x = XS[i], y = YS[i];
            gates.put(x, _ -> true);
            for (int j = 0; j < 45; j++) {
                boolean eval = gates.get(ZS[j]).eval(gates);
                if (eval != (i == j)) {
                    System.out.printf("Wrong output for x%02d=1 + y%02d=0 with z%02d=%s%n", i, i, j, eval);
                }
            }
            gates.put(y, _ -> true);
            for (int j = 1; j <= 45; j++) {
                boolean eval = gates.get(ZS[j]).eval(gates);
                if (eval != (i + 1 == j)) {
                    System.out.printf("Wrong output for x%02d=1 + y%02d=1 with z%02d=%s%n", i, i, j, eval);
                }
            }
            if (i < 44) {
                String x2 = XS[i + 1], y2 = YS[i + 1];
                gates.put(x2, _ -> true);
                for (int j = 2; j <= 45; j++) {
                    boolean eval = gates.get(ZS[j]).eval(gates);
                    if (eval != (i + 2 == j)) {
                        System.out.printf("Wrong output for x%02d=1 + y%02d=1 + x%02d=1 with z%02d=%s%n", i, i, i + 1,
                                j, eval);
                    }
                }
                gates.put(x2, _ -> false);
                gates.put(y2, _ -> true);
                for (int j = 2; j <= 45; j++) {
                    boolean eval = gates.get(ZS[j]).eval(gates);
                    if (eval != (i + 2 == j)) {
                        System.out.printf("Wrong output for x%02d=1 + y%02d=1 + y%02d=1 with z%02d=%s%n", i, i, i + 1,
                                j, eval);
                    }
                }
                gates.put(y2, _ -> false);
            }
            gates.put(x, _ -> false);
            for (int j = 0; j < 45; j++) {
                boolean eval = gates.get(ZS[j]).eval(gates);
                if (eval != (i == j)) {
                    System.out.printf("Wrong output for x%02d=0 + y%02d=1 with z%02d=%s%n", i, i, j, eval);
                }
            }
            gates.put(y, _ -> false);
        }

        return swaps.stream().sorted().collect(joining(","));
    }

    @Override
    public boolean hasPart2ExpectedResult() {
        return false;
    }
}
