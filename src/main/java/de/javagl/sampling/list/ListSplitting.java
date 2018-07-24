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

import java.awt.Point;
import java.util.AbstractList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Methods to split lists. The methods provide views on "chunks" of other
 * lists, or views that omit chunks of other lists.<br>
 */
public class ListSplitting
{
    /**
     * Creates a stream that provides the given number of chunks from the 
     * given list, in form of unmodifiable lists. <br>
     * <br>
     * The stream will attempt to divide the given list into equal-sized 
     * chunks. If the size of the list is not divisible by the given  
     * number of chunks, then the last chunks will contain one fewer  
     * element than the first ones. <br>
     * <br>
     * For example, a call with
     * <pre><code>
     * list = [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 ]
     * numChunks = 3
     * </code></pre>
     * will create a stream that contains lists
     * <pre><code>
     * [ 0, 1, 2, 3 ]
     * [ 4, 5, 6 ]
     * [ 7, 8, 9 ]
     * </code></pre>
     * (This means that if the number of chunks is greater than the
     * size of the list, then the last chunks will be <i>empty</i> lists).<br>
     * <br>
     * If the given list is modified after this stream has been created, 
     * the behavior of the stream is unspecified.<br> 
     * <br>
     * If the given list is modified after the chunks have been obtained
     * from the stream, the behavior of the chunks is unspecified.<br> 
     * <br>
     * 
     * @param <T> The element type
     * 
     * @param list The input list
     * @param numChunks The number of chunks
     * @return The resulting lists
     * @throws IllegalArgumentException If the number of chunks is smaller
     * than 1 
     */
    public static <T> Stream<List<T>> extractChunks(List<T> list, int numChunks)
    {
        if (numChunks < 1)
        {
            throw new IllegalArgumentException(
                "The number of chunks must be at least 1, but is " + numChunks);
        }
        Iterator<List<T>> iterator = new Iterator<List<T>>()
        {
            /**
             * The counter for the chunks
             */
            private int chunkCounter = 0;
            
            @Override
            public boolean hasNext()
            {
                return chunkCounter < numChunks;
            }

            @Override
            public List<T> next()
            {
                if (!hasNext())
                {
                    throw new NoSuchElementException("No more elements");
                }
                List<T> result = extractChunk(list, numChunks, chunkCounter);
                chunkCounter++;
                return result;
            }
        };
        Spliterator<List<T>> spliterator = 
            Spliterators.spliteratorUnknownSize(iterator, 0);
        Stream<List<T>> stream = 
            StreamSupport.stream(spliterator, false);
        return stream;
    }

    /**
     * Creates a stream that provides lists where chunks of the given list
     * are omitted, in form of unmodifiable lists. <br>
     * <br>
     * The stream will attempt to divide the given list into equal-sized 
     * chunks. If the size of the list is not divisible by the given  
     * number of chunks, then the last chunks will contain one fewer  
     * element than the first ones. <br>
     * <br>
     * For example, a call with
     * <pre><code>
     * list = [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 ]
     * numChunks = 3
     * </code></pre>
     * will create a stream that returns lists
     * <pre><code>
     * [ _  _  _  _  4, 5, 6, 7, 8, 9 ]
     * [ 0, 1, 2, 3, _  _  _  7, 8, 9 ]
     * [ 0, 1, 2, 3, 4, 5, 6  _  _  _ ]
     * </code></pre>
     * (This means that if the number of chunks is greater than the
     * size of the list, then the last elements will be the whole 
     * input list!).<br>
     * <br>
     * If the given list is modified after this stream has been created, 
     * the behavior of the stream is unspecified.<br> 
     * <br>
     * If the given list is modified after the chunks have been obtained
     * from the stream, the behavior of the chunks is unspecified.<br>
     * <br>
     * 
     * @param <T> The element type
     * 
     * @param list The input list
     * @param numChunks The number of chunks
     * @return The resulting lists
     * @throws IllegalArgumentException If the number of chunks is smaller
     * than 1 
     */
    public static <T> Stream<List<T>> omitChunks(List<T> list, int numChunks)
    {
        if (numChunks < 1)
        {
            throw new IllegalArgumentException(
                "The number of chunks must be at least 1, but is " + numChunks);
        }
        Iterator<List<T>> iterator = new Iterator<List<T>>()
        {
            /**
             * The counter for the chunks
             */
            private int chunkCounter = 0;
            
            @Override
            public boolean hasNext()
            {
                return chunkCounter < numChunks;
            }

            @Override
            public List<T> next()
            {
                if (!hasNext())
                {
                    throw new NoSuchElementException("No more elements");
                }
                List<T> result = omitChunk(list, numChunks, chunkCounter);
                chunkCounter++;
                return result;
            }
        };
        Spliterator<List<T>> spliterator = 
            Spliterators.spliteratorUnknownSize(iterator, 0);
        Stream<List<T>> stream = 
            StreamSupport.stream(spliterator, false);
        return stream;
    }    
    
