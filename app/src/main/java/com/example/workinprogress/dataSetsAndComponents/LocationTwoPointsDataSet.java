package com.example.workinprogress.dataSetsAndComponents;

import com.example.workinprogress.DisplayImage;

import java.util.ArrayList;

/**
 * Extends the TwoPointsDataSet and has maximum and minimum values preset. The results are not scaled
 * at the datapoint, but final scaling is applied to ensure the range is within the bounds of the image.
 */
public class LocationTwoPointsDataSet extends TwoPointsDataSet {

    private final int maxValueLongitude = 180;
    private final int maxValueLatitude = 90;

    private final int minValueLongitude = -180;
    private final int minValueLatitude = -90;

    public LocationTwoPointsDataSet(String dataTypeName){
        super(dataTypeName);
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
    public void setScaledResults() {
        scaledResults1 = new ArrayList<>();
        scaledResults2 = new ArrayList<>();

        for (DataSetPoint dataSetPoint : getResults()) {
            scaledResults1.add(applyScaling((float)dataSetPoint.getResults().get(0),true));
            scaledResults2.add(applyScaling((float)dataSetPoint.getResults().get(1),false));
        }

    }

    private Integer applyScaling(float dataSetPoint,boolean lat){

        float longRange = maxValueLongitude-minValueLongitude;
        float latRange = maxValueLatitude-minValueLatitude;
        float scalar;
        if(lat){
            scalar = DisplayImage.IMAGE_SIZE_SCALAR/latRange;
        }else{
            scalar = DisplayImage.IMAGE_SIZE_SCALAR/longRange;
        }
        return (int)(dataSetPoint * scalar);
    }
}
