
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AInputData(day = 13, year = 2024)
public class Day13 implements IAdventDay {

    private static final long EXPANSION = 10_000_000_000_000L;

    @Override
    public String part1(IInputHelper inputHelper) {
        List<ClawMachine> clawMachines = parseInput(inputHelper);
        return Long.toString(clawMachines.stream()
                                         .mapToLong(this::prizeOf).sum());
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        List<ClawMachine> clawMachines = expandMachines(parseInput(inputHelper));
        return Long.toString(clawMachines.stream()
                                         .mapToLong(this::prizeOf).sum());
    }

    private long prizeOf(ClawMachine machine) {
        long b = (machine.prize.y * machine.buttonA.x - machine.prize.x * machine.buttonA.y) /
                 (machine.buttonB.y * machine.buttonA.x-machine.buttonB.x * machine.buttonA.y);
        long a = (machine.prize.x - b * machine.buttonB.x) / machine.buttonA.x;
        Cord location = new Cord(a * machine.buttonA.x + b * machine.buttonB.x,
                                 a * machine.buttonA.y + b * machine.buttonB.y);
        if(location.equals(machine.prize)) {
            return a * 3 + b;
        }
        return 0L;
    }

    private List<ClawMachine> expandMachines(List<ClawMachine> clawMachines) {
        return clawMachines.stream().map(ClawMachine::expandPrize).toList();
    }

    private List<ClawMachine> parseInput(IInputHelper inputHelper) {
        Pattern coordPattern = Pattern.compile(".*X.(\\d+), Y.(\\d+)");

        List<List<String>> groups = new ArrayList<>();
        List<String> currentGroup = new ArrayList<>();
        for (String line : inputHelper.getInputAsStream().toList()) {
            if (line.isEmpty()) {
                groups.add(currentGroup);
                currentGroup = new ArrayList<>();
            } else {
                currentGroup.add(line);
            }
        }
        if (!currentGroup.isEmpty()) {
            groups.add(currentGroup);
        }

        return groups.stream().map(group -> {
            List<Cord> coordinates = group.stream().map(line -> {
                Matcher cordMatcher = coordPattern.matcher(line);
                if (!cordMatcher.matches()) {
                    throw new IllegalArgumentException("Invalid input");
                }

                long x = Long.parseLong(cordMatcher.group(1));
                long y = Long.parseLong(cordMatcher.group(2));

                return new Cord(x, y);
            }).toList();

            return new ClawMachine(coordinates.get(0), coordinates.get(1), coordinates.get(2));
        }).toList();
    }

    record Cord(long x, long y) {}

    record ClawMachine(Cord buttonA, Cord buttonB, Cord prize) {

        public ClawMachine expandPrize() {
            return new ClawMachine(new Cord(buttonA.x, buttonA.y),
                                   new Cord(buttonB.x, buttonB.y),
                                   new Cord(EXPANSION + prize.x, EXPANSION + prize.y));
        }

    }
}
