package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@AInputData(day = 1, year = 2024)
public class Day1 implements IAdventDay {

    @Override
    public String part1(IInputHelper inputHelper) {
        AtomicInteger sum = new AtomicInteger();

        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();

        inputHelper.getInputAsStream().forEach(line -> {
            String[] sides = line.split("   ");

            left.add(Integer.parseInt(sides[0]));
            right.add(Integer.parseInt(sides[1]));
        });

        left.sort(Integer::compareTo);
        right.sort(Integer::compareTo);

        for (int i = 0; i < left.size(); i++) {
            sum.addAndGet(Math.abs(left.get(i) - right.get(i)));
        }

        return sum.get() + "";
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        AtomicInteger sum = new AtomicInteger();

        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();

        inputHelper.getInputAsStream().forEach(line -> {
            String[] sides = line.split("   ");

            left.add(Integer.parseInt(sides[0]));
            right.add(Integer.parseInt(sides[1]));
        });

        left.sort(Integer::compareTo);
        right.sort(Integer::compareTo);

        for (Integer value : left) {
            // Cont how often left[i] is in right
            int count = 0;
            for (Integer integer : right) {
                if (value.equals(integer)) {
                    count++;
                }
            }
            sum.addAndGet(value * count);
        }

        return sum.get() + "";
    }

}
