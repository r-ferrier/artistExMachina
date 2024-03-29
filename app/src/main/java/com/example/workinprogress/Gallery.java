package com.example.workinprogress;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Gallery view will need to display any files it finds in a scrollable panel. Each file is
 * represented by a thumbnail which is clickable, and will take the user to the displayImage view
 * associated with that file, from which they can share, delete, or view data from files.
 */
public class Gallery extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GalleryRecyclerViewAdapter galleryRecyclerViewAdapter;
    private GridLayoutManager gridLayoutManager;
    private String TAG = "Gallery info: ";


    /**
     * creates a list of all saved files found at the album location and then passes them to a
     * recycler view to lay out.
     *
     * @param savedInstanceState Gallery view created by main activity or display image view
     */
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //get a list of all files saved by the app
        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getString(R.string.album_storage_name));
        File[] files = directory.listFiles();

        //first check if files exist - if not, instantiate an empty view
        if (files == null) {
            setContentView(R.layout.activity_gallery);
            Log.e(TAG, "no files found at this location: "+directory);

        } else {

            Log.i(TAG, files.length+" files found at this location: "+directory);

            //sort the list so that the most recently modified files are shown first
            Arrays.sort(files, (f1, f2) -> Long.valueOf(f2.lastModified()).compareTo(f1.lastModified()));

            //set content view and set up recycler view so that it has three columns and each image
            //has a fixed size
            setContentView(R.layout.activity_gallery);
            recyclerView = findViewById(R.id.gallery_recycler_view);
            recyclerView.setHasFixedSize(true);

            gridLayoutManager = new GridLayoutManager(this, 3);
            recyclerView.setLayoutManager(gridLayoutManager);

            //send files to viewadapter to insert into the recyclerView
            galleryRecyclerViewAdapter = new GalleryRecyclerViewAdapter(files);
            recyclerView.setAdapter(galleryRecyclerViewAdapter);
        }
    }

    /**
     * gets the filepath which has been saved as the view content description and uses this to
     * instantiate a new displayImage view, with the filepath as the imagelocation.
     *
     * @param view View associated with the thumbnail the user just clicked
     */
    public void displayImage(View view) {
        Intent intent = new Intent(this, DisplayImage.class);
        String imageLocation = view.getContentDescription().toString();
        intent.putExtra("imageLocation", imageLocation);
        startActivity(intent);
    }

    //pressing back from the gallery always returns the user to the main activity to avoid the user
    //accidentally revisiting a now deleted image
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
