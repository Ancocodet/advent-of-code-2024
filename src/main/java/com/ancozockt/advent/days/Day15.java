
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.*;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;

@AInputData(day = 15, year = 2024)
public class Day15 implements IAdventDay {

    private char[][] grid;
    private int robotX = 0;
    private int robotY = 0;

    @Override
    public String part1(IInputHelper inputHelper) {
        InputData data = parseData(inputHelper, false);

        String moves = data.moves;
        grid = data.data;
        robotX = data.robot[0];
        robotY = data.robot[1];

        for (int i = 0; i < moves.length(); i++) {
            applyMovePart1(moves.charAt(i));
        }

        long sum = getGPS('O', grid);
        return Long.toString(sum);
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        InputData data = parseData(inputHelper, true);

        String moves = data.moves;
        grid = data.data;
        robotX = data.robot[0];
        robotY = data.robot[1];

        for (int i = 0; i < moves.length(); i++) {
            applyMovePart2(moves.charAt(i));
        }
        long sum = getGPS('[',grid);
        return Long.toString(sum);
    }

    private void applyMovePart1(char move) {
        if (move == '^' && canMoveVertical(robotX, robotY, true)) {
            moveVertical(robotX, robotY, true);
            robotY--;
        } else if (move == 'v' && canMoveVertical(robotX, robotY, false)) {
            moveVertical(robotX, robotY, false);
            robotY++;
        } else if (move == '<' && canMoveLeft(robotX, robotY)) {
            moveLeft(robotX, robotY);
            robotX--;
        } else if (move == '>' && canMoveRight(robotX, robotY)) {
            moveRight(robotX, robotY);
            robotX++;
        }
    }

    private void applyMovePart2(char move) {
        if (move == '^' && canMoveDirection(robotX, robotY, y -> y - 1)) {
            moveUpP2(robotX, robotY);
            robotY--;
        } else if (move == 'v' && canMoveDirection(robotX, robotY, y -> y + 1)) {
            moveDownP2(robotX, robotY);
            robotY++;
        } else if (move == '<' && canMoveLeft(robotX, robotY)) {
            moveLeft(robotX, robotY);
            robotX--;
        } else if (move == '>' && canMoveRight(robotX, robotY)) {
            moveRight(robotX, robotY);
            robotX++;
        }
    }

    private boolean canMoveVertical(int x, int y, boolean up) {
        if (up) {
            for (int iY = y - 1; iY > 0; iY--) {
                if (grid[iY][x] == '.') {
                    return true;
                } else if (grid[iY][x] == '#') {
                    return false;
                }
            }
        } else {
            int height = grid.length;
            for (int iY = y + 1; iY < height; ++iY) {
                if (grid[iY][x] == '.') {
                    return true;
                } else if (grid[iY][x] == '#') {
                    return false;
                }
            }
        }
        return false;
    }

    private void moveVertical(int x, int y, boolean up) {
        checkRobotOn(x, y);
        int firstSpace = up ? -1 : grid.length;
        int iY = y;
        while ((up && firstSpace == -1 && iY > 0) || (!up && firstSpace == grid.length && iY < grid.length)) {
            if (grid[iY][x] == '.') {
                firstSpace = iY;
            } else {
                iY += up ? -1 : 1;
            }
        }
        iY = firstSpace;
        while ((up && iY < y) || (!up && iY > y)) {
            char tmp = grid[iY][x];
            grid[iY][x] = grid[iY + (up ? 1 : -1)][x];
            grid[iY + (up ? 1 : -1)][x] = tmp;
            iY += up ? 1 : -1;
        }
    }

    private boolean canMoveLeft(int x, int y) {
        for (int iX = x - 1; iX >= 0; --iX) {
            if (grid[y][iX] == '.') {
                return true;
            } else if (grid[y][iX] == '#') {
                return false;
            }
        }
        return false;
    }

