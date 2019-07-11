package com.example.workinprogress;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    private CreateImage createdImage;

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

    public void displayExistingImage() {
        findViewById(R.id.artistBusyView).setVisibility(View.INVISIBLE);
    }

    public void createAndDisplayNewImage() {

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

        createdImage = new CreateImage(this, lightLevels, locations, positions);
        ((ImageView) findViewById(R.id.createdImage)).setImageDrawable(createdImage);
        imageCreated = true;

    }

//    public static Bitmap drawableToBitmap(CreateImage createdImage) {

//        Drawable drawable = (Drawable)createdImage;

//        Bitmap mutableBitmap = Bitmap.createBitmap(createdImage.getWidth(), createdImage.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(mutableBitmap);
//        createdImage.setBounds(0, 0, createdImage.getWidth(),createdImage.getHeight());
//        createdImage.draw(canvas);

//        Bitmap mutableBitmap = ((BitmapDrawable)drawable).getBitmap();

//        return mutableBitmap;
//    }


    public void save(View view) {

        if (isExternalStorageWritable()) {

            Log.i("external storage?", isExternalStorageWritable() + "");

            try {
                File file = getPublicAlbumStorageDir("artist_ex_machina");
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

    public File getPublicAlbumStorageDir(String albumName) throws IOException {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);

        if (!file.mkdirs()) {
            Log.e("no directory", "Directory not created");
        }

        Log.e("filename", file.getAbsolutePath());

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                file      /* directory */
        );

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

//    private void saveImageToExternalStorage(Bitmap finalBitmap) {
//
//        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
//
//        Log.i("file path",root);
//
//        File myDir = new File(root + "/saved_images_1");
//        myDir.mkdirs();
//        Random generator = new Random();
//        int n = 10000;
//        n = generator.nextInt(n);
//        String fname = "Image-" + n + ".jpg";
//        File file = new File(myDir, fname);
//        if (file.exists())
//            file.delete();
//        try {
//            FileOutputStream out = new FileOutputStream(file);
//            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//            out.flush();
//            out.close();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        // Tell the media scanner about the new file so that it is
//        // immediately available to the user.
//        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
//                new MediaScannerConnection.OnScanCompletedListener() {
//                    public void onScanCompleted(String path, Uri uri) {
//                        Log.i("ExternalStorage", "Scanned " + path + ":");
//                        Log.i("ExternalStorage", "-> uri=" + uri);
//                    }
//                });
//
//    }

//


    public void share(View view) {
        //share image using other applications? Investigate possibilities
    }

    public void discard(View view) {

    }


}
