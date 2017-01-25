package com.chenwang;

/**
 * Created by ClarkWong on 24/1/17.
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Context {
    int numOfFeatures;
    int labelCounter;

    int numOfExamples;
    Example[] examples;
    int exampleCounter;

    Map<String, Map<String, Integer>> featuresMap;
    Map<String, Integer> labelsMap;

    public Context(int numOfFeatures) {
        this.numOfFeatures = numOfFeatures;
        this.labelCounter = 0;

        this.featuresMap = new HashMap<String, Map<String, Integer>>();
        this.labelsMap = new HashMap<String, Integer>();
    }

    public boolean addFeature(String line) {
        String[] tokens = line.split("\\s+");
        String featureName = tokens[0];

        if (featuresMap.containsKey(featureName)) {
            System.err.println("Feature Has Already Existed.");
            return false;
        }

        Map<String, Integer> featureVals = new HashMap<String, Integer>();
        featuresMap.put(featureName, featureVals);

        int featureValLen = tokens.length-2;
        for (int i=0; i<featureValLen; ++i) {
            featureVals.put(tokens[2+i], i);
        }

        return true;
    }

    public boolean addLabel(String label) {
        if (labelsMap.containsKey(label)) {
            System.err.println("Label Has Already Existed");
            return false;
        }

        labelsMap.put(label, labelCounter);
        ++labelCounter;

        return true;
    }

    public void setNumOfExamples(int numOfExamples) {
        this.numOfExamples = numOfExamples;
        this.examples = new Example[numOfExamples];
        this.exampleCounter = 0;
    }

    public void addExample(String line) {
        Example example = new Example(line, this.numOfFeatures);
        this.examples[this.exampleCounter] = example;
        ++this.exampleCounter;
    }

    @Override
    public String toString() {
        String ret = "";

        ret += "# of features: ";
        ret += numOfFeatures;
        ret += "\n";

        Iterator<Map.Entry<String, Map<String, Integer>>> iter = featuresMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Map<String, Integer>> entry = iter.next();
            String featureName = entry.getKey();
            ret += featureName;
            ret += ": ";

            Map<String, Integer> featureVals = entry.getValue();
            Iterator<Map.Entry<String, Integer>> valIter = featureVals.entrySet().iterator();
            while (valIter.hasNext()) {
                Map.Entry<String, Integer> valEntry = valIter.next();
                String valName = valEntry.getKey();
                ret += valName;
                ret += " - ";
                Integer valInt = valEntry.getValue();
                ret += valInt;
                ret += " ";
            }
            ret += "\n";
        }

        ret += "Labels: ";
        Iterator<Map.Entry<String, Integer>> labelIter = labelsMap.entrySet().iterator();
        while (labelIter.hasNext()) {
            Map.Entry<String, Integer> entry = labelIter.next();
            String labelName = entry.getKey();
            ret += labelName;
            ret += " - ";
            Integer labelInt = entry.getValue();
            ret += labelInt;
            ret += " ";
        }
        ret += "\n";

        for (int i=0; i<numOfExamples; ++i) {
            ret += this.examples[i];
            ret += "\n";
        }

        return ret;
    }
}
