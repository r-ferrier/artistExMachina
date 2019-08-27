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


    /**
     * result can only be added if it is for the same type of data and has the same number of data points
     * @param dataSetPoint piece of data to be added to the collection
     */
    public void addResult(DataSetPoint dataSetPoint){

        if (dataSetPoint.getNumberOfDataPointsInSet()==numberOfDataPointsInEachSet&&dataSetPoint.getDataTypeName().equals(dataTypeName)) {
            results.add(dataSetPoint);
        }
    }

    public abstract void setScaledResults();

    /**
     * @return string for every dataset includes its name and the full dataset printed as a string
     * with lines between each point.
     */
    public String toString() {

        String string = getDataTypeName()+"\ntotal: "+getResults().size()+"\naverage: "+getAverageString()+"\n\n";
        for (DataSetPoint dataSetPoint : getResults()){
            string += dataSetPoint.toString()+"\n";
        }
        return string;
    }

    public abstract String getAverageString();

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
