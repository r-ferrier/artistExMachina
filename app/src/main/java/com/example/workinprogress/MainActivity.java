package com.example.workinprogress;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
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

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.android.gms.fitness.data.DataType.*;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {


    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
    private ResultCallback<Status> mSubscribeResultCallback;
    private ResultCallback<Status> distanceSubscribeResultCallback;
//    private LoadAlbum loadAlbumTask;
    GridView galleryGridView;
    ArrayList<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();

    private GoogleApiClient mGoogleApiClient;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission
                (this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission
                (this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions
                    (this, new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

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

//
//        galleryGridView = (GridView) findViewById(R.id.galleryGridView);
//        int iDisplayWidth = getResources().getDisplayMetrics().widthPixels ;
//        Resources resources = getApplicationContext().getResources();
//        DisplayMetrics metrics = resources.getDisplayMetrics();
//        float dp = iDisplayWidth / (metrics.densityDpi / 160f);
//
//        if(dp < 360)
//        {
//            dp = (dp - 17) / 2;
//            float px = Function.convertDpToPixel(dp, getApplicationContext());
//            galleryGridView.setColumnWidth(Math.round(px));
//        }
//
//
//        loadAlbumTask = new LoadAlbum();
//        loadAlbumTask.execute();



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

    public void beginShortPortraitActivity(View view){

        Intent intent = new Intent(this, ShortPortrait.class);
        startActivity(intent);

    }

    public void beginGalleryActivity(View view){

        Intent intent = new Intent(this, Gallery.class);
        startActivity(intent);

    }

    public void beginAboutActivity(View view){

        Intent intent = new Intent(this, About.class);
        startActivity(intent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_OAUTH: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
//                    loadAlbumTask = new LoadAlbum();
//                    loadAlbumTask.execute();
                } else
                {
                    Toast.makeText(MainActivity.this, "You must accept permissions.", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if(!Function.hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_OAUTH);
        }else{
//            loadAlbumTask = new LoadAlbum();
//            loadAlbumTask.execute();
        }

    }

//    class LoadAlbum extends AsyncTask<String, Void, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            albumList.clear();
//        }
//
//        protected String doInBackground(String... args) {
//            String xml = "";
//
//            String path = null;
//            String album = null;
//            String timestamp = null;
//            String countPhoto = null;
//            Uri uriExternal = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//            Uri uriInternal = android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI;
////            String selection = "_data IS NOT NULL) GROUP BY (bucket_display_name";
//            String selection = "_data IS NOT NULL";
//
//
//            String[] projection = { MediaStore.MediaColumns.DATA,
//                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED };
//            Cursor cursorExternal = getContentResolver().query(uriExternal, projection,selection,
//                    null, null);
//
//
//            Cursor cursorInternal = getContentResolver().query(uriInternal, projection, selection,
//                    null, null);
//            Cursor cursor = new MergeCursor(new Cursor[]{cursorExternal,cursorInternal});
//
//            while (cursor.moveToNext()) {
//
//                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
//                album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
//                timestamp = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED));
//                countPhoto = Function.getCount(getApplicationContext(), album);
//
//                albumList.add(Function.mappingInbox(album, path, timestamp, Function.converToTime(timestamp), countPhoto));
//            }
//            cursor.close();
//            Collections.sort(albumList, new MapComparator(Function.KEY_TIMESTAMP, "dsc")); // Arranging photo album by timestamp decending
//            return xml;
//        }
//
//        @Override
//        protected void onPostExecute(String xml) {
//
//            AlbumAdapter adapter = new AlbumAdapter(MainActivity.this, albumList);
//            galleryGridView.setAdapter(adapter);
//            galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                public void onItemClick(AdapterView<?> parent, View view,
//                                        final int position, long id) {
//                    Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
//                    intent.putExtra("name", albumList.get(+position).get(Function.KEY_ALBUM));
//                    startActivity(intent);
//                }
//            });
//        }
//    }

//    class AlbumAdapter extends BaseAdapter {
//        private Activity activity;
//        private ArrayList<HashMap< String, String >> data;
//        public AlbumAdapter(Activity a, ArrayList< HashMap < String, String >> d) {
//            activity = a;
//            data = d;
//        }
//        public int getCount() {
//            return data.size();
//        }
//        public Object getItem(int position) {
//            return position;
//        }
//        public long getItemId(int position) {
//            return position;
//        }
//
//        public View getView(int position, View convertView, ViewGroup parent) {
//            AlbumViewHolder holder = null;
//            if (convertView == null) {
//                holder = new AlbumViewHolder();
//                convertView = LayoutInflater.from(activity).inflate(
//                        R.layout.album_row, parent, false);
//
//                holder.galleryImage = (ImageView) convertView.findViewById(R.id.galleryImage);
//                holder.gallery_count = (TextView) convertView.findViewById(R.id.gallery_count);
//                holder.gallery_title = (TextView) convertView.findViewById(R.id.gallery_title);
//
//                convertView.setTag(holder);
//            } else {
//                holder = (AlbumViewHolder) convertView.getTag();
//            }
//            holder.galleryImage.setId(position);
//            holder.gallery_count.setId(position);
//            holder.gallery_title.setId(position);
//
//            HashMap < String, String > song = new HashMap < String, String > ();
//            song = data.get(position);
//            try {
//                holder.gallery_title.setText(song.get(Function.KEY_ALBUM));
//                holder.gallery_count.setText(song.get(Function.KEY_COUNT));
//
//                Glide.with(activity)
//                        .load(new File(song.get(Function.KEY_PATH))) // Uri of the picture
//                        .into(holder.galleryImage);
//
//
//            } catch (Exception e) {}
//            return convertView;
//        }
//    }
//
//
//    class AlbumViewHolder {
//        ImageView galleryImage;
//        TextView gallery_count, gallery_title;
//    }


}
