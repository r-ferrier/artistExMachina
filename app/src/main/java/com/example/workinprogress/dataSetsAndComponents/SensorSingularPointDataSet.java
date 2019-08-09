package com.example.workinprogress.dataSetsAndComponents;

import android.hardware.Sensor;

import java.util.ArrayList;

public class SensorSingularPointDataSet extends SingularPointDataSet {

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
    }
}
