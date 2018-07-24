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

import java.util.Random;

/**
 * Some random utility methods. I mean, not random ones, but ones that are
 * related to random numbers.
 */
class RandomUtils
{
    /**
     * Creates an array with the given size, containing distinct random values 
     * between the given minimum value (inclusive) and maximum value
     * (exclusive).  
     *  
     * @param size The size of the returned array
     * @param min The minimum value (inclusive)
     * @param max The maximum value (exclusive)
     * @param random The random number generator
     * @return The array
     * @throws IllegalArgumentException If the size is negative, or the minimum 
     * is larger than the maximum, or the requested size is larger than the 
     * difference between the maximum and the minimum
     */
    static int[] randomSample(
        int size, int min, int max, Random random)
    {
        if (size < 0)
        {
            throw new IllegalArgumentException("The size may not " 
                + "be negative, but is " + size);
        }
        if (min > max)
        {
            throw new IllegalArgumentException("The minimum is " + min 
                + ", which is larger than the maximum " + max);
        }
        if (size > max - min)
        {
            throw new IllegalArgumentException(
                "Can not create a sample of size " + size
                + " with values between " + min + " and " + max);
                
        }
        return reservoirSampling(size, min, max, random);
    }
    
    /**
     * Performs a reservoir sampling. Creates an array with the given size,
     * containing distinct random values in the specified range.
     * 
     * @param size The size of the sample
     * @param min The minimum value (inclusive)
     * @param max The maximum value (exclusive)
     * @param random The random number generator
     * @return The array
     */
    private static int[] reservoirSampling(
        int size, int min, int max, Random random)
    {
        int reservoir[] = new int[size];
        for (int i = 0; i < size; i++)
        {
            reservoir[i] = i + min;
        }
        for (int i = size; i < max - min; i++)
        {
            int j = random.nextInt(i + 1);
            if (j < size)
            {
                reservoir[j] = i + min;
            }
        }
        return reservoir;
    }    
    
    /**
     * Private constructor to prevent instantiation
     */
    private RandomUtils()
    {
        // Private constructor to prevent instantiation
    }
}
