package com.chenwang;

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

    int bias;

    int threshold;

    double learningRate;

    public PerceptronLearning(Context trainingSet, Context tuningSet, Context testingSet,
                              int threshold, double learningRate) {
        this.trainingSet = trainingSet;
        this.tuningSet = tuningSet;
        this.testingSet = testingSet;

        numOfWeights = trainingSet.numOfFeatures;
        weights = new double[numOfWeights];

        for (int i=0; i<numOfWeights; ++i) {
            weights[i] = new Random().nextDouble();
        }

        this.threshold = threshold;
        this.learningRate = learningRate;
    }

    private void learnOneExample(Example example) {
        String[] strFeatures = example.features;
        int[] features = new int[numOfWeights];
        for (int i=0; i<numOfWeights; ++i) {

        }
    }
}
