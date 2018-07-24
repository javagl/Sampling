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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Methods to create streams of lists that are sliding window views on 
 * other lists.
 */
public class ListSlidingWindows
{
    
    /**
     * Create a stream that contains lists that are a sliding window over
     * the given input list. The lists will be returned in form of 
     * unmodifiable lists.<br>
     * <br>
     * The lists of the resulting stream will cover the <i>closed</i> range
     * of the given input list. This means that the first result starts
     * at the beginning of the list, and the last result at most includes the
     * last element of the list.<br>
     * <br>
     * This implies that all returned lists will have exactly 
     * <code>windowSize</code> elements. If the window size is larger
     * than the input list, then the stream will be empty.<br>
     * <br>
     * Examples:<br>
     * <br>
     * For a window size of 5 and a step size of 2, the method will behave as
     * follows:
     * <pre><code>
     * input   :             [0, 1, 2, 3, 4, 5, 6, 7 ]
     * output  : 
     *                       [0, 1, 2, 3, 4 ]
     *                             [2, 3, 4, 5, 6 ]
     * </code></pre> 
     * <br>
     * <pre><code>
     * input   :             [0, 1, 2, 3, 4, 5, 6, 7, 8 ]
     * output  : 
     *                       [0, 1, 2, 3, 4 ]
     *                             [2, 3, 4, 5, 6 ]
     *                                   [4, 5, 6, 7, 8 ]
     * </code></pre> 
     * <br>
     * <br>
     * If the given list is modified after the stream has been created, 
     * the behavior of the stream is unspecified.<br> 
     * <br>
     * If the given list is modified after the windows have been obtained
     * from the stream, the behavior of the windows is unspecified.<br>
     * <br>
     * 
     * @param <T> The element type
     *  
     * @param input The input list
     * @param windowSize The window size
     * @param stepSize The step size
     * @return The stream
     * @throws IllegalArgumentException If the window size or the step size
     * is not positive
     */
    public static <T> Stream<List<T>> createClosedSlidingWindow(
        List<? extends T> input, int windowSize, int stepSize)
    {
        int start = 0;
        int maxEnd = input.size();
        return createSlidingWindow(
            input, windowSize, start, maxEnd, stepSize);
    }

