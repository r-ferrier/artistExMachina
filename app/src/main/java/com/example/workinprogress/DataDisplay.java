package com.example.workinprogress;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DataDisplay extends AppCompatActivity {

    private ArrayList<String> dataStrings;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        if(getIntent().getExtras() != null){
            dataStrings = getIntent().getStringArrayListExtra("dataStrings");
        }

        ((TextView)findViewById(R.id.lightTextView)).setText(dataStrings.get(0));
        ((TextView)findViewById(R.id.positionTextView)).setText(dataStrings.get(1));
        ((TextView)findViewById(R.id.locationTextView)).setText(dataStrings.get(2));
        ((TextView)findViewById(R.id.stepsTextView)).setText(dataStrings.get(3));
//        ((TextView)findViewById(R.id.distanceTextView)).setText(dataStrings.get(4));


    }
}
