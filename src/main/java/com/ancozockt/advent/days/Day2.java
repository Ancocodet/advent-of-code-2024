
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

            for (int i = 1; i < levels.length - 1; i++) {
                int diff = levels[i + 1] - levels[i];
                if (!increasing) {
                    diff = levels[i] - levels[i + 1];
                }

                if (diff < 1 || diff > 3) {
                    safe = false;
                    break;
                }

                if((increasing && levels[i] > levels[i + 1]) || (!increasing && levels[i] < levels[i + 1])) {
                    safe = false;
                    break;
                }
            }

            if(safe){
                sum++;
            }
        }

        return sum + "";
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        return null;
    }

    private List<int[]> parseReports(IInputHelper inputHelper){
        return inputHelper.getInputAsStream()
            .map(line -> Arrays.stream(line.split(" "))
                    .mapToInt(Integer::parseInt).toArray())
            .toList();
    }

}
