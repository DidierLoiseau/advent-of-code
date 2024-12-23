package org.sedam.aoc;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static java.util.Map.entry;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

public class Day23 extends Day {

    @Override
    public long part1Long(List<String> input) {
        Map<String, Set<String>> graph = input.stream()
                .flatMap(s -> {
                    var split = s.split("-");
                    return Stream.of(entry(split[0], split[1]), entry(split[1], split[0]));
                })
                .collect(groupingBy(Map.Entry::getKey, mapping(Map.Entry::getValue, toSet())));

        return graph.entrySet().stream()
                .mapToLong(e -> {
                    var node = e.getKey();
                    var neighb = e.getValue();
                    return neighb.stream()
                            .mapToLong(n -> {
                                if (node.startsWith("t") || n.startsWith("t")) {
                                    return graph.get(n).stream().filter(neighb::contains).count();
                                } else {
                                    return graph.get(n).stream().filter(
                                            o -> o.startsWith("t") && neighb.contains(o)).count();
                                }
                            })
                            .sum();
                })
                .sum() / 6;
    }

    @Override
    public String part2(List<String> input) {
        Map<String, Set<String>> graph = input.stream()
                .flatMap(s -> {
                    var split = s.split("-");
                    return Stream.of(entry(split[0], split[1]), entry(split[1], split[0]));
                })
                .collect(groupingBy(Map.Entry::getKey, mapping(Map.Entry::getValue, toSet())));
        // they all have the same number of neighbors!
        System.out.println("Size: " + graph.size() +
                ", neighbour count: " + graph.values().stream().map(Set::size).collect(groupingBy(i -> i, counting())));

        Set<String> maxClique = bronKerbosch(graph, new HashSet<>(), graph.keySet(), emptySet(), 0);
        return maxClique.stream().sorted().collect(joining(","));
    }

    private Set<String> bronKerbosch(Map<String, Set<String>> graph, Set<String> r, Set<String> origP,
                                     Set<String> origX, int minSize) {
        if (origP.isEmpty() && origX.isEmpty()) {
            if (r.size() > minSize) {
                System.out.println("New max clique: " + r.stream().sorted().collect(joining(",")));
                return new HashSet<>(r);
            }
            return emptySet();
        }
        Set<String> maxClique = Set.of();
        var x = new HashSet<>(origX);
        var p = new HashSet<>(origP);
        for (String n : origP) {
            r.add(n);
            Set<String> neihbs = graph.get(n);
            var newP = neihbs.stream().filter(p::contains).collect(toSet());
            var newX = x.stream().filter(neihbs::contains).collect(toSet());
            Set<String> tmpClique = bronKerbosch(graph, r, newP, newX, minSize);
            p.remove(n);
            x.add(n);
            if (tmpClique.size() > minSize) {
                maxClique = tmpClique;
                minSize = tmpClique.size();
            }
            r.remove(n);
        }

        return maxClique;
    }
}
