package com.example.workinprogress;

import android.hardware.SensorEvent;

import java.io.Serializable;
import java.util.ArrayList;

public class Position implements Serializable, ResultValuesAppendable {

    private final float xAxis;
    private final float yAxis;
    private final float zAxis;

    private ArrayList<Integer> scaledResults;

    private String xAxisString;
    private String yAxisString;
    private String zAxisString;

    private final float highestValue;
    private final float lowestValue;


    public Position(SensorEvent sensorEvent){


        this.xAxis = sensorEvent.values[0];
        this.yAxis = sensorEvent.values[1];
        this.zAxis = sensorEvent.values[2];

        this.xAxisString = String.valueOf(xAxis);
        this.yAxisString = String.valueOf(yAxis);
        this.zAxisString = String.valueOf(zAxis);

        highestValue = sensorEvent.sensor.getMaximumRange();
        lowestValue = highestValue*-1;

    }


    public float getxAxis() {
        return xAxis;
    }

    public float getyAxis() {
        return yAxis;
    }

    public float getzAxis() {
        return zAxis;
    }

    public String getxAxisString() {
        return xAxisString;
    }

    public String getyAxisString() {
        return yAxisString;
    }

    public String getzAxisString() {
        return zAxisString;
    }

    @Override
    public void setScaledResults() {

        float range = highestValue - lowestValue;
        float scalar = 100 / range;

        scaledResults = new ArrayList<>();

        scaledResults.add(Math.round(xAxis * scalar));
        scaledResults.add(Math.round(yAxis * scalar));
        scaledResults.add(Math.round(zAxis * scalar));

        this.xAxisString = String.valueOf(xAxis);
        this.yAxisString = String.valueOf(yAxis);
        this.zAxisString = String.valueOf(zAxis);
    }

    @Override
    public ArrayList<Integer> getScaledResults() {
        return scaledResults;
    }

}
