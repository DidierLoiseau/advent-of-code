package org.sedam.aoc;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class Day24 extends Day {
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
}
