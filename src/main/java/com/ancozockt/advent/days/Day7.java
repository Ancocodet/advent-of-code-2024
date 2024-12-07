
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@AInputData(day = 7, year = 2024)
public class Day7 implements IAdventDay {

    @Override
    public String part1(IInputHelper inputHelper) {
        List<TestCalculation> testCalculations = parseInput(inputHelper);

        AtomicLong sum = new AtomicLong();
        testCalculations.forEach(testCalculation -> {
            long result = testCalculation.result;
            long[] values = testCalculation.values;

            if (canCalculateResult(values, result, 0, 0)) {
                sum.addAndGet(result);
            }
        });

        return sum.get() + "";
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        List<TestCalculation> testCalculations = parseInput(inputHelper);

        AtomicLong sum = new AtomicLong();
        testCalculations.forEach(testCalculation -> {
            long result = testCalculation.result;
            long[] values = testCalculation.values;

            if (canCalculateResultWithConcatenation(values, result, 0, 0)) {
                sum.addAndGet(result);
            }
        });

        return sum.get() + "";
    }

    private boolean canCalculateResult(long[] values, long result, long current, int index) {
        if (index == values.length) {
            return current == result;
        }

        return canCalculateResult(values, result, current + values[index], index + 1) ||
                canCalculateResult(values, result, current * values[index], index + 1);
    }

    private boolean canCalculateResultWithConcatenation(long[] values, long result, long current, int index) {
        if (index == values.length) {
            return current == result;
        }

        long concatenatedValue = Long.parseLong(current + "" + values[index]);
        return canCalculateResultWithConcatenation(values, result, current + values[index], index + 1) ||
                canCalculateResultWithConcatenation(values, result, current * values[index], index + 1) ||
                canCalculateResultWithConcatenation(values, result, concatenatedValue, index + 1);
    }

    private List<TestCalculation> parseInput(IInputHelper inputHelper) {
        return inputHelper.getInputAsStream().map(line -> {
            String[] parts = line.split(": ");

            long result = Long.parseLong(parts[0]);
            long[] values = Arrays.stream(parts[1].split(" "))
                    .mapToLong(Long::parseLong).toArray();

            return new TestCalculation(result, values);
        }).toList();
    }

    record TestCalculation(long result, long[] values) { }


}
