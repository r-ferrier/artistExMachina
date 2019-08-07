package com.example.workinprogress;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.workinprogress.dataSetsAndComponents.DataSet;
import com.example.workinprogress.dataSetsAndComponents.DataSetPoint;
import com.example.workinprogress.paintings.AbstractShapes;
import com.example.workinprogress.paintings.AlbersImage;
import com.example.workinprogress.paintings.AutomaticDrawing;
import com.example.workinprogress.paintings.KineticArt;
import com.example.workinprogress.paintings.Landscape;
import com.example.workinprogress.paintings.Painting;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DisplayImage extends AppCompatActivity implements ViewPager.OnPageChangeListener {

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
    private boolean[] saved;
    private boolean[] deleted;
    private LinearLayoutManager linearLayoutManager;

    private ArrayList<DataSet> dataSets;

    private LayoutInflater inflater;
    private ViewPager vp;

    private ArrayList<Drawable> drawables;
    private ArrayList<Bitmap> bitmaps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);


        if (getIntent().getExtras() != null) {

            imageType = getIntent().getStringExtra("imageType");
            createNewImage = getIntent().getBooleanExtra("createImage", false);

            if (!createNewImage) {
                currentImagePath = getIntent().getStringExtra("imageLocation");
                findViewById(R.id.keepButton).setVisibility(View.INVISIBLE);
                findViewById(R.id.imageSavedText).setVisibility(View.INVISIBLE);
                displayExistingImage();
            } else {
                findViewById(R.id.discardButton).setVisibility(View.INVISIBLE);
                inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //Reference ViewPager defined in activity
                vp = (ViewPager) findViewById(R.id.pager);
                //set the adapter that will create the individual pages
                vp.setAdapter(new ImagePagerAdapter(this));

                TabLayout tabLayout = (TabLayout) findViewById(R.id.dots);
                tabLayout.setupWithViewPager(vp, true);


                vp.addOnPageChangeListener(this);
                createAndDisplayImages();
            }
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        int currentItem = vp.getCurrentItem();
        createdImage = (Painting) (drawables.get(currentItem));

        TextView text = findViewById(R.id.imageSavedText);
        Button keepButton = findViewById(R.id.keepButton);
        Button deleteButton = findViewById(R.id.discardButton);

        if (saved[position]) {
            keepButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            text.setText(R.string.image_saved);
            text.setTextColor(getResources().getColor(R.color.iguanaGreen));

        } else {
            keepButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
            text.setText(R.string.image_not_saved);
            text.setTextColor(getResources().getColor(R.color.darkGrey));
        }

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class ImagePagerAdapter extends PagerAdapter {

        private Context context;

        public ImagePagerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            //Return total pages, one for each image type
            return 5;
        }

        //Create the given page (indicated by position)
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View page = inflater.inflate(R.layout.image_pager_view, null);
            createdImage = (Painting) drawables.get(position);

            ((ImageView) page.findViewById(R.id.createdImage)).setImageDrawable(createdImage);
//            ((ImageView)page.findViewById(R.id.createdImage)).setImageBitmap(createdImage.createBitmap());

//            if (saved[position]) {
//                ((TextView)findViewById(R.id.textView7)).setText(R.string.image_saved);
////                ((ImageView) page.findViewById(R.id.createdImage2)).setImageDrawable(new ImageSavedorDeleted(true));
//            }
//            if (deleted[position]) {
//                ((TextView)findViewById(R.id.textView7)).setText(R.string.image_not_saved);
////                ((ImageView) page.findViewById(R.id.createdImage2)).setImageDrawable(new ImageSavedorDeleted(false));
//            }

            //Add the page to the front of the queue
            ((ViewPager) container).addView(page, 0);
//            createdImage = (Painting)drawables.get(position);
            imageCreated = true;
            return page;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            //See if object from instantiateItem is related to the given view
            //required by API
            return arg0 == (View) arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
            object = null;
        }

        //forces an update to images when notifyDataChange is called
        public int getItemPosition(Object object) {
            return POSITION_NONE;
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
        findViewById(R.id.pager).setVisibility(View.INVISIBLE);

        ((ImageView) findViewById(R.id.createdImageDisplayImage
        )).setImageBitmap(BitmapFactory.decodeFile(currentImagePath));

    }

    public void createAndDisplayImages() {

        imageCreated = false;
        dataStrings = new ArrayList<>();

        if (getIntent().getExtras() != null) {

            dataSets = (ArrayList<DataSet>) this.getIntent().getExtras().getSerializable(getString(R.string.data_sets));

            for (DataSet dataSet : dataSets) {
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

//        createImage();
        createDrawables();
        createdSaveAndDeleteddStatusArrays();
    }

    private void createdSaveAndDeleteddStatusArrays() {
        saved = new boolean[drawables.size()];
        deleted = new boolean[drawables.size()];

        for (boolean savedStatus : saved) {
            savedStatus = false;
        }

        for (boolean deletedStatus : deleted) {
            deletedStatus = false;
        }
    }

    private void createDrawables() {
        drawables = new ArrayList<>();

        drawables.add(new AbstractShapes(this, dataSets));
        drawables.add(new AutomaticDrawing(this, dataSets));
        drawables.add(new AlbersImage(this, dataSets));
        drawables.add(new Landscape(this, dataSets));
        drawables.add(new KineticArt(this, dataSets));

    }

//    private void createImage(){
    //
//        if(imageType.equals(getString(R.string.albers_image))){
//            createNewAlbersImage();
//        }else if(imageType.equals(getString(R.string.abstract_shapes))){
//            createNewAbstractShapesImage();
//        }else{
//            createNewAutomaticDrawingImage();
//        }
//    }

//    public void createNewAbstractShapesImage(){
//        createdImage = new AbstractShapes(this,  dataSets);
//        ((ImageView) findViewById(R.id.createdImage)).setImageDrawable(createdImage);
//        //   ((ImageView) findViewById(R.id.createdImage)).setImageBitmap(createdImage.createBitmap());
//        imageCreated = true;
//    }
//
//    public void createNewTextImage(){
//        createdImage = new TextImage(this,  dataSets);
//        ((ImageView) findViewById(R.id.createdImage)).setImageDrawable(createdImage);
//     //   ((ImageView) findViewById(R.id.createdImage)).setImageBitmap(createdImage.createBitmap());
//        imageCreated = true;
//    }
//
//    public void createNewAlbersImage(){
//        createdImage = new AlbersImage(this, dataSets);
//        ((ImageView) findViewById(R.id.createdImage)).setImageDrawable(createdImage);
//  //      ((ImageView) findViewById(R.id.createdImage)).setImageBitmap(createdImage.createBitmap());
//        imageCreated = true;
//    }
//
//    public void createNewAutomaticDrawingImage(){
//        createdImage = new AutomaticDrawing(this,dataSets);
//        ((ImageView) findViewById(R.id.createdImage)).setImageDrawable(createdImage);
//        //      ((ImageView) findViewById(R.id.createdImage)).setImageBitmap(createdImage.createBitmap());
//        imageCreated = true;
//    }
//
//    public void createNewLandscapeImage(){
//        createdImage = new Landscape(this,dataSets);
//        ((ImageView) findViewById(R.id.createdImage)).setImageDrawable(createdImage);
//        //      ((ImageView) findViewById(R.id.createdImage)).setImageBitmap(createdImage.createBitmap());
//        imageCreated = true;
//    }
//
//    public void createNewKineticImage(){
//        createdImage = new KineticArt(this,dataSets);
//        ((ImageView) findViewById(R.id.createdImage)).setImageDrawable(createdImage);
//        //      ((ImageView) findViewById(R.id.createdImage)).setImageBitmap(createdImage.createBitmap());
//        imageCreated = true;
//    }


    public void save(View view) {

        if (imageCreated) {

            //find the currently viewed drawable
            int currentItem = vp.getCurrentItem();

            // create an empty file for it in the correct location
            setCurrentImagePath(getString(R.string.album_storage_name));
            createdImage = (Painting) (drawables.get(currentItem));


            // check if we can write to external storage
            if (isExternalStorageWritable()) {

                //output new file to new imagePath
                try {
                    File file = new File(currentImagePath);
                    FileOutputStream out = new FileOutputStream(file);

                    Bitmap bitmap = createdImage.createBitmap();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                    out.flush();
                    out.close();

                    Toast.makeText(getApplicationContext(), "Saved successfully", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.keepButton).setVisibility(View.INVISIBLE);


                    saved[currentItem] = true;
                    deleted[vp.getCurrentItem()] = false;
                    vp.getAdapter().notifyDataSetChanged();


                    galleryAddPic();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // goToGallery();
    }

    public void setCurrentImagePath(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);

        if (!file.mkdirs()) {
            Log.e("no directory", "Directory not created");
        }

        Log.e("filename", file.getAbsolutePath());

        // create a couple of strings to name the image file with
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());

        String imageFileName = timeStamp + "_" + createdImage.getClass().getSimpleName();

        // create a temporary file using the directory, created filename and a jpg suffix
//        File image = null;
//        try {
//            image = File.createTempFile(
//                    imageFileName,  /* prefix */
//                    ".jpg",         /* suffix */
//                    file      /* directory */
//            );
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //store the path to use for this image as currentImagePath
        currentImagePath = file + "/" + imageFileName + ".jpg";
        System.out.println("file: " + currentImagePath);
    }


    //helper method to check if external storage can be written to
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    // helper method to enable the android system to recognise a new file has been added to an external folder
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentImagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    public void data(View view) {


        Intent intent = new Intent(this, DataDisplay.class);

        intent.putStringArrayListExtra("dataStrings", dataStrings);

        startActivity(intent);

        //share image using other applications? Investigate possibilities
    }

    public void discard(View view) {

        File file = new File(currentImagePath);

        if (file.exists()) {
            if (file.delete()) {
                Toast.makeText(getApplicationContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
            }
        }

        if (imageCreated) {

            findViewById(R.id.discardButton).setVisibility(View.INVISIBLE);
            findViewById(R.id.keepButton).setVisibility(View.VISIBLE);
            deleted[vp.getCurrentItem()] = true;
            saved[vp.getCurrentItem()] = false;
            vp.getAdapter().notifyDataSetChanged();

        } else {

            goToGallery(view);
        }

    }

    public void goToGallery(View view) {
        Intent intent = new Intent(this, Gallery.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }


}
