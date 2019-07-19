package com.example.workinprogress.dataSetsAndComponents;

import android.hardware.Sensor;
import com.example.workinprogress.DisplayImage;
import java.util.ArrayList;

public class PositionSensorThreePointsDataSet extends ThreePointsDataSet {

    private float max;
    private float min;

    public PositionSensorThreePointsDataSet(String dataTypeName, Sensor sensor){
        super(dataTypeName);

        max = sensor.getMaximumRange();
        min = max*-1;

        System.out.println("max range: "+max+" ****************************");

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

        float xmax = 0;
        float xmin = 1000;
        float ymax = 0;
        float ymin = 1000;
        float zmax = 0;
        float zmin = 1000;


        for (DataSetPoint dataSetPoint : getResults()) {
            scaledResults1.add(applyScaling((float)dataSetPoint.getResults().get(0)));
            scaledResults2.add(applyScaling((float)dataSetPoint.getResults().get(1)));
            scaledResults3.add(applyScaling((float)dataSetPoint.getResults().get(2)));


            if((float)dataSetPoint.getResults().get(0)>xmax){
                xmax = (float)dataSetPoint.getResults().get(0);
            }else if((float)dataSetPoint.getResults().get(0)<xmin){
                xmin = (float)dataSetPoint.getResults().get(0);
            }
            if((float)dataSetPoint.getResults().get(1)>ymax){
                ymax = (float)dataSetPoint.getResults().get(1);
            }else if((float)dataSetPoint.getResults().get(1)<ymin){
                ymin = (float)dataSetPoint.getResults().get(1);
            }
            if((float)dataSetPoint.getResults().get(2)>zmax){
                zmax = (float)dataSetPoint.getResults().get(2);
            }else if((float)dataSetPoint.getResults().get(2)<zmin){
                zmin = (float)dataSetPoint.getResults().get(2);
            }

        }

        System.out.println("x axis maximum = "+xmax+" --------------------------------------------------");
        System.out.println("x axis minimum = "+xmin+" --------------------------------------------------");
        System.out.println("y axis maximum = "+ymax+" --------------------------------------------------");
        System.out.println("y axis minimum = "+ymin+" --------------------------------------------------");
        System.out.println("z axis maximum = "+zmax+" --------------------------------------------------");
        System.out.println("z axis minimum = "+zmin+" --------------------------------------------------");


    }


    private Integer applyScaling(float dataSetPoint){



        float range = max - min;
        dataSetPoint-=min;

        float scalar = DisplayImage.IMAGE_SIZE_SCALAR/range;
        return (int)(dataSetPoint * scalar);
    }
}
