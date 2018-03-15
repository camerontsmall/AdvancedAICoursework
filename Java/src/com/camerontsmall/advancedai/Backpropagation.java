package com.camerontsmall.advancedai;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

class BackpropRun{

    public int epochs;
    public int numHiddenNodes;
    public Double mse;
    public Double step;
    public String equation;
    public long duration;

    public BackpropRun(int epochs, int numHiddenNodes, Double mse, Double step, String equation, long duration){
        this.epochs = epochs;
        this.numHiddenNodes = numHiddenNodes;
        this.mse = mse;
        this.step = step;
        this.equation = equation;
        this.duration = duration;
    }

    public String getReport(){
        return epochs + " epochs | " +  numHiddenNodes + " hidden nodes | " + mse + " mean squared error | " + (duration / 1000) + " seconds";
    }


}

/**
 * Created by Cameron on 10/03/2018.
 */
public class Backpropagation {

    /* Config */
    private Double step = 0.1;
    private Integer epochs = 700;
    private Integer numHiddenNodes = 5;

    private Integer verificationSetSize = 200;
    private Integer verificationInterval = 100;

    private Boolean useMomentum = false;
    private Double momentumValue = 0.9;

    //private Boolean useBoldDriver = false;
    //private Integer boldDriverInterval = 500;

    private Boolean useAnnealing = false;
    private Double annealingP = 0.01;
    private Double annealingQ = 0.1;
    private int annealingR = 3000;


    /* Variables */

    private Dataset data;
    private Random rand = new Random();

    private ArrayList<InputNode> inputs = new ArrayList<InputNode>();
    private ArrayList<PerceptronNode> hiddens = new ArrayList<PerceptronNode>();
    private OutputNode output;

    private Integer pointer = 0;

    private ArrayList<BackpropRun> runs = new ArrayList<BackpropRun>();

    public Backpropagation(String inputDataFilePath){

        data = new Dataset();

        data.importCSV(inputDataFilePath);

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

    public void setVerificationSetSize(int verificationSetSize1){
        verificationSetSize = verificationSetSize1;
    }

    public void setMomentum(Boolean momentum){
        useMomentum = momentum;
    }

    public void setMomentumValue(Double momentumValue1){
        momentumValue = momentumValue1;
    }


    public void setupNodes(){
        inputs = data.getInputNodes();  //Generate input nodes from dataset
        output = data.getOutputNode();  //Generate output node from dataset

        hiddens.clear();    //Reset the hidden nodes

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
        data.report();  //Print out initial MSE for comparison

        long start = System.currentTimeMillis();
        double lastReportedError = data.getMSE();

        for(int i = 0; i < epochs; i++){

            if((i % verificationInterval) == 0 && (verificationSetSize > 0)){
                for(int k = (data.getSize() - verificationSetSize); k < data.getSize(); k++){
                    updatePointers(k);
                    step(i);
                }
            }

            /*
            if((i % boldDriverInterval) == 0 && useBoldDriver ){
                exportComputedValues();
                if(data.getMSE() > lastReportedError){

                }
            }*/

            for(int j = 0; j < (data.getSize() - verificationSetSize); j++){
                updatePointers(j);
                step(i);
            }


        }

        long end = System.currentTimeMillis();

        System.out.println("Results after " + epochs + " epochs with " + numHiddenNodes + " hidden nodes:");
        String equation = this.output.getEquation();
        System.out.println(equation);

        exportComputedValues();
        data.report();

        long duration = end - start;

        runs.add(new BackpropRun(epochs, numHiddenNodes, data.getMSE(), step, equation, duration));

    }

    public void step(int currentEpoch){

        double stepAmount;

        if(useAnnealing){
            stepAmount = annealing(currentEpoch);
        }else{
            stepAmount = step;
        }

        //Output.getValue will perform a forward pass
        Double outputDelta = output.getError() * sigmoidPrime(output.getValue());

        output.setBias(output.getBias() + (stepAmount * outputDelta * sigmoid(1.0)));

        for(NodeWeighting inputL1 : output.getInputs()){

            Double inputActivation = sigmoid(inputL1.node.getValue());

            Double weightChange = (stepAmount * outputDelta * inputActivation);
            inputL1.weight = inputL1.weight + weightChange;

            if(useMomentum){
                inputL1.weight = inputL1.weight + (momentumValue * inputL1.lastWeightChange);
                inputL1.lastWeightChange = weightChange;
            }

            Double hiddenDelta = inputL1.weight * outputDelta * sigmoidPrime(inputL1.node.getValue());

            inputL1.node.setBias(inputL1.node.getBias() + (stepAmount * hiddenDelta * sigmoid(1.0)));

            for(NodeWeighting inputL2 : inputL1.node.getInputs()){

                Double hiddenActivation = sigmoid(inputL2.node.getValue());

                Double weightChange2 = (stepAmount * hiddenDelta * hiddenActivation);
                inputL2.weight = inputL2.weight + weightChange2;

                if(useMomentum){
                    inputL2.weight = inputL2.weight + (momentumValue * inputL2.lastWeightChange);
                    inputL2.lastWeightChange = weightChange;
                }
            }

        }

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

    public double annealing(int epoch){
        return annealingP + ((annealingQ - annealingP) * (1 - sigmoid((10 - ((20 * epoch) / (double) annealingR)))));
    }

    public void report(){

        if(this.runs.size() < 1) return;

        BackpropRun best = this.runs.get(0);

        for(BackpropRun run : this.runs){
            if(run.mse < best.mse) best = run;
            System.out.println(run.getReport());
        }

        System.out.println("Best performing cycle:");
        System.out.println(best.getReport());
    }

    public void saveReport(String filePath){

        try{

            PrintWriter pw = new PrintWriter( new File(filePath) );
            StringBuilder sb = new StringBuilder();

            sb.append("mse,epochs,hiddennodes,duration,equation");
            sb.append('\n');

            for(BackpropRun run : this.runs){
                sb.append(run.mse.toString());
                sb.append(',');
                sb.append(run.epochs);
                sb.append(',');
                sb.append(run.numHiddenNodes);
                sb.append(',');
                sb.append(run.duration);
                sb.append(',');
                sb.append(run.equation);
                sb.append('\n');
            }

            pw.write(sb.toString());
            pw.close();
            System.out.println("Wrote results of results to " + filePath);

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

    }

}