    /**
     * Create a stream that contains lists that are a sliding window over
     * the given input list. The lists will be returned in form of 
     * unmodifiable lists.<br>
     * <br>
     * The lists of the resulting stream will cover the <i>open</i> range
     * of the given input list. This means that the first result consists
     * of the first element of the list, and the last result contains the
     * last element of the list (possibly as its first and only element).   
     * <br>
     * Examples:<br>
     * <br>
     * For a window size of 5 and a step size of 2, the method will behave as
     * follows:
     * <pre><code>
     * input   :             [0, 1, 2, 3, 4, 5, 6, 7]
     * output  :
     *           [_  _  _  _  0]
     *                 [_  _  0, 1, 2]
     *                       [0, 1, 2, 3, 4]
     *                             [2, 3, 4, 5, 6] 
     *                                   [4, 5, 6, 7  _]
     *                                         [6, 7  _  _  _]
     *                                          
     * </code></pre> 
     * <pre><code>
     * input   :             [0, 1, 2, 3, 4, 5, 6, 7, 9 ]
     * output  :
     *           [_  _  _  _  0]
     *                 [_  _  0, 1, 2]
     *                       [0, 1, 2, 3, 4]
     *                             [2, 3, 4, 5, 6] 
     *                                   [4, 5, 6, 7  8]
     *                                         [6, 7, 8  _  _]
     *                                               [8  _  _  _  _]
     * </code></pre> 
     * <br>
     * <br>
     * If the given list is modified after the stream has been created, 
     * the behavior of the stream is unspecified.<br> 
     * <br>
     * If the given list is modified after the windows have been obtained
     * from the stream, the behavior of the windows is unspecified.<br>
     * <br>
     * 
     * @param <T> The element type
     *  
     * @param input The input list
     * @param windowSize The window size
     * @param stepSize The step size
     * @return The stream
     * @throws IllegalArgumentException If the window size or the step size
     * is not positive
     */
    static <T> Stream<List<T>> createOpenSlidingWindow(
        List<? extends T> input, int windowSize, int stepSize)
    {
        int start = -windowSize + 1;
        int maxEnd = input.size() + windowSize;
        return createSlidingWindow(
            input, windowSize, start, maxEnd, stepSize);
    }
    
    
    /**
     * Create a stream that contains lists that are a sliding window over
     * the given input list. The lists will be returned in form of 
     * unmodifiable lists.<br>
     * <br>
     * The sliding windows will cover the given index range. These indices 
     * will be clamped to <code>[0, input.size())</code>.<br>
     * <br>
     * The first element of the first window list will be the element that is 
     * located at the <code>start</code> index of the given list. The last
     * window will contain no element that is located at an index of the input
     * list that is equal to or greater than the <code>maxEnd</code> index.<br>
     * <br>
     * Note that this method does very few checks on the given parameters.
     * It is possible to call it with parameters that will not make sense
     * in a real application, for example, using a window size or a step size
     * that is larger than the list, or starting the computations at a
     * <code>start</code> index that is smaller than <code>-windowSize</code>.
     * (The latter will simply cause the first lists to be empty.) 
     * <br> 
     * If the given list is modified after the stream has been created, 
     * the behavior of the stream is unspecified.<br> 
     * <br>
     * If the given list is modified after the windows have been obtained
     * from the stream, the behavior of the windows is unspecified.<br>
     * <br>
     * 
     * @param <T> The element type
     *  
     * @param input The input list
     * @param windowSize The window size
     * @param start The start index, inclusive
     * @param maxEnd The maximum end index, exclusive
     * @param stepSize The step size
     * @return The stream
     * @throws IllegalArgumentException If the window size or the step size
     * is not positive
     */
    public static <T> Stream<List<T>> createSlidingWindow(
        List<? extends T> input, int windowSize, 
        int start, int maxEnd, int stepSize)
    {
        Iterator<List<T>> iterator = createSlidingWindowIterator(
            input, windowSize, start, maxEnd, stepSize);
        Spliterator<List<T>> spliterator = 
            Spliterators.spliteratorUnknownSize(iterator, 0);
        Stream<List<T>> stream = 
            StreamSupport.stream(spliterator, false);
        return stream;
    }
    
    
    /**
     * Creates the iterator for the sliding window streams
     * 
     * @param <T> The element type
     * 
     * @param input The input list
     * @param windowSize The window size
     * @param start The start index, inclusive
     * @param maxEnd The maximum end index, exclusive
     * @param stepSize The step size
     * @return The iterator
     * @throws IllegalArgumentException If the window size or the step size
     * is not positive
     */
    private static <T> Iterator<List<T>> createSlidingWindowIterator(
        List<? extends T> input, int windowSize, 
        int start, int maxEnd, int stepSize)
    {
        if (windowSize <= 0)
        {
            throw new IllegalArgumentException(
                "The window size must be positive, but is " + windowSize);
        }
        if (stepSize <= 0)
        {
            throw new IllegalArgumentException(
                "The step size must be positive, but is " + stepSize);
        }
        return new Iterator<List<T>>()
        {
            /**
             * The current index of the start of the list that will be returned
             */
            private int currentIndex = start;

            @Override
            public boolean hasNext()
            {
                return currentIndex < input.size() && 
                    currentIndex + windowSize <= maxEnd;
            }

            @Override
            public List<T> next()
            {
                if (!hasNext())
                {
                    throw new NoSuchElementException("No more elements");
                }
                int fromIndex = Math.max(currentIndex, 0);
                int to = currentIndex + windowSize;
                int toIndex = Math.min(Math.max(to, 0), input.size());
                List<T> result = Collections.unmodifiableList(
                    input.subList(fromIndex, toIndex));
                currentIndex += stepSize;
                return result;
            }
        };
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private ListSlidingWindows()
    {
        // Private constructor to prevent instantiation
    }
}
