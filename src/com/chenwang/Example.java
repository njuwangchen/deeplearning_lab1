package com.chenwang;

/**
 * Created by ClarkWong on 24/1/17.
 */
public class Example {
    int numOfFeature;
    String[] features;
    String label;

    public Example(String line) {
        String[] tokens = line.split("\\s+");
        this.numOfFeature = Integer.parseInt(tokens[0]);
        this.features = new String[this.numOfFeature];
        this.label = tokens[1];

        for (int i=0; i<numOfFeature; ++i) {
            this.features[i] = tokens[i+2];
        }
    }

}
