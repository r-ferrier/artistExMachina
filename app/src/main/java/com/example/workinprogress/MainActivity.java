package com.example.workinprogress;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.workinprogress.dataSetsAndComponents.UnscaledSingleEntryDataSet;
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
    private int animationDuration1 = 100;
    private int animationDuration2 = 120;
    private int animationDuration3 = 5000;
    private int animationDuration4 = 6000;
    private int animationPause1 = 3000;
    private int animationPause2 = 2000;
    private ObjectAnimator moveButtons;


    /**
     * On creation the mainActivity will set up content, request any necessary permissions, collect aggregate
     * data and create an animation for its home screen
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        connectToAPIs(savedInstanceState);
        new ViewWeekStepCountTask().execute();
        runAnimations();
    }

    /**
     * Method to force animation of buttons to end on click so that users do not have to wait for animation
     * to complete when not opening app for the first time
     * @param view called by onClick method of the layout
     */
    public void stopAnimation(View view){
        moveButtons.cancel();
        View buttonsLayout = findViewById(R.id.buttonPanel);
        buttonsLayout.setTranslationY(0);
    }

    /**
     * Calls on helper methods that are used to generate animations for the home screen
     */
    private void runAnimations() {
        crossfadeText();
        moveButtonsUpFromBottom();
        drawShapes();
    }

    /**
     * translates buttons from outside the screen at the bottom to their set position onscreen
     */
    private void moveButtonsUpFromBottom() {
        View buttonsLayout = findViewById(R.id.buttonPanel);
        moveButtons = ObjectAnimator.ofFloat(buttonsLayout, "translationY", 1000f, 0f, 30f);
        buttonsLayout.setVisibility(View.VISIBLE);
        moveButtons.setDuration(animationDuration4);
        moveButtons.start();
    }

    /**
     * fades title text from black to white
     */
    private void crossfadeText() {
        TextView titleText = findViewById(R.id.titleText);
        TextView titleText2 = findViewById(R.id.titleText2);

        titleText.setVisibility(View.GONE);
        int longAnimationDuration = animationDuration3;

        titleText.setAlpha(0f);
        titleText.setVisibility(View.VISIBLE);
        titleText.animate().alpha(1f).setDuration(longAnimationDuration).setListener(null);
        titleText2.animate().alpha(0f).setDuration(longAnimationDuration).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                titleText2.setVisibility(View.GONE);
            }
        });
    }

    /**
     * creates animated background from lists of shapes obtained from MainClassAnimation class
     */
    private void drawShapes() {

        FrameLayout animatedShapesDrawing = findViewById(R.id.animationPortion);
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewTreeObserver observer = animatedShapesDrawing.getViewTreeObserver();

        //animation cannot be completed until layout has finished so listener added to determine when
        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //as soon as layout is complete, listener is removed
                        animatedShapesDrawing.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        //animation can now be constructed and run using the width and height of the layout
                        MainClassAnimation mainClassAnimation = new MainClassAnimation(animatedShapesDrawing.getWidth(), animatedShapesDrawing.getHeight());

                        ArrayList<ImageView> shapeImageViews = inflateViewsForAnimation(inflater, mainClassAnimation.getShapes());
                        ArrayList<ImageView> thinShapeImageViews = inflateViewsForAnimation(inflater, mainClassAnimation.getThinShapes());
                        ArrayList<ImageView> thinnestShapeImageViews = inflateViewsForAnimation(inflater, mainClassAnimation.getThinnestShapes());

                        //three threads created, each one calls helper method with different shape arrays after a small pause
                        Thread shapesThread = new Thread(() -> {
                            try {
                                Thread.sleep(animationPause1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            runnableTask(mainClassAnimation.getShapes(), shapeImageViews, animatedShapesDrawing,animationDuration1);
                        });

                        Thread thinShapesThread = new Thread(() -> {
                            try {
                                Thread.sleep(animationPause2);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            runnableTask(mainClassAnimation.getThinShapes(), thinShapeImageViews, animatedShapesDrawing,animationDuration1);

                        });

                        Thread thinnestShapesThread = new Thread(() -> runnableTask(mainClassAnimation.getThinnestShapes(), thinnestShapeImageViews, animatedShapesDrawing, animationDuration2));

                        //run threads
                        shapesThread.start();
                        thinShapesThread.start();
                        thinnestShapesThread.start();
                    }
                });
    }

    /**
     * helper method for drawshapes method, used to place shapes on background one by one. This method
     * is not called by the main thread so it calls sleep first, before using a handler to post tasks back onto
     * the main thread in order to update the views
     * @param shapes list of drawables
     * @param shapeImageViews list of imageviews into which drawables should be inserted
     * @param animatedShapesDrawing
     * @param sleepDuration
     */
    private void runnableTask(ArrayList<Shape> shapes, ArrayList<ImageView> shapeImageViews, FrameLayout animatedShapesDrawing, int sleepDuration) {
        for (int i = 0; i < shapes.size(); i++) {

            try {
                Thread.sleep(sleepDuration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //get the imageview and shape and declare them as final so they can be accessed by the Handler
            final ImageView imageView = shapeImageViews.get(i);
            final Shape shape = shapes.get(i);

            new Handler(Looper.getMainLooper()).post(
                    () -> {
                        //runnable to add new view to the frameLayout and add the drawable to that view
                        animatedShapesDrawing.addView(imageView);
                        imageView.setImageDrawable(shape);
                    }
            );
        }
    }

    /**
     * helper method for the drawShapes method, creates a new imageview for each shape. Imageview is the size of
     * the full screen and will be placed into a frame layout so that many can be layed on top of each other
     * @param inflater LayoutInflater
     * @param shapes ArrayList of Shapes
     * @return
     */
    private ArrayList<ImageView> inflateViewsForAnimation(LayoutInflater inflater, ArrayList<Shape> shapes) {
        ArrayList<ImageView> shapeImageViews = new ArrayList<>();

        for (Shape shape : shapes) {
            shapeImageViews.add((ImageView) inflater.inflate(R.layout.single_image, null));
        }
        return shapeImageViews;
    }

    public void beginShortPortraitActivity(View view) {

        ArrayList<Float> distanceCovered = new ArrayList<>();
        distanceCovered.add(distance);

        ArrayList<com.example.workinprogress.dataSetsAndComponents.DataSet> dataSets = new ArrayList<>();
        dataSets.add(new UnscaledSingleEntryDataSet<>(steps, getString(R.string.data_type_steps)));
        dataSets.add(new UnscaledSingleEntryDataSet<>(distance, getString(R.string.data_type_distance)));

        Intent intent = new Intent(this, ShortPortrait.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("dataSet_Array",dataSets);
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
