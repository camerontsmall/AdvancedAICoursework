package com.camerontsmall.advancedai;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Cameron on 10/03/2018.
 */
public class Backpropagation {

    /* Config */
    private Double step = 0.1;
    private Integer epochs = 700;
    private Integer numHiddenNodes = 5;

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

        data.normalise(0.1,0.9);

        data.chooseOutput();

        setupNodes();

    }

    public void setEpochs(int epochValue){
        epochs = epochValue;
    }

    public void setNumHiddenNodes(int hiddenNodes){
        numHiddenNodes = hiddenNodes;
    }

    public void setStep(Double stepValue){
        step = stepValue;
    }


    public void setupNodes(){
        inputs = data.getInputNodes();
        output = data.getOutputNode();

        hiddens.clear();
        for(int i = 0; i < numHiddenNodes; i++){
            PerceptronNode newNode = new PerceptronNode();
            hiddens.add(newNode);
        }

        output.setBias(randWeight(numHiddenNodes));

        //Assign inputs & initial random weights
        for(PerceptronNode hidden : hiddens){

            output.addInput(hidden, randWeight(numHiddenNodes));

            hidden.setBias(randWeight(inputs.size()));

            for(PerceptronNode input : inputs){
                hidden.addInput(input, randWeight(inputs.size()));
            }
        }

    }

    public void run(){

        System.out.println("Starting values:");
        System.out.println(this.output.getEquation());
        exportComputedValues();
        data.report();

        for(int i = 0; i < epochs; i++){
            for(int j = 0; j < data.getSize(); j++){
                updatePointers(j);
                step();
            }
        }

        System.out.println("Results after " + epochs + " epochs with " + numHiddenNodes + " hidden nodes:");
        System.out.println(this.output.getEquation());

        exportComputedValues();
        data.report();

    }

    public void exportComputedValues(){
        ArrayList<Double> computed = new ArrayList<Double>();

        for(int j = 0; j < data.getSize(); j++){
            updatePointers(j);
            computed.add(output.getValue());
        }

        data.importComputedValues(computed);
    }

    public void export(String filepath){
        exportComputedValues();
        data.exportCSV(filepath);
    }

    public Double getMSE(){
        return data.getMSE();
    }

    public void step(){

        //Output.getValue will perform a forward pass
        Double outputDelta = output.getError() * sigmoidPrime(output.getValue());

        for(NodeWeighting inputL1 : output.getInputs()){

            Double inputActivation = sigmoid(inputL1.node.getValue());

            inputL1.weight = inputL1.weight + (step * outputDelta * inputActivation);

            Double hiddenDelta = inputL1.weight * outputDelta * sigmoidPrime(inputL1.node.getValue());

            for(NodeWeighting inputL2 : inputL1.node.getInputs()){

                Double hiddenActivation = sigmoid(inputL2.node.getValue());
                inputL2.weight = inputL2.weight + (step * hiddenDelta * hiddenActivation);

            }

        }

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

    public static Double sigmoid(Double val){
        return (1 / (1 + Math.exp(-val)));
    }

    public static Double sigmoidPrime(Double val){
        Double value = sigmoid(val) * (1 - sigmoid(val));
        return value;
    }
}
