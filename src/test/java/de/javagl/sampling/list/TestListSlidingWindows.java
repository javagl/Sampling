/*
 * www.javagl.de - Sampling
 *
 * Copyright (c) 2013-2018 Marco Hutter - http://www.javagl.de
 */
package de.javagl.sampling.list;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.javagl.sampling.list.ListSlidingWindows;

/**
 * Tests for the {@link ListSlidingWindows} class
 */
@SuppressWarnings("javadoc")
public class TestListSlidingWindows
{
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testInvalidWindowSizeError()
    {
        List<Integer> input = TestUtils.createList(10);
        int windowSize = 0;
        int start = -10;
        int maxEnd = 20;
        int stepSize = 1;
        exception.expect(IllegalArgumentException.class);        
        ListSlidingWindows.createSlidingWindow(
            input, windowSize, start, maxEnd, stepSize);
    }

    @Test
    public void testInvalidStepSizeError()
    {
        List<Integer> input = TestUtils.createList(10);
        int windowSize = 2;
        int start = -10;
        int maxEnd = 20;
        int stepSize = 0;
        exception.expect(IllegalArgumentException.class);        
        ListSlidingWindows.createSlidingWindow(
            input, windowSize, start, maxEnd, stepSize);
    }
    
    @Test
    public void testBasic()
    {
        List<Integer> input = TestUtils.createList(5);
        int windowSize = 3;
        int start = 0;
        int maxEnd = 5;
        int stepSize = 1;

        Stream<List<Integer>> stream = ListSlidingWindows.createSlidingWindow(
            input, windowSize, start, maxEnd, stepSize);
        List<List<Integer>> actual = stream.collect(Collectors.toList());
        
        //TestUtils.printAligned(input, actual);
        
        List<List<Integer>> expected = Arrays.asList(
            Arrays.asList(0, 1, 2),
            Arrays.asList(1, 2, 3),
            Arrays.asList(2, 3, 4)
        );
        assertEquals(expected, actual);
    }
    
    @Test
    public void testTooLargeWindow()
    {
        List<Integer> input = TestUtils.createList(5);
        int windowSize = 20;
        int start = 0;
        int maxEnd = 5;
        int stepSize = 1;

        Stream<List<Integer>> stream = ListSlidingWindows.createSlidingWindow(
            input, windowSize, start, maxEnd, stepSize);
        List<List<Integer>> actual = stream.collect(Collectors.toList());
        
        //TestUtils.printAligned(input, actual);
        
        List<List<Integer>> expected = Arrays.asList();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testStepSize()
    {
        List<Integer> input = TestUtils.createList(5);
        int windowSize = 3;
        int start = 0;
        int maxEnd = 5;
        int stepSize = 2;

        Stream<List<Integer>> stream = ListSlidingWindows.createSlidingWindow(
            input, windowSize, start, maxEnd, stepSize);
        List<List<Integer>> actual = stream.collect(Collectors.toList());
        
        //TestUtils.printAligned(input, actual);
        
        List<List<Integer>> expected = Arrays.asList(
            Arrays.asList(0, 1, 2),
            Arrays.asList(2, 3, 4)
        );
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLargeStepSize()
    {
        List<Integer> input = TestUtils.createList(10);
        int windowSize = 2;
        int start = 0;
        int maxEnd = 100;
        int stepSize = 5;

        Stream<List<Integer>> stream = ListSlidingWindows.createSlidingWindow(
            input, windowSize, start, maxEnd, stepSize);
        List<List<Integer>> actual = stream.collect(Collectors.toList());
        
        //TestUtils.printAligned(input, actual);
        
        List<List<Integer>> expected = Arrays.asList(
            Arrays.asList(0, 1),
            Arrays.asList(5, 6)
        );
        assertEquals(expected, actual);
    }
    
    @Test
    public void testVeryLargeStepSize()
    {
        List<Integer> input = TestUtils.createList(5);
        int windowSize = 3;
        int start = 0;
        int maxEnd = 100;
        int stepSize = 10;

        Stream<List<Integer>> stream = ListSlidingWindows.createSlidingWindow(
            input, windowSize, start, maxEnd, stepSize);
        List<List<Integer>> actual = stream.collect(Collectors.toList());
        
        //TestUtils.printAligned(input, actual);
        
        List<List<Integer>> expected = Arrays.asList(
            Arrays.asList(0, 1, 2)
        );
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNegativeStart()
    {
        List<Integer> input = TestUtils.createList(5);
        int windowSize = 3;
        int start = -3;
        int maxEnd = 100;
        int stepSize = 2;

        Stream<List<Integer>> stream = ListSlidingWindows.createSlidingWindow(
            input, windowSize, start, maxEnd, stepSize);
        List<List<Integer>> actual = stream.collect(Collectors.toList());
        
        //TestUtils.printAligned(input, actual);
        
        List<List<Integer>> expected = Arrays.asList(
            Arrays.asList(),
            Arrays.asList(0, 1),
            Arrays.asList(1, 2, 3),
            Arrays.asList(3, 4)
        );
        assertEquals(expected, actual);
    }
    
    @Test
    public void testTooLargeStart()
    {
        List<Integer> input = TestUtils.createList(5);
        int windowSize = 3;
        int start = 10;
        int maxEnd = 100;
        int stepSize = 2;

        Stream<List<Integer>> stream = ListSlidingWindows.createSlidingWindow(
            input, windowSize, start, maxEnd, stepSize);
        List<List<Integer>> actual = stream.collect(Collectors.toList());
        
        //TestUtils.printAligned(input, actual);
        
        List<List<Integer>> expected = Arrays.asList();
        assertEquals(expected, actual);
    }
    

}
