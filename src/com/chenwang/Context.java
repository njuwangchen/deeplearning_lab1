package com.chenwang;

/**
 * Created by ClarkWong on 24/1/17.
 */

import java.util.HashMap;
import java.util.Map;

public class Context {
    int numOfFeatures;

    int numOfExamples;
    Example[] examples;

    Map<String, Map<String, Integer>> featuresMap;
    Map<String, Integer> labelsMap;

    public Context(int numOfFeatures, int numOfExamples) {
        this.numOfExamples = numOfExamples;
        this.examples = new Example[numOfExamples];

        this.featuresMap = new HashMap<String, Map<String, Integer>>();
        this.labelsMap = new HashMap<String, Integer>();
    }
}
