package com.example.workinprogress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ShortPortrait extends AppCompatActivity implements SensorEventListener, LocationListener {

    private SensorManager sensorManager;

    private Sensor light;
    private Sensor temp;
    private Sensor accelerometer;
    private boolean recordingData = false;

    public TextView lightText;
    //    public TextView tempText;
    public TextView locationText;
    private TextView accelerometerText;

    private ArrayList<Float> lightLevels;
    private ArrayList<double[]> locations;
    private ArrayList<Position> positions;

    private LocationManager locationManager;
    private android.location.Location location;
    private LocationListener locationListener;

    private boolean GPSExists;
    private boolean networkIsEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_portrait);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assignSensorTextAndManagers();

        lightLevels = new ArrayList<>();
        locations = new ArrayList<>();
        positions = new ArrayList<>();
    }

    @SuppressLint("MissingPermission")
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, temp, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000,
                10, this);
    }

    public void assignSensorTextAndManagers() {
        lightText = findViewById(R.id.lightValues);
//        tempText = findViewById(R.id.temperatureValues);
        locationText = findViewById(R.id.locationValues);
        accelerometerText = findViewById(R.id.accelerometerValues);

        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        temp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }


    public void startStopButtonPressed(View view) {

        if (!recordingData) {
            recordingData = true;
            ((Button) view).setText(R.string.stop);
            locations.add(new double[]{getLocation().getLatitude(), getLocation().getLongitude()});
            ((TextView)findViewById(R.id.lightValues)).setTextColor(Color.CYAN);
            ((TextView)findViewById(R.id.temperatureValues)).setTextColor(Color.CYAN);
            ((TextView)findViewById(R.id.locationValues)).setTextColor(Color.CYAN);
            ((TextView)findViewById(R.id.accelerometerValues)).setTextColor(Color.CYAN);
        } else {
            stop(view);
        }

    }


    public void stop(View view) {

        Intent intent = new Intent(this, DisplayImage.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("lightLevels", lightLevels);
        bundle.putSerializable("locations", locations);
        bundle.putSerializable("positions", positions);
        intent.putExtra("createImage",true);

        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor == light) {
            lightText.setText(String.valueOf(sensorEvent.values[0]));
            if (recordingData) {
                lightLevels.add(sensorEvent.values[0]);
            }
//        } else if (sensorEvent.sensor == temp) {
//            tempText.setText(String.valueOf(sensorEvent.values[0]));
        } else if (sensorEvent.sensor == accelerometer) {

            Position position = new Position(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
            String accelerometerTextString = " x: " + position.getxAxisString() + " y: " + position.getyAxisString() + " z: " + position.getzAxisString();
            accelerometerText.setText(accelerometerTextString);

            if (recordingData) {
                positions.add(position);
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    // is this necessary? MAY JUST BE ABLE TO USE LOCATION INFO SENT TO LISTENER ONLOCATIONCHANGE
    @Override
    public void onLocationChanged(android.location.Location location) {
//        double[] locationCoordinates = getLocation();
        String locationTextString = "lat: " + location.getLatitude() + " long: " + location.getLongitude();
        Log.i("location", locationTextString);
        locationText.setText(locationTextString);
        if (recordingData) {
            locations.add(new double[]{location.getLatitude(), location.getLongitude()});
        }
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

    @SuppressLint("MissingPermission")
    public android.location.Location getLocation() {

        try {
            networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); // check if it is network enabled
            GPSExists = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); //check if it is GPS enabled

            if (!GPSExists && !networkIsEnabled) {
                return null;
            } else if (!GPSExists) {
                location = null;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);

                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        return location;
                    }
                }
            } else {
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);

                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        return location;
                    }
                }
            }


        } catch (
                Exception e) {
            e.printStackTrace();
        }

        return location;

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
        finish();

    }


}
