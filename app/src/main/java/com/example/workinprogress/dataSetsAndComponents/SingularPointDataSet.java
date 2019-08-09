package com.example.workinprogress.dataSetsAndComponents;

import java.util.ArrayList;

/**
 * SingularPointDataSet can be used to store any type of data set with just one value for each point
 */
public abstract class SingularPointDataSet extends DataSet {

    /**
     * constructor sets up an arraylist for the datasetpoints to be stored in and declares that there will
     * be just 1 value stored in each datasetpoint. This ensures datasetpoints containing multiple values
     * cannot be added to this set.
     * @param dataTypeName type of data to be stored
     */
    public SingularPointDataSet(String dataTypeName){
        setDataTypeName(dataTypeName);
        scaledResults1 = new ArrayList<>();
        numberOfDataPointsInEachSet = 1;
    }

    protected ArrayList<Integer> scaledResults1;

    public abstract ArrayList<Integer> getScaledResults1();

}
