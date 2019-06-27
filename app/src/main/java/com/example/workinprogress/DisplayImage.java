package com.example.workinprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DisplayImage extends AppCompatActivity {

    private String location = "";
    private String light = "";
    private String temp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);


        if (getIntent().getExtras() != null) {
            location = getIntent().getStringExtra("location");
            light = getIntent().getStringExtra("light");
            temp = getIntent().getStringExtra("temp");

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
