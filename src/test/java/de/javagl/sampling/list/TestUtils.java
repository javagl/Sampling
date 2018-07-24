/*
 * www.javagl.de - Sampling
 *
 * Copyright (c) 2013-2018 Marco Hutter - http://www.javagl.de
 */
package de.javagl.sampling.list;

import java.util.AbstractList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility methods for the tests
 */
class TestUtils
{
    /**
     * Create an unmodifiable list with integers in [0,n).
     * 
     * @param n The size of the list
     * @return The list
     */
    static List<Integer> createList(int n)
    {
        return new AbstractList<Integer>()
        {
            @Override
            public Integer get(int index)
            {
                if (index < 0 || index >= size()) 
                {
                    throw new IndexOutOfBoundsException(
                        "Index is " + index + ", size is " + size());
                }
                return index;
            }

            @Override
            public int size()
            {
                return n;
            }
        };
    }
    
    /**
     * Print the given input and results, nicely aligned
     * 
     * @param input The input 
     * @param results The results
     */
    static void printAligned(
        List<Integer> input, Stream<List<Integer>> results)
    {
        printAligned(input, results.collect(Collectors.toList()));
    }
    
    /**
     * Print the given input and results, nicely aligned
     * 
     * @param input The input 
     * @param results The results
     */
    static void printAligned(
        List<Integer> input, List<List<Integer>> results)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("input : ").append(input).append("\n");
        sb.append("output: ").append("\n");
        results.stream().forEach(result -> 
        {
            String indentation = computeIndentation(result);
            sb.append("        ");
            sb.append(indentation);
            sb.append(result);
            sb.append("\n");
        });
        System.out.println(sb.toString());
    }
    
    /**
     * Compute the indentation for the {@link #printAligned(List, Stream)} 
     * call of the given result.
     * 
     * @param result The result
     * @return The indentation string
     */
    private static String computeIndentation(List<Integer> result) 
    {
        if (result.isEmpty())
        {
            return "";
        }
        Integer first = result.get(0);
        if (first.equals(Integer.valueOf(0)))
        {
            return "";
        }
        int pad = result.get(0) * 3;
        String formatString = "%" + pad + "s";
        return String.format(formatString, "");
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private TestUtils()
    {
        // Private constructor to prevent instantiation
    }

}
