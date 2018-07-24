/*
 * www.javagl.de - Sampling
 *
 * Copyright (c) 2013-2018 Marco Hutter - http://www.javagl.de
 */
package de.javagl.sampling.list;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import de.javagl.sampling.list.ListSlidingWindows;

/**
 * An example showing the {@link ListSlidingWindows} methods
 */
public class ListSlidingWindowsExample
{
    /**
     * The entry point of this sample
     * 
     * @param args Not used
     */
    public static void main(String[] args)
    {
        showClosedSlidingWindow();
        showOpenSlidingWindow();
        showGenericSlidingWindowA();
        showGenericSlidingWindowB();
    }

    /**
     * An example for the {@link ListSlidingWindows#createClosedSlidingWindow}
     * method.
     */
    private static void showClosedSlidingWindow()
    {
        List<Integer> input = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        int windowSize = 3;
        int stepSize = 2;
        
        System.out.println("Closed  Sliding Window");
        System.out.println("    window size: " + windowSize);
        System.out.println("    step size  : " + stepSize);
        
        Stream<List<Integer>> stream = 
            ListSlidingWindows.createClosedSlidingWindow(
                input, windowSize, stepSize);
        
        TestUtils.printAligned(input, stream);
    }
    
    /**
     * An example for the {@link ListSlidingWindows#createOpenSlidingWindow}
     * method.
     */
    private static void showOpenSlidingWindow()
    {
        List<Integer> input = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        int windowSize = 3;
        int stepSize = 2;
        
        System.out.println("Open    Sliding Window");
        System.out.println("    window size: " + windowSize);
        System.out.println("    step size  : " + stepSize);
        
        Stream<List<Integer>> stream = 
            ListSlidingWindows.createOpenSlidingWindow(
                input, windowSize, stepSize);
        
        TestUtils.printAligned(input, stream);
    }
    
    /**
     * An example for the {@link ListSlidingWindows#createSlidingWindow} method.
     */
    private static void showGenericSlidingWindowA()
    {
        List<Integer> input = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        
        int windowSize = 2;
        int stepSize = 3;
        int minStart = 1;
        int maxEnd = 9;
        
        System.out.println("Generic Sliding Window A");
        System.out.println("    window size: " + windowSize);
        System.out.println("    step size  : " + stepSize);
        System.out.println("    min. start : " + minStart);
        System.out.println("    max. end   : " + maxEnd);
        
        Stream<List<Integer>> stream = 
            ListSlidingWindows.createSlidingWindow(
                input, windowSize, minStart, maxEnd, stepSize);
        
        TestUtils.printAligned(input, stream);
    }
    
    /**
     * An example for the {@link ListSlidingWindows#createSlidingWindow} method.
     */
    private static void showGenericSlidingWindowB()
    {
        List<Integer> input = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        
        int windowSize = 4;
        int stepSize = 2;
        int minStart = -1;
        int maxEnd = 14;
        
        System.out.println("Generic Sliding Window B");
        System.out.println("    window size: " + windowSize);
        System.out.println("    step size  : " + stepSize);
        System.out.println("    min. start : " + minStart);
        System.out.println("    max. end   : " + maxEnd);
        
        Stream<List<Integer>> stream = 
            ListSlidingWindows.createSlidingWindow(
                input, windowSize, minStart, maxEnd, stepSize);
        
        TestUtils.printAligned(input, stream);
    }
    

}
