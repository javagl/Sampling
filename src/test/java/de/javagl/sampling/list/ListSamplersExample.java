/*
 * www.javagl.de - Sampling
 *
 * Copyright (c) 2013-2018 Marco Hutter - http://www.javagl.de
 */
package de.javagl.sampling.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import de.javagl.sampling.list.ListSampler;
import de.javagl.sampling.list.ListSamplers;

/**
 * An example showing how to use the {@link ListSamplers}
 */
public class ListSamplersExample
{
    /**
     * The entry point of this sample
     * 
     * @param args Not used
     */
    public static void main(String[] args)
    {
        showFull();
        showRelativeSize();
        showAbsoluteSize();
    }
    
    /**
     * Show an example for the sampling that is performed with the
     * ListSamplers.createFull method
     */
    private static void showFull()
    {
        System.out.println("Using createFull");
        
        List<Integer> inputData = createInputData(10);
        
        // Create a list sampler that creates samples 
        // that are the full list
        ListSampler listSampler = 
            ListSamplers.createFull();

        Stream<List<Integer>> samples = listSampler.createSamples(inputData);
        
        // Print the first few samples
        samples.limit(5).forEach(sample -> 
        {
            System.out.println(
                "Sample " + sample + " (size " + sample.size() + ")");
        });
    }
    
    
    /**
     * Show an example for the sampling that is performed with the
     * ListSamplers.createWithRelativeSize method
     */
    private static void showRelativeSize()
    {
        System.out.println("Using createWithRelativeSize");
        
        List<Integer> inputData = createInputData(100);
        
        // Create a list sampler that creates samples where
        // each sample has a size of 0.08 times the input size:
        double relativeSampleSize = 0.08;
        ListSampler listSampler = 
            ListSamplers.createWithRelativeSize(
                relativeSampleSize, new Random(0));

        Stream<List<Integer>> samples = listSampler.createSamples(inputData);
        
        // Print the first few samples
        samples.limit(5).forEach(sample -> 
        {
            System.out.println(
                "Sample " + sample + " (size " + sample.size() + ")");
        });
    }

    /**
     * Show an example for the sampling that is performed with the
     * ListSamplers.createWithRelativeSize method
     */
    private static void showAbsoluteSize()
    {
        System.out.println("Using createWithAbsoluteSize");
        
        List<Integer> inputData = createInputData(100);
        
        // Create a list sampler that creates samples where
        // each sample has a size of 10
        int absoluteSampleSize = 10;
        ListSampler listSampler = 
            ListSamplers.createWithAbsoluteSize(
                absoluteSampleSize, new Random(0));

        Stream<List<Integer>> samples = listSampler.createSamples(inputData);
        
        // Print the first few samples
        samples.limit(5).forEach(sample -> 
        {
            System.out.println(
                "Sample " + sample + " (size " + sample.size() + ")");
        });
    }
    
    
    /**
     * Create a list containing the given number of consecutive integers,
     * starting at 0
     * 
     * @param n The number of integers
     * @return The list
     */
    private static List<Integer> createInputData(int n)
    {
        ArrayList<Integer> list = new ArrayList<Integer>(n);
        for (int i=0; i<n; i++)
        {
            list.add(i);
        }
        return list;
    }
    
}
