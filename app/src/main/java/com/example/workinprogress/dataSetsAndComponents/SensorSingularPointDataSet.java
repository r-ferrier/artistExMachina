package com.example.workinprogress.dataSetsAndComponents;

import android.hardware.Sensor;
import com.example.workinprogress.DisplayImage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class SensorSingularPointDataSet extends SingularPointDataSet {


    public SensorSingularPointDataSet(String dataTypeName, Sensor sensor){
        super(dataTypeName);
        max = sensor.getMaximumRange();
       min = 0;

    }

    @Override
    public ArrayList<Integer> getScaledResults1() {
        System.out.println("light "+ scaledResults1);
        return scaledResults1;
    }

    @Override
    public void setScaledResults() {

        scaledResults1 = new ArrayList<>();

            for (DataSetPoint dataSetPoint : getResults()) {
                scaledResults1.add(Math.round((float)dataSetPoint.getResults().get(0)));
            }

    }



}
