package com.camerontsmall.advancedai;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.time.*;
import java.time.format.*;

public class Main {

    public static void main(String[] args) {

        /* CONFIGURATION */

        String inputData = "";

        /* */


        Backpropagation backprop = new Backpropagation("data-random.csv");

        //backprop.run();

        //backprop.export("export.csv");


        if(true){

            BackPropConfig config = readConfig("config.txt");

            for(int i = 0; i < config.numRepeats; i++){

                System.out.println("ITERATION " + i);
                for(Integer numEpochs : config.epochsToRun){
                    for(Integer numNodes : config.numNodesToRun){

                        backprop.setNumHiddenNodes(numNodes);
                        backprop.setEpochs(numEpochs);
                        backprop.setupNodes();

                        backprop.run();
                        Double mse = backprop.getMSE();

                        backprop.export("export/export_"  + mse + "_mse_" + numNodes + "_nodes_" + numEpochs + "_epochs" + ".csv");

                    }
                }
            }


            backprop.report();

            String thisMoment = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mmX")
                    .withZone(ZoneOffset.UTC)
                    .format(Instant.now());

            String reportFilePath = "report/report" + thisMoment + ".csv";

            backprop.saveReport(reportFilePath);

        }else{

            backprop.setEpochs(1600);
            backprop.setNumHiddenNodes(6);
            backprop.setVerificationSetSize(100);

            backprop.setupNodes();
            backprop.run();

            backprop.export("export/test.csv");
            backprop.report();

        }




    }

    public static BackPropConfig readConfig(String configFilePath){

        BufferedReader bread = null;
        String splitter = ",";

        ArrayList<Integer> epochsToRun = new ArrayList<Integer>();
        ArrayList<Integer> numNodesToRun = new ArrayList<Integer>();
        int numRepeats;

        try{

            bread = new BufferedReader(new FileReader(configFilePath));

            numRepeats = Integer.parseInt(bread.readLine());

            String epochsLine = bread.readLine();
            String[] epochs = epochsLine.split(splitter);

            for(String epoch : epochs){
               epochsToRun.add(Integer.parseInt(epoch));
            }

            String nodesLine = bread.readLine();
            String[] nodes = nodesLine.split(splitter);

            for(String numNodes : nodes){
                numNodesToRun.add(Integer.parseInt(numNodes));
            }

            System.out.println("Loaded configuration from " + configFilePath);

            return new BackPropConfig(epochsToRun, numNodesToRun, numRepeats);

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if (bread != null) {
                try {
                    bread.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}

class BackPropConfig{
    public ArrayList<Integer> epochsToRun = new ArrayList<Integer>();
    public ArrayList<Integer> numNodesToRun = new ArrayList<Integer>();
    public int numRepeats;

    public BackPropConfig(ArrayList<Integer> epochsToRun1, ArrayList<Integer> numNodesToRun1, int numRepeats1){
        epochsToRun = epochsToRun1;
        numNodesToRun = numNodesToRun1;
        numRepeats = numRepeats1;
    }
}
