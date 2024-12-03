
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.Arrays;
import java.util.List;

@AInputData(day = 2, year = 2024)
public class Day2 implements IAdventDay {

    @Override
    public String part1(IInputHelper inputHelper) {
        int sum = 0;

        for (int[] levels : parseReports(inputHelper)) {
            boolean increasing = levels[0] < levels[1];
            boolean safe = true;

            for (int i = 0; i < levels.length - 1; i++) {
                int diff = levels[i + 1] - levels[i];
                if (increasing && diff < 1 || diff > 3) {
                    safe = false;
                    break;
                }
                if (!increasing && (diff > -1 || diff < -3)) {
                    safe = false;
                    break;
                }
            }

            if (safe) {
                sum++;
            }
        }

        return sum + "";
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        int sum = 0;

        for (int[] levels : parseReports(inputHelper)) {
            boolean increasing = levels[0] < levels[1];
            boolean safe = true;

            for (int i = 0; i < levels.length - 1; i++) {
                int diff = levels[i + 1] - levels[i];
                if (increasing && diff < 1 || diff > 3) {
                    safe = false;
                    break;
                }
                if (!increasing && (diff > -1 || diff < -3)) {
                    safe = false;
                    break;
                }
            }

            if (safe) {
                sum++;
            } else if (tryWithDampers(levels)) {
                sum++;
            }
        }

        return sum + "";
    }

    private boolean tryWithDampers(int[] levels) {
        for (int i = 0; i < levels.length; i++) {
            int[] newLevels = new int[levels.length - 1];
            int index = 0;
            for (int j = 0; j < levels.length; j++) {
                if (j != i) {
                    newLevels[index++] = levels[j];
                }
            }

            if (isSafe(newLevels)) {
                return true;
            }
        }

        return false;
    }

    private boolean isSafe(int[] levels){
        boolean increasing = levels[0] < levels[1];
        for (int i = 0; i < levels.length - 1; i++) {
            int diff = levels[i + 1] - levels[i];
            if (increasing && diff < 1 || diff > 3) {
                return false;
            }
            if (!increasing && (diff > -1 || diff < -3)) {
                return false;
            }
        }
        return true;
    }

    private List<int[]> parseReports(IInputHelper inputHelper){
        return inputHelper.getInputAsStream()
            .map(line -> Arrays.stream(line.split(" "))
                    .mapToInt(Integer::parseInt).toArray())
            .toList();
    }

}
