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

import com.example.workinprogress.dataSetsAndComponents.DataSet;
import com.example.workinprogress.dataSetsAndComponents.LightData;
import com.example.workinprogress.dataSetsAndComponents.LocationData;
import com.example.workinprogress.dataSetsAndComponents.LocationTwoPointsDataSet;
import com.example.workinprogress.dataSetsAndComponents.PositionData;
import com.example.workinprogress.dataSetsAndComponents.SensorSingularPointDataSet;
import com.example.workinprogress.dataSetsAndComponents.UnscaledSingleEntryDataSet;

import java.util.ArrayList;

public class ShortPortrait extends AppCompatActivity implements SensorEventListener, LocationListener {

    private SensorManager sensorManager;

    private Sensor lightSensor;
    private Sensor positionSensor;
    private boolean recordingData = false;

    public TextView lightText;
    public TextView locationText;
    private TextView accelerometerText;
    private TextView distanceText;
    private TextView stepsText;

    private ArrayList<DataSet> dataSets = new ArrayList<>();

    private LocationManager locationManager;
    private android.location.Location location;

    private boolean GPSExists;
    private boolean networkIsEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_portrait);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assignSensorTextAndManagers();

        if (getIntent().getExtras() != null) {

            dataSets.add((UnscaledSingleEntryDataSet)getIntent().getSerializableExtra(getString(R.string.data_type_steps)));
            dataSets.add((UnscaledSingleEntryDataSet)getIntent().getSerializableExtra(getString(R.string.data_type_distance)));
        }

        dataSets.add(new SensorSingularPointDataSet(getString(R.string.data_type_light), lightSensor));
        dataSets.add(new SensorSingularPointDataSet(getString(R.string.data_type_position), positionSensor));
        dataSets.add(new LocationTwoPointsDataSet(getString(R.string.data_type_location)));

        updateStaticValues();

    }

    @SuppressLint("MissingPermission")
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, positionSensor, SensorManager.SENSOR_DELAY_NORMAL);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000,
                10, this);
    }

    private void assignSensorTextAndManagers() {

        lightText = findViewById(R.id.lightValues);
        locationText = findViewById(R.id.locationValues);
        accelerometerText = findViewById(R.id.accelerometerValues);
        stepsText = findViewById(R.id.stepsValues);
        distanceText = findViewById(R.id.distanceValues);

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        positionSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private void updateStaticValues(){

        for(DataSet dataset: dataSets){
            if(dataset.getDataTypeName()==getString(R.string.data_type_steps)){
                stepsText.setText(dataset.toString());
            }
            if(dataset.getDataTypeName()==getString(R.string.data_type_distance)){
                distanceText.setText(dataset.toString());
            }
        }
    }


    public void startStopButtonPressed(View view) {

        if (!recordingData) {
            recordingData = true;
            ((Button) view).setText(R.string.stop);

            for(DataSet dataSet: dataSets){
                if(dataSet.getDataTypeName()==getString(R.string.data_type_location)){
                    dataSet.addResult(new LocationData(getString(R.string.data_type_location),0,0));
                }
            }

            ((TextView)findViewById(R.id.lightValues)).setTextColor(Color.CYAN);
            ((TextView)findViewById(R.id.locationValues)).setTextColor(Color.CYAN);
            ((TextView)findViewById(R.id.accelerometerValues)).setTextColor(Color.CYAN);
        } else {
            stop(view);
        }

    }


    public void stop(View view) {

        sensorManager.unregisterListener(this);

        for(DataSet dataSet:dataSets){
            dataSet.setScaledResults();
        }

        Intent intent = new Intent(this, DisplayImage.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.data_sets),dataSets);
        intent.putExtra("createImage",true);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor == lightSensor) {
            lightText.setText(String.valueOf(sensorEvent.values[0]));
            if (recordingData) {
                for(DataSet dataSet: dataSets){
                    if(dataSet.getDataTypeName()==getString(R.string.data_type_light)){
                        dataSet.addResult(new LightData(getString(R.string.data_type_light),sensorEvent.values[0]));
                    }
                }
            }
        } else if (sensorEvent.sensor == positionSensor) {

            String accelerometerTextString = " x: " + sensorEvent.values[0] + " y: " + sensorEvent.values[1] + " z: " + sensorEvent.values[2];

            accelerometerText.setText(accelerometerTextString);

            if (recordingData) {

                for(DataSet dataSet: dataSets){
                    if(dataSet.getDataTypeName()==getString(R.string.data_type_position)){
                        dataSet.addResult(new PositionData(getString(R.string.data_type_position),sensorEvent.values[0],sensorEvent.values[1],sensorEvent.values[2]));
                    }
                }
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    // is this necessary? MAY JUST BE ABLE TO USE LOCATION INFO SENT TO LISTENER ONLOCATIONCHANGE
    @Override
    public void onLocationChanged(android.location.Location location) {

//        location = getLocation();
        String locationTextString = "lat: " + location.getLatitude() + " long: " + location.getLongitude();
        Log.i("location", locationTextString);
        locationText.setText(locationTextString);
        if (recordingData) {

            for(DataSet dataSet: dataSets){
                if(dataSet.getDataTypeName()==getString(R.string.data_type_location)){
                    dataSet.addResult(new LocationData(getString(R.string.data_type_location),location.getLatitude(),location.getLongitude()));
                }
            }
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




}
