
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.*;

@AInputData(day = 16, year = 2024)
public class Day16 implements IAdventDay {

    private char[][] grid;
    private int height;
    private int width;

    private Cord start;
    private Cord end;

    private Set<Cord> bestPathPoints = new HashSet<>();
    private Map<Reindeer, Integer> seen = new HashMap<>();

    private int minPrice = Integer.MAX_VALUE;

    @Override
    public String part1(IInputHelper inputHelper) {
        parseInput(inputHelper);
        findPath();
        return Integer.toString(minPrice);
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        parseInput(inputHelper);
        findPath();
        return Integer.toString(bestPathPoints.size());
    }

    private void findPath() {
        Queue<ReindeerState> priorityQueue = new PriorityQueue<>(ReindeerState.PRICE_COMPARATOR);
        Map<Cord, Set<Cord>> bests = new HashMap<>();
        Reindeer startDeer = new Reindeer(start, Cord.EAST);
        seen.put(startDeer, 0);
        priorityQueue.add(new ReindeerState(startDeer, 0, new HashSet<>(List.of(start))));
        boolean weAreOverBestPaths = false;
        while (!priorityQueue.isEmpty() && !weAreOverBestPaths) {
            ReindeerState state = priorityQueue.poll();
            for (ReindeerState next : state.next()) {
                if (isFree(next.reindeer.position) && next.price <= seen.getOrDefault(next.reindeer, Integer.MAX_VALUE)) {
                    if (next.price < seen.getOrDefault(next.reindeer, Integer.MAX_VALUE)) {
                        priorityQueue.add(next);
                        seen.put(next.reindeer, next.price);
                        if (end.equals(next.reindeer.position)) {
                            if (minPrice < next.price) {
                                weAreOverBestPaths = true;
                            }
                            minPrice = Math.min(minPrice, next.price);
                            bestPathPoints.addAll(next.visited);
                        }
                        bests.put(next.reindeer.position, next.visited);
                    } else {
                        bests.get(next.reindeer.position).addAll(next.visited);
                    }
                }
            }
        }
    }

    private boolean isFree(Cord position) {
        return position.x >= 0 && position.x < width && position.y >= 0
               && position.y < height && grid[position.y][position.x] != '#';
    }

    private void parseInput(IInputHelper inputHelper) {
        grid = inputHelper.getInputAsStream()
                          .map(String::toCharArray)
                          .toArray(char[][]::new);

        height = grid.length;
        width = grid[0].length;

        Cord start = null;
        Cord end = null;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[y][x] == 'S') {
                    start = new Cord(x, y);
                }
                if (grid[y][x] == 'E') {
                    end = new Cord(x, y);
                }
            }
        }

        if (start == null || end == null) {
            throw new IllegalStateException("Start or end not found");
        }

        this.start = start;
        this.end = end;

        this.bestPathPoints = new HashSet<>();
        this.seen = new HashMap<>();
    }

    record Cord(int x, int y) {
        static final Cord EAST = new Cord(1, 0);
        Cord add(Cord other) {
            return new Cord(x + other.x, y + other.y);
        }
    }

    record Reindeer(Cord position, Cord orientation) {
        Reindeer turnLeft() {
            return new Reindeer(position, new Cord(-orientation.y, orientation.x));
        }

        Reindeer turnRight() {
            return new Reindeer(position, new Cord(orientation.y, -orientation.x));
        }

        Reindeer moveForward() {
            return new Reindeer(position.add(orientation), orientation);
        }
    }

    record ReindeerState(Reindeer reindeer, int price, Set<Cord> visited) {
        static Comparator<ReindeerState> PRICE_COMPARATOR = Comparator.comparingInt(ReindeerState::price);

        ReindeerState turnLeft() {
            return new ReindeerState(reindeer.turnLeft(), price + 1000, visited);
        }

        ReindeerState turnRight() {
            return new ReindeerState(reindeer.turnRight(), price + 1000, visited);
        }

        ReindeerState moveForward() {
            Reindeer reindeer = this.reindeer().moveForward();
            Set<Cord> visited = new HashSet<>(this.visited());
            visited.add(reindeer.position());
            return new ReindeerState(reindeer, price + 1, visited);
        }

        List<ReindeerState> next() {
            return List.of(turnLeft(), turnRight(), moveForward());
        }
    }

}
