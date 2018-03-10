package com.camerontsmall.advancedai;

import java.util.ArrayList;

/**
 * Created by camer on 10/03/2018.
 */
public class InputNode extends PerceptronNode{

    private ArrayList<Double> values = new ArrayList<Double>();
    private int pointer = 0;

    public void updatePointer(int pointerValue){
        this.pointer = pointerValue;
    }

    public void addDataPoint(Double value){
        this.values.add(value);
    }

    public Double getValue() {
        return this.values.get(this.pointer);
    }

}
