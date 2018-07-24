/*
 * www.javagl.de - Sampling
 *
 * Copyright (c) 2013-2018 Marco Hutter - http://www.javagl.de
 */

package de.javagl.sampling.list;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.javagl.sampling.list.ListSampler;
import de.javagl.sampling.list.ListSamplers;

/**
 * Tests for the {@link ListSamplers#createFull()} method
 */
@SuppressWarnings("javadoc")
public class TestListSamplersFull
{
    @Test
    public void testBasic()
    {
        List<Integer> input = TestUtils.createList(10);
        
        ListSampler s = ListSamplers.createFull();
        List<List<Integer>> actual = 
            ListSamplers.createList(s, input, 3);
        
        List<List<Integer>> expected = Arrays.asList(
            Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
            Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
            Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        );
        assertEquals(expected, actual);
    }
}
