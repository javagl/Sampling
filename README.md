# Sampling

Utility classes for sampling.

These classes are used internally and should not be considered to be part
of an official, stable library.

## List sampling

The `ListSampling` class defines methods for obtaining random samples from
a given list:

    // Create the stream of samples
    int sampleSize = 10;
    Stream<List<Integer>> samples = 
        ListSampling.createSamples(
            inputData, sampleSize, new Random(0));

    // Print the first samples:
    samples.limit(5).forEach(sample -> 
    {
        System.out.println(
            "Sample " + sample + " (size " + sample.size() + ")");
    });
  

The `ListSampler` interface defines a method for creating samples from 
an input list. Different implementations of this interface may be obtained 
from the `ListSamplers` class: 

    // Create a list sampler that creates random samples where 
    // each sample has a size of 0.1 times the input size:
    double relativeSampleSize = 0.1;
    ListSampler listSampler = 
        ListSamplers.createWithRelativeSize(
            relativeSampleSize, new Random(0));
    
    // Create the stream of samples
    Stream<List<Integer>> samples = 
        listSampler.createSamples(inputData);

    // Print the first samples:
    samples.limit(5).forEach(sample -> 
    {
        System.out.println(
            "Sample " + sample + " (size " + sample.size() + ")");
    });


## List sliding windows

The `ListSlidingWindows` class contains methods that provides streams of
lists that are a sliding window over another list:

    List<Integer> input = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
    
    // Create a sliding window with a size of 3, moving
    // over the input list with a step size of 2:
    // [0, 1, 2]
    //       [2, 3, 4]
    //             [4, 5, 6]
    
    int windowSize = 3;
    int stepSize = 2;
    Stream<List<Integer>> stream = 
        ListSlidingWindows.createClosedSlidingWindow(
            input, windowSize, stepSize);
    stream.forEach(element -> 
    {
        System.out.println(element);
    });
        


## List splitting

The `ListSplitting` class offers methods that allow splitting a list
into multiple parts. These parts are *views* on parts of the original
list. This may be used to perform a memory-efficient cross validation: 

    int n = 10000000;
    int numFolds = 10;
    int fold = 3;
    
    // This list contains 10 million elements, and will occupy
    // approximately 200 MB of memory:
    List<Integer> inputData = createInputData(n);
    
    // This list contains 9 million elements, but will occupy
    // less than ONE KILOBYTE (!) of memory:
    List<Integer> trainingData = 
        ListSplitting.omitChunk(inputData, numFolds, fold)    

    // This list contains 1 million elements, but will occupy
    // less than ONE KILOBYTE (!) of memory:
    List<Integer> testingData = 
        ListSplitting.extractChunk(inputData, numFolds, fold)    
    
    performCrossValidation(trainingData, testingData);    
    
    

    