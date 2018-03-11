package com.camerontsmall.advancedai;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        /* CONFIGURATION */

        String inputData = "";

        /* */

        Backpropagation backprop = new Backpropagation();

        backprop.runOnce();

        /*
        ArrayList<PerceptronNode> inputs = new ArrayList<PerceptronNode>();
        ArrayList<PerceptronNode> neurons = new ArrayList<PerceptronNode>();
        PerceptronNode output = new OutputNode("PanE");

        PerceptronNode node2 = new PerceptronNode();
        node2.setBias(1.0);

        output.addInput(node2,1.1);
        output.setBias(4.0);

        InputNode inNode = new InputNode("test");
        inNode.addDataPoint(0.023);

        InputNode inNode2 = new InputNode("another thing");
        inNode2.addDataPoint(2.13);

        node2.addInput(inNode, 1.00);
        node2.addInput(inNode2, -0.232);

        System.out.println(output.getValue());
        System.out.println(PerceptronNode.ExistingNodes.size());
        System.out.println(output.getEquation());*/
    }
}