    private void moveLeft(int x, int y) {
        checkRobotOn(x, y);
        int firstSpace = -1;
        int iX = x;
        while (firstSpace == -1) {
            if (grid[y][iX] == '.') {
                firstSpace = iX;
            } else {
                --iX;
            }
        }
        iX = firstSpace;
        for (; iX < x; ++iX) {
            char tmp = grid[y][iX];
            grid[y][iX] = grid[y][iX + 1];
            grid[y][iX + 1] = tmp;
        }
    }

    private boolean canMoveRight(int x, int y) {
        int width = grid[0].length;
        for (int iX = x + 1; iX < width; ++iX) {
            if (grid[y][iX] == '.') {
                return true;
            } else if (grid[y][iX] == '#') {
                return false;
            }
        }
        return false;
    }

    private void moveRight(int x, int y) {
        checkRobotOn(x, y);
        int width = grid[0].length;
        int firstSpace = width;
        int iX = x;
        while (firstSpace == width && iX < width) {
            if (grid[y][iX] == '.') {
                firstSpace = iX;
            } else {
                ++iX;
            }
        }
        iX = firstSpace;
        for (; iX > x; --iX) {
            char tmp = grid[y][iX];
            grid[y][iX] = grid[y][iX - 1];
            grid[y][iX - 1] = tmp;
        }
    }

    private void moveUpP2(int x, int y) {
        int height = grid.length;

        checkRobotOn(x, y);
        boolean canStop = false;
        List<List<Integer>> wantsToMove = new ArrayList<>();
        wantsToMove.add(List.of(x));
        for (int iY = y - 1; iY < height && !canStop; --iY) {
            boolean allSpaces = true;
            List<Integer> prevMoves = wantsToMove.getLast();
            for (int mx : prevMoves) {
                allSpaces &= grid[iY][mx] == '.';
            }
            if (!allSpaces) {
                boolean anyWall = false;
                for (int mx : prevMoves) {
                    anyWall |= grid[iY][mx] == '#';
                }
                if (!anyWall) {
                    SequencedSet<Integer> nextmoves = findNextMoves(prevMoves, iY);
                    wantsToMove.addLast(new ArrayList<>(nextmoves));
                } else {
                    return;
                }
            } else {
                canStop = true;
            }
        }
        copyLines(wantsToMove, y, i -> i - 1);
    }

    private void moveDownP2(int x, int y) {
        int height = grid.length;

        checkRobotOn(x, y);
        boolean canStop = false;
        List<List<Integer>> wantsToMove = new ArrayList<>();
        wantsToMove.add(List.of(x));
        for (int iY = y + 1; iY < height && !canStop; ++iY) {
            boolean allSpaces = true;
            List<Integer> prevMoves = wantsToMove.getLast();
            for (int mx : prevMoves) {
                allSpaces &= grid[iY][mx] == '.';
            }
            if (!allSpaces) {
                boolean anyWall = false;
                for (int mx : prevMoves) {
                    anyWall |= grid[iY][mx] == '#';
                }
                if (!anyWall) {
                    wantsToMove.addLast(new ArrayList<>(findNextMoves(prevMoves, iY)));
                } else {
                    return;
                }
            } else {
                canStop = true;
            }
        }
        copyLines(wantsToMove, y, i -> i + 1);
    }

    private void copyLines(List<List<Integer>> wantsToMove, int y, IntUnaryOperator direction) {
        int iY = y;
        Map<Integer, Character> prevLine = new HashMap<>();
        Map<Integer, Character> currentLine;
        for (var toMove : wantsToMove) {
            currentLine = new HashMap<>();
            for (int mx : toMove) {
                currentLine.put(mx, grid[iY][mx]);
                grid[iY][mx] = prevLine.getOrDefault(mx, '.');
            }
            for (int mx : prevLine.keySet()) {
                grid[iY][mx] = prevLine.get(mx);
            }
            prevLine = currentLine;
            iY = direction.applyAsInt(iY);
        }
        for (int mx : prevLine.keySet()) {
            grid[iY][mx] = prevLine.get(mx);
        }
    }

