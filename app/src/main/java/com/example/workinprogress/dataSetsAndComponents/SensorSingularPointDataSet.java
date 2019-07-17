package com.example.workinprogress.dataSetsAndComponents;

import android.hardware.Sensor;

import com.example.workinprogress.DisplayImage;
import com.example.workinprogress.dataSetsAndComponents.DataSetPoint;
import com.example.workinprogress.dataSetsAndComponents.SingularPointDataSet;

import java.util.ArrayList;

public class SensorSingularPointDataSet extends SingularPointDataSet {

    private float max;
    private float min;

    public SensorSingularPointDataSet(String dataTypeName, Sensor sensor){
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
    public void setScaledResults() {

        scaledResults1 = new ArrayList<>();

            for (DataSetPoint dataSetPoint : getResults()) {
                scaledResults1.add(applyScaling((float)dataSetPoint.getResults().get(0)));
            }

    }


    private Integer applyScaling(float dataSetPoint){

        float range = max - min;
        float scalar = DisplayImage.IMAGE_SIZE_SCALAR/range;
        return (int)(dataSetPoint * scalar);
    }
}
