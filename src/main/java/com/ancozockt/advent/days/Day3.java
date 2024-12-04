
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AInputData(day = 3, year = 2024)
public class Day3 implements IAdventDay {

    private static final String MUL_REGEX = "mul\\((\\d+),(\\d+)\\)";

    @Override
    public String part1(IInputHelper inputHelper) {
        return sumAll(inputHelper.getInputAsString()) + "";
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        String input = inputHelper.getInputAsString();

        return sumAll(removeDont(input, 0)) + "";
    }

    private String removeDont(String input, int index) {
        int start = input.indexOf("don't()");
        if (start == -1) {
            return input;
        }

        int end = input.indexOf("do()");
        while(end != -1 && end < start) {
            end = input.indexOf("do()", end + 1);
        }

        if (end == -1) {
            return removeDont(input.substring(0, start), index + 1);
        }

        return removeDont(input.substring(0, start) + input.substring(end + 4), index + 1);
    }

    private long sumAll(String input) {
        Pattern pattern = Pattern.compile(MUL_REGEX);

        long sum = 0;
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            int a = Integer.parseInt(matcher.group(1));
            int b = Integer.parseInt(matcher.group(2));

            sum += (long) a * b;
        }

        return sum;
    }
}
