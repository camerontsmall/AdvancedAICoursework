package com.camerontsmall.advancedai;

import java.lang.reflect.Array;
import java.util.Set;
import java.util.ArrayList;

/**
 * Created by camer on 10/03/2018.
 */
public class PerceptronNode {

    private static int PerceptronIdCounter = 0;
    private static ArrayList<PerceptronNode> ExistingNodes = new ArrayList<PerceptronNode>();

    public static ArrayList<PerceptronNode> FindOuputs(PerceptronNode targetNode){
        ArrayList<PerceptronNode> outputs = new ArrayList<PerceptronNode>();

        for(PerceptronNode node : ExistingNodes){

            for(NodeWeighting input : node.getInputs()){
                if(input.node.getId() == targetNode.getId()){
                    outputs.add(input.node);
                }
            }
        }

        return outputs;
    }
    public static PerceptronNode getById(Integer id){
        for(PerceptronNode node: ExistingNodes){
            if(node.getId() == id) return node;
        }
        return null;
    }

    public static Boolean enableTracking = true;   //set false to reduce memory usage for lots of repeats

    public PerceptronNode(){
        this.id = PerceptronIdCounter;
        PerceptronIdCounter++;
        if(enableTracking) ExistingNodes.add(this);
    }

    private ArrayList<NodeWeighting> inputs = new ArrayList<NodeWeighting>();
    private Double bias = 0.0;
    private Double lastSum = 0.0;

    private Integer id;

    public Integer getId(){
        return this.id;
    }

    public String getTitle(){
        return  "Node " + this.id.toString();
    }

    public double getLastSum(){
        return lastSum;
    }

    public Double getValue(){
        Double outputValue = 0.0;

        for(NodeWeighting input : this.inputs){
            Double incrementor = input.node.getValue() * input.weight;
            outputValue += incrementor;
        }

        outputValue += this.bias;
        lastSum = outputValue;
        return outputValue;
    }

    public ArrayList<NodeWeighting> getInputs(){
        return this.inputs;
    }

    public void setWeight(int inputId, Double weight){
        this.inputs.get(inputId).weight = weight;
    }

    public Double getBias(){
         return this.bias;
    }
    public void setBias(Double bias){
        this.bias = bias;
    }

    public void addInput(PerceptronNode input, Double weight){
        NodeWeighting weighting = new NodeWeighting(input, (double) weight);
        this.inputs.add(weighting);
    }

    public boolean hasInputNode(PerceptronNode checkNode){
        for(NodeWeighting input : this.inputs){
            if(input.node == checkNode) return true;
        }
        return false;
    }

    public String getEquation(){
        String equation = "";
        for(NodeWeighting input : this.inputs){
            if(equation.length() > 0) equation = equation + " + ";
            equation = equation + "(" + input.node.getEquation() + " * " + input.weight.toString() + ")";
        }
        if(equation.length() > 0) equation = equation + " + ";
        equation = equation + this.bias.toString();
        return "(" + equation + ")";
    }

}
