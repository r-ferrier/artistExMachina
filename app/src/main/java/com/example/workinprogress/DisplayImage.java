package com.example.workinprogress;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DisplayImage extends AppCompatActivity {

    private boolean createNewImage;
    private boolean imageCreated;

    private ArrayList<Float> lightLevels;
    private ArrayList<double[]> locations;
    private ArrayList<Position> positions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        if (getIntent().getExtras() != null) {
            createNewImage = getIntent().getBooleanExtra("createImage", false);
        }

        if (!createNewImage) {
            displayExistingImage();
        } else {
            createAndDisplayNewImage();
        }
    }

    private void animateWaitingScreen(TextView animationText) {
        try {
            Thread.sleep(200);
            animationText.setText(R.string.artistBusy2);
            Thread.sleep(200);
            animationText.setText(R.string.artistBusy3);
            Thread.sleep(200);
            animationText.setText(R.string.artistBusy4);
            Thread.sleep(200);
            animationText.setText(R.string.artistBusy1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public void returnToHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void displayExistingImage(){
        findViewById(R.id.artistBusyView).setVisibility(View.INVISIBLE);
    }

    public void createAndDisplayNewImage(){

        imageCreated = false;

        Thread thready = new Thread(new Runnable() {
            @Override
            public void run() {
                TextView animationtext = findViewById(R.id.artistBusyText);

                for (int i = 0; i < 3; i++) {
                    animateWaitingScreen(animationtext);
                }
                while (!imageCreated) {
                    animateWaitingScreen(animationtext);
                }

                findViewById(R.id.artistBusyView).setVisibility(View.INVISIBLE);
            }
        });
        thready.start();


        if (getIntent().getExtras() != null) {

            Bundle bundle = this.getIntent().getExtras();

            lightLevels = (ArrayList<Float>) bundle.getSerializable("lightLevels");
            locations = (ArrayList<double[]>) bundle.getSerializable("locations");
            positions = (ArrayList<Position>) bundle.getSerializable("positions");

            for (int i = 0; i < 10; i++) {
                if (i < lightLevels.size()) {
                    String light = lightLevels.get(i) + "";
                    Log.i("lightlevels", light);
                }
            }
        }

        CreateImage createdImage = new CreateImage(this, lightLevels,locations,positions);
        ((ImageView) findViewById(R.id.createdImage)).setImageDrawable(createdImage);
        imageCreated = true;

    }


    public void save(View view) {
        onResume();
    }

    public void share(View view) {
        //share image using other applications? Investigate possibilities
    }


}
