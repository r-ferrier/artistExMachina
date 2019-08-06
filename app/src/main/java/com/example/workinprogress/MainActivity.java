package com.example.workinprogress;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.workinprogress.dataSetsAndComponents.UnscaledSingleEntryDataSet;
import com.example.workinprogress.paintings.shapes.BumpyShape;
import com.example.workinprogress.paintings.shapes.CurvedShape;
import com.example.workinprogress.paintings.shapes.LineShape;
import com.example.workinprogress.paintings.shapes.Shape;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.android.gms.fitness.data.DataType.AGGREGATE_DISTANCE_DELTA;
import static com.google.android.gms.fitness.data.DataType.AGGREGATE_STEP_COUNT_DELTA;
import static com.google.android.gms.fitness.data.DataType.TYPE_DISTANCE_DELTA;
import static com.google.android.gms.fitness.data.DataType.TYPE_STEP_COUNT_DELTA;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
    private ResultCallback<Status> mSubscribeResultCallback;
    private ResultCallback<Status> distanceSubscribeResultCallback;
    private GoogleApiClient mGoogleApiClient;
    private int steps;
    private float distance;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        connectToAPIs(savedInstanceState);
        new ViewWeekStepCountTask().execute();

        runAnimations();
    }

    private void runAnimations() {

        TextView titleText = findViewById(R.id.titleText);

        ImageButton galleryButton = findViewById(R.id.galleryButton);
        ImageButton aboutButton = findViewById(R.id.aboutButton);
        ImageButton portraitButton = findViewById(R.id.sitForPortraitButton);

        ObjectAnimator positionTextOffScreen = ObjectAnimator.ofFloat(titleText, "translationY", -600f);
        ObjectAnimator moveTextDown = ObjectAnimator.ofFloat(titleText, "translationY", 0f);

        positionTextOffScreen.setDuration(0);
        positionTextOffScreen.start();
        titleText.setVisibility(View.VISIBLE);

        drawShapes();

        moveTextDown.setDuration(3000);
        moveTextDown.start();
    }

    private void drawShapes() {

        FrameLayout animatedShapesDrawing = findViewById(R.id.animationPortion);
        ViewTreeObserver observer= animatedShapesDrawing.getViewTreeObserver();

        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        animatedShapesDrawing.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        float height = animatedShapesDrawing.getHeight();

                        ArrayList<Shape> shapes = new ArrayList<>();

                        shapes.add(new CurvedShape(0, 0, 0, (int) (height / 4), true, new int[]{255, 230, 50, 20}, (int) (height / 2)));
                        shapes.add(new CurvedShape(shapes.get(0).getX1End(), shapes.get(0).getY1End(), shapes.get(0).getX2End(), shapes.get(0).getY2End(), false, new int[]{255, 230, 150, 20}, (int) (height / 2)));
                        shapes.add(new BumpyShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), (int) (height), new int[]{255, 230, 200, 20}));
                        shapes.add(new CurvedShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), false, new int[]{255, 240, 220, 20}, (int) (height / 1.5)));
                        shapes.add(new CurvedShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), false, new int[]{255, 200, 220, 20}, (int) (height / 3)));
                        shapes.add(new CurvedShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), false, new int[]{255, 180, 220, 40}, (int) (height / 2.5)));
                        shapes.add(new CurvedShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), false, new int[]{255, 180, 220, 70}, (int) (height / 3.5)));
                        shapes.add(new LineShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), (int) (height), new int[]{155, 130, 200, 130}));

                        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        ArrayList<ImageView> imageViews = new ArrayList<>();


                        for(Shape shape: shapes){

                            imageViews.add((ImageView) inflater.inflate(R.layout.single_image, null));

                        }




                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                for (int i = 0; i < shapes.size(); i++) {

                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    final ImageView imageView = imageViews.get(i);
                                    final Shape shape = shapes.get(i);

                                    new Handler(Looper.getMainLooper()).post(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    animatedShapesDrawing.addView(imageView);

                                                    imageView.setImageDrawable(shape);
                                                }
                                            }
                                    );


//            image.setLayoutParams();


//            ImageView imageView = new ImageView(this);
//            imageView.setImageDrawable(shape);
//
//           animatedShapesDrawing.addView(imageView);


                                }
                            }
                        });

                        thread.start();



                    }
                });


//        ImageView animatedShapesDrawing = findViewById(R.id.animationPortion);

