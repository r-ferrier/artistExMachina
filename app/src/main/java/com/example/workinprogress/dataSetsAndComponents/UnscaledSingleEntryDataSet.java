package com.example.workinprogress.dataSetsAndComponents;

import java.util.ArrayList;

/**
 * An unscaledSingleEntryDataSet is a little bit different in that it contains just one value which
 * is set when it is constructed. The value can be any type of number.
 * @param <e> this dataset type can contain any kind of Number as its value
 */
public class UnscaledSingleEntryDataSet<e extends Number> extends SingularPointDataSet {

    /**
     * adds rounded data point to scaledresults array and sets datatype name using the super.
     * @param dataPoint numeric datapoint to be stored
     * @param dataTypeName references the type of data this data point will be
     */
    public UnscaledSingleEntryDataSet(e dataPoint, String dataTypeName){

        super(dataTypeName);

        if(dataPoint instanceof Integer){
            scaledResults1.add((Integer)dataPoint);
        }else{
            scaledResults1.add(Math.round((Float)dataPoint));
        }

    }


    @Override
    public ArrayList<Integer> getScaledResults1() {
        return scaledResults1;
    }

    @Override
    public void addResult(DataSetPoint dataSetPoint) {
    }

    @Override
    public void setScaledResults() {
        //scaling not necessary for this data type
    }

    @Override
    public String toString() {
        return getDataTypeName()+"\n"+scaledResults1.get(0).toString()+"\n";
    }

    @Override
    public String getAverageString() {
        return "N/A";
    }
}
