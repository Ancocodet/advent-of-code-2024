
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AInputData(day = 5, year = 2024)
public class Day5 implements IAdventDay {

    private final static Pattern ORDER_REGEX = Pattern.compile("(\\d+)\\|(\\d+)");

    @Override
    public String part1(IInputHelper inputHelper) {
        HashMap<Integer, List<Integer>> pageOrders = new HashMap<>();
        List<Produce> produces = new ArrayList<>();

        parseInput(inputHelper, pageOrders, produces);

        // Check if produces are in correct order
        List<Produce> correctProduces = new ArrayList<>();
        for (Produce produce : produces) {
            boolean correct = true;
            for (int i = 0; i < produce.pages.size() - 1; i++) {
                if (pageOrders.containsKey(produce.pages.get(i))
                    && !pageOrders.get(produce.pages.get(i)).contains(produce.pages.get(i + 1))) {
                    correct = false;
                    break;
                }

                if (pageOrders.containsKey(produce.pages.get(i+1))
                    && pageOrders.get(produce.pages.get(i + 1)).contains(produce.pages.get(i))) {
                    correct = false;
                    break;
                }
            }
            if (correct)
                correctProduces.add(produce);
        }

        Optional<Integer> sum = correctProduces.stream().map(Produce::getCenterPage).reduce(Integer::sum);
        return sum.map(Object::toString).orElse("0");
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        HashMap<Integer, List<Integer>> pageOrders = new HashMap<>();
        List<Produce> produces = new ArrayList<>();

        parseInput(inputHelper, pageOrders, produces);

        List<Produce> incorrectProduces = new ArrayList<>();
        for (Produce produce : produces) {
            boolean correct = true;
            for (int i = 0; i < produce.pages.size() - 1; i++) {
                if (pageOrders.containsKey(produce.pages.get(i))
                    && !pageOrders.get(produce.pages.get(i)).contains(produce.pages.get(i + 1))) {
                    correct = false;
                    break;
                }

                if (pageOrders.containsKey(produce.pages.get(i+1))
                    && pageOrders.get(produce.pages.get(i + 1)).contains(produce.pages.get(i))) {
                    correct = false;
                    break;
                }
            }
            if (!correct)
                incorrectProduces.add(produce);
        }

        // Order incorrect produces correctly
        List<Produce> orderedProduces = new ArrayList<>();
        for (Produce produce : incorrectProduces) {
            orderedProduces.add(new Produce(sortByRules(pageOrders, produce.pages())));
        }

        Optional<Integer> sum = orderedProduces.stream().map(Produce::getCenterPage).reduce(Integer::sum);
        return sum.map(Object::toString).orElse("0");
    }

    record Produce(List<Integer> pages) {

        public Integer getCenterPage() {
            return pages.get(pages.size() / 2);
        }

        @Override
        public String toString() {
            return String.join(",", pages.stream().map(Object::toString).toList());
        }
    }

    private void parseInput(IInputHelper inputHelper, HashMap<Integer, List<Integer>> orders, List<Produce> produces) {
        inputHelper.getInputAsStream().forEach(line -> {
            Matcher matcher = ORDER_REGEX.matcher(line);
            if (matcher.matches()) {
                if (orders.containsKey(Integer.parseInt(matcher.group(1)))) {
                    orders.get(Integer.parseInt(matcher.group(1))).add(Integer.parseInt(matcher.group(2)));
                } else {
                    orders.put(Integer.parseInt(matcher.group(1)), new ArrayList<>(List.of(Integer.parseInt(matcher.group(2)))));
                }
                return;
            }

            if (line.length() > 1 && line.contains(",")) {
                produces.add(new Produce(Arrays.stream(line.split(",")).mapToInt(Integer::parseInt)
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll)));
            }
        });
    }

    private List<Integer> sortByRules(HashMap<Integer, List<Integer>> rules, List<Integer> input) {
        List<Integer> sorted = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Set<Integer> inputSet = new HashSet<>(input);

        for (Integer item : input) {
            if (!visited.contains(item)) {
                visit(item, rules, visited, sorted, inputSet);
            }
        }

        return sorted.reversed();
    }

    private void visit(Integer item, HashMap<Integer, List<Integer>> rules, Set<Integer> visited, List<Integer> sorted, Set<Integer> inputSet) {
        if (!visited.contains(item) && inputSet.contains(item)) {
            visited.add(item);

            if (rules.containsKey(item)) {
                for (Integer dependency : rules.get(item)) {
                    visit(dependency, rules, visited, sorted, inputSet);
                }
            }

            sorted.add(item);
        }
    }
}
