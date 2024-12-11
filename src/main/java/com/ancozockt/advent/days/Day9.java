package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;
import lombok.Getter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@AInputData(day = 9, year = 2024)
public class Day9 implements IAdventDay {

    private static final char EMPTY = '.';

    @Override
    public String part1(IInputHelper inputHelper) {
        char[] diskMap = inputHelper.getInputAsString().trim().toCharArray();
        Deque<Integer> diskDeque = new ArrayDeque<>();

        int fileId = 0;
        int lastBlockIndex = 0;

        for(int i = 0; i < diskMap.length; i++) {
            int blockSize = Character.getNumericValue(diskMap[i]);
            for(int blockIndex = lastBlockIndex; blockIndex < lastBlockIndex + blockSize; blockIndex++) {
                if(i % 2 == 0) {
                    diskDeque.add(fileId);
                } else {
                    diskDeque.add(-1);
                }
            }
            lastBlockIndex += blockSize;
            if(i % 2 == 0) {
                fileId++;
            }
        }

        long result = 0;
        int index = 0;
        while(!diskDeque.isEmpty()) {
            int current = diskDeque.pollFirst();
            if(current != -1) {
                result += (long) index * current;
                index++;
            } else if(diskDeque.peekLast() != null){
                diskDeque.addFirst(diskDeque.pollLast());
            }
        }

        return result + "";
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        char[] diskMap = inputHelper.getInputAsString().trim().toCharArray();

        int fileId = 0;
        int blockIndex = 0;

        List<Unallocated> unallocateds = new ArrayList<>();
        Deque<File> files = new ArrayDeque<>();

        for (int i = 0; i < diskMap.length; i++) {
            int blockSize = Character.getNumericValue(diskMap[i]);

            if (i % 2 == 0) {
                files.add(new File(fileId++, blockIndex, blockSize));
            } else {
                unallocateds.add(new Unallocated(blockIndex, blockSize));
            }

            blockIndex += blockSize;
        }

        long result = 0;

        while (!files.isEmpty()) {
            File file = files.pollLast();
            result += reallocate(file, unallocateds);
        }

        return result + "";
    }

    private long reallocate(File file, List<Unallocated> unallocateds) {

        for(Unallocated unallocated : unallocateds) {
            if(unallocated.canAllocate(file)) {
                unallocated.allocate(file);
                break;
            }
        }

        return file.checksum();
    }

    static class File {

        private final int id;
        @Getter
        private int startBlockIndex;
        private int endBlockIndex;
        @Getter
        private final int size;

        public File(int fileId, int startBlockIndex, int size) {
            this.id = fileId;
            this.startBlockIndex = startBlockIndex;
            this.endBlockIndex = startBlockIndex + size - 1;
            this.size = size;
        }

        public void move(int startBlockIndex) {
            this.startBlockIndex = startBlockIndex;
            this.endBlockIndex = startBlockIndex + size - 1;
        }

        public long checksum() {
            long result = 0;
            for(int i = startBlockIndex; i <= endBlockIndex; i++) {
                result += (long) id * i;
            }
            return result;
        }
    }

    static  class Unallocated {

        private int startBlockIndex;
        private int size;

        public Unallocated(int startBlockIndex, int size) {
            this.startBlockIndex = startBlockIndex;
            this.size = size;
        }

        public boolean canAllocate(File file) {
            return this.size >= file.getSize() && this.startBlockIndex <= file.getStartBlockIndex();
        }

        public void allocate(File file) {

            if(canAllocate(file)) {
                file.move(this.startBlockIndex);
                this.startBlockIndex += file.getSize();
                this.size -= file.getSize();
            }
        }

    }

}
