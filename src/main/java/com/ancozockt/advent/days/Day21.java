
package com.ancozockt.advent.days;

import com.ancozockt.advent.general.Coordinate;
import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@AInputData(day = 21, year = 2024, skip = true)
public class Day21 implements IAdventDay {

    private final Grid keypad = new Grid(new char[][]{
        {'7', '8', '9'},
        {'4', '5', '6'},
        {'1', '2', '3'},
        {'#', '0', 'A'}
    });

    private final Grid directionPad = new Grid(new char[][] {
        {'#', '^', 'A'},
        {'<', 'v', '>'},
    });

    private final Map<Coordinate, Character> changeToDirection = Map.of(
        Coordinate.up(), '^',
        Coordinate.down(), 'v',
        Coordinate.left(), '<',
        Coordinate.right(), '>'
    );

    private final HashMap<String, Integer> cache = new HashMap<>();

    @Override
    public String part1(IInputHelper inputHelper) {
        List<String> codes = inputHelper.getInputAsStream().toList();

        AtomicLong sum = new AtomicLong();
        codes.forEach(code -> {
            System.out.println("code = " + code);

            long numericValue = Long.parseLong(code.substring(0, code.length() - 1));
            long complexity = cost(code, 2, -1);
            System.out.println("complexity = " + complexity);

            sum.addAndGet(numericValue * complexity);
        });

        return Long.toString(sum.get());
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        List<String> codes = inputHelper.getInputAsStream().toList();

        AtomicLong sum = new AtomicLong();
        codes.forEach(code -> {
            System.out.println("code = " + code);

            long numericValue = Long.parseLong(code.substring(0, code.length() - 1));
            System.out.println("numericValue = " + numericValue);

            long complexity = cost(code, 25, -1);
            System.out.println("complexity = " + complexity);

            sum.addAndGet(numericValue * complexity);
        });

        return Long.toString(sum.get());
    }

    private int cost(String code, int maxRobots, int robot) {
        if (cache.containsKey(code + robot)) {
            return cache.get(code + robot);
        }

        Grid maze = keypad;
        if (robot > 0) {
            maze = directionPad;
        }

        int cost = 0;
        char prev = 'A';
        for (char curr : code.toCharArray()) {
            Coordinate posA = maze.find(prev);
            Coordinate posB = maze.find(curr);

            List<String> paths = allPaths(posA, posB, maze);
            int cheapest = Integer.MAX_VALUE;
            for (String path : paths) {
                if (robot == -1) {
                    int length = cost(path, maxRobots, maxRobots);
                    if (length < cheapest) {
                        cheapest = length;
                    }
                } else if (robot > 1) {
                    int length = cost(path, maxRobots, robot - 1);
                    if (length < cheapest) {
                        cheapest = length;
                    }
                } else {
                    int length = path.length();
                    if (length < cheapest) {
                        cheapest = length;
                    }
                }
            }

            cost += cheapest;
            prev = curr;
        }

        cache.put(code + robot, cost);
        return cost;
    }

    private List<String> allPaths(Coordinate a, Coordinate b, Grid maze) {
        List<List<Coordinate>> result = new ArrayList<>();
        Deque<GridRun> runs = new ArrayDeque<>();
        runs.add(new GridRun(new ArrayList<>(), List.of(a)));

        while (!runs.isEmpty()) {
            GridRun curr = runs.removeFirst();
            Coordinate currHead = curr.seen().getLast();

            if (currHead.equals(b)) {
                result.add(curr.path());
            }

            for (Coordinate dir : changeToDirection.keySet()) {
                Coordinate nextPos = currHead.add(dir);

                if (maze.get(nextPos) == '#') {
                    continue;
                }

                if (curr.seen().contains(nextPos)) {
                    continue;
                }

                runs.add(curr.add(dir, nextPos));
            }
        }

        List<String> resultStrings = new ArrayList<>();
        for (List<Coordinate> path : result) {
            StringBuilder sb = new StringBuilder();
            for (Coordinate c : path) {
                sb.append(changeToDirection.get(c));
            }
            sb.append('A');
            resultStrings.add(sb.toString());
        }

        return resultStrings;
    }

    private record Grid(char[][] grid) {

        private char get(Coordinate c) {
            if (c.getY() < 0 || c.getY() >= grid.length
                || c.getX() < 0 || c.getX() >= grid[0].length) {
                return '#';
            }
            return grid[c.getY()][c.getX()];
        }

        public Coordinate find(char c) {
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[row].length; col++) {
                    if (grid[row][col] == c) {
                        return new Coordinate(col, row);
                    }
                }
            }
            return null;
        }

    }

    private record GridRun(List<Coordinate> path, List<Coordinate> seen) {

        public GridRun add(Coordinate d, Coordinate next) {
            List<Coordinate> path = new ArrayList<>(this.path);
            path.add(new Coordinate(d));

            List<Coordinate> seen = new ArrayList<>(this.seen);
            seen.add(next);

            return new GridRun(path, seen);
        }

    }
}
