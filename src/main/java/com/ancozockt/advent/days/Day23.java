
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@AInputData(day = 23, year = 2024)
public class Day23 implements IAdventDay {

    @Override
    public String part1(IInputHelper inputHelper) {
        Map<String, Set<String>> network = new HashMap<>();

        inputHelper.getInputAsStream().forEach(line -> {
            String[] computers = line.split("-");
            network.computeIfAbsent(computers[0],  i -> new HashSet<>()).add(computers[1]);
            network.computeIfAbsent(computers[1],  i -> new HashSet<>()).add(computers[0]);
        });

        return Long.toString(interconnected(network).size());
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        return null;
    }

    private Set<Set<String>> interconnected(Map<String, Set<String>> network) {
        Set<Set<String>> trios = new HashSet<>();
        network.keySet().stream().filter(s -> s.startsWith("t"))
                .forEach(firstComputer -> network.get(firstComputer).stream()
                        .filter(secondComputer -> !secondComputer.equals(firstComputer))
                        .forEach(secondComputer -> network.get(secondComputer).stream()
                                .filter(thirdComputer -> !thirdComputer.equals(firstComputer) && network.get(thirdComputer).contains(firstComputer))
                                .forEach(thirdComputer -> trios.add(Set.of(firstComputer, secondComputer, thirdComputer)))));
        return trios;
    }

}
