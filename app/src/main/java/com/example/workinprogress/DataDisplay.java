package com.example.workinprogress;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class DataDisplay extends AppCompatActivity {

    private ArrayList<String> dataStrings;
    private String data = "";
    private boolean newImageCreated;
    private String TAG = "DataDisplay information";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        if(getIntent().getExtras() != null){

            newImageCreated = getIntent().getBooleanExtra("newImageCreated",true);
            dataStrings = getIntent().getStringArrayListExtra("dataStrings");
            data = getIntent().getStringExtra("data");

            if(data!=null){
                data = data.replaceAll("\\?","\n");
            }else{
                data = "There was a problem storing this data.";
            }



            Log.i(TAG,data);
        }else{
            Log.e(TAG,"no data passed in to class");
        }

        if(newImageCreated){

            String dataString = "";
            for(String dataSet: dataStrings){
                dataString+=dataSet;
                dataString+="\n\n";
            }

            ((TextView)findViewById(R.id.dataTextView)).setText(dataString);
        }else {
            ((TextView) findViewById(R.id.dataTextView)).setText(data);
        }

        if(data.equals("")) {
            Log.e(TAG,"data not saved correctly");
        }

    }
}
