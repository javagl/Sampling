/*
 * www.javagl.de - Sampling
 *
 * Copyright (c) 2013-2018 Marco Hutter - http://www.javagl.de
 */
package de.javagl.sampling.list;

import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import de.javagl.sampling.list.RandomUtils;

/**
 * Tests for the {@link RandomUtils} class
 */
@SuppressWarnings("javadoc")
public class TestRandomUtils
{
    @Test
    public void testRandomSampling()
    {
        testRandomSampling(10, 0, 100);
        testRandomSampling(50, 50, 150);
        testRandomSampling(100, 100, 200);
    }
    
    /**
     * Call {@link RandomUtils#randomSample(int, int, int, Random)} with 
     * the given parameters and different random number generators for
     * several times, and make sure that all values appear approximately
     * equally often
     * 
     * @param size The sample size
     * @param min The minimum value, inclusive
     * @param max The maximum value, exclusive
     */
    private static void testRandomSampling(int size, int min, int max)
    {
        int runs = 10000;
        Map<Integer, Integer> counts = new LinkedHashMap<Integer, Integer>();
        for (int i = min; i < max; i++)
        {
            counts.put(i, 0);
        }
        for (int i = 0; i < runs; i++)
        {
            IntStream randomSample = IntStream.of(
                RandomUtils.randomSample(size, min, max, new Random(i)));
            randomSample.forEach(value -> 
            {
                counts.computeIfPresent(value, (v, c) -> c + 1);
            });
        }
        assertEqualDistribution(counts.values());
    }
    
    
    /**
     * Assert that the given numbers are "approximately equally distributed", 
     * meaning that they do not deviate for more than a small factor 
     * from their average.
     * 
     * @param numbers The numbers
     */
    private static void assertEqualDistribution(Collection<Integer> numbers)
    {
        IntSummaryStatistics statistics = numbers.stream().collect(
            Collectors.summarizingInt(Number::intValue));
        int min = statistics.getMin();
        int max = statistics.getMax();
        double average = statistics.getAverage();
        double maxRelativeError = 0.2;
        int maxAbsoluteError = (int)(average * maxRelativeError);
        boolean minValid = Math.abs(average - min) < maxAbsoluteError;
        boolean maxValid = Math.abs(max - average) < maxAbsoluteError;
        assertTrue(minValid);
        assertTrue(maxValid);
    }

}
