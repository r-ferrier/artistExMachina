package com.example.workinprogress.dataSetsAndComponents;

import com.example.workinprogress.dataSetsAndComponents.DataSet;

import java.util.ArrayList;

public abstract class ThreePointsDataSet extends DataSet {

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
