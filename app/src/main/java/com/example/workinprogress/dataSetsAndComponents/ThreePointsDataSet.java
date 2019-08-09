package com.example.workinprogress.dataSetsAndComponents;

import java.util.ArrayList;

/**
 * Can be used to store any type of data set with three values for each point
 */
public abstract class ThreePointsDataSet extends DataSet {

    /**
     * constructor sets up arraylists for the datasetpoints to be stored in and declares that there will
     * be 3 values stored in each datasetpoint. This ensures only datasetpoints containing 3 values
     * can be added to this set.
     * @param dataTypeName type of data to be stored
     */
    public ThreePointsDataSet(String dataTypeName){
        setDataTypeName(dataTypeName);
        scaledResults1 = new ArrayList<>();
        scaledResults2 = new ArrayList<>();
        scaledResults3 = new ArrayList<>();
        numberOfDataPointsInEachSet = 3;
    }

    protected ArrayList<Integer> scaledResults1;
    protected ArrayList<Integer> scaledResults2;
    protected ArrayList<Integer> scaledResults3;

    public abstract ArrayList<Integer> getScaledResults1();
    public abstract ArrayList<Integer> getScaledResults2();
    public abstract ArrayList<Integer> getScaledResults3();

}
