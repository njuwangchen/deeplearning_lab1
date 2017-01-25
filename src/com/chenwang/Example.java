package com.chenwang;

/**
 * Created by ClarkWong on 24/1/17.
 */
public class Example {
    String name;
    int numOfFeature;
    String[] features;
    String label;

    public Example(String line, int numOfFeature) {
        this.numOfFeature = numOfFeature;

        String[] tokens = line.split("\\s+");
        this.name = tokens[0];

        this.features = new String[this.numOfFeature];
        this.label = tokens[1];

        for (int i=0; i<numOfFeature; ++i) {
            this.features[i] = tokens[i+2];
        }
    }

    @Override
    public String toString() {
        String ret = "";
        ret += name;
        ret += ": ";
        for (int i=0; i<numOfFeature; ++i) {
            ret += features[i];
            ret += ", ";
        }
        ret += label;

        return ret;
    }
}
