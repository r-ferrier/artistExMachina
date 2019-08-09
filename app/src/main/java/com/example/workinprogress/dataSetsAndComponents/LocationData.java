package com.example.workinprogress.dataSetsAndComponents;

import java.util.ArrayList;

/**
 * Location datasetpoints contain two datapoints and are not scaled, so they are simply stored as
 * they are.
 */
public class LocationData implements DataSetPoint {

    private ArrayList<Float> dataPoints = new ArrayList<>();
    private final int numberOfDataPoints;
    private String dataTypeName;

    /**
     * @param dataTypeName references the type of data this data point will be
     * @param dataPoint numeric datapoint to be stored
     * @param dataPoint2 numeric datapoint to be stored
     */
    public LocationData(String dataTypeName, double dataPoint, double dataPoint2){
        this.numberOfDataPoints = 2;
        this.dataTypeName = dataTypeName;
        dataPoints.add((float)dataPoint);
        dataPoints.add((float)dataPoint2);
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
    public ArrayList getScaledResults() {
        return null;
    }

    @Override
    public String toString() {
        return "long: "+dataPoints.get(0).toString()+" lat: "+dataPoints.get(1).toString();
    }
}