    /**
     * Virtually splits the given list into the given number of chunks, and
     * returns an unmodifiable view on the chunk with the given index.<br>
     * 
     * @param <T> The type of the elements
     * 
     * @param list the backing list
     * @param numChunks The number of chunks
     * @param chunkIndex The index of the chunk to return
     * @return The view on the chunk
     * @throws IllegalArgumentException If the number of chunks is smaller
     * than 1, or the chunk index is negative or not smaller than the number
     * of chunks 
     */
    public static <T> List<T> extractChunk(
        List<? extends T> list, int numChunks, int chunkIndex)
    {
        validateChunk(numChunks, chunkIndex);
        Point indexRange = computeChunkIndexRange(
            list.size(), numChunks, chunkIndex);
        return Collections.unmodifiableList(
            list.subList(indexRange.x, indexRange.y));
    }
    
    /**
     * Virtually splits the given list into the given number of chunks, and
     * returns an unmodifiable view on the list, <i>omitting</i> the chunk 
     * with the given index.<br>
     * 
     * @param <T> The type of the elements
     * 
     * @param list the backing list
     * @param numChunks The number of chunks
     * @param chunkIndex The index of the chunk to return
     * @return The view on the list, except for the specified chunk
     * @throws IllegalArgumentException If the number of chunks is smaller
     * than 1, or the chunk index is negative or not smaller than the number
     * of chunks 
     */
    public static <T> List<T> omitChunk(
        List<? extends T> list, int numChunks, int chunkIndex)
    {
        validateChunk(numChunks, chunkIndex);
        Point indexRange = computeChunkIndexRange(
            list.size(), numChunks, chunkIndex);
        return inverseSubList(list, indexRange.x, indexRange.y);
    }
    
    /**
     * Compute the index range for the specified chunk of a list with the
     * given size.<br>
     * <br>
     * The method assumes that the parameters are valid according to the
     * {@link #validateChunk(int, int)} method.
     * 
     * @param listSize The list size
     * @param numChunks The number of chunks
     * @param chunkIndex The chunk index
     * @return The point consisting of (fromIndexInclusive, toIndexExclusive)
     */
    private static Point computeChunkIndexRange(
        int listSize, int numChunks, int chunkIndex)
    {
        int stepSize = listSize / numChunks;
        int remainder = listSize % numChunks;
        if (chunkIndex < remainder)
        {
            int fromIndex = chunkIndex * stepSize + chunkIndex;
            int toIndex = fromIndex + stepSize + 1;
            return new Point(fromIndex, toIndex);
        }
        int fromIndex = chunkIndex * stepSize + remainder;
        int toIndex = fromIndex + stepSize;
        return new Point(fromIndex, toIndex);
    }
    
    /**
     * Validates the given parameters for {@link #extractChunk(List, int, int)}
     * or {@link #omitChunk(List, int, int)}.
     * 
     * @param numChunks The number of chunks
     * @param chunkIndex The chunk index
     * @throws IllegalArgumentException If the number of chunks is smaller
     * than 1, or the chunk index is negative or not smaller than the number
     * of chunks 
     */
    private static void validateChunk(
        int numChunks, int chunkIndex)
    {
        if (numChunks < 1)
        {
            throw new IllegalArgumentException(
                "The number of chunks must be at least 1, but is " + numChunks);
        }
        if (chunkIndex < 0)
        {
            throw new IllegalArgumentException(
                "The chunk index may not be negative, but is " + chunkIndex);
        }
        if (chunkIndex >= numChunks)
        {
            throw new IllegalArgumentException(
                "The chunk index is " + chunkIndex
                + ", but must be smaller than the number of chunks, "
                + "which is " + numChunks);
        }
    }
    
    /**
     * Creates an unmodifiable <i>view</i> on the given list that consists 
     * of all elements of the list, <i>except</i> for the specified range.<br>
     * <br>
     * Changes in the given list will be visible in the returned list. The 
     * behavior of the returned list will be undefined when the backing 
     * list is structurally modified.<br>
     * 
     * @param list The list
     * @param fromIndex The start index of the range to omit, inclusive
     * @param toIndex The end index of the range to omit, exclusive
     * @return The resulting list
     */
    private static <T> List<T> inverseSubList(
        List<? extends T> list, int fromIndex, int toIndex)
    {
        int omittedSize = toIndex - fromIndex;
        List<T> result =  new AbstractList<T>()
        {
            @Override
            public T get(int index)
            {
                if (index < fromIndex)
                {
                    return list.get(index);
                }
                return list.get(index + omittedSize);
            }
            
            @Override
            public int size()
            {
                return list.size() - omittedSize;
            }
        };
        return result;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private ListSplitting()
    {
        // Private constructor to prevent instantiation
    }
}

