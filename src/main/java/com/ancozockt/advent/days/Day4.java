
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

@AInputData(day = 4, year = 2024)
public class Day4 implements IAdventDay {

    @Override
    public String part1(IInputHelper inputHelper) {
        char[][] grid = toCharArray(inputHelper.getInputAsString());

        String word = "XMAS";
        long count = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                // Horizontal
                count += countWord(grid, x, y, 1, 0, word);
                count += countWord(grid, x, y, -1, 0, word);

                // Vertical
                count += countWord(grid, x, y, 0, 1, word);
                count += countWord(grid, x, y, 0, -1, word);

                // Diagonal Down
                count += countWord(grid, x, y, 1, 1, word);
                count += countWord(grid, x, y, 1, -1, word);

                // Diagonal Up
                count += countWord(grid, x, y, -1, 1, word);
                count += countWord(grid, x, y, -1, -1, word);
            }
        }

        return count + "";
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        char[][] grid = toCharArray(inputHelper.getInputAsString());

        long count = 0;
        for (int y = 1; y < grid.length - 1; y++) {
            for (int x = 1; x < grid[y].length - 1; x++) {
                if (hasCross(grid, x, y)) {
                    count++;
                }
            }
        }

        return count + "";
    }

    private boolean hasCross(char[][] grid, int x, int y) {
        if (grid[x][y] != 'A') {
            return false;
        }

        if ((grid[x -1][y - 1] == 'M' && grid[x + 1][y + 1] == 'S')
            || (grid[x -1][y - 1] == 'S' && grid[x + 1][y + 1] == 'M')) {
            if (grid[x - 1][y + 1] == 'S' && grid[x + 1][y - 1] == 'M') {
                return true;
            }
            return grid[x - 1][y + 1] == 'M' && grid[x + 1][y - 1] == 'S';
        }

        return false;
    }

    private int countWord(char[][] grid, int x, int y, int dx, int dy, String word) {
        for (int i = 0; i < word.length(); i++) {
            int row = y + i * dy;
            int col = x + i * dx;

            if (row < 0 || row >= grid.length || col < 0 || col >= grid[row].length) {
                return 0;
            }

            if (grid[row][col] != word.charAt(i)) {
                return 0;
            }
        }
        return 1;
    }

    private char[][] toCharArray(String input) {
        String[] lines = input.split("\n");
        char[][] chars = new char[lines.length][lines[0].length()];
        for (int i = 0; i < lines.length; i++) {
            chars[i] = lines[i].toCharArray();
        }
        return chars;
    }

}
