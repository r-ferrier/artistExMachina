package com.example.workinprogress;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Subscription;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.fitness.result.ListSubscriptionsResult;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private SensorManager sensorManager;
    private LocationManager locationManager;
    public TextView lightText;
    public TextView tempText;
    public TextView locationText;
    //    private List<Sensor> deviceSensors;
    private Sensor light;
    private Sensor temp;
    private Location location;
    private boolean GPS;
    private boolean networkEnabled;
    private double latitude;
    private double longitude;
    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
    private GoogleApiClient mApiClient;
    private final long[] timeBounds = new long[]{0, 20};
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions googleSignInOptions;
    private ResultCallback<Status> mSubscribeResultCallback;
    private ResultCallback<Status> mCancelSubscriptionResultCallback;
    private ResultCallback<ListSubscriptionsResult> mListSubscriptionsResultCallback;

    private GoogleApiClient mGoogleApiClient;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCallbacks();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API).addApi(Fitness.RECORDING_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE)).addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .addConnectionCallbacks(this)
                .enableAutoManage(this, 0, this)
                .build();

        new ViewWeekStepCountTask().execute();


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        lightText = findViewById(R.id.lightText);
        tempText = findViewById(R.id.temperatureText);
        locationText = findViewById(R.id.locationText);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        temp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }

    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, temp, SensorManager.SENSOR_DELAY_NORMAL);
        getLocation();
    }

    protected void onStart() {
        super.onStart();
    }

    private class ViewWeekStepCountTask extends AsyncTask<Void, Void, Void> {


        protected Void doInBackground(Void... params) {
//            displayLastWeeksData();
            displayYesterdaysData();
            return null;
        }


        private void displayYesterdaysData(){


            Calendar date = Calendar.getInstance();
// reset hour, minutes, seconds and millis
            date.set(Calendar.HOUR_OF_DAY, 0);
            date.set(Calendar.MINUTE, 0);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);

            Calendar day = Calendar.getInstance();
            day.setTime(new Date());
            long endTime = date.getTimeInMillis();
            date.add(Calendar.DAY_OF_WEEK,-1);
            long startTime = date.getTimeInMillis();

            DataReadRequest readRequest = new DataReadRequest.Builder()
                    .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                    .bucketByTime(1, TimeUnit.DAYS)
                    .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                    .build();


            DataReadResult dataReadResult = Fitness.HistoryApi.readData(mGoogleApiClient, readRequest).await(1, TimeUnit.MINUTES);


            if (dataReadResult.getBuckets().size() > 0) {
                Log.e("History", "Number of buckets: " + dataReadResult.getBuckets().size());
                for (Bucket bucket : dataReadResult.getBuckets()) {
                    List<DataSet> dataSets = bucket.getDataSets();
                    for (DataSet dataSet : dataSets) {
                        showDataSet(dataSet);
                    }
                }
            }
//Used for non-aggregated data
            else if (dataReadResult.getDataSets().size() > 0) {
                Log.e("History", "Number of returned DataSets: " + dataReadResult.getDataSets().size());
                for (DataSet dataSet : dataReadResult.getDataSets()) {
                    showDataSet(dataSet);
                }
            }

        }




        private void displayLastWeeksData() {
            Calendar cal = Calendar.getInstance();
            Date now = new Date();
            cal.setTime(now);
            long endTime = cal.getTimeInMillis();
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            long startTime = cal.getTimeInMillis();

            java.text.DateFormat dateFormat = DateFormat.getDateInstance();
            Log.e("History", "Range Start: " + dateFormat.format(startTime));
            Log.e("History", "Range End: " + dateFormat.format(endTime));

//Check how many steps were walked and recorded in the last 7 days
            DataReadRequest readRequest = new DataReadRequest.Builder()
                    .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                    .bucketByTime(1, TimeUnit.DAYS)
                    .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                    .build();

            DataReadResult dataReadResult = Fitness.HistoryApi.readData(mGoogleApiClient, readRequest).await(1, TimeUnit.MINUTES);

            if (dataReadResult.getBuckets().size() > 0) {
                Log.e("History", "Number of buckets: " + dataReadResult.getBuckets().size());
                for (Bucket bucket : dataReadResult.getBuckets()) {
                    List<DataSet> dataSets = bucket.getDataSets();
                    for (DataSet dataSet : dataSets) {
                        showDataSet(dataSet);
                    }
                }
            }
//Used for non-aggregated data
            else if (dataReadResult.getDataSets().size() > 0) {
                Log.e("History", "Number of returned DataSets: " + dataReadResult.getDataSets().size());
                for (DataSet dataSet : dataReadResult.getDataSets()) {
                    showDataSet(dataSet);
                }
            }

        }

        private void showDataSet(DataSet dataSet) {
            Log.e("History", "Data returned for Data type: " + dataSet.getDataType().getName());
            DateFormat dateFormat = DateFormat.getDateInstance();
            DateFormat timeFormat = DateFormat.getTimeInstance();

            for (DataPoint dp : dataSet.getDataPoints()) {
                Log.e("History", "Data point:");
                Log.e("History", "\tType: " + dp.getDataType().getName());
                Log.e("History", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                Log.e("History", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                for (Field field : dp.getDataType().getFields()) {
                    Log.e("History", "\tField: " + field.getName() +
                            " Value: " + dp.getValue(field));
                }
            }
        }
    }


    public void viewImage(View view) {

        String location = ((TextView) findViewById(R.id.locationText)).getText().toString();
        String light = ((TextView) findViewById(R.id.lightText)).getText().toString();
        String temp = ((TextView) findViewById(R.id.temperatureText)).getText().toString();

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

        String locationTextString = "latitude: " + latitude + "\nLongitude: " + longitude;
        locationText.setText(locationTextString);

        return location;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor == light) {
            lightText.setText(String.valueOf(sensorEvent.values[0]));
        } else if (sensorEvent.sensor == temp) {
            tempText.setText(String.valueOf(sensorEvent.values[0]));
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

    @Override
    public void onConnected(Bundle bundle) {
        Fitness.RecordingApi.subscribe(mGoogleApiClient, DataType.TYPE_STEP_COUNT_DELTA)
                .setResultCallback(mSubscribeResultCallback);
//
//        Fitness.RecordingApi.listSubscriptions(mGoogleApiClient)
//                .setResultCallback(mListSubscriptionsResultCallback);
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (!authInProgress) {

            authInProgress = true;
            try {
                connectionResult.startResolutionForResult(MainActivity.this, REQUEST_OAUTH);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("GoogleFit", "authInProgress");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AUTH_PENDING, authInProgress);
    }

    private void initCallbacks() {
        mSubscribeResultCallback = new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    if (status.getStatusCode() == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                        Log.e("RecordingAPI", "Already subscribed to the Recording API");
                    } else {
                        Log.e("RecordingAPI", "Subscribed to the Recording API");
                    }
                }
            }
        };

    }

}
