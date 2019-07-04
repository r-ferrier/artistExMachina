package com.example.workinprogress;

import android.annotation.SuppressLint;
import android.location.LocationListener;
import android.location.LocationManager;

public class Location {

    private LocationManager locationManager;
    private android.location.Location location;
    private LocationListener locationListener;

    private boolean GPSExists;
    private boolean networkIsEnabled;
//    private double latitude;
//    private double longitude;


    public Location(LocationManager locationManager, LocationListener locationListener) {
        this.locationManager = locationManager;
        this.locationListener = locationListener;
    }


//    @SuppressLint("MissingPermission")
//    public double[] getLocation() {
//
//        double[] locationCooordinates = new double[]{0,0};
//
//        try {
//            networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); // check if it is network enabled
//            GPSExists = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); //check if it is GPS enabled
//
//            if (!GPSExists && !networkIsEnabled) {
//                // can't connect, need to think about what to do here
//            } else if (!GPSExists) {
//                location = null;
//                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, locationListener);
//
//                if (locationManager != null) {
//                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                    if (location != null) {
//                        locationCooordinates[0] = location.getLatitude();
//                        locationCooordinates[1] = location.getLongitude();
//                    }
//                }
//            } else {
//                location = null;
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, locationListener);
//
//                if (locationManager != null) {
//                    location = locationManager
//                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                    if (location != null) {
//                        locationCooordinates[0] = location.getLatitude();
//                        locationCooordinates[1] = location.getLongitude();
//                    }
//                }
//            }
//
//
//        } catch (
//                Exception e) {
//            e.printStackTrace();
//        }
//
//        return locationCooordinates;
//    }





}
