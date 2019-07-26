package com.example.workinprogress.dataSetsAndComponents;

import android.hardware.Sensor;
import com.example.workinprogress.DisplayImage;
import java.util.ArrayList;

public class PositionSensorThreePointsDataSet extends ThreePointsDataSet {

    private ArrayList<Float> temporaryValuesStore;
    private ArrayList<Float> temporaryValuesStore2;
    private ArrayList<Float> temporaryValuesStore3;

    ArrayList<Float> xNegRange = new ArrayList<>();
    ArrayList<Float> xPosRange = new ArrayList<>();
    ArrayList<Float> yNegRange = new ArrayList<>();
    ArrayList<Float> yPosRange = new ArrayList<>();
    ArrayList<Float> zNegRange = new ArrayList<>();
    ArrayList<Float> zPosRange = new ArrayList<>();

    public PositionSensorThreePointsDataSet(String dataTypeName, Sensor sensor){
        super(dataTypeName);

        max = sensor.getMaximumRange();
        min = max+1;

//        max+= (float) 10/(max * Math.log(max));
//        min = max+1;
        min *= -1;

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


        for (DataSetPoint dataSetPoint : getResults()) {

            scaledResults1.add(applyScaling((float) dataSetPoint.getScaledResults().get(0)));
            scaledResults2.add(applyScaling((float) dataSetPoint.getScaledResults().get(1)));
            scaledResults3.add(applyScaling((float) dataSetPoint.getScaledResults().get(2)));


//
//        System.out.println("x axis maximum = "+xmax+" --------------------------------------------------");
//        System.out.println("x axis minimum = "+xmin+" --------------------------------------------------");
//        System.out.println("y axis maximum = "+ymax+" --------------------------------------------------");
//        System.out.println("y axis minimum = "+ymin+" --------------------------------------------------");
//        System.out.println("z axis maximum = "+zmax+" --------------------------------------------------");
//        System.out.println("z axis minimum = "+zmin+" --------------------------------------------------");


        }

        int max = 0;

        for(Integer integer: scaledResults1){
            if(integer>max){
                max = integer;
            }
        }
        for(Integer integer: scaledResults2){
            if(integer>max){
                max = integer;
            }
        }
        for(Integer integer: scaledResults3){
            if(integer>max){
                max = integer;
            }
        }

        float unscaledMax = 0;


        for (DataSetPoint dataSetPoint : getResults()) {

            if((float)dataSetPoint.getResults().get(0)>unscaledMax){
                unscaledMax = (float)dataSetPoint.getResults().get(0);
            }

            if((float)dataSetPoint.getResults().get(1)>unscaledMax){
                unscaledMax = (float)dataSetPoint.getResults().get(1);
            }

            if((float)dataSetPoint.getResults().get(2)>unscaledMax){
                unscaledMax = (float)dataSetPoint.getResults().get(2);
            }

        }


        System.out.println("MAXIMUM SCALED LIGHT VALUE "+max+"-----------------------------------");

        System.out.println("MAXIMUM UNSCALED LIGHT VALUE "+unscaledMax+"-----------------------------------");

        System.out.println(scaledResults1+"\nyuguygytyryrddresreyuguygytyryrddresreyuguygytyryrddresre");
        System.out.println(scaledResults2+"\nyuguygytyryrddresreyuguygytyryrddresreyuguygytyryrddresre");
        System.out.println(scaledResults3+"\nyuguygytyryrddresreyuguygytyryrddresreyuguygytyryrddresre");
    }





    private Integer applyScaling(float dataSetPoint){

        return Math.round(dataSetPoint);
    }
}
