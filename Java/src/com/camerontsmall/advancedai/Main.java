package com.camerontsmall.advancedai;

public class Main {

    public static void main(String[] args) {
	// write your code here

        PerceptronNode node1 = new PerceptronNode();

        PerceptronNode node2 = new PerceptronNode();
        node2.setBias(1.0);

        node1.addInput(node2,1.1);
        node1.setBias(4.0);

        System.out.println(node1.getValue());
    }
}
