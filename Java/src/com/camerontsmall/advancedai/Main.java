package com.camerontsmall.advancedai;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        /* CONFIGURATION */

        String inputData = "";

        /* */



        Backpropagation backprop = new Backpropagation();

        //backprop.run();

        //backprop.export("export.csv");


        ArrayList<Integer> epochRuns = new ArrayList<Integer>();
        epochRuns.add(50);
        epochRuns.add(100);
        epochRuns.add(200);
        epochRuns.add(500);
        epochRuns.add(700);
        epochRuns.add(1000);

        for(Integer numEpochs : epochRuns){
            for(int i = 4; i < 10; i++){

                backprop.setNumHiddenNodes(i);
                backprop.setEpochs(numEpochs);
                backprop.setupNodes();

                backprop.run();
                Double mse = backprop.getMSE();

                backprop.export("export/export_" + i + "nodes_" + numEpochs + "_epochs" + mse +  "mse.csv");

            }
        }




    }
}
