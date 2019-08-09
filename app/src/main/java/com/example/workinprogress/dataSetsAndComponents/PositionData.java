package com.example.workinprogress.dataSetsAndComponents;

import com.example.workinprogress.DisplayImage;

import java.util.ArrayList;

public class PositionData implements DataSetPoint {

    private final ArrayList<Float> scaledDataPoints = new ArrayList<>();
    private final ArrayList<Float> unscaledDataPoints = new ArrayList<>();
    private final int numberOfDataPoints = 3;
    private String dataTypeName;

    private float max;
    private float min;

    /**
     * constructor will store all of the parameters, and then scale the datapoints and add them
     * to a scaled dataPoints arraylist.
     *
     * @param dataTypeName references the type of data this data point will be
     * @param dataPoint numeric datapoint to be stored
     * @param dataPoint2 numeric datapoint to be stored
     * @param dataPoint3 numeric datapoint to be stored
     * @param max the maximum possible value this datapoint could have
     * @param min the minimum possible value this datapoint could have
     */
    public PositionData(String dataTypeName,
                        Float dataPoint, Float dataPoint2, Float dataPoint3, float max, float min) {
        this.dataTypeName = dataTypeName;
        this.max = max;
        this.min = min;

        unscaledDataPoints.add(dataPoint);
        unscaledDataPoints.add(dataPoint2);
        unscaledDataPoints.add(dataPoint3);

        scaledDataPoints.add(scaleResults(dataPoint));
        scaledDataPoints.add(scaleResults(dataPoint2));
        scaledDataPoints.add(scaleResults(dataPoint3));
    }


    @Override
    public String toString() {
        return "x: " + unscaledDataPoints.get(0).toString() + " y: " +
                unscaledDataPoints.get(1).toString() + " z: " + unscaledDataPoints.get(2).toString();
    }

    /**
     * Scaling the position data requires the values to be heavily redistributed so that the values
     * at the lower end which the phone is very sensitive to register as proportionally higher than
     * the values towards the higher end, which the phone requires very aggressive movements to
     * register at all.
     * @param f result to be scaled
     * @return scaled datapoint
     */
    private float scaleResults(float f) {

        float newMax = max;
        float newMin;

        double d = (double)f;

        if (d < 0) {
            d *= -1;
            d =(-0.0554*d*d) + (14.634*d) + 29.85;
            d *= -1;
        } else {
            d =(-0.0554*d*d) + (14.634*d) + 29.85;
        }

        newMax = (float)((-0.0554*newMax*newMax) + (14.634*newMax) + 29.85);
        newMin = (newMax*-1)+1;
        float range = newMax - newMin;

        float scalar = DisplayImage.IMAGE_SIZE_SCALAR / range;

        d += newMax;
        d *= scalar;

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
        return unscaledDataPoints;
    }

    @Override
    public ArrayList getScaledResults() {
        return scaledDataPoints;
    }
}
