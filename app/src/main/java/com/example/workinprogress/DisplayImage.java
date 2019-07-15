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

    String currentImagePath;
    private TextImage createdImage;

    private ArrayList<Integer> lightLevels;
    private ArrayList<Location> locations;
    private ArrayList<Position> positions;
    private int steps;
    private int distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        if (getIntent().getExtras() != null) {

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

        if (getIntent().getExtras() != null) {

            Bundle bundle = this.getIntent().getExtras();

            ArrayList<SensorResult> sensorResults = (ArrayList<SensorResult>) bundle.getSerializable("sensorResults");

            for (SensorResult s : sensorResults) {

                switch (s.getName()) {
                    case "steps":
                        steps = (int) s.getResultsNumbers().get(0);
                        break;
                    case "distance":
                        distance = (int) s.getResultsNumbers().get(0);
                        break;
                    case "light":
                        lightLevels = s.getResultsNumbers();
                        break;
                    case "locations":
                        locations = s.getResultsObjects();
                        break;
                    case "positions":
                        positions = s.getResultsObjects();
                        break;

                }

            }
        }

//        steps = getIntent().getIntExtra("steps",0);
//        distance = getIntent().getFloatExtra("distance",0);

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





//            lightLevels = (ArrayList<Float>) bundle.getSerializable("lightLevels");
//            locations = (ArrayList<double[]>) bundle.getSerializable("locations");
//            positions = (ArrayList<Position>) bundle.getSerializable("positions");

        createdImage = new TextImage(this, lightLevels, locations, positions, steps, distance);
        ((ImageView) findViewById(R.id.createdImage)).setImageDrawable(createdImage);
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


//                drawableToBitmap(createdImage).compress(Bitmap.CompressFormat.JPEG, 90, out);
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


    public void share(View view) {
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
