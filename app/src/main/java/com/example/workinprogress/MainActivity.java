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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.android.gms.fitness.data.DataType.*;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {


    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
    private ResultCallback<Status> mSubscribeResultCallback;
    private ResultCallback<Status> distanceSubscribeResultCallback;

    private GoogleApiClient mGoogleApiClient;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        initCallbacks();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API).addApi(Fitness.RECORDING_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
                .addConnectionCallbacks(this)
                .enableAutoManage(this, 0, this)
                .build();

        new ViewWeekStepCountTask().execute();

        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }



    }


    private class ViewWeekStepCountTask extends AsyncTask<Void, Void, Void> {


        protected Void doInBackground(Void... params) {
//            displayLastWeeksData();
            displayYesterdaysData();
            return null;
        }

        private void displayYesterdaysData() {


            Calendar date = new GregorianCalendar();
// reset hour, minutes, seconds and millis
            date.set(Calendar.HOUR_OF_DAY, 0);
            date.set(Calendar.MINUTE, 0);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);


            long endTime = date.getTimeInMillis();
            date.add(Calendar.DAY_OF_YEAR, -1);
            long startTime = date.getTimeInMillis();

            Log.e("time", "\tStart: " + DateFormat.getDateInstance().format(startTime)+" "+ DateFormat.getTimeInstance().format(startTime));
            Log.e("time", "\tEnd: " + DateFormat.getDateInstance().format(endTime)+" "+ DateFormat.getTimeInstance().format(endTime));

            DataReadRequest steps = new DataReadRequest.Builder()
                    .aggregate(TYPE_STEP_COUNT_DELTA, AGGREGATE_STEP_COUNT_DELTA)
                    .aggregate(TYPE_DISTANCE_DELTA, AGGREGATE_DISTANCE_DELTA)
                    .bucketByTime(1, TimeUnit.DAYS)
                    .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                    .build();


            DataReadResult stepsResult = Fitness.HistoryApi.readData(mGoogleApiClient, steps).await(1, TimeUnit.MINUTES);


            if (stepsResult.getBuckets().size() > 0) {

                Log.e("History", "Number of steps buckets: " + stepsResult.getBuckets().size());
                List<DataSet> dataSets = stepsResult.getBuckets().get(0).getDataSets();

                int i = 0;

                for (DataSet dataSet : dataSets) {
                    showDataSet(dataSet);
                    Log.i("dataset",i+"");
                    i++;
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
                Log.e("History", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                for (Field field : dp.getDataType().getFields()) {
                    Log.e("History", "\tField: " + field.getName() +
                            " Value: " + dp.getValue(field));
                }
            }
        }
    }


//    public void viewImage(View view) {
//
//        String location = ((TextView) findViewById(R.id.locationText)).getText().toString();
//        String light = ((TextView) findViewById(R.id.lightText)).getText().toString();
//        String temp = ((TextView) findViewById(R.id.temperatureValues)).getText().toString();
//
//        Intent intent = new Intent(this, DisplayImage.class);
//        intent.putExtra("temp", temp);
//        intent.putExtra("light", light);
//        intent.putExtra("location", location);
//        startActivity(intent);
//
//
//    }



    @Override
    public void onConnected(Bundle bundle) {
        Fitness.RecordingApi.subscribe(mGoogleApiClient, TYPE_STEP_COUNT_DELTA)
                .setResultCallback(mSubscribeResultCallback);

        Fitness.RecordingApi.subscribe(mGoogleApiClient, TYPE_DISTANCE_DELTA)
                .setResultCallback(distanceSubscribeResultCallback);

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

        distanceSubscribeResultCallback = new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    if (status.getStatusCode() == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                        Log.e("RecordingAPI", "Already subscribed to the distance API");
                    } else {
                        Log.e("RecordingAPI", "Subscribed to the distance API");
                    }
                }
            }
        };

    }

    public void sitForPortrait(View view){

        Intent intent = new Intent(this, ShortPortrait.class);
        startActivity(intent);

    }

}
