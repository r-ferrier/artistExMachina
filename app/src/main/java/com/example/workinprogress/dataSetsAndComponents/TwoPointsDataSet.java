package com.example.workinprogress.dataSetsAndComponents;

import com.example.workinprogress.dataSetsAndComponents.DataSet;

import java.util.ArrayList;

public abstract class TwoPointsDataSet extends DataSet {

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
