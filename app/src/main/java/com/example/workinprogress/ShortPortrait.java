package com.example.workinprogress;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShortPortrait extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_portrait);

    }


    public void start(View view){
        ((TextView)view).setText(R.string.start);
    }

    public void stop(View view){
        ((TextView)view).setText(R.string.stop);
    }
}
