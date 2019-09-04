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
import com.example.workinprogress.dataSetsAndComponents.PositionSensorThreePointsDataSet;

import java.util.ArrayList;

public class ShortPortrait extends AppCompatActivity implements SensorEventListener, LocationListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Sensor positionSensor;
    private boolean recordingData = false;
    private String imageType = "Albers Image";
    private String TAG = "ShortPortraitError";

    public TextView lightText;
    private TextView xText;
    private TextView yText;
    private TextView zText;
    private boolean nightMode = false;
    private ArrayList<DataSet> dataSets = new ArrayList<>();
    private LocationManager locationManager;
    private android.location.Location location;
    private boolean GPSExists;
    private boolean networkIsEnabled;

    /**
     * sets short portrait activity as the view and then sets up sensors and sensor managers.
     * Retrieves any aggregate data set information from the intent if existing, and adds new
     * datasets for information to be gathered while the app is open.
     * @param savedInstanceState instantiated by mainactivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_portrait);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assignSensorsAndSensorText();

        if (getIntent().getExtras() != null) {
            dataSets = (ArrayList<DataSet>)(getIntent().getSerializableExtra("dataSet_Array"));
        }

        SensorSingularPointDataSet lightData = new SensorSingularPointDataSet(getString(R.string.data_type_light), lightSensor);
        lightData.addResult(new LightData(getString(R.string.data_type_light),(float)1000,lightData.getMax(),lightData.getMin(),nightMode));

        DataSet locationData = new LocationTwoPointsDataSet(getString(R.string.data_type_location));
        locationData.addResult(new LocationData(getString(R.string.data_type_location),0,0));

        dataSets.add(lightData);
        dataSets.add(new PositionSensorThreePointsDataSet(getString(R.string.data_type_position), positionSensor));
        dataSets.add(locationData);

//        updateStaticValues();

    }

    /**
     * listeners and location will be registered
     */
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

    /**
     * helper method to insantiate textViews and sensors
     */
    private void assignSensorsAndSensorText() {

        lightText = findViewById(R.id.lightValues);
//        locationText = findViewById(R.id.locationValues);
        xText = findViewById(R.id.xValues);
        yText = findViewById(R.id.yValues);
        zText = findViewById(R.id.zValues);
//        stepsText = findViewById(R.id.stepsValues);
//        distanceText = findViewById(R.id.distanceValues);

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        positionSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        if(lightSensor==null||positionSensor==null){
            Log.e(TAG,"problem locating light or position sensor");
        }
    }

//    private void updateStaticValues(){
//
//        for(DataSet dataset: dataSets){
//
//            if(dataset.getDataTypeName().equals(getString(R.string.data_type_steps))){
//      //          stepsText.setText(dataset.toString());
//            }
//            if(dataset.getDataTypeName().equals(getString(R.string.data_type_distance))){
//     //           distanceText.setText(dataset.toString());
//            }
//        }
//    }


    /**
     * onclick method for the start/stop button. If the button is pressed and data is not currently being recorded,
     * it will start to be recorded and elements of the layout will change to reflect this. If it is being recorded,
     * it will call the stop method.
     * @param view start/stop button
     */
    public void startStopButtonPressed(View view) {

        if (!recordingData) {
            recordingData = true;

            ((Button) view).setText(R.string.stop);
            view.setBackground(getResources().getDrawable(R.drawable.white_button));
            ((Button) view).setTextColor(getResources().getColor(R.color.gradientMiddle));

            findViewById(R.id.linearLayout2).setBackground(getResources().getDrawable(R.drawable.elipse));


            for(DataSet dataSet: dataSets){
                if(dataSet.getDataTypeName()==getString(R.string.data_type_location)){
                    dataSet.addResult(new LocationData(getString(R.string.data_type_location),0,0));
                }
            }

            ((TextView)findViewById(R.id.lightValues)).setTextColor(Color.WHITE);
//            ((TextView)findViewById(R.id.locationValues)).setTextColor(Color.CYAN);
            ((TextView)findViewById(R.id.xValues)).setTextColor(Color.WHITE);
            ((TextView)findViewById(R.id.yValues)).setTextColor(Color.WHITE);
            ((TextView)findViewById(R.id.zValues)).setTextColor(Color.WHITE);
        } else {
            stop();
        }

    }


    /**
     * unregisters listener and begins the display image intent, sending it all of the recorded data
     */
    public void stop() {

        sensorManager.unregisterListener(this);

        for(DataSet dataSet:dataSets){
            dataSet.setScaledResults();
        }

        Intent intent = new Intent(this, DisplayImage.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.data_sets),dataSets);
        intent.putExtra("createImage",true);
        intent.putExtra("imageType",imageType);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    /**
     * checks to see which sensor triggered the event and updates the view. If data is being
     * recorded, adds result to the appropriate dataset.
     * @param sensorEvent triggered by sensorlistener
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor == lightSensor) {
            String light = getString(R.string.lightLevels)+"  "+String.valueOf(sensorEvent.values[0]);
            lightText.setText(light);
            if (recordingData) {
                for(DataSet dataSet: dataSets){
                    if(dataSet.getDataTypeName()==getString(R.string.data_type_light)){
                        dataSet.addResult(new LightData(getString(R.string.data_type_light),sensorEvent.values[0],dataSet.getMax(),dataSet.getMin(),nightMode));
                    }
                }
            }
        } else if (sensorEvent.sensor == positionSensor) {
            String xTextString = getString(R.string.positionX)+"  "+ sensorEvent.values[0];
            String yTextString = getString(R.string.positionY)+"  "+ sensorEvent.values[1];
            String zTextString = getString(R.string.positionZ)+"  "+ sensorEvent.values[2];
            xText.setText(xTextString);
            yText.setText(yTextString);
            zText.setText(zTextString);

            if (recordingData) {
                for(DataSet dataSet: dataSets){
                    if(dataSet.getDataTypeName()==getString(R.string.data_type_position)){
                        dataSet.addResult(new PositionData(getString(R.string.data_type_position),sensorEvent.values[0],sensorEvent.values[1],sensorEvent.values[2], dataSet.getMax(),dataSet.getMin()));
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //not used
    }

    /**
     * on location change, add a location to the list of locations if being recorded
     * @param location
     */
    @Override
    public void onLocationChanged(android.location.Location location) {
//        location = getLocation();
        String locationTextString = "lat: " + location.getLatitude() + " long: " + location.getLongitude();
//        Log.i("location", locationTextString);
//        locationText.setText(locationTextString);
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
