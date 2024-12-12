package com.ancozockt.advent.days;

import com.ancozockt.advent.general.Coordinate;
import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

@AInputData(day = 8, year = 2024)
public class Day8 implements IAdventDay {

    private static final char EMPTY = '.';

    @Override
    public String part1(IInputHelper inputHelper) {
        char[][] input = parseInput(inputHelper);
        Map<Character, List<Coordinate>> antennasPerFrequency = parseAntennas(input);

        Set<Coordinate> antinodes = new HashSet<>();
        for(Map.Entry<Character,List<Coordinate>> frequency : antennasPerFrequency.entrySet()) {

            List<Coordinate> antennas = frequency.getValue();
            for(List<Coordinate> pair : CollectionUtils.permutations(antennas)) {
                antinodes = findAntinodes(antinodes, false, pair.get(0), pair.get(1), input);
                antinodes = findAntinodes(antinodes, false, pair.get(1), pair.get(0), input);
            }

        }

        return Long.toString(antinodes.size());
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        char[][] input = parseInput(inputHelper);
        Map<Character, List<Coordinate>> antennasPerFrequency = parseAntennas(input);

        Set<Coordinate> antinodes = new HashSet<>();
        for(Map.Entry<Character,List<Coordinate>> frequency : antennasPerFrequency.entrySet()) {

            List<Coordinate> antennas = frequency.getValue();
            for(List<Coordinate> pair : CollectionUtils.permutations(antennas)) {
                antinodes = findAntinodes(antinodes, true, pair.get(0), pair.get(1), input);
                antinodes = findAntinodes(antinodes, true, pair.get(1), pair.get(0), input);
            }

        }

        return Long.toString(antinodes.size());
    }

    private Map<Character, List<Coordinate>> parseAntennas(char[][] input) {
        int rows = input.length;
        int cols = input[0].length;

        Map<Character, List<Coordinate>> antennasPerFrequency = new HashMap<>();
        for(int row = 0; row < rows; row++){

            for(int col = 0; col < cols; col++) {
                if(input[row][col] != EMPTY) {
                    char frequency = input[row][col];
                    List<Coordinate> antennaPositions = antennasPerFrequency.getOrDefault(frequency, new ArrayList<>());
                    antennaPositions.add(new Coordinate(col, row));
                    antennasPerFrequency.putIfAbsent(frequency, antennaPositions);
                }

            }
        }

        return antennasPerFrequency;
    }

    private Set<Coordinate> findAntinodes(Set<Coordinate> antinodes, boolean countHarmonics, Coordinate antennaA, Coordinate antennaB, char[][] map) {
        Set<Coordinate> newAntinodes = antinodes;

        Coordinate direction = Coordinate.direction(antennaA, antennaB);
        Coordinate antinode = new Coordinate(antennaA);
        do {
            antinode.transform(direction);

            if (isInbounds(antinode, map)) {
                antinodes.add(new Coordinate(antinode));
            }
        } while(isInbounds(antinode, map) && countHarmonics);

        if(countHarmonics) {
            antinodes.add(antennaA);
        }

        return newAntinodes;
    }

    private boolean isInbounds(Coordinate antinodePos, char[][] map) {
        int rows = map.length;
        int cols = map[0].length;

        return antinodePos.getY() >= 0 && antinodePos.getY() < rows && antinodePos.getX() >= 0 && antinodePos.getX() < cols;
    }

    private char[][] parseInput(IInputHelper inputHelper) {
        return inputHelper.getInputAsStream()
                          .map(String::toCharArray)
                          .toArray(char[][]::new);
    }
}
