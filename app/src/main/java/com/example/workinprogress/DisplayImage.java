package com.example.workinprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DisplayImage extends AppCompatActivity {

    private String location = "";
    private String light = "";
    private String temp = "";

    private float[] lightLevels;
    private ArrayList<double[]> locations;
    private ArrayList<Position> positions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);


        if (getIntent().getExtras() != null) {
            location = getIntent().getStringExtra("location");
            light = getIntent().getStringExtra("light");
            temp = getIntent().getStringExtra("temp");
//            float [] lightLevels = getIntent().getFloatArrayExtra("lightLevels");
//
//            String lightLevels;
//
//            for(int i = 0; i<10; i++){
//                if(i<lightLevels.length())
//                lightLevels[0]
//            }
//
//            Log.i("lightlevels",)

        }

        CreateImage createdImage = new CreateImage(this,location, light, temp);
        ((ImageView) findViewById(R.id.createdImage)).setImageDrawable(createdImage);

    }

    protected void onResume() {
        super.onResume();
        if (getIntent().getExtras() != null) {





            location = getIntent().getStringExtra("location");
            light = getIntent().getStringExtra("light");
            temp = getIntent().getStringExtra("temp");

        }

        CreateImage createdImage = new CreateImage(this,location, light, temp);
        ((ImageView) findViewById(R.id.createdImage)).setImageDrawable(createdImage);

    }


    public void returnToHome(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void save(View view) {
        onResume();
    }

    public void share(View view) {
        //share image using other applications? Investigate possibilities
    }


}
