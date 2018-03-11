package com.camerontsmall.advancedai;

import jdk.internal.util.xml.impl.Input;

import javax.xml.crypto.Data;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.nio.Buffer;

//we could just write the data straight to the nodes, but this adds a bit of flexibility
class DataColumn{

    public DataColumn(String title){
        this.title  = title;
    }

    public String title;
    public ArrayList<Double> values = new ArrayList<Double>();
    public ArrayList<Double> computedValues = new ArrayList<Double>();

    double multiplier = 1.0;
    double offset = 0.0;

    public void addValue(Double value){
        values.add(value);
    }

    public InputNode makeInputNode(){
        InputNode node = new InputNode(this.title);

        for(Double value : this.values){
            value = value * multiplier;
            value = value + offset;
            node.addDataPoint(value);
        }

        return node;
    }

    public OutputNode makeOutputNode(){
        OutputNode node = new OutputNode(this.title);

        for(Double value : this.values){
            value = value * multiplier;
            value = value + offset;
            node.addDataPoint(value);
        }

        return node;
    }

    public void importComputedValues(ArrayList<Double> values){

        computedValues.clear();
        for(Double readValue : values) {
            readValue = readValue - offset;
            readValue = readValue / multiplier;
            computedValues.add(readValue);
        }

    }

    public double calculateMSE(){

        Double mse = 0.0;
        for(int j = 0; j < values.size(); j++) {
                Double observed = values.get(j);
                Double computed = computedValues.get(j);
                Double error = observed - computed;
                mse += Math.pow(error, 2);
        }
        mse = mse / values.size();
        return mse;
    }

    public double getMax(){
        double max = 0.0;
        for(double value : values){
            if(value > max) max = value;
        }
        return max;
    }

    public double getMin(){
        double min = 0.0;
        for(double value : values){
            if(value < min) min = value;
        }
        return min;
    }

    public void normalise(double min, double max){

        double currentMin = getMin();
        double currentMax = getMax();

        double currentRange = currentMax - currentMin;
        double targetRange = max - min;

        multiplier = targetRange / currentRange;
        offset = currentMin - min;

    }
}

/**
 * Created by Cameron on 10/03/2018.
 */
public class Dataset {

    public Dataset(){

    }

    private DataColumn outputColumn = null;
    private Integer size = 0;
    public ArrayList<DataColumn> columns = new ArrayList<DataColumn>();

    public Integer getSize(){
        return size;
    }

    public void chooseOutput(){
        System.out.println("Columns:");
        for(int i = 0; i < this.columns.size() ; i++){

            System.out.println(i + ". " + this.columns.get(i).title);
            outputColumn = this.columns.get(i);
        }

        System.out.println("Selecting " + this.outputColumn.title  + " as the operand");
    }

    public ArrayList<InputNode> getInputNodes(){

        ArrayList<InputNode> inputs = new ArrayList<InputNode>();

        for(DataColumn column : this.columns){
            if(column != this.outputColumn){
                inputs.add(column.makeInputNode());
            }
        }

        return inputs;
    }

    public OutputNode getOutputNode(){
        return this.outputColumn.makeOutputNode();
    }

    public void importComputedValues(ArrayList<Double> normalValues){
        outputColumn.importComputedValues(normalValues);
    }

    public void report(){
        Double mse = outputColumn.calculateMSE();
        System.out.println("MSE: " + mse);
    }

    public double getMSE(){
        return outputColumn.calculateMSE();
    }

    public boolean importCSV(String filePath){

        if(filePath == null) return false;

        BufferedReader bread = null;
        String line = "";
        String titleLine = "";
        String splitter = ",";

        try{

            bread = new BufferedReader(new FileReader(filePath));

            titleLine = bread.readLine();
            String[] titles = titleLine.split(splitter);

            for(String title : titles){
                DataColumn column = new DataColumn(title);
                this.columns.add(column);
            }

            int lineCounter = 0;
            int successCounter = 0;

            while((line = bread.readLine()) != null){

                lineCounter++;

                try{
                    String[] values = line.split(splitter);

                    //Check data parses as double
                    for(String value : values){
                        Double checkValue = Double.parseDouble(value);  //If value fails check, throws error and skips
                    }

                    for(int i = 0; i < values.length ; i++){
                        Double doubleValue = Double.parseDouble(values[i]);
                        columns.get(i).addValue(doubleValue);
                    }

                    successCounter++;

                }catch(NumberFormatException e){
                    System.out.println("Skipping line " + (lineCounter  + 1) + ": " + e.getMessage());
                    continue;
                }

            }

            size = successCounter;

            System.out.println("Imported " + successCounter + " values, skipped " + (lineCounter - successCounter) + " lines");

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if (bread != null) {
                try {
                    bread.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;

    }

    public void normalise(Double min, Double max){

        for(DataColumn column : columns) {
            column.normalise(min, max);
        }

    }

    public void exportCSV(String filepath){

        try{

            PrintWriter pw = new PrintWriter( new File(filepath) );
            StringBuilder sb = new StringBuilder();

            sb.append("observed");
            sb.append(',');
            sb.append("computed");
            sb.append('\n');

            for(int i = 0; i < outputColumn.values.size(); i++){
                double observed = outputColumn.values.get(i);
                double computed = outputColumn.computedValues.get(i);
                sb.append(observed);
                sb.append(',');
                sb.append(computed);
                sb.append('\n');
            }

            pw.write(sb.toString());
            pw.close();
            System.out.println("Wrote results to " + filepath);

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

    }

}
