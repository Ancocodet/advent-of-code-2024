
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@AInputData(day = 24, year = 2024)
public class Day22 implements IAdventDay {

    private final long PRUNE_MODULO = 16_777_216L;
    private final long PRUNE_BIT_MASK = PRUNE_MODULO - 1L;

    private final int MULTIPLIER_ONE_SHIFT = 6;
    private final int MULTIPLIER_TWO_SHIFT = 11;
    private final int DIVIDER_SHIFT = 5;

    private static final int RADIX = 19;
    private static final int RADIX3 = RADIX * RADIX * RADIX;

    private final HashMap<Long, Long> cache = new HashMap<>();

    @Override
    public String part1(IInputHelper inputHelper) {
        long[] secrets = inputHelper.getInputAsStream()
                .mapToLong(Long::parseLong).toArray();

        long sum = 0;
        for (long secret : secrets) {
            sum += calculateSecret(secret, 2_000);
        }

        return Long.toString(sum);
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        Map<Long, Long> summingCache = new HashMap<>();
        inputHelper.getInputAsStream().mapToLong(Long::parseLong)
                .forEach(secret -> {
                    long[] prices = Stream.iterate(secret, this::calculateNextSecret)
                            .mapToLong(Long::longValue).map(l -> l % 10L)
                            .limit(2001).toArray();
                    Set<Long> seen = new HashSet<>();
                    long runningDiff = 0L;
                    for(int i=1; i<prices.length; i++) {
                        runningDiff = (runningDiff % RADIX3) * RADIX + (prices[i] - prices[i-1] + 9);
                        if(i>=4 && seen.add(runningDiff)) {
                            summingCache.merge(runningDiff, prices[i], Long::sum);
                        }
                    }
                });

        long max = summingCache.values().stream().mapToLong(Long::longValue).max().orElseThrow();
        return Long.toString(max);
    }

    private long calculateSecret(long secret, int steps) {
        long nextSecret = secret;
        for (int i = 0; i < steps; i++) {
            nextSecret = calculateNextSecret(nextSecret);
        }
        return nextSecret;
    }

    private long calculateNextSecret(long secret) {
        if (cache.containsKey(secret)) {
            return cache.get(secret);
        }

        long nextSecret = secret;

        nextSecret = ((nextSecret << MULTIPLIER_ONE_SHIFT) ^ nextSecret) & PRUNE_BIT_MASK;

        nextSecret = ((nextSecret >> DIVIDER_SHIFT) ^ nextSecret) & PRUNE_BIT_MASK;

        nextSecret = ((nextSecret << MULTIPLIER_TWO_SHIFT) ^ nextSecret) & PRUNE_BIT_MASK;

        cache.put(secret, nextSecret);
        return nextSecret;
    }

}
