package com.example.workinprogress;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workinprogress.dataSetsAndComponents.DataSet;
import com.example.workinprogress.dataSetsAndComponents.DataSetPoint;
import com.example.workinprogress.paintings.AbstractShapes;
import com.example.workinprogress.paintings.AlbersImage;
import com.example.workinprogress.paintings.AutomaticDrawing;
import com.example.workinprogress.paintings.Painting;
import com.example.workinprogress.paintings.Recursion;
import com.example.workinprogress.paintings.TextImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DisplayImage extends AppCompatActivity {

    private boolean createNewImage;
    private boolean imageCreated;
    public static int IMAGE_SIZE_SCALAR = 1000;

    String currentImagePath;
    private Painting createdImage;
    private String imageType = "Albers Image";

    private ArrayList<Integer> lightLevels;
    private ArrayList<DataSetPoint> locations;
    private ArrayList<DataSetPoint> positions;
    private ArrayList<Integer> steps;
    private ArrayList<Integer> distance;
    private ArrayList<String> dataStrings;

    private ArrayList<DataSet> dataSets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        if (getIntent().getExtras() != null) {

            imageType = getIntent().getStringExtra("imageType");

            createNewImage = getIntent().getBooleanExtra("createImage", false);

            if (!createNewImage) {
                currentImagePath = getIntent().getStringExtra("imageLocation");
                displayExistingImage();
            } else {
                createAndDisplayNewImage();
            }
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

    public void displayExistingImage() {
        findViewById(R.id.artistBusyView).setVisibility(View.INVISIBLE);
        ((ImageView) findViewById(R.id.createdImage)).setImageBitmap(BitmapFactory.decodeFile(currentImagePath));
    }

    public void createAndDisplayNewImage() {

        imageCreated = false;
        getPublicAlbumStorageDir(getString(R.string.album_storage_name));
        dataStrings = new ArrayList<>();

        if (getIntent().getExtras() != null) {

            dataSets = (ArrayList<DataSet>) this.getIntent().getExtras().getSerializable(getString(R.string.data_sets));

            for(DataSet dataSet: dataSets){
                dataStrings.add(dataSet.toString());
            }

        }

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

//        createNewTextImage();
//        createNewAlbersImage();
//        createNewAutomaticDrawingImage();
//        createNewAbstractShapesImage();

//
//        if(imageType.equals(getString(R.string.albers_image))){
//            createNewAlbersImage();
//        }else if(imageType.equals(getString(R.string.abstract_shapes))){
//            createNewAbstractShapesImage();
//        }else{
//            createNewAutomaticDrawingImage();
//        }

        createNewRecursionImage();

    }

    public void createNewAbstractShapesImage(){
        createdImage = new AbstractShapes(this,  dataSets);
        ((ImageView) findViewById(R.id.createdImage)).setImageDrawable(createdImage);
        //   ((ImageView) findViewById(R.id.createdImage)).setImageBitmap(createdImage.createBitmap());
        imageCreated = true;
    }

    public void createNewTextImage(){
        createdImage = new TextImage(this,  dataSets);
        ((ImageView) findViewById(R.id.createdImage)).setImageDrawable(createdImage);
     //   ((ImageView) findViewById(R.id.createdImage)).setImageBitmap(createdImage.createBitmap());
        imageCreated = true;
    }

    public void createNewAlbersImage(){
        createdImage = new AlbersImage(this, dataSets);
        ((ImageView) findViewById(R.id.createdImage)).setImageDrawable(createdImage);
  //      ((ImageView) findViewById(R.id.createdImage)).setImageBitmap(createdImage.createBitmap());
        imageCreated = true;
    }

    public void createNewAutomaticDrawingImage(){
        createdImage = new AutomaticDrawing(this,dataSets);
        ((ImageView) findViewById(R.id.createdImage)).setImageDrawable(createdImage);
        //      ((ImageView) findViewById(R.id.createdImage)).setImageBitmap(createdImage.createBitmap());
        imageCreated = true;
    }

    public void createNewRecursionImage(){
        createdImage = new Recursion(this,dataSets);
        ((ImageView) findViewById(R.id.createdImage)).setImageDrawable(createdImage);
        //      ((ImageView) findViewById(R.id.createdImage)).setImageBitmap(createdImage.createBitmap());
        imageCreated = true;
    }



    public void save(View view) {

        if(imageCreated) {

            if (isExternalStorageWritable()) {

                Log.i("external storage?", isExternalStorageWritable() + "");

                try {
                    File file = new File(currentImagePath);
                    FileOutputStream out = new FileOutputStream(file);

                    Bitmap bitmap = createdImage.createBitmap();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                    out.flush();
                    out.close();
                    Toast.makeText(getApplicationContext(), "Saved successfully, Check gallery", Toast.LENGTH_SHORT).show();
                    galleryAddPic();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        goToGallery();
    }

    public File getPublicAlbumStorageDir(String albumName){
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);

        if (!file.mkdirs()) {
            Log.e("no directory", "Directory not created");
        }

        Log.e("filename", file.getAbsolutePath());

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    file      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        currentImagePath = image.getAbsolutePath();

        return image;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentImagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    public void data(View view) {


        Intent intent = new Intent(this,DataDisplay.class);

        intent.putStringArrayListExtra("dataStrings",dataStrings);

        startActivity(intent);


        //share image using other applications? Investigate possibilities
    }

    public void discard(View view) {

        File file = new File(currentImagePath);

        if(file.exists()) {
            file.delete();
        }
        onBackPressed();

    }

    private void goToGallery(){
        Intent intent = new Intent(this, Gallery.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
        finish();

    }


}
