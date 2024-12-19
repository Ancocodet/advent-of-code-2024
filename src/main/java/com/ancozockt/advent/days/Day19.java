
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

@AInputData(day = 19, year = 2024)
public class Day19 implements IAdventDay {

    private HashMap<String, Boolean> cache = new HashMap<>();

    @Override
    public String part1(IInputHelper inputHelper) {
        String[] input = inputHelper.getInputAsString().split("\n\n");

        List<String> towels = Stream.of(input[0].split(", "))
                                    .sorted(new TowelComparator()).toList();
        List<String> designs = List.of(input[1].split("\n"));

        long count = designs.stream().filter(design -> canBuildDesign(design, towels)).count();
        return Long.toString(count);
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        String[] input = inputHelper.getInputAsString().split("\n\n");

        List<String> towels = Stream.of(input[0].split(", "))
                                    .sorted(new TowelComparator()).toList();
        List<String> designs = List.of(input[1].split("\n"));


        long count = designs.stream().mapToLong(design -> countPossibleArrangements(design, towels, new HashMap<>())).sum();
        return Long.toString(count);
    }

    private long countPossibleArrangements(String design, List<String> towels, HashMap<String, Long> cache) {
        if (cache.containsKey(design)) {
            return cache.get(design);
        }

        long count = 0;
        for (String towel : towels) {
            if (design.equals(towel)) {
                count++;
            } else if (design.startsWith(towel)) {
                count += countPossibleArrangements(design.substring(towel.length()), towels, cache);
            }
        }

        cache.put(design, count);
        return count;
    }

    private boolean canBuildDesign(String design, List<String> towels) {
        if (cache.containsKey(design)) {
            return cache.get(design);
        }

        if (design.isEmpty()) {
            return true;
        }

        for (String towel : towels) {
            int index = design.indexOf(towel);
            if (index != -1) {
                String left = design.substring(0, index);
                String right = design.substring(index + towel.length());

                cache.put(left, canBuildDesign(left, towels));
                cache.put(right, canBuildDesign(right, towels));

                if (cache.get(left) && cache.get(right)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static class TowelComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return Integer.compare(o2.length(), o1.length());
        }
    }

}
