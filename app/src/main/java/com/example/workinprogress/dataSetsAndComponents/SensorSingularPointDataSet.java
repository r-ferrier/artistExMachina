package com.example.workinprogress.dataSetsAndComponents;

import android.hardware.Sensor;
import android.util.Log;

import java.util.ArrayList;

public class SensorSingularPointDataSet extends SingularPointDataSet {

    private String TAG = "light sensor information";

    /**
     * constructor sets range and data type name and will set up arraylists in the super constructor.
     * @param dataTypeName  references the type of data this data point will be
     * @param sensor the sensor used to collect the data. Required to discover the maximum potential
     *               value for this data range.
     */
    public SensorSingularPointDataSet(String dataTypeName, Sensor sensor) {

        super(dataTypeName);
        max = sensor.getMaximumRange();
        min = 0;

        Log.i(TAG,"max range: "+max);
        Log.i(TAG, "min range: "+min);
    }

    @Override
    public ArrayList<Integer> getScaledResults1() {
        return scaledResults1;
    }

    /**
     * scaling applied to individual datapoints so final scaling requires only rounding
     */
    @Override
    public void setScaledResults() {
        scaledResults1 = new ArrayList<>();
        for (DataSetPoint dataSetPoint : getResults()) {
            scaledResults1.add(Math.round((float) dataSetPoint.getScaledResults().get(0)));
        }

        for (int i = 0; i< scaledResults1.size(); i++){
            Log.i(TAG, "scaled result "+i+": "+scaledResults1.get(i)+"\n");
            Log.i(TAG, "unscaled result "+i+": "+getResults().get(i)+"\n");
        }
    }

    @Override
    public String getAverageString(){

        float total = 0;
        int count = 0;

        for (DataSetPoint dataSetPoint:getResults()){
            count++;
            total+= (Float)dataSetPoint.getResults().get(0);
        }
        return ""+total/count;
    }
}
