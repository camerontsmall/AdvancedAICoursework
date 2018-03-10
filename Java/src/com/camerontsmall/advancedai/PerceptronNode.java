package com.camerontsmall.advancedai;

import java.util.Set;
import java.util.ArrayList;

/**
 * Created by camer on 10/03/2018.
 */
public class PerceptronNode {

    private ArrayList<NodeWeighting> inputs = new ArrayList<NodeWeighting>();
    private Double bias = 0.0;

    public int id;

    public Double getValue(){
        Double outputValue = 0.0;

        for(NodeWeighting input : this.inputs){
            Double incrementor = input.node.getValue() * input.weight;
            outputValue += incrementor;
        }

        outputValue += this.bias;
        return outputValue;
    }

    public void setWeight(int inputId, Double weight){
        this.inputs.get(inputId).weight = weight;
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

}
