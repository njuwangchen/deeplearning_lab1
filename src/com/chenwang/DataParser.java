package com.chenwang;

/**
 * Created by ClarkWong on 24/1/17.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DataParser {

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
