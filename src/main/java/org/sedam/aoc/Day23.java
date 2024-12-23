package org.sedam.aoc;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.Map.entry;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;

public class Day23 extends Day {

    @Override
    public long part1Long(List<String> input) {
        Map<String, Set<String>> graph = input.stream()
                .flatMap(s -> {
                    var split = s.split("-");
                    return Stream.of(entry(split[0], split[1]), entry(split[1], split[0]));
                })
                .collect(groupingBy(Map.Entry::getKey, mapping(Map.Entry::getValue, Collectors.toSet())));

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
                .collect(groupingBy(Map.Entry::getKey, mapping(Map.Entry::getValue, Collectors.toSet())));

        Set<String> visited = new HashSet<>();
        return graph.keySet().stream()
                .filter(visited::add)
                .map(node -> connectedGraph(graph, visited, node))
                .max(comparing(Set::size))
                .orElseThrow()
                .stream()
                .sorted()
                .collect(joining(","));
    }

    private Set<String> connectedGraph(Map<String, Set<String>> graph, Set<String> visited,
                                       String node) {
        // this is incorrect, we need to find maximal cliques instead
        Set<String> neighbs = graph.get(node);
        var connect = new HashSet<>(neighbs);
        visited.addAll(neighbs);
        var toVisit = new ArrayDeque<>(neighbs);
        while (!toVisit.isEmpty()) {
            String neighb = toVisit.pop();
            if (visited.add(neighb)) {
                toVisit.addAll(graph.get(neighb));
            }
        }
        return connect;
    }
}
