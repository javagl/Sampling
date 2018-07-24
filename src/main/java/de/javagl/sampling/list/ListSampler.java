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

import java.util.List;
import java.util.stream.Stream;

/**
 * Interface for classes that may provide samples from a given list.
 */
public interface ListSampler
{
    /**
     * Returns a stream of samples from the given list. The samples will be 
     * returned in form of unmodifiable lists.<br>
     * <br>
     * Many implementations of this interface are able to provide an
     * infinite stream, although this is not guaranteed.  
     * <br>
     * If the given list is modified after the stream has been created, 
     * the behavior of the stream is unspecified.<br> 
     * <br>
     * If the given list is modified after the samples have been obtained
     * from the stream, the behavior of the samples is unspecified.<br>
     * <br>
     * If the given list has to be modified after the sampling, a <i>copy</i>  
     * of the list should be given to this method.<br>
     * <br> 
     * 
     * @param <T> The type of the elements in the list
     * @param input The input list
     * @return The stream
     * @throws NullPointerException If the input is <code>null</code>
     */
    <T> Stream<List<T>> createSamples(List<? extends T> input);
}