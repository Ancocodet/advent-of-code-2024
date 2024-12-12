
package com.ancozockt.advent.days;

import com.ancozockt.advent.general.Coordinate;
import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.*;
import java.util.function.UnaryOperator;

@AInputData(day = 12, year = 2024)
public class Day12 implements IAdventDay {

    @Override
    public String part1(IInputHelper inputHelper) {
        Set<Set<Coordinate>> groups = parseInput(inputHelper);

        long sum = groups.stream().mapToLong(plot -> {
            long area = plot.size();
            long perimeter = perimeterOf(plot);
            return area * perimeter;
        }).sum();
        return Long.toString(sum);
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        Set<Set<Coordinate>> groups = parseInput(inputHelper);

        long sum = groups.stream().mapToLong(plot->{
            long area = plot.size();
            long sides = sidesOf(plot);
            return area * sides;
        }).sum();

        return Long.toString(sum);
    }

    private Set<Set<Coordinate>> parseInput(IInputHelper inputHelper) {
        String[] input = inputHelper.getInputAsStream()
                                    .toArray(String[]::new);
        char[][] map = new char[input.length][input[0].length()];
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[y].length(); x++) {
                map[y][x] = input[y].charAt(x);
            }
        }

        int width = map[0].length;
        int height = map.length;

        Set<Set<Coordinate>> groups = new HashSet<>();
        Set<Coordinate> processed = new HashSet<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Coordinate current = new Coordinate(x, y);
                if (!processed.contains(current)) {
                    Set<Coordinate> group = findGroup(current, map);
                    processed.addAll(group);
                    groups.add(group);
                }
            }
        }

        return groups;
    }

    private Set<Coordinate> findGroup(Coordinate start, char[][] map) {
        char type = getType(start, map);

        Set<Coordinate> group = new HashSet<>();
        Set<Coordinate> processed = new HashSet<>();

        Queue<Coordinate> queue = new ArrayDeque<>();
        group.add(start);
        processed.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();
            current.neighbours().stream()
                   .filter(processed::add)
                   .filter(neighbor -> isValid(neighbor, map))
                   .filter(neighbor -> getType(neighbor, map) == type)
                   .forEach(neighbor -> {
                       group.add(neighbor);
                       queue.add(neighbor);
                   });
        }
        return group;
    }

    private long perimeterOf(Set<Coordinate> plot) {
        long count = 0;
        for (Coordinate current : plot) {
            for (Coordinate neighbour : current.neighbours()) {
                if (!plot.contains(neighbour)) {
                    ++count;
                }
            }
        }
        return count;
    }

    private long sidesOf(Set<Coordinate> group) {
        Set<Set<Border>> edges = new HashSet<>();
        for (Coordinate spot : group) {
            edges.addAll(edgesOf(spot, group, Coordinate.horizontal, Coordinate.vertical, edges));
            edges.addAll(edgesOf(spot, group, Coordinate.vertical, Coordinate.horizontal, edges));
        }
        return edges.size();
    }

    private Set<Set<Border>> edgesOf(Coordinate spot, Set<Coordinate> group, List<UnaryOperator<Coordinate>> walks, List<UnaryOperator<Coordinate>> checks, Set<Set<Border>> foundEdges) {
        Set<Set<Border>> edges = new HashSet<>();
        for (UnaryOperator<Coordinate> innerCheck : checks) {
            UnaryOperator<Coordinate> outerCheck = innerCheck == checks.getFirst() ? checks.getLast() : checks.getFirst();
            Coordinate current = outerCheck.apply(spot);
            Set<Border> edge = new HashSet<>();
            Border start = new Border(spot, current);
            if (!group.contains(current) && !alreadyProcessed(start, foundEdges)) {
                edge.add(start);
                for (UnaryOperator<Coordinate> walker : walks) {
                    Coordinate walk = walker.apply(current);
                    while (group.contains(innerCheck.apply(walk)) && !group.contains(walk)) {
                        edge.add(new Border(innerCheck.apply(walk), walk));
                        walk = walker.apply(walk);
                    }
                }
                edges.add(edge);
            }
        }
        return edges;
    }

    private boolean alreadyProcessed(Border start, Set<Set<Border>> foundEdges) {
        return foundEdges.stream().anyMatch(edge -> edge.contains(start));
    }

    private char getType(Coordinate cord, char[][] map) {
        return map[cord.y][cord.x];
    }

    private boolean isValid(Coordinate cord, char[][] map) {
        int width = map[0].length;
        int height = map.length;

        return cord.x >= 0 && cord.x < width
               && cord.y >= 0 && cord.y < height;
    }

    private record Border(Coordinate innerSide, Coordinate outerSide) {

    }
}
