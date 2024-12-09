package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.ArrayDeque;
import java.util.Deque;

@AInputData(day = 9, year = 2024)
public class Day9 implements IAdventDay {

    private static final char EMPTY = '.';
    private char[] diskMap;
    private char[] disk;
    private int lastBlockIndex = 0;
    private Deque<Integer> diskDeque = new ArrayDeque<>();

    @Override
    public String part1(IInputHelper inputHelper) {
        diskMap = inputHelper.getInputAsString().trim().toCharArray();
        disk = new char[diskMap.length * 9];

        int fileId = 0;
        for(int i = 0; i < diskMap.length; i++) {
            int blockSize = Character.getNumericValue(diskMap[i]);
            for (int blockIndex = lastBlockIndex; blockIndex < lastBlockIndex + blockSize; blockIndex++) {
                if (i % 2 == 0) {
                    disk[blockIndex] = Character.forDigit(fileId, 10);
                    diskDeque.add(fileId);
                } else {
                    disk[blockIndex] = EMPTY;
                    diskDeque.add(-1);
                }
            }

            lastBlockIndex += blockSize;
            if (i % 2 == 0) {
                fileId++;
            }
        }

        long result = 0;
        int index = 0;
        while(!diskDeque.isEmpty()) {
            int current = diskDeque.pollFirst();
            if(current != -1) {
                result += index * current;
                index++;
            } else if(diskDeque.peekLast() != null){
                diskDeque.addFirst(diskDeque.pollLast());
            }
        }

        return result + "";
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        return null;
    }
}
