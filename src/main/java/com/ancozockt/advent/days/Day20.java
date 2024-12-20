
package com.ancozockt.advent.days;

import com.ancozockt.advent.general.Coordinate;
import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@AInputData(day = 20, year = 2024)
public class Day20 implements IAdventDay {

    private Set<Coordinate> directions = Set.of(Coordinate.right(), Coordinate.left(), Coordinate.up(), Coordinate.down());

    private static final char WALL = '#';
    private static final char START = 'S';
    private static final char END = 'E';

    private char[][] map;

    private int width;
    private int height;
    private int originalCost;

    private Map<Coordinate, Integer> costsFromStart = new HashMap<>();
    private Map<Coordinate, Integer> costsFromEnd = new HashMap<>();

    @Override
    public String part1(IInputHelper inputHelper) {
        parseInput(inputHelper);

        int sum = costsWithCheats(findCheats(2));
        return Integer.toString(sum);
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        parseInput(inputHelper);

        int sum = costsWithCheats(findCheats(20));
        return Integer.toString(sum);
    }

    private Map<Coordinate, Integer> distance(Coordinate start) {
        Map<Coordinate, Integer> distances = new HashMap<>();
        distances.put(start, 0);

        Queue<Coordinate> queue = new ArrayDeque<>(List.of(start));
        Set<Coordinate> visited = new HashSet<>(List.of(start));
        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();
            for (Coordinate neighbor : current.neighbours()) {
                if (isValid(neighbor) && visited.add(neighbor)) {
                    distances.compute(neighbor, (c, i) -> distances.get(current) + 1);
                    queue.add(neighbor);
                }
            }

        }
        return distances;
    }

    private int costsWithCheats(Set<Cheat> cheats) {
        int sum = 0;
        for (Cheat cheat : cheats) {
            int cost = costsFromStart.get(cheat.start()) + cheat.length() + costsFromEnd.get(cheat.end());
            if (cost <= originalCost - 100) {
                sum++;
            }
        }
        return sum;
    }

    private Set<Cheat> findCheats(int maxDistance) {
        Set<Cheat> cheats = new HashSet<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Coordinate start = new Coordinate(x, y);
                if (isValid(start)) {
                    for (int yE = y - maxDistance; yE <= y + maxDistance; yE++) {
                        for (int xE = x - maxDistance; xE <= x + maxDistance; xE++) {
                            Coordinate end = new Coordinate(xE, yE);
                            if (isValid(end) && start.distance(end) <= maxDistance) {
                                cheats.add(new Cheat(start, end));
                            }
                        }
                    }
                }
            }
        }
        return cheats;
    }

    private boolean isValid(Coordinate coordinate) {
        return coordinate.x >= 0 && coordinate.x < width
               && coordinate.y >= 0 && coordinate.y < height
               && map[coordinate.y][coordinate.x] != WALL;
    }

    private void parseInput(IInputHelper inputHelper) {
        map = inputHelper.getInputAsStream()
                         .map(String::toCharArray)
                         .toArray(char[][]::new);

        height = map.length;
        width = map[0].length;

        Coordinate start = null;
        Coordinate end = null;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char c = map[y][x];
                if (c == START) {
                    start = new Coordinate(x, y);
                } else if (c == END) {
                    end = new Coordinate(x, y);
                }
            }
        }

        if (start == null || end == null) {
            throw new IllegalStateException("Start or end not found");
        }

        costsFromStart = distance(start);
        costsFromEnd = distance(end);
        originalCost = costsFromStart.get(end);
    }

    private record Cheat(Coordinate start, Coordinate end) {
        int length() {
            return start.distance(end);
        }
    }

}
