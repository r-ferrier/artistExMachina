package com.example.workinprogress.dataSetsAndComponents;

import com.example.workinprogress.DisplayImage;

import java.util.ArrayList;

public class PositionData implements DataSetPoint {

    private final ArrayList<Float> dataPoints = new ArrayList<>();
    private final int numberOfDataPoints = 3;
    private String dataTypeName;

    private float max;
    private float min;

    public PositionData(String dataTypeName, Float dataPoint, Float dataPoint2, Float dataPoint3, float max, float min) {
        this.dataTypeName = dataTypeName;
        this.max = max;
        this.min = min;

        dataPoints.add(scaleResults(dataPoint));
        dataPoints.add(scaleResults(dataPoint2));
        dataPoints.add(scaleResults(dataPoint3));

        System.out.println(dataPoint + " = original value ---------"+dataPoints.get(0)+" = scaled value ------------");

    }


    @Override
    public String toString() {
        return "x: " + dataPoints.get(0).toString() + " y: " + dataPoints.get(1).toString() + " z: " + dataPoints.get(2).toString();
    }

    private float scaleResults(float f) {

        float newMax = max;
        float newMin = min;

        double d = (double)f;


        if (d < 0) {
            d *= -1;
//            f+= (float) 2/(f * Math.log(f));
//            f = (float)(20*(f/Math.log(f)));
//            d = 362.67*Math.log(d) - 772.38;
            d =(-0.0554*d*d) + (14.634*d) + 29.85;
//            d = Math.pow(10,(0.0027*d))*8.6476;
//            f = (float)((-0.0577*f*f) + (15.073*f) + 12.524);
//            f =(float)((0.0007*(f*5)) - (0.0223*(f*4)) + (0.2899*(f*3)) - (1.909*(f*2)) + (6.5849*(f)) + 0.0128);

//            f = (float)(f* Math.log(f));

            d *= -1;
        } else {
            d =(-0.0554*d*d) + (14.634*d) + 29.85;
//            d = 362.67*Math.log(d) - 772.38;
//            d = Math.pow(10,(0.0027*d))*8.6476;
//            f = (float)((-0.0577*f*f) + (15.073*f) + 12.524);
//            f = (float)(20*(f/Math.log(f)));
//            f+= (float) 2/(f * Math.log(f));
//            f =(float)((0.0007*(f*5)) - (0.0223*(f*4)) + (0.2899*(f*3)) - (1.909*(f*2)) + (6.5849*(f)) + 0.0128);

        }

        newMax = (float)((-0.0554*newMax*newMax) + (14.634*newMax) + 29.85);
        newMin = (newMax*-1)+1;
        float range = newMax - newMin;

        float scalar = DisplayImage.IMAGE_SIZE_SCALAR / range;

        d += newMax;
        d *= scalar;


        System.out.println("max range res" +
                "caled: "+max+" ****************************");
        System.out.println("min range rescaled: "+min+" ****************************");
        System.out.println("total range rescaled: "+range+" ****************************");
        System.out.println("scalar rescaled: "+scalar+" ****************************");

        return (float)d;
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

    @Override
    public ArrayList getScaledResults() {
        return null;
    }
}
