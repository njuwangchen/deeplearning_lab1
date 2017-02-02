package com.chenwang.submit;

import java.util.*;
import java.io.*;

/**
 * Created by ClarkWong on 2/2/17.
 */

public class Lab1 {
    public static void main(String[] args) {
        DataParser dp = new DataParser();
        Context trainingContext = dp.parseFile("data/red-wine-quality-train.data");
        Context tuningContext = dp.parseFile("data/red-wine-quality-tune.data");
        Context testingContext = dp.parseFile("data/red-wine-quality-test.data");

        PerceptronLearning perceptronLearning = new PerceptronLearning(trainingContext, tuningContext, testingContext, 0, 0.01);
        perceptronLearning.startTraining();

        System.out.println();

        Context trainingContext2 = dp.parseFile("data/Thoracic_Surgery_Data_train.data");
        Context tuningContext2 = dp.parseFile("data/Thoracic_Surgery_Data_tune.data");
        Context testingContext2 = dp.parseFile("data/Thoracic_Surgery_Data_test.data");

        PerceptronLearning perceptronLearning2 = new PerceptronLearning(trainingContext2, tuningContext2, testingContext2, 0, 0.01);
        perceptronLearning2.startTraining();
    }
}

class Context {
    int numOfFeatures;
    int labelCounter;

    int numOfExamples;
    Example[] examples;
    int exampleCounter;

    // Map<String, Map<String, Integer>> featuresMap;
    List<Map<String, Integer>> featuresList;
    Map<String, Integer> labelsMap;

    public Context(int numOfFeatures) {
        this.numOfFeatures = numOfFeatures;
        this.labelCounter = 0;

        // this.featuresMap = new HashMap<String, Map<String, Integer>>();
        this.featuresList = new ArrayList<Map<String, Integer>>();
        this.labelsMap = new HashMap<String, Integer>();
    }

    public boolean addFeature(String line) {
        String[] tokens = line.split("\\s+");
        String featureName = tokens[0];

//        if (featuresMap.containsKey(featureName)) {
//            System.err.println("Feature Has Already Existed.");
//            return false;
//        }

        Map<String, Integer> featureVals = new HashMap<String, Integer>();
        // featuresMap.put(featureName, featureVals);
        featuresList.add(featureVals);

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

        Iterator<Map<String, Integer>> iter = featuresList.iterator();
        while (iter.hasNext()) {
//            Map.Entry<String, Map<String, Integer>> entry = iter.next();
//            String featureName = entry.getKey();
//            ret += featureName;
//            ret += ": ";

            Map<String, Integer> featureVals = iter.next();
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

class DataParser {

    public Context parseFile(String path) {
        if (path == null || path.length() == 0) {
            System.err.println("Please provide valid file name");
            System.exit(1);
        }

        // Try creating a scanner to read the input file.
        Scanner fileScanner = null;
        try {
            fileScanner = new Scanner(new File(path));
        } catch(FileNotFoundException e) {
            System.err.println("Could not find file '" + path +
                    "'.");
            System.exit(1);
        }
        Context context = null;
        // Iterate through each line in the file.
        int lineCount = 0;
        while(fileScanner.hasNext()) {
            String line = fileScanner.nextLine().trim();

            // Skip blank lines and comments
            if(line.length() == 0 || line.startsWith("//")) {
                continue;
            }

            ++lineCount;

            if(lineCount == 1) {
                context = new Context(Integer.parseInt(line));
            } else if (lineCount <= context.numOfFeatures+1) {
                context.addFeature(line);
            } else if (lineCount <= context.numOfFeatures+1+2) {
                context.addLabel(line);
            } else if (lineCount <= context.numOfFeatures+1+2+1) {
                context.setNumOfExamples(Integer.parseInt(line));
            } else {
                context.addExample(line);
            }
        }

        return context;
    }
}

class Example {
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

class PerceptronLearning {
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