//        animatedShapesDrawing.setImageDrawable( new MainClassAnimation());

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

            Log.e("time", "\tStart: " + DateFormat.getDateInstance().format(startTime) + " " + DateFormat.getTimeInstance().format(startTime));
            Log.e("time", "\tEnd: " + DateFormat.getDateInstance().format(endTime) + " " + DateFormat.getTimeInstance().format(endTime));

            DataReadRequest stepsAndDistance = new DataReadRequest.Builder()
                    .aggregate(TYPE_STEP_COUNT_DELTA, AGGREGATE_STEP_COUNT_DELTA)
                    .aggregate(TYPE_DISTANCE_DELTA, AGGREGATE_DISTANCE_DELTA)
                    .bucketByTime(1, TimeUnit.DAYS)
                    .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                    .build();

            DataReadResult result = Fitness.HistoryApi.readData(mGoogleApiClient, stepsAndDistance).await(1, TimeUnit.MINUTES);

            if (result.getBuckets().size() > 0) {

                Log.e("History", "Number of buckets: " + result.getBuckets().size());
                List<DataSet> dataSets = result.getBuckets().get(0).getDataSets();

                int i = 1;

                for (DataSet dataSet : dataSets) {
                    Log.i("dataset number", i + "");
                    showAndStoreDataSet(dataSet);
                    i++;
                }
            }
        }


        private void showAndStoreDataSet(DataSet dataSet) {
            Log.e("History", "Data returned for Data type: " + dataSet.getDataType().getName());

            DateFormat dateFormat = DateFormat.getDateInstance();
            DateFormat timeFormat = DateFormat.getTimeInstance();

            for (DataPoint dp : dataSet.getDataPoints()) {
                Log.e("History", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                Log.e("History", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));

                for (Field field : dp.getDataType().getFields()) {

                    if (dp.getDataType().equals(TYPE_STEP_COUNT_DELTA)) {
                        steps = dp.getValue(field).asInt();
                    } else if (dp.getDataType().equals(TYPE_DISTANCE_DELTA)) {
                        distance = dp.getValue(field).asFloat();
                    }

                    Log.e("History", "\tField: " + field.getName());
                    Log.e("History", "\tValue: " + dp.getValue(field));
                }
            }
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        Fitness.RecordingApi.subscribe(mGoogleApiClient, TYPE_STEP_COUNT_DELTA)
                .setResultCallback(mSubscribeResultCallback);

        Fitness.RecordingApi.subscribe(mGoogleApiClient, TYPE_DISTANCE_DELTA)
                .setResultCallback(distanceSubscribeResultCallback);

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

    public void beginShortPortraitActivity(View view) {

//        ArrayList<Integer> stepsCount = new ArrayList<>();
//        stepsCount.add(steps);

//        System.out.println("steps: "+steps);

        UnscaledSingleEntryDataSet<Integer> stepsData = new UnscaledSingleEntryDataSet<>(steps, getString(R.string.data_type_steps));
        UnscaledSingleEntryDataSet<Float> distanceData = new UnscaledSingleEntryDataSet<>(distance, getString(R.string.data_type_distance));


//        ArrayList<Float> distanceCovered = new ArrayList<>();
//        distanceCovered.add(distance);
//
//        System.out.println("steps: "+distance);

//        SensorResult<Integer,ResultValuesAppendable> stepsSensorResult = new SensorResult<>(false,stepsCount,"steps");
//        SensorResult<Float,ResultValuesAppendable> distanceSensorResult = new SensorResult<>(false, distanceCovered,"distance");

        Intent intent = new Intent(this, ShortPortrait.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.data_type_steps), stepsData);
        bundle.putSerializable(getString(R.string.data_type_distance), distanceData);
//        bundle.putSerializable("steps", stepsSensorResult);
//        bundle.putSerializable("distance",distanceSensorResult);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    public void beginGalleryActivity(View view) {

        Intent intent = new Intent(this, Gallery.class);
        startActivity(intent);

    }

    public void beginAboutActivity(View view) {

        Intent intent = new Intent(this, About.class);
        startActivity(intent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_OAUTH: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(MainActivity.this, "You must accept permissions.", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission
                (this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission
                (this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions
                    (this, new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        initCallbacks();
    }

    private void connectToAPIs(Bundle savedInstanceState) {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API).addApi(Fitness.RECORDING_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
                .addConnectionCallbacks(this)
                .enableAutoManage(this, 0, this)
                .build();

        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }


    }


}
