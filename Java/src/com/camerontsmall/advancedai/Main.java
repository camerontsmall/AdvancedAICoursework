package com.camerontsmall.advancedai;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        /* CONFIGURATION */

        String inputData = "";

        /* */



        Backpropagation backprop = new Backpropagation();

        //backprop.run();

        //backprop.export("export.csv");

        for(int i = 4; i < 10; i++){

            backprop.setNumHiddenNodes(i);
            backprop.setupNodes();

            backprop.run();
            Double mse = backprop.getMSE();

            backprop.export("export_" + i + "nodes_" + mse +  "mse.csv");

        }


    }
}
