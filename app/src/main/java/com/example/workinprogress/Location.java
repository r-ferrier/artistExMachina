package com.example.workinprogress;

import java.io.Serializable;
import java.util.ArrayList;

public class Location implements ResultValuesAppendable, Serializable {

    private final double latitude;
    private final double longitude;

    private final int maxValueLongitude = 180;
    private final int maxValueLatitude = 90;

    private final int minValueLongitude = -180;
    private final int minValueLatitude = -90;

    private ArrayList<Integer> scaledResults;

    public Location (double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

    }


    @Override
    public void setScaledResults() {

        double longitudeRange = maxValueLongitude - minValueLongitude;
        double latitudeRange = maxValueLatitude - minValueLatitude;

        double longitudeScalar = 100.0 / longitudeRange;
        double latitudeScalar = 100.0 / latitudeRange;

        scaledResults = new ArrayList<>();
        scaledResults.add((int)Math.round(longitude * longitudeScalar));
        scaledResults.add((int)Math.round(latitude * latitudeScalar));

    }


    @Override
    public ArrayList<Integer> getScaledResults() {
        return scaledResults;
    }
}
