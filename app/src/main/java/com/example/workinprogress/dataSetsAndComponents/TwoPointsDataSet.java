package com.example.workinprogress.dataSetsAndComponents;

import java.util.ArrayList;

/**
 * Can be used to store any type of data set with two values for each point
 */
public abstract class TwoPointsDataSet extends DataSet {

    /**
     * constructor sets up arraylists for the datasetpoints to be stored in and declares that there will
     * be 2 values stored in each datasetpoint. This ensures only datasetpoints containing 2 values
     * can be added to this set.
     * @param dataTypeName type of data to be stored
     */
    public TwoPointsDataSet(String dataTypeName){
        setDataTypeName(dataTypeName);
        scaledResults1 = new ArrayList<>();
        scaledResults2 = new ArrayList<>();
        numberOfDataPointsInEachSet = 2;
    }

    protected ArrayList<Integer> scaledResults1;
    protected ArrayList<Integer> scaledResults2;

    public abstract ArrayList<Integer> getScaledResults1();
    public abstract ArrayList<Integer> getScaledResults2();



}
