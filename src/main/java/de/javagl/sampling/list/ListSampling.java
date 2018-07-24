/*
 * www.javagl.de - Sampling
 *
 * Copyright (c) 2013-2018 Marco Hutter - http://www.javagl.de
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.sampling.list;

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Methods to create random samples from lists
 */
public class ListSampling
{
    /**
     * Creates a stream that provides random samples from the given list,
     * each having the given size.
     * 
     * @param <T> The type of the elements in the list
     * 
     * @param list The input list
     * @param sampleSize The sample size
     * @param random The random number generator
     * @return The stream
     * @throws IllegalArgumentException If the sample size is not 
     * positive, or the sample size is larger than the list size
     */
    public static <T> Stream<List<T>> createSamples(
        List<? extends T> list, int sampleSize, Random random)
    {
        validateSampleSize(sampleSize, list.size());
        Stream<List<T>> stream = Stream.generate(() -> 
        {
            List<T> next = ListSampling.createSample(
                list, sampleSize, random);
            return Collections.unmodifiableList(next);
        });
        return stream;
    }
    
    
    /**
     * Creates a random sample from the given list, with the given size.<br>
     * <br>
     * The returned list will contain distinct elements from the given 
     * list, in random order.<br>
     * <br>
     * The returned list will be an unmodifiable <i>view</i> on the given 
     * list. This means that changes in the given list will affect the
     * returned list. If the given list is structurally modified after
     * it was passed to this method, the behavior of the returned list
     * is undefined.
     *  
     * @param <T> The type of the elements in the list
     * 
     * @param list The list
     * @param sampleSize The size of the sample
     * @param random The random number generator
     * @return The random sample
     * @throws IllegalArgumentException If the sample size is negative or
     * larger than the list size
     */
    public static <T> List<T> createSample(
        List<? extends T> list, int sampleSize, Random random)
    {
        validateSampleSize(sampleSize, list.size());
        int indices[] = 
            RandomUtils.randomSample(sampleSize, 0, list.size(), random);
        return createView(list, indices);
    }

    /**
     * Create a list that is an unmodifiable <i>view</i> on the elements 
     * in the given list that have the given indices.<br>
     * <br>
     * Changes in the given list or the given array will be visible in
     * the returned list. Extreme care has to be taken when attempting
     * such a modification.
     * 
     * @param <T> The type of the elements in the list
     * @param input The input list
     * @param indices The indices to select
     * @return The list with the elements from the selected indices
     */
    private static <T> List<T> createView(
        List<? extends T> input, int indices[])
    {
        return new AbstractList<T>()
        {
            @Override
            public T get(int index)
            {
                int inputIndex = indices[index];
                return input.get(inputIndex);
            }

            @Override
            public int size()
            {
                return indices.length;
            }
        };
    }

    /**
     * Make sure that the given sample size is valid for the given list size,
     * and throw an <code>IllegalArgumentException</code> if not.
     * 
     * @param sampleSize The sample size
     * @param listSize The list size
     * @throws IllegalArgumentException If the sample size is not 
     * positive, or the sample size is larger than the list size
     */
    private static void validateSampleSize(int sampleSize, int listSize)
    {
        if (sampleSize <= 0)
        {
            throw new IllegalArgumentException(
                "The sample size must be positive, but is " + sampleSize);
        }
        if (sampleSize > listSize)
        {
            throw new IllegalArgumentException(
                "Can not create a sample of size " + sampleSize
                + " from list with size " + listSize);
        }
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private ListSampling()
    {
        // Private constructor to prevent instantiation
    }
}