    private boolean canMoveDirection(int x, int y, IntUnaryOperator direction) {
        List<List<Integer>> wantsToMove = new ArrayList<>();
        int height = grid.length;
        wantsToMove.add(List.of(x));
        int iY = direction.applyAsInt(y);
        while (0 < iY && iY < height) {
            Optional<Boolean> canMove = checkMoves(iY, wantsToMove.getLast(), wantsToMove);
            if (canMove.isPresent()) {
                return canMove.get();
            }
            iY = direction.applyAsInt(iY);
        }
        return false;
    }

    private Optional<Boolean> checkMoves(int y, List<Integer> prevMoves, List<List<Integer>> wantsToMove) {
        boolean allSpaces = prevMoves.stream().allMatch(mx -> grid[y][mx] == '.');
        if (!allSpaces) {
            boolean anyWall = prevMoves.stream().anyMatch(mx -> grid[y][mx] == '#');
            if (!anyWall) {
                wantsToMove.addLast(new ArrayList<>(findNextMoves(prevMoves, y)));
            } else {
                return Optional.of(false);
            }
        } else {
            return Optional.of(true);
        }
        return Optional.empty();
    }

    private SequencedSet<Integer> findNextMoves(List<Integer> prevMoves, int iY) {
        SequencedSet<Integer> nextmoves = new LinkedHashSet<>();
        for (int mx : prevMoves) {
            if (grid[iY][mx] == '[') {
                nextmoves.add(mx);
                nextmoves.add(mx + 1);
            } else if (grid[iY][mx] == ']') {
                nextmoves.add(mx - 1);
                nextmoves.add(mx);
            }
        }
        return nextmoves;
    }

    private void checkRobotOn(int x, int y) {
        if (grid[y][x] != '@') {
            throw new IllegalArgumentException("Illegal move, robot is not on x: " + x + ", y: " + y);
        }
    }

    private long getGPS(char sumVal, char[][] data) {
        int height = data.length;
        int width = data[0].length;

        long sum = 0L;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (data[y][x] == sumVal) {
                    sum += (100L * y + x);
                }
            }
        }
        return sum;
    }

    private InputData parseData(IInputHelper inputHelper, boolean expand) {
        StringBuilder stringBuilder = new StringBuilder();
        String grid = null;

        String[] lines = inputHelper.getInputAsStream().toArray(String[]::new);

        for (String line : lines) {
            if (line.isBlank()) {
                if (grid == null) {
                    grid = stringBuilder.toString();
                    stringBuilder = new StringBuilder();
                }
            } else {
                stringBuilder.append(line);
                if (grid == null) {
                    stringBuilder.append("\n");
                }
            }
        }

        if (grid == null) {
            throw new IllegalStateException("No grid found");
        }

        String moves = stringBuilder.toString();
        if (expand) {
          grid = grid.lines().map(this::expand)
                     .collect(Collectors.joining("\n"));
        }
        char[][] data = grid.lines().map(String::toCharArray)
                            .toArray(char[][]::new);

        int[] robot = {0, 0};
        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[y].length; x++) {
                if (data[y][x] == '@') {
                    robot[0] = x;
                    robot[1] = y;
                    break;
                }
            }
        }

        return new InputData(data, moves, robot);
    }

    private String expand(String line) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '#') {
                stringBuilder.append("##");
            } else if (c == 'O') {
                stringBuilder.append("[]");
            } else if (c == '.') {
                stringBuilder.append("..");
            } else if (c == '@') {
                stringBuilder.append("@.");
            } else {
                throw new IllegalArgumentException("invalid character: " + c);
            }
        }
        return stringBuilder.toString();
    }

    record InputData(char[][] data, String moves, int[] robot) { }

}
