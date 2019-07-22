package com.example.workinprogress.dataSetsAndComponents;

import com.example.workinprogress.DisplayImage;

import java.util.ArrayList;
import java.util.Random;

public class LightData implements DataSetPoint {

    private ArrayList<Float> dataPoints = new ArrayList<>();
    private final int numberOfDataPoints;
    private String dataTypeName;
    private boolean nightMode;

    private float max;
    private float min;

    public LightData (String dataTypeName, Float dataPoint, float max, float min, boolean nightMode){

        this.numberOfDataPoints = 1;
        this.dataTypeName = dataTypeName;
        this.max = max;
        this.min = min;
        this.nightMode = nightMode;
        Random r = new Random();
//        dataPoints.add((float)r.nextInt(500)+500);
        dataPoints.add(scale(dataPoint));
        System.out.println("lightdata object"+dataPoint+"-----------------------------------");
    }

    private Float scale(Float datapoint){
        float range = max - min;
        float scalar = DisplayImage.IMAGE_SIZE_SCALAR / range;

        if(nightMode){
            datapoint = (float) (100 * (datapoint / Math.log(datapoint)));
        }else {
            datapoint = (float) (20 * (datapoint / Math.log(datapoint)));
            datapoint *= (float) (Math.log(datapoint));
            datapoint *= scalar;
        }


        return datapoint;
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
