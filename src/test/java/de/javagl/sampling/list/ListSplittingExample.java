/*
 * www.javagl.de - Sampling
 *
 * Copyright (c) 2013-2018 Marco Hutter - http://www.javagl.de
 */
package de.javagl.sampling.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import de.javagl.sampling.list.ListSplitting;

/**
 * An example showing a use case for the {@link ListSplitting} class:
 * The memory-efficient creation of training- and test data for a
 * n-fold cross validation.
 */
public class ListSplittingExample
{
    /**
     * The entry point of this example
     * 
     * @param args Not used
     */
    public static void main(String[] args)
    {
        int n = 10000000;
        int numChunks = 10;
        
        System.out.println("Computing input size");
        long inputBytes = approximateSizeInBytes(() -> createInputData(n));
        
        System.out.println("Creating input data...");
        List<Integer> input = createInputData(n);

        System.out.println("Computing training size");
        long trainingBytes = approximateSizeInBytes(
            () -> ListSplitting.omitChunks(input, numChunks)
                    .collect(Collectors.toList()));

        System.out.println("Computing test size");
        long testBytes = approximateSizeInBytes(
            () -> ListSplitting.extractChunks(input, numChunks)
                    .collect(Collectors.toList()));
        
        System.out.println("input size   : " + format(inputBytes));
        System.out.println("training size: " + format(trainingBytes));
        System.out.println("test size    : " + format(testBytes));
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
    
    /**
     * Returns an APPROXIMATION of the number of bytes that are occupied
     * by the object that is provided by the given supplier. Take this 
     * with a grain of salt, it's just a best-effort approach.
     * (Inspired by javaspecialists.eu, Issue 29)
     * 
     * @param supplier The supplier
     * @return The APPROXIMATE size in bytes
     */
    private static long approximateSizeInBytes(Supplier<?> supplier)
    {
        Object object = supplier.get();
        if (object != null)
        {
            object = null;
        }
        tryToTriggerGC();
        long usedBytesBefore = Runtime.getRuntime().totalMemory()
            - Runtime.getRuntime().freeMemory();
        object = supplier.get();
        tryToTriggerGC();
        long usedBytesAfter = Runtime.getRuntime().totalMemory()
            - Runtime.getRuntime().freeMemory();
        return usedBytesAfter - usedBytesBefore;
    }
    
    /**
     * Try hard to trigger a garbage collection. Take this with a grain of
     * salt, it's just a best-effort approach... 
     */
    private static void tryToTriggerGC()
    {
        for (int i = 0; i < 10; i++)
        {
            System.gc();
            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    
    /**
     * Returns a formatted string showing the given number of bytes 
     * 
     * @param bytes The bytes
     * @return The string
     */
    private static String format(long bytes)
    {
        double mib = (double)bytes / (1024 * 1024);
        return String.format(Locale.ENGLISH, "%10d bytes (%8.2f MiB)", 
            bytes, mib);
    }
}
