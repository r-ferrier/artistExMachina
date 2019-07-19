package com.example.workinprogress.dataSetsAndComponents;

import java.util.ArrayList;

public abstract class SingularPointDataSet extends DataSet {

    public SingularPointDataSet(String dataTypeName){
        setDataTypeName(dataTypeName);
        scaledResults1 = new ArrayList<>();
        numberOfDataPointsInEachSet = 1;
    }

    protected ArrayList<Integer> scaledResults1;

    public abstract ArrayList<Integer> getScaledResults1();

}
