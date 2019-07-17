package com.example.workinprogress.dataSetsAndComponents;

import android.hardware.Sensor;

import com.example.workinprogress.DisplayImage;
import com.example.workinprogress.dataSetsAndComponents.DataSetPoint;
import com.example.workinprogress.dataSetsAndComponents.ThreePointsDataSet;

import java.util.ArrayList;

public class SensorThreePointsDataSet extends ThreePointsDataSet {

    private float max;
    private float min;

    public SensorThreePointsDataSet(String dataTypeName, Sensor sensor){
        super(dataTypeName);

        max = sensor.getMaximumRange();
        if (sensor.getType()==Sensor.TYPE_LIGHT){
            min = 0;
        } else {
            min = max*-1;
        }

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

    @Override
    public void setScaledResults() {

        scaledResults1 = new ArrayList<>();
        scaledResults2 = new ArrayList<>();
        scaledResults3 = new ArrayList<>();

        for (DataSetPoint dataSetPoint : getResults()) {
            scaledResults1.add(applyScaling((float)dataSetPoint.getResults().get(0)));
            scaledResults2.add(applyScaling((float)dataSetPoint.getResults().get(1)));
            scaledResults3.add(applyScaling((float)dataSetPoint.getResults().get(2)));
        }


    }


    private Integer applyScaling(float dataSetPoint){

        float range = max - min;
        float scalar = DisplayImage.IMAGE_SIZE_SCALAR/range;
        return (int)(dataSetPoint * scalar);
    }
}
