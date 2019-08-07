package com.example.workinprogress;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.Arrays;

public class Gallery extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private GalleryRecyclerViewAdapter galleryRecyclerViewAdapter;
    private String[] reverseFiles;
    private GridLayoutManager gridLayoutManager;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getString(R.string.album_storage_name));

        String[] files = file.list();
        for(int i = 0; i< files.length; i++){
            files[i] = file.getPath()+"/"+files[i];
        }

        reverseFiles = new String[files.length];

        for(int i = 0; i< files.length; i++){
            reverseFiles[files.length-i-1]=files[i];

        }

        System.out.println("files: "+Arrays.toString(files));

        setContentView(R.layout.activity_gallery);
        recyclerView = findViewById(R.id.gallery_recycler_view);
        recyclerView.setHasFixedSize(true); //improves performance for fixed size elements


        gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);

        // specify an adapter (see also next example)
        galleryRecyclerViewAdapter = new GalleryRecyclerViewAdapter(reverseFiles);
        recyclerView.setAdapter(galleryRecyclerViewAdapter);

    }

    public void displayImage(View view){

        Intent intent = new Intent(this, DisplayImage.class);

        String imageLocation = view.getContentDescription().toString();

        intent.putExtra("imageLocation",imageLocation);

        startActivity(intent);


    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(this,MainActivity.class));
        finish();

    }

}
