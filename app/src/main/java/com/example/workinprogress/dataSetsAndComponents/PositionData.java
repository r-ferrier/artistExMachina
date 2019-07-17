package com.example.workinprogress.dataSetsAndComponents;

import com.example.workinprogress.dataSetsAndComponents.DataSetPoint;

import java.util.ArrayList;

public class PositionData implements DataSetPoint {

    private final ArrayList<Float> dataPoints = new ArrayList<>();
    private final int numberOfDataPoints = 3;
    private String dataTypeName;

    public PositionData(String dataTypeName, Float dataPoint, Float dataPoint2, Float dataPoint3){
        this.dataTypeName = dataTypeName;
        dataPoints.add(dataPoint);
        dataPoints.add(dataPoint2);
        dataPoints.add(dataPoint3);
    }


    @Override
    public String toString() {
        return "x: "+dataPoints.get(0).toString()+" y: "+dataPoints.get(1).toString()+" z: "+dataPoints.get(2).toString();
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
