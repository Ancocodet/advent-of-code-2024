
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.list.UnmodifiableList;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@AInputData(day = 17, year = 2024)
public class Day17 implements IAdventDay {

    private final static Pattern REGISTRY_REGEX = Pattern.compile("Register (\\w): (\\d+)");

    @Override
    public String part1(IInputHelper inputHelper) {
        Computer computer = parseInput(inputHelper);

        computer.run();

        return computer.print();
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        Computer computer = parseInput(inputHelper);
        List<Integer> remaining = new ArrayList<>(computer.program);
        List<Integer> program = new ArrayList<>();

        long A = 0;
        while (!remaining.isEmpty()) {
            --A;
            program.addFirst(remaining.removeLast());
            String pString = program.stream().map(Long::toString).collect(Collectors.joining(","));
            Computer test;
            do {
                ++A;
                test = new Computer(A, computer.registerB, computer.registerC, computer.program);
                test.run(true, program);
            } while(!test.print().equals(pString));
            if (!remaining.isEmpty()) {
                A = A << 3;
            }
        }

        return Long.toString(A);
    }

    private static class Computer {
        private long registerA;
        private long registerB;
        private long registerC;

        private List<Integer> program;
        private int instructionPointer = 0;

        List<Integer> output = new ArrayList<>();

        public Computer(long registerA, long registerB, long registerC, List<Integer> program) {
            this.registerA = registerA;
            this.registerB = registerB;
            this.registerC = registerC;
            this.program = program;
        }

        public void run() {
            run(false, List.of());
        }

        public void run(boolean earlySkip, List<Integer> expected) {
            while (instructionPointer < program.size()) {
                int opcode = program.get(instructionPointer);
                int operand = program.get(instructionPointer + 1);

                boolean skipIncrease = false;
                switch (opcode) {
                    case 0: adv(getOperand(operand)); break;
                    case 1: bxl(operand); break;
                    case 2: bst(getOperand(operand)); break;
                    case 3: if (registerA != 0) {
                            instructionPointer = operand;
                            skipIncrease = true;
                            if (earlySkip && !outPutMatches(expected)) {
                                return;
                            }
                        }
                        break;
                    case 4: bxc();break;
                    case 5: out(getOperand(operand)); break;
                    case 6: bdv(getOperand(operand)); break;
                    case 7: cdv(getOperand(operand)); break;
                }

                if (!skipIncrease) {
                    instructionPointer += 2;
                }
            }
        }

        boolean outPutMatches(List<Integer> expected) {
            if(output.size()>expected.size()) {
                return false;
            }
            for(int i=0; i < output.size(); i++) {
                if(!output.get(i).equals(expected.get(i))) {
                    return false;
                }
            }
            return true;
        }

        private long getOperand(int operator) {
            return switch (operator) {
                case 0, 1, 2, 3 -> operator;
                case 4 -> registerA;
                case 5 -> registerB;
                case 6 -> registerC;
                default -> throw new IllegalArgumentException("Invalid operand: " + operator);
            };
        }

        private void adv(long operand) {
            registerA = (long) Math.floor(registerA / Math.pow(2, operand));
        }

        private void bdv(long operand) {
            registerB = (long) Math.floor(registerA / Math.pow(2, operand));
        }

        private void cdv(long operand) {
            registerC = (long) Math.floor(registerA / Math.pow(2, operand));
        }

        private void bxl(long operand) {
            registerB = registerB ^ operand;
        }

        private void bxc() {
            registerB = registerB ^ registerC;
        }

        private void jnz(int operator) {
            if(registerA != 0) {
                instructionPointer = operator;
            } else {
                instructionPointer += 2;
            }
        }

        private void bst(long operand) {
            registerB = operand % 8;
        }

        private void out(long operand) {
            int result = (int) (operand % 8);
            output.add(result);
        }

        public String print() {
            return String.join(",", output.stream().map(i -> Integer.toString(i)).toList());
        }
    }

    private Computer parseInput(IInputHelper inputHelper) {
        List<Integer> program = new ArrayList<>();

        int registerA = 0;
        int registerB = 0;
        int registerC = 0;

        String[] lines = inputHelper.getInputAsStream().toArray(String[]::new);
        for (String line : lines) {
            if (line.startsWith("Register")) {
                Matcher matcher = REGISTRY_REGEX.matcher(line);
                if (matcher.find()) {
                    String register = matcher.group(1);
                    int value = Integer.parseInt(matcher.group(2));

                    switch (register) {
                        case "A":
                            registerA = value;
                            break;
                        case "B":
                            registerB = value;
                            break;
                        case "C":
                            registerC = value;
                            break;
                    }
                }
            } else if (line.startsWith("Program: ")) {
                Arrays.stream(line.replace("Program: ", "")
                                  .split(",")) // Split by comma
                      .mapToInt(Integer::parseInt) // Parse to int
                      .forEach(program::add);
                program = UnmodifiableList.unmodifiableList(program);
            }
        }

        return new Computer(registerA, registerB, registerC, program);
    }

}
