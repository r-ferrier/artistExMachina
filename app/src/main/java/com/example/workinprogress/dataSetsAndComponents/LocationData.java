package com.example.workinprogress.dataSetsAndComponents;

import java.util.ArrayList;

public class LocationData implements DataSetPoint {

    private ArrayList<Float> dataPoints = new ArrayList<>();
    private final int numberOfDataPoints;
    private String dataTypeName;

    public LocationData(String dataTypeName, double dataPoint, double dataPoint2){
        this.numberOfDataPoints = 2;
        this.dataTypeName = dataTypeName;
        dataPoints.add((float)dataPoint);
        dataPoints.add((float)dataPoint2);
        System.out.println("locationData object"+dataPoint+" "+dataPoint2+"-----------------------------------");
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

    @Override
    public String toString() {
        return "long: "+dataPoints.get(0).toString()+" lat: "+dataPoints.get(1).toString();
    }
}
