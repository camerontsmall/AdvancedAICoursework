package com.camerontsmall.advancedai;

/**
 * Created by camer on 10/03/2018.
 */
public class NodeWeighting {

    public NodeWeighting(PerceptronNode node, Double weight){
        this.node = node;
        this.weight = weight;
    }

    public PerceptronNode node;
    public Double weight;
}
