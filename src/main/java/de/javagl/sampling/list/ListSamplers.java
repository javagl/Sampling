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

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Factory- and utility methods for {@link ListSampler} instances.
 */
public class ListSamplers
{
    /**
     * Create a list consisting of the samples that are provided by the
     * given {@link ListSampler} when it is applied to the given
     * input.<br>
     * <br>
     * The resulting list will contain <b>at most</b> the given number of 
     * samples. If the given {@link ListSampler} cannot provide the 
     * requested number, the resulting list may contain fewer elements.
     * 
     * @param <T> The type of the elements
     * 
     * @param listSampler The {@link ListSampler}
     * @param input The input
     * @param numSamples The number of samples
     * @return The list
     */
    public static <T> List<List<T>> createList(
        ListSampler listSampler, List<? extends T> input, int numSamples)
    {
        return listSampler.<T>createSamples(input)
            .limit(numSamples)
            .collect(Collectors.toList());
    }
    
    /**
     * Create a {@link ListSampler} where each sample consists of the full list 
     *  
     * @return The {@link ListSampler}
     */
    public static ListSampler createFull()
    {
        return new ListSampler()
        {
            @Override
            public <T> Stream<List<T>> createSamples(List<? extends T> input)
            {
                return Stream.generate(() -> 
                    Collections.unmodifiableList(input));
            }
        };
    }
    
    /**
     * Create a {@link ListSampler} where each sample consists of a random 
     * sample of the input list. The size of each sample will 
     * be <code>ceil(input.size() * relativeSampleSize)</code>.
     * 
     * @param relativeSampleSize The relative sample size
     * @param random The random number generator
     * @return The {@link ListSampler}
     * @throws IllegalArgumentException If the relative sample size is not
     * in (0.0, 1.0]. 
     */
    public static ListSampler createWithRelativeSize(
        double relativeSampleSize, Random random)
    {
        if (relativeSampleSize <= 0.0 || relativeSampleSize > 1.0) 
        {
            throw new IllegalArgumentException(
                "The relative sample size must be in (0.0, 1.0], but is "
                + relativeSampleSize);
        }
        return new ListSampler()
        {
            @Override
            public <T> Stream<List<T>> createSamples(List<? extends T> input)
            {
                if (input.isEmpty()) 
                {
                    return Stream.generate(Collections::emptyList);
                }
                int sampleSize = 
                    (int)Math.ceil(input.size() * relativeSampleSize);
                return ListSampling.createSamples(
                    input, sampleSize, random);
            }
        };
    }

    /**
     * Create a {@link ListSampler} where each sample consists of a random 
     * sample of the input list. Each sample will have <b>at most</b>
     * the given absolute size. If the resulting sampler is applied to
     * a list that has a size that is smaller than the given sample size,
     * then the full list will be returned.
     * 
     * @param absoluteSampleSize The absolute sample size
     * @param random The random number generator
     * @return The {@link ListSampler}
     * @throws IllegalArgumentException If the sample size is not positive 
     */
    public static ListSampler createWithAbsoluteSize(
        int absoluteSampleSize, Random random)
    {
        if (absoluteSampleSize <= 0)
        {
            throw new IllegalArgumentException(
                "The sample size must be positive, but is " 
                + absoluteSampleSize);
        }
        return new ListSampler()
        {
            @Override
            public <T> Stream<List<T>> createSamples(List<? extends T> input)
            {
                if (input.size() <= absoluteSampleSize) 
                {
                    return Stream.generate(() -> 
                        Collections.unmodifiableList(input));
                }
                return ListSampling.createSamples(
                    input, absoluteSampleSize, random);
            }
        };
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private ListSamplers()
    {
        // Private constructor to prevent instantiation
    }
}

