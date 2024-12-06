
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.security.Guard;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AInputData(day = 6, year = 2024)
public class Day6 implements IAdventDay {

    @Override
    public String part1(IInputHelper inputHelper) {
        Lab lab = parseInput(inputHelper.getInputAsStream().toArray(String[]::new));

        Guard guard = lab.guard();
        Set<Cord> obstacles = lab.obstacles();

        long width = lab.width;
        long height = lab.height;

        if (guard == null) {
            return null;
        }

        Set<Cord> touched = moveGuard(guard, obstacles, width, height);

        return touched.size() + "";
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        Lab lab = parseInput(inputHelper.getInputAsStream().toArray(String[]::new));

        Guard guard = lab.guard();
        Set<Cord> obstacles = lab.obstacles();

        long width = lab.width;
        long height = lab.height;

        if (guard == null) {
            return null;
        }

        Set<Cord> touched = moveGuard(guard, obstacles, width, height);

        long count = 0;
        for (Cord cord : touched) {
            Set<Cord> newObstacles = new HashSet<>(obstacles);
            newObstacles.add(cord);

            if (isLooping(guard, newObstacles, cord, width, height)) {
                count++;
            }
        }

        return count + "";
    }

    private boolean isLooping(Guard guard, Set<Cord> obstacles, Cord newBlocker, long width, long height) {
        Set<Guard> visited = new HashSet<>();

        Guard currentGuard = guard;
        Cord guardCord = currentGuard.cord();

        long count = 0;
        while (guardCord.x() >= 0 && guardCord.x() < width
            && guardCord.y() >= 0 && guardCord.y() < height) {
            if(!visited.add(currentGuard)) {
                Cord newCord = guardCord.move(guard.direction());
                if (newCord.equals(newBlocker)) {
                    return true;
                }
            }

            Cord newCord = guardCord.move(guard.direction());
            if (obstacles.contains(newCord)) {
                guard = guard.rotate();
                newCord = guardCord.move(guard.direction());
            }

            currentGuard = new Guard(newCord, guard.direction());
            guardCord = currentGuard.cord();

            if(count++ > 10_000) {
                break;
            }
        }

        return false;
    }

    private Set<Cord> moveGuard(Guard guard, Set<Cord> obstacles, long width, long height) {
        Set<Cord> touched = new HashSet<>();
        Cord guardCord = guard.cord();

        while (guardCord.x() >= 0 && guardCord.x() < width
            && guardCord.y() >= 0 && guardCord.y() < height) {
            Cord newCord = guardCord.move(guard.direction());
            if (obstacles.contains(newCord)) {
                guard = guard.rotate();
                newCord = guardCord.move(guard.direction());
            }

            guardCord = newCord;
            touched.add(guardCord);
        }

        return touched;
    }

    private Lab parseInput(String[] input) {
        Guard guard = null;
        Set<Cord> obstacles = new HashSet<>();

        long width = 0;
        long height = 0;

        for (int i = 0; i < input.length; i++) {
            String[] split = input[i].split("");
            for (int j = 0; j < split.length; j++) {
                if (Direction.fromSymbol(split[j]) != null) {
                    Cord cord = new Cord(j, i);
                    guard = new Guard(cord, Direction.fromSymbol(split[j]));
                } else if (split[j].equals("#")) {
                    obstacles.add(new Cord(j, i));
                }
            }

            width = Math.max(width, input[i].length() - 1);
            height = Math.max(height, i);
        }

        return new Lab(guard, obstacles, width, height);
    }

    record Lab(Guard guard,
               Set<Cord> obstacles,
               long width,
               long height) { }

    record Guard(Cord cord, Direction direction) {

        public Guard rotate() {
            return switch (direction) {
                case UP -> new Guard(cord, Direction.RIGHT);
                case DOWN -> new Guard(cord, Direction.LEFT);
                case LEFT -> new Guard(cord, Direction.UP);
                case RIGHT -> new Guard(cord, Direction.DOWN);
            };
        }

    }

    record Cord(int x, int y) {

        public Cord move(Direction direction) {
            return switch (direction) {
                case UP -> new Cord(x, y - 1);
                case DOWN -> new Cord(x, y + 1);
                case LEFT -> new Cord(x - 1, y);
                case RIGHT -> new Cord(x + 1, y);
            };
        }

    }

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;

        public static Direction fromSymbol(String symbol) {
            return switch (symbol) {
                case "^" -> UP;
                case "v" -> DOWN;
                case "<" -> LEFT;
                case ">" -> RIGHT;
                default -> null;
            };
        }
    }
}
