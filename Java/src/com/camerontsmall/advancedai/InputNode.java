package com.camerontsmall.advancedai;

import java.util.ArrayList;

/**
 * Created by camer on 10/03/2018.
 */
public class InputNode extends PerceptronNode{

    private ArrayList<Double> values = new ArrayList<Double>();
    private int pointer = 0;
    private String title = "";

    public InputNode(String title){
        super();
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public String getEquation(){
        return this.title;
    }

    public void setPointer(int pointerValue){
        this.pointer = pointerValue;
    }

    public void addDataPoint(Double value){
        this.values.add(value);
    }

    public Double getValue() {
        return this.values.get(this.pointer);
    }

}
