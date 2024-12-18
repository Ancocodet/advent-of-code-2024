
package com.ancozockt.advent.days;

import com.ancozockt.advent.general.Coordinate;
import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.*;

@AInputData(day = 18, year = 2024)
public class Day18 implements IAdventDay {

    private final int width = 70;
    private final int height = 70;

    @Override
    public String part1(IInputHelper inputHelper) {
        List<Coordinate> coordinates = inputHelper.getInputAsStream()
                                                  .map(Coordinate::parse).toList();

        ByteMap map = new ByteMap(width + 1, height + 1);
        coordinates.stream().limit(1024).forEach(map::mark);

        return Integer.toString(requiredSteps(map).size());
    }

    /*
     * Part 2 cannot be tested and it will always return 0,0 for the test input.
     */
    @Override
    public String part2(IInputHelper inputHelper) {
        List<Coordinate> coordinates = inputHelper.getInputAsStream()
                                                  .map(Coordinate::parse).toList();
        int min = 0;
        int max = coordinates.size();
        int cuttingIndex = Integer.MIN_VALUE;
        while(cuttingIndex == Integer.MIN_VALUE) {
            int mid = (min + max)/2;
            if(mid == min) {
                cuttingIndex = max;
            } else {
                ByteMap positions = new ByteMap(height + 1, width + 1);
                coordinates.stream().limit(mid + 1).forEach(positions::mark);
                ByteMap path = requiredSteps(positions);
                if (path.size() == 0) {
                    max = mid;
                } else {
                    min = mid;
                }
            }
        }

        if (cuttingIndex >= coordinates.size()) {
            return "0,0";
        }

        Coordinate cut = coordinates.get(cuttingIndex);
        return cut.x + "," + cut.y;
    }

    private ByteMap requiredSteps(ByteMap positions) {
        Coordinate start = new Coordinate(0, 0);
        Coordinate end = new Coordinate(width, height);
        Queue<Steps> queue = new ArrayDeque<>();
        ByteMap visited = new ByteMap(height+1, width+1);
        visited.mark(start);
        queue.add(new Steps(start, new ByteMap(height+1, width+1)));
        while (!queue.isEmpty()) {
            Steps cur = queue.poll();
            if (cur.position.equals(end)) {
                return cur.history;
            }
            for (var neighbour : cur.position.neighbours()) {
                if (!visited.isMarked(neighbour) && isValid(neighbour) && !positions.isMarked(neighbour)) {
                    visited.mark(neighbour);
                    ByteMap nh = cur.history.copy();
                    nh.mark(neighbour);
                    queue.add(new Steps(neighbour, nh));
                }
            }
        }
        return new ByteMap(height+1, width+1);
    }

    private boolean isValid(Coordinate cord) {
        return 0 <= cord.x && cord.x <= width && 0 <= cord.y && cord.y <= height;
    }

    private record Steps(Coordinate position, ByteMap history) {
    }

    private static class ByteMap {
        BitSet[] map;

        public ByteMap(int width, int height) {
            map = new BitSet[height];
            for (int i = 0; i < height; i++) {
                map[i] = new BitSet(width);
            }
        }

        private ByteMap(BitSet[] map) {
            this.map = map;
        }

        boolean isMarked(Coordinate cord) {
            if (cord.y < 0 || cord.y >= map.length
                || cord.x < 0 || cord.x >= map[0].size()) {
                return false;
            }
            return map[cord.y].get(cord.x);
        }

        void mark(Coordinate cord) {
            map[cord.y].set(cord.x);
        }

        int size() {
            return Arrays.stream(map).mapToInt(BitSet::cardinality).sum();
        }

        ByteMap copy() {
            BitSet[] copy = new BitSet[map.length];
            for(int i = 0; i < map.length; ++i) {
                copy[i] = (BitSet) map[i].clone();
            }
            return new ByteMap(copy);
        }
    }

}
