package com.example.workinprogress;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ShortPortrait extends AppCompatActivity implements SensorEventListener, LocationListener {

    private SensorManager sensorManager;
    private LocationManager locationManager;

    private Sensor light;
    private Sensor temp;
    private Sensor accelerometer;
    private Location location;

    private boolean GPS;
    private boolean networkEnabled;
    private double latitude;
    private double longitude;


    public TextView lightText;
    public TextView tempText;
    public TextView locationText;
    private TextView accelerometerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_portrait);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        lightText = findViewById(R.id.lightValues);
        tempText = findViewById(R.id.temperatureValues);
        locationText = findViewById(R.id.locationValues);
        accelerometerText = findViewById(R.id.accelerometerValues);

        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        temp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);



    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, temp, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        getLocation();
    }


    public void start(View view){
        view.setPressed(true);
    }


    public void stop(View view) {

        String location = ((TextView) findViewById(R.id.locationValues)).getText().toString();
        String light = ((TextView) findViewById(R.id.lightValues)).getText().toString();
        String temp = ((TextView) findViewById(R.id.temperatureValues)).getText().toString();

        Intent intent = new Intent(this, DisplayImage.class);
        intent.putExtra("temp", temp);
        intent.putExtra("light", light);
        intent.putExtra("location", location);
        startActivity(intent);

    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); // check if it is network enabled
            GPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); //check if it is GPS enabled

            if (!GPS && !networkEnabled) {
                // can't connect, need to think about what to do here
            } else {

            }
            if (!GPS) {
                location = null;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);

                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }

            if (GPS) {
                location = null;

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }


        } catch (
                Exception e) {
            e.printStackTrace();
        }

        String locationTextString = "latitude: " + latitude + " Longitude: " + longitude;
        locationText.setText(locationTextString);

        return location;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor == light) {
            lightText.setText(String.valueOf(sensorEvent.values[0]));
        } else if (sensorEvent.sensor == temp) {
            tempText.setText(String.valueOf(sensorEvent.values[0]));
        } else if (sensorEvent.sensor == accelerometer){
            String accelerometerTextStringX = " x: "+sensorEvent.values[0];
            String accelerometerTextStringY = " y: "+sensorEvent.values[1];
            String accelerometerTextStringZ = " z: "+sensorEvent.values[2];

            String accelerometerTextString = accelerometerTextStringX+accelerometerTextStringY+accelerometerTextStringZ;
            accelerometerText.setText(accelerometerTextString);
            Log.i("accelerometer",accelerometerTextString);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {


    }

    @Override
    public void onLocationChanged(Location location) {
        getLocation();
    }



    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
