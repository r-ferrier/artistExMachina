package com.example.workinprogress;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.File;

public class Gallery extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gallery);

        String albumName = "artist_ex_machina";

        Uri file = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName));


        if(file.toString() != null && file.toString().length()>0)
        {
            Picasso.with(this).load(file).placeholder(R.drawable.elipse).into();
            nameTxt.setText(file.toString());
        }else
        {
            Toast.makeText(this, "Empty URI", Toast.LENGTH_SHORT).show();
        }


    }
}
