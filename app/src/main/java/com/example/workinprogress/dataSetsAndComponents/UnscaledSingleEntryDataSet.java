package com.example.workinprogress.dataSetsAndComponents;

import java.util.ArrayList;

/*steps data class is a subclass of singular point data set. It requires no scaling or rounding and
*stores its data only as an Integer in position 1 of the array.
*/
public class UnscaledSingleEntryDataSet<e extends Number> extends SingularPointDataSet {

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

}
