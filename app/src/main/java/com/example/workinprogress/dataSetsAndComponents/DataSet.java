package com.example.workinprogress.dataSetsAndComponents;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Instances of DataSets will hold arrays of data points (in some cases, multiple arrays, or arrays of objects)
 * Datasets will be created and added to whilst data is being gathered. Once data gathering is complete, the
 * data they hold will be scaled appropriately. Subclasses will define how the data is returned.
 */
public abstract class DataSet implements Serializable {

    private ArrayList<DataSetPoint> results = new ArrayList<>();
    private String dataTypeName;
    protected int numberOfDataPointsInEachSet;

    protected float max;
    protected float min;

    //result can only be added if it is for the same type of data and has the same number of data points
    public void addResult(DataSetPoint dataSetPoint){

        System.out.println("dataset added "+dataTypeName + dataSetPoint.toString());

        if (dataSetPoint.getNumberOfDataPointsInSet()==numberOfDataPointsInEachSet&&dataSetPoint.getDataTypeName()==dataTypeName) {
            results.add(dataSetPoint);
            System.out.println(dataSetPoint);
        }
    }

    public abstract void setScaledResults();

    public String toString() {

        String string = getDataTypeName()+"\n";
        for (DataSetPoint dataSetPoint : getResults()){
            string += dataSetPoint.toString()+"\n";
        }
        return string;
    }

    public String getDataTypeName(){
        return dataTypeName;
    }

    public ArrayList<DataSetPoint> getResults(){
        return results;
    }

    protected void setDataTypeName(String name){
        this.dataTypeName = name;
    }

    public int getNumberOfDataPointsInEachSet() {
        return numberOfDataPointsInEachSet;
    }

    public float getMax(){
        return max;
    }


    public float getMin(){
        return min;
    }
}
