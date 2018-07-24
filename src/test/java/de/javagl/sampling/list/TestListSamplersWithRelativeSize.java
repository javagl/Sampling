/*
 * www.javagl.de - Sampling
 *
 * Copyright (c) 2013-2018 Marco Hutter - http://www.javagl.de
 */

package de.javagl.sampling.list;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Random;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.javagl.sampling.list.ListSampler;
import de.javagl.sampling.list.ListSamplers;

/**
 * Tests for the {@link ListSamplers#createWithRelativeSize}
 * method
 */
@SuppressWarnings("javadoc")
public class TestListSamplersWithRelativeSize
{
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testRelativeSizeNotPositiveError()
    {
        Random random = new Random(0);

        exception.expect(IllegalArgumentException.class);

        double relativeSampleSize = 0.0;
        ListSamplers.createWithRelativeSize(
            relativeSampleSize, random);
    }

    @Test
    public void testRelativeSizeTooLargeError()
    {
        Random random = new Random(0);

        exception.expect(IllegalArgumentException.class);

        double relativeSampleSize = 1.1;
        ListSamplers.createWithRelativeSize(
            relativeSampleSize, random);
    }

    @Test
    public void testBasicA()
    {
        List<Integer> input = TestUtils.createList(100);
        Random random = new Random(0);

        double relativeSampleSize = 0.05;
        ListSampler s = 
            ListSamplers.createWithRelativeSize(
                relativeSampleSize, random);

        List<List<Integer>> actual =
            ListSamplers.createList(s, input, 10);

        for (List<Integer> list : actual)
        {
            assertEquals(5, list.size());
        }
    }
    
    @Test
    public void testBasicB()
    {
        List<Integer> input = TestUtils.createList(100);
        Random random = new Random(0);

        double relativeSampleSize = 0.25;
        ListSampler s = 
            ListSamplers.createWithRelativeSize(
                relativeSampleSize, random);

        List<List<Integer>> actual =
            ListSamplers.createList(s, input, 5);

        for (List<Integer> list : actual)
        {
            assertEquals(25, list.size());
        }
    }
    
    
    @Test
    public void testBasicRoundedUp()
    {
        List<Integer> input = TestUtils.createList(10);
        Random random = new Random(0);

        double relativeSampleSize = 0.51;
        ListSampler s = 
            ListSamplers.createWithRelativeSize(
                relativeSampleSize, random);
        
        List<List<Integer>> actual =
            ListSamplers.createList(s, input, 5);

        for (List<Integer> list : actual)
        {
            assertEquals(6, list.size());
        }
    }
    

}
