/*
 * www.javagl.de - Sampling
 *
 * Copyright (c) 2013-2018 Marco Hutter - http://www.javagl.de
 */
package de.javagl.sampling.list;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.javagl.sampling.list.ListSplitting;

/**
 * Tests for the {@link ListSplitting} class
 */
@SuppressWarnings("javadoc")
public class TestListSplitting
{
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testZeroChunksError()
    {
        List<Integer> input = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        exception.expect(IllegalArgumentException.class);        
        ListSplitting.extractChunk(input, 0, 0);
    }

    @Test
    public void testInvalidChunkIndexErrorA()
    {
        List<Integer> input = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        exception.expect(IllegalArgumentException.class);        
        ListSplitting.extractChunk(input, 3, -1);
    }

    @Test
    public void testInvalidChunkIndexErrorB()
    {
        List<Integer> input = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        exception.expect(IllegalArgumentException.class);        
        ListSplitting.extractChunk(input, 3, 3);
    }
    
    @Test
    public void testExtractChunk()
    {
        List<Integer> input = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        List<Integer> chunk0 = ListSplitting.extractChunk(input, 3, 0);
        List<Integer> chunk1 = ListSplitting.extractChunk(input, 3, 1);
        List<Integer> chunk2 = ListSplitting.extractChunk(input, 3, 2);
        
        List<List<Integer>> actual = Arrays.asList(chunk0, chunk1, chunk2);

        List<List<Integer>> expected = Arrays.asList(
            Arrays.asList(0, 1, 2, 3),
            Arrays.asList(4, 5, 6),
            Arrays.asList(7, 8, 9)
        );
        assertEquals(expected, actual);
    }
    
    @Test
    public void testOmitChunk()
    {
        List<Integer> input = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        List<Integer> omit0 = ListSplitting.omitChunk(input, 3, 0);
        List<Integer> omit1 = ListSplitting.omitChunk(input, 3, 1);
        List<Integer> omit2 = ListSplitting.omitChunk(input, 3, 2);
        
        List<List<Integer>> actual = Arrays.asList(omit0, omit1, omit2);

        List<List<Integer>> expected = Arrays.asList(
            Arrays.asList(4, 5, 6, 7, 8, 9),
            Arrays.asList(0, 1, 2, 3, 7, 8, 9),
            Arrays.asList(0, 1, 2, 3, 4, 5, 6)
        );
        assertEquals(expected, actual);
    }
    
    
    @Test
    public void testChunks()
    {
        int n = 100;
        List<Integer> list = new ArrayList<Integer>();
        for (int i=0; i<n; i++)
        {
            list.add(i);
        }
        for (int numChunks = 1; numChunks <= 100; numChunks++)
        {
            for (int i = 0; i < numChunks; i++)
            {
                List<Integer> testData =
                    ListSplitting.extractChunk(list, numChunks, i);
                List<Integer> trainingData =
                    ListSplitting.omitChunk(list, numChunks, i);
                assertTrue(setsAreEqual(list, trainingData, testData));
            }
        }
    }

    /**
     * Returns whether the given original collection is equal to the collection
     * containing all elements from the given parts, disregarding the order.
     * 
     * @param original The original collection
     * @param partA The first part
     * @param partB The second part
     * @return Whether the sets are equal
     */
    private static boolean setsAreEqual(Collection<?> original,
        Collection<?> partA, Collection<?> partB)
    {
        Set<Object> originalSet = new LinkedHashSet<Object>(original);
        Set<Object> partsSet = new LinkedHashSet<Object>();
        partsSet.addAll(partA);
        partsSet.addAll(partB);
        boolean equal = partsSet.equals(originalSet);
        if (!equal)
        {
            System.err.println("Not equal: ");
            System.err.println("  original: " + original);
            System.err.println("  partA   : " + partA);
            System.err.println("  partB   : " + partB);
        }
        return equal;
    }
    
}
