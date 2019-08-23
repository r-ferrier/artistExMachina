package com.example.workinprogress.dataSetsAndComponents;

import com.example.workinprogress.DisplayImage;

import java.util.ArrayList;
import java.util.Random;

public class LightData implements DataSetPoint {

    private ArrayList<Float> dataPoints = new ArrayList<>();
    private ArrayList<Float> scaledDataPoints = new ArrayList<>();
    private final int numberOfDataPoints;
    private String dataTypeName;
    private boolean nightMode;

    private float max;
    private float min;

    /**
     * LightData constructor will store all of the parameters and then scale the datapoint and add it
     * to a scaled dataPoints arraylist. Datapoints are stored in lists because other classes they must interact
     * with do not necessarily know how many individual datapoints are held for each datasetpoint.
     * @param dataTypeName references the type of data this data point will be
     * @param dataPoint the numeric datapoint to be stored
     * @param max the maximum possible value this datapoint could have
     * @param min the minimum possible value
     * @param nightMode boolean to state whether the data point is likely to have come from a darker
     *                  or lighter range
     */
    public LightData (String dataTypeName, Float dataPoint, float max, float min, boolean nightMode){

        this.numberOfDataPoints = 1;
        this.dataTypeName = dataTypeName;
        this.max = max;
        this.min = min;
        this.nightMode = nightMode;

        dataPoints.add(dataPoint);
        scaledDataPoints.add(scale(dataPoint));

    }

    /**
     * Scaling aims to redistribute values to increase the value of lower light values and squish up higher values.
     * @param datapoint actual value of data
     * @return scaled version of the original datapoint, scaled according to range available.
     */
    private Float scale(Float datapoint){

        float range = (float)(20 * (max / Math.log(max)));
//        range*=(float) (Math.log(range));
//        range*=(float) (Math.log(range));
        range-=min;
        float scalar = DisplayImage.IMAGE_SIZE_SCALAR / range;

        if(nightMode){
            datapoint = (float) (100 * (datapoint / Math.log(datapoint)));
            datapoint *= scalar*5;
        }else {
            datapoint = (float) (20 * (datapoint / Math.log(datapoint)));
            datapoint *= (float) (Math.log(datapoint));
//            datapoint *= (float) (Math.log(datapoint));
            datapoint *= scalar;
        }

        if(datapoint>1000){
            datapoint = 1000f;
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

    public ArrayList getScaledResults(){
        return scaledDataPoints;
    }
}
