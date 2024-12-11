
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@AInputData(day = 11, year = 2024)
public class Day11 implements IAdventDay {

    @Override
    public String part1(IInputHelper inputHelper) {
        Deque<Long> rocks = Arrays.stream(inputHelper.getInputAsString().trim().split(" "))
                                  .map(Long::parseLong)
                                  .collect(Collectors.toCollection(ArrayDeque::new));

        for (int i = 0; i < 25; i++) {
            rocks = blink(rocks);
        }

        return rocks.size() + "";
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        List<Long> rocks = Arrays.stream(inputHelper.getInputAsString().trim().split(" "))
                                  .map(Long::parseLong).toList();

        Map<Long, Long> rockMap = rocks.stream()
                                       .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        for (int i = 0; i < 75; i++) {
            rockMap = blinkB(rockMap);
        }

        return countRocksInMap(rockMap) + "";
    }

    private long countRocksInMap(Map<Long, Long> rockMap) {
        long result = 0;
        for(Long number : rockMap.keySet()) {
            result += rockMap.get(number);
        }
        return result;
    }

    private Map<Long, Long> blinkB(Map<Long, Long> rockMap) {

        Map<Long,Long> processedRocks = new HashMap<>();

        for(Long number : rockMap.keySet()) {
            long digits = String.valueOf(number).length();
            long rockCount = rockMap.get(number);
            long newNumber;
            if(number == 0) {
                newNumber = 1L;
                rockCount += processedRocks.getOrDefault(newNumber, 0L);
                processedRocks.put(1L, rockCount);
            } else if(digits % 2 == 0) {
                String strNumber = String.valueOf(number);

                String strNumberLeft = strNumber.substring(0, strNumber.length() / 2);
                newNumber = Long.parseLong(strNumberLeft);
                long rockCountLeft = rockCount + processedRocks.getOrDefault(newNumber, 0L);
                processedRocks.put(newNumber, rockCountLeft);

                String strNumberRight = strNumber.substring(strNumber.length() / 2);
                newNumber = Long.parseLong(strNumberRight);
                long rockCountRight = rockCount + processedRocks.getOrDefault(newNumber, 0L);
                processedRocks.put(newNumber, rockCountRight);

            } else {
                newNumber = number*2024L;
                rockCount += processedRocks.getOrDefault(newNumber, 0L);
                processedRocks.put(newNumber, rockCount);
            }
        }

        return processedRocks;
    }

    private Deque<Long> blink(Deque<Long> rocks) {
        Deque<Long> processedRocks = new ArrayDeque<>();

        while(!rocks.isEmpty()) {
            long number = rocks.pollFirst();
            long digits = String.valueOf(number).length();
            if(number == 0) {
                processedRocks.add(1L);
            } else if(digits % 2 == 0) {
                String strNumber = String.valueOf(number);

                String numberLeft = strNumber.substring(0, strNumber.length() / 2);
                String numberRight = strNumber.substring(strNumber.length() / 2);

                processedRocks.add(Long.valueOf(numberLeft));
                processedRocks.add(Long.valueOf(numberRight));
            } else {
                processedRocks.add(number * 2024);
            }
        }

        return processedRocks;
    }

}
