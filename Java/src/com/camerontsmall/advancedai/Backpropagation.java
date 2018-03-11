package com.camerontsmall.advancedai;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Cameron on 10/03/2018.
 */
public class Backpropagation {

    /* Config */
    private Double step = 0.1;
    private Integer epochs = 100;
    private Integer hiddenNodes = 2;

    /* Variables */

    private Dataset data;
    private Random rand = new Random();

    private ArrayList<InputNode> inputs = new ArrayList<InputNode>();
    private ArrayList<PerceptronNode> hiddens = new ArrayList<PerceptronNode>();
    private OutputNode output;

    private Integer pointer = 0;

    public Backpropagation(){

        data = new Dataset();

        data.importCSV("data.csv");

        data.chooseOutput();

        setupNodes();

    }

    public void setupNodes(){
        inputs = data.getInputNodes();
        output = data.getOutputNode();

        hiddens.clear();
        for(int i = 0; i < hiddenNodes; i++){
            PerceptronNode newNode = new PerceptronNode();
            hiddens.add(newNode);
        }

        output.setBias(randWeight(hiddenNodes));

        //Assign inputs & initial random weights
        for(PerceptronNode hidden : hiddens){

            output.addInput(hidden, randWeight(hiddenNodes));

            hidden.setBias(randWeight(inputs.size()));

            for(PerceptronNode input : inputs){
                hidden.addInput(input, randWeight(inputs.size()));
            }
        }

    }

    public void step(){

        

    }

    public void run(){

        System.out.println("Starting values:");
        System.out.println(this.output.getEquation());
        System.out.println("Initial MSE: " + calculateMSE());

        for(int i = 0; i < epochs; i++){
            for(int j = 0; j < data.getSize(); j++){
                updatePointers(j);
                step();
            }
        }

        System.out.println("Results after " + epochs + " epochs:");
        System.out.println(this.output.getEquation());
        System.out.println("Final MSE: " + calculateMSE());

    }

    public double calculateMSE(){
        Double mse = 0.0;
        for(int j = 0; j < data.getSize(); j++){
            updatePointers(j);
            mse += Math.pow(output.getError(), 2);
        }
        mse = mse / data.getSize();
        return mse;
    }


    /**
     * Set pointer values for all nodes containing data
     * @param pointer
     */
    private void updatePointers(Integer pointer){

        output.setPointer(pointer);

        for(InputNode input : inputs){
            input.setPointer(pointer);
        }
    }


    public Double randWeight(int inputs){
        Double upper = (2.0 / inputs);
        Double lower = -upper;

        Double normal = rand.nextDouble();

        return (normal * (upper - lower) - lower);
    }

    public static Double sigmoid(Double sum){
        return 1 / (1 + Math.exp(-sum));
    }

    public static Double sigmoidPrime(Double sum){
        return sigmoid(sum) * (1 - sigmoid(sum));
    }
}
