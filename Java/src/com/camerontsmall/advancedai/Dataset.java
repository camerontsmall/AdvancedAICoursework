package com.camerontsmall.advancedai;

import jdk.internal.util.xml.impl.Input;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;

//we could just write the data straight to the nodes, but this adds a bit of flexibility
class DataColumn{

    public DataColumn(String title){
        this.title  = title;
    }

    public String title;
    public ArrayList<Double> values = new ArrayList<Double>();

    public void addValue(Double value){
        values.add(value);
    }

    public InputNode makeInputNode(){
        InputNode node = new InputNode(this.title);

        for(Double value : this.values){
            node.addDataPoint(value);
        }

        return node;
    }

    public OutputNode makeOutputNode(){
        OutputNode node = new OutputNode(this.title);

        for(Double value : this.values){
            node.addDataPoint(value);
        }

        return node;
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

    public void exportCSV(String filepath){}

}
