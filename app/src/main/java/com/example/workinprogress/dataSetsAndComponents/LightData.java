package com.example.workinprogress.dataSetsAndComponents;

import com.example.workinprogress.dataSetsAndComponents.DataSetPoint;

import java.util.ArrayList;

public class LightData implements DataSetPoint {

    private ArrayList<Float> dataPoints = new ArrayList<>();
    private final int numberOfDataPoints;
    private String dataTypeName;

    public LightData (String dataTypeName, Float dataPoint){
        this.numberOfDataPoints = 1;
        this.dataTypeName = dataTypeName;
        dataPoints.add(dataPoint);
        System.out.println("lightdata object"+dataPoint+"-----------------------------------");
    }

    @Override
    public String toString() {
        return dataPoints.get(0).toString();
    }

    @Override
    public int getNumberOfDataPointsInSet() {
        return numberOfDataPoints;
    }

    @Override
    public String getDataTypeName() {
        return dataTypeName;
    }

    @Override
    public ArrayList getResults() {
        return dataPoints;
    }
}
