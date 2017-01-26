package com.chenwang;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

/**
 * Created by chenwang on 1/25/17.
 */
public class PerceptronLearning {
    Context trainingSet;
    Context tuningSet;
    Context testingSet;

    int numOfWeights;
    double[] weights;

    double bias;

    double threshold;

    double learningRate;

    public PerceptronLearning(Context trainingSet, Context tuningSet, Context testingSet,
                              double threshold, double learningRate) {
        this.trainingSet = trainingSet;
        this.tuningSet = tuningSet;
        this.testingSet = testingSet;

        numOfWeights = trainingSet.numOfFeatures;
        weights = new double[numOfWeights];

        for (int i=0; i<numOfWeights; ++i) {
//            weights[i] = new Random().nextDouble();
            weights[i] = 1.0;
        }

        this.threshold = threshold;
        this.learningRate = learningRate;

        this.bias = 1.0;
    }

    private int[] transformFeature(String[] strFeatures) {
        int[] features = new int[numOfWeights];
        for (int i=0; i<numOfWeights; ++i) {
            Map<String, Integer> map = trainingSet.featuresList.get(i);
            Integer featureVal = map.get(strFeatures[i]);
            features[i] = featureVal;
        }
        return features;
    }

    private boolean learnOneExample(Example example) {
        String[] strFeatures = example.features;
        int[] features = transformFeature(strFeatures);

//        // print out the features array
//        for (int i=0; i<numOfWeights; ++i) {
//            System.out.print(features[i]+" ");
//        }
//        System.out.println();

        double weightedSum = 0;
        // calculate the summed weights
        for (int i=0; i<numOfWeights; ++i) {
            weightedSum += features[i] * weights[i];
        }
        weightedSum += bias;

        int output = 0;
        if (weightedSum > threshold) {
            output = 1;
        }

        // adjust the weights
        int teacher = trainingSet.labelsMap.get(example.label);

        if (teacher == output) return true;

        if (teacher != output) {
            for (int i=0; i<numOfWeights; ++i) {
                weights[i] += learningRate*features[i]*(teacher-output);
            }
            bias += learningRate*(teacher-output);
        }

//        // print out the new weights
//        for (int i=0; i<numOfWeights; ++i) {
//            System.out.print(weights[i]+" ");
//        }
//        System.out.println();

        return false;
    }

    private boolean testOneExample(Example example) {

        String[] strFeatures = example.features;
        int[] features = transformFeature(strFeatures);

        double weightedSum = 0;
        // calculate the summed weights
        for (int i=0; i<numOfWeights; ++i) {
            weightedSum += features[i] * weights[i];
        }
        weightedSum += bias;

        int output = 0;
        if (weightedSum > threshold) {
            output = 1;
        }

        int teacher = trainingSet.labelsMap.get(example.label);

        return teacher == output;
    }

    public void learnOneEpoch() {
        int count = 0;

        for (int i=0; i<trainingSet.numOfExamples; ++i) {
            if (!learnOneExample(trainingSet.examples[i])) {
                ++count;
            }
        }

        System.out.println("Training Set Error Rate: " + (double)count/trainingSet.numOfExamples);
    }

    private double testAndGetErrorRate(Example[] examples) {
        int countOfError = 0;
        int countOfExample = examples.length;
        for (int i=0; i<countOfExample; ++i) {
            if (!testOneExample(examples[i])) countOfError++;
        }

        return (double)countOfError/countOfExample;
    }

    public double getTuningSetErrorRate() {
        double tuningSetErrorRate = testAndGetErrorRate(tuningSet.examples);
        System.out.println("Tuning Set Error Rate is: "+tuningSetErrorRate);
        return tuningSetErrorRate;
    }

    public double getTestingSetErrorRate() {
        double testingSetErrorRate = testAndGetErrorRate(testingSet.examples);
        System.out.println("Testing Set Error Rate is: "+testingSetErrorRate);
        return testingSetErrorRate;
    }

    public void startTraining() {
        double lastErrorRate = 1.0;
        double[] lastEpochWeights;
        double lastEpochBias;
        while (true) {
            lastEpochWeights = Arrays.copyOf(this.weights, numOfWeights);
            lastEpochBias = this.bias;
            learnOneEpoch();
            double thisTimeErrorRate = getTuningSetErrorRate();
            if (thisTimeErrorRate > lastErrorRate) {
                this.weights = lastEpochWeights;
                this.bias = lastEpochBias;
                getTestingSetErrorRate();
                break;
            }
            lastErrorRate = thisTimeErrorRate;
            getTestingSetErrorRate();
        }
    }
}
