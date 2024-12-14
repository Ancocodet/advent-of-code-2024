
package com.ancozockt.advent.days;

import com.ancozockt.advent.general.Coordinate;
import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AInputData(day = 14, year = 2024)
public class Day14 implements IAdventDay {

    private static final Pattern ROBOT_PATTERN = Pattern.compile("p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)");

    private static final int WIDTH = 101;
    private static final int HEIGHT = 103;

    @Override
    public String part1(IInputHelper inputHelper) {
        List<Robot> robots = parseRobots(inputHelper);
        for (int i = 0; i < 100; i++) {
            robots.forEach(Robot::move);
        }

        int[] quadrantCounts = new int[4];
        for (Robot robot : robots) {
            int quadrant = robot.getQuadrant();
            if (quadrant != -1) {
                quadrantCounts[quadrant]++;
            }
        }

        int safetyScore = quadrantCounts[0] * quadrantCounts[1] * quadrantCounts[2] * quadrantCounts[3];
        return Integer.toString(safetyScore);
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        List<Robot> robots = parseRobots(inputHelper);

        int steps = 0;
        boolean collisionDetected = false;
        do {
            steps++;
            Set<RobotCord> positions = new HashSet<>();
            boolean collided = false;
            for (Robot robot : robots) {
                robot.move();
                if (!collided) {
                    collided = !positions.add(RobotCord.fromRobot(robot));
                }
            }
            collisionDetected = !collided;
        } while (!collisionDetected);

        return Integer.toString(steps);
    }

    private List<Robot> parseRobots(IInputHelper inputHelper) {
        return inputHelper.getInputAsStream()
                          .map(Robot::fromString).toList();
    }

    private static class Robot {

        private int x;
        private int y;
        private final int vx;
        private final int vy;

        public Robot(int x, int y, int vx, int vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }

        public void move() {
            x = (x + vx + WIDTH) % WIDTH;
            y = (y + vy + HEIGHT) % HEIGHT;
        }

        public int getQuadrant() {
            int centerX = WIDTH / 2;
            int centerY = HEIGHT / 2;
            if (x < centerX) {
                if (y < centerY) {
                    return 0;
                } else if (y > centerY) {
                    return 1;
                }
            } else if (x > centerX) {
                if (y > centerY) {
                    return 2;
                } else if (y < centerY) {
                    return 3;
                }
            }
            return -1;
        }

        public static Robot fromString(String input) {
            Matcher matcher = ROBOT_PATTERN.matcher(input);
            if (matcher.find()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                int vx = Integer.parseInt(matcher.group(3));
                int vy = Integer.parseInt(matcher.group(4));
                return new Robot(x, y, vx, vy);
            }
            throw new IllegalArgumentException("Invalid input: " + input);
        }

    }

    private record RobotCord(int x, int y) {

        public static RobotCord fromRobot(Robot robot) {
            return new RobotCord(robot.x, robot.y);
        }

    }

}
