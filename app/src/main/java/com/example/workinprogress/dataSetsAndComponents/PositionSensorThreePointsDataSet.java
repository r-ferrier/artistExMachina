package com.example.workinprogress.dataSetsAndComponents;

import android.hardware.Sensor;
import android.util.Log;

import java.util.ArrayList;

public class PositionSensorThreePointsDataSet extends ThreePointsDataSet {

    private String TAG = "PositionSensorThreePointsDataSet";

    /**
     * constructor sets range and data type name and will set up arraylists in the super constructor.
     *
     * @param dataTypeName references the type of data this data point will be
     * @param sensor       the sensor used to collect the data. Required to discover the maximum/minimum
     *                     potential values for this data range.
     */
    public PositionSensorThreePointsDataSet(String dataTypeName, Sensor sensor) {
        super(dataTypeName);
        max = sensor.getMaximumRange();
        min = max + 1;
        min *= -1;

        Log.i(TAG,"max range: "+max);
        Log.i(TAG, "min range: "+min);
    }

    @Override
    public ArrayList<Integer> getScaledResults1() {
        return scaledResults1;
    }

    @Override
    public ArrayList<Integer> getScaledResults2() {
        return scaledResults2;
    }

    @Override
    public ArrayList<Integer> getScaledResults3() {
        return scaledResults3;
    }

    /**
     * Applies final scaling (in this instance, just rounding) to each datapoint to save them as
     * integers.
     */
    @Override
    public void setScaledResults() {

        if(getResults().size()<1){
            Log.e(TAG,"No "+getDataTypeName()+" data collected");
            addResult(new PositionData(getDataTypeName(),0f,0f,0f,getMax(),getMin()));
        }

        for (DataSetPoint dataSetPoint : getResults()) {
            scaledResults1.add(applyScaling((float) dataSetPoint.getScaledResults().get(0)));
            scaledResults2.add(applyScaling((float) dataSetPoint.getScaledResults().get(1)));
            scaledResults3.add(applyScaling((float) dataSetPoint.getScaledResults().get(2)));
        }

        for (int i = 0; i < scaledResults1.size(); i++) {
            Log.i(TAG, "scaled results " + i + ": x:" + scaledResults1.get(i) + " y:" + scaledResults2.get(i) + " z:" + scaledResults3.get(i) + "\n");
            if (i < getResults().size()) {
                Log.i(TAG, "unscaled results " + i + ": x:" + getResults().get(i).getResults().get(0) + " y:" + getResults().get(i).getResults().get(1) + " z:" + getResults().get(i).getResults().get(2) + "\n");
            }
        }

    }

    private Integer applyScaling(float dataSetPoint) {
        return Math.round(dataSetPoint);
    }

    public String getAverageString() {

        float totalX = 0;
        float totalY = 0;
        float totalZ = 0;

        int count = 0;

        for (DataSetPoint dataSetPoint : getResults()) {
            count++;
            float x = (Float) dataSetPoint.getResults().get(0);
            float y = (Float) dataSetPoint.getResults().get(1);
            float z = (Float) dataSetPoint.getResults().get(2);
            if (x < 0) {
                totalX -= x;
            } else {
                totalX += x;
            }
            if (y < 0) {
                totalY -= y;
            } else {
                totalY += y;
            }
            if (z < 0) {
                totalZ -= z;
            } else {
                totalZ += z;
            }
        }
        return " x: " + totalX / count + " y: " + totalY / count + " z: " + totalZ / count;
    }
}
