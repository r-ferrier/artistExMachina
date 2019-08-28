package com.example.workinprogress;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.workinprogress.dataSetsAndComponents.classesForTestingOutputs.CSVDataSet;
import com.example.workinprogress.dataSetsAndComponents.DataSet;
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
    private String currentImagePath;
    private Painting createdImage;
    private ArrayList<String> dataStrings;
    private boolean[] saved;
    private boolean[] deleted;
    private ArrayList<DataSet> dataSets;
    private LayoutInflater inflater;
    private ViewPager viewPager;
    private ArrayList<Drawable> drawables;
    private String TAG = "display image info";



    /**
     * onCreate activity_display_image is assigned as content view and intent is checked for extras.
     * Checks to see if it has been passed a true boolean to create a new image, and if it has, sets
     * up the view to show 5 new images and creates them. If it hasn't, it looks for the string it
     * should have been passed containing the image path instead and sets up the view to show this
     * one single saved image. Once the view has been fully instantiated, whichever image is currently
     * being viewed can be permanently saved or deleted, and once it is saved, shared.
     * @param savedInstanceState //
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);


        if (getIntent().getExtras() != null) {

            createNewImage = getIntent().getBooleanExtra("createImage", false);

            if (!createNewImage) {
                currentImagePath = getIntent().getStringExtra("imageLocation");
                findViewById(R.id.keepButton).setVisibility(View.INVISIBLE);
                findViewById(R.id.imageSavedText).setVisibility(View.INVISIBLE);
                findViewById(R.id.shareButton).setVisibility(View.VISIBLE);
                findViewById(R.id.invisiButton3).setVisibility(View.INVISIBLE);
                displayExistingImage();
            } else {
                createAndDisplayImages();

                findViewById(R.id.discardButton).setVisibility(View.INVISIBLE);
                findViewById(R.id.shareButton).setVisibility(View.GONE);
                findViewById(R.id.invisiButton3).setVisibility(View.GONE);
                inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //Reference ViewPager defined in activity
                viewPager = findViewById(R.id.pager);
                //set the adapter that will create the individual pages
                viewPager.setAdapter(new ImagePagerAdapter(this));

                TabLayout tabLayout = findViewById(R.id.dots);
                tabLayout.setupWithViewPager(viewPager, true);


                viewPager.addOnPageChangeListener(this);

            }
        }
    }


    /**
     * If a new image has been created, when the page is scrolled to view each image relevant data
     * about the status of each image must be updated on the page. This method checks and displays
     * whether the image is saved or not, and sets relevant buttons and text to reflect this.
     * @param position current scroll position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        int currentItem = viewPager.getCurrentItem();
        createdImage = (Painting) (drawables.get(currentItem));

        TextView text = findViewById(R.id.imageSavedText);
        ImageButton keepButton = findViewById(R.id.keepButton);
        ImageButton deleteButton = findViewById(R.id.discardButton);

        if (saved[position]) {
            keepButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            findViewById(R.id.shareButton).setVisibility(View.VISIBLE);
            findViewById(R.id.invisiButton3).setVisibility(View.INVISIBLE);
            text.setText(R.string.image_saved);
            text.setTextColor(getResources().getColor(R.color.iguanaGreen));

        } else {
            keepButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
            findViewById(R.id.shareButton).setVisibility(View.GONE);
            findViewById(R.id.invisiButton3).setVisibility(View.GONE);
            text.setText(R.string.image_not_saved);
            text.setTextColor(getResources().getColor(R.color.darkGrey));
        }

    }

    @Override
    public void onPageSelected(int position) {
    //not required
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    //not required
    }


    /**
     * This class will allow individual pages to be made for each newly created image, which can then
     * be placed in the view so that the user can swipe between them
     */
    class ImagePagerAdapter extends PagerAdapter {

        private Context context;

        private ImagePagerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            //Return total pages, one for each image type
            return drawables.size();
        }

        //Create the given page (indicated by position)
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View page = inflater.inflate(R.layout.image_pager_view, null);
            createdImage = (Painting) drawables.get(position);

            ((ImageView) page.findViewById(R.id.createdImage)).setImageDrawable(createdImage);

            //Add the page to the front of the queue
            container.addView(page, 0);
            imageCreated = true;
            return page;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            //this is required to check if the object instantiated is related to the view
            return arg0 == (View) arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            object = null;
        }

        //this forces images to update when notifyDataChange is called
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


    }

    /**
     * animation to be displayed before created images are shown. Manipulation of the view must be
     * done with the main thread (samsung devices crash otherwise), so as animation runs,
     * it posts tasks back onto the main thread using a handler.
     * @param animationText textView which will be animated
     */
    private void animateWaitingScreen(TextView animationText) {
        try {
            Thread.sleep(200);
            new Handler(Looper.getMainLooper()).post(
                    new Runnable() {
                        @Override
                        public void run() {
                            animationText.setText(R.string.artistBusy2);
                        }
                    }
            );
            Thread.sleep(200);
            new Handler(Looper.getMainLooper()).post(
                    new Runnable() {
                        @Override
                        public void run() {
                            animationText.setText(R.string.artistBusy3);
                        }
                    }
            );

            Thread.sleep(200);
            new Handler(Looper.getMainLooper()).post(
                    new Runnable() {
                        @Override
                        public void run() {
                            animationText.setText(R.string.artistBusy4);
                        }
                    }
            );

            Thread.sleep(200);
            new Handler(Looper.getMainLooper()).post(
                    new Runnable() {
                        @Override
                        public void run() {
                            animationText.setText(R.string.artistBusy1);
                        }
                    }
            );

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * called to set up the view if it is displaying a saved image from the gallery
     */
    public void displayExistingImage() {

        if(createNewImage){
            Log.e(TAG,"Should not be displaying existing image, should be creating new images");
        }

        findViewById(R.id.artistBusyView).setVisibility(View.INVISIBLE);
        findViewById(R.id.pager).setVisibility(View.INVISIBLE);

        ((ImageView) findViewById(R.id.createdImageDisplayImage
        )).setImageBitmap(BitmapFactory.decodeFile(currentImagePath));

    }

    /**
     * checks intent for datasets and adds a toString version of each dataset to the datastrings array.
     * starts new thread to run animation, and then calls helper methods to create drawables and set
     * up arrays to store the saved status of each image when they are scrolled through
     */
    public void createAndDisplayImages() {

        if(!createNewImage){
            Log.e(TAG,"Should not be creating new images, should be displaying currently stored image");
        }

        //enables log in painting class to log out which datasets are being used
        Painting.firstInstanceOfPainting = true;
        imageCreated = false;
        dataStrings = new ArrayList<>();

        if (getIntent().getExtras() != null) {

            dataSets = (ArrayList<DataSet>) this.getIntent().getExtras().getSerializable(getString(R.string.data_sets));

            for (DataSet dataSet : dataSets) {
                dataStrings.add(dataSet.toString());
            }

        }

        // thread animated waiting screen a minimum of three times, and will continue to animate it
        // until all drawables have been created.
        Thread thready = new Thread(() -> {
            TextView animationtext = findViewById(R.id.artistBusyText);

            for (int i = 0; i < 3; i++) {
                animateWaitingScreen(animationtext);
            }
            while (!imageCreated) {
                animateWaitingScreen(animationtext);
            }

            findViewById(R.id.artistBusyView).setVisibility(View.INVISIBLE);
        });
        thready.start();

        /*special method that can be used to insert csv inputs into images*/
        /*----------------------------------------*/
//        testImages();
        /*----------------------------------------*/

        createDrawables();
        createdSaveAndDeleteddStatusArrays();
    }

    private void createdSaveAndDeleteddStatusArrays() {
        saved = new boolean[drawables.size()];
        deleted = new boolean[drawables.size()];

        for(int i = 0; i<saved.length;i++){
            saved[i] = false;
            deleted[i] = false;
        }

    }

    /**
     * This list must contain every type of painting that the app is going to draw. No other parameters
     * in this class need to be changed to add a new type of painting
     */
    private void createDrawables() {
        drawables = new ArrayList<>();
        drawables.add(new AbstractShapes(this, dataSets));

      //this is to prevent log from logging out information for every drawable, just needs calling after first one is created
        Painting.firstInstanceOfPainting = false;

        drawables.add(new AutomaticDrawing(this, dataSets));
        drawables.add(new AlbersImage(this, dataSets));
        drawables.add(new Landscape(this, dataSets));
        drawables.add(new KineticArt(this, dataSets));

    }

    /**
     * method to save images. Calls on helper method to create file path and then writes file to it.
     * File will be stored in external storage within the users device in order to allow sharing and
     * use of the image by other apps. Also to allow for easy deletion of mutliple images by the user
     * using their device's existing file browsing apps and system. Also saves the accompanying data
     * as a string in the images metadata so that it can be retrieved and displayed alongside the image.
     * @param view called by onclick method for the save button in the displayImage view
     */
    public void save(View view) {

        if (imageCreated) {

            findViewById(R.id.shareButton).setVisibility(View.VISIBLE);

            //find the currently viewed drawable
            int currentItem = viewPager.getCurrentItem();

            // create a new file path for it in the correct location
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

                    //update saved status and notify viewPager of changes to information
                    saved[currentItem] = true;
                    deleted[viewPager.getCurrentItem()] = false;
                    viewPager.getAdapter().notifyDataSetChanged();

                    // create new Exifinterface to enable changes to be made to image's metadata
                    final ExifInterface exif = new ExifInterface(currentImagePath);
                    // create string containing data information to be saved as image description
                    String exifString = "";
                    for(String dataString: dataStrings){
                        exifString+= dataString;
                        exifString+="\n";
                    }
                    //set string as image description and save to image
                    exif.setAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION, exifString);
                    exif.saveAttributes();

                    //helper method to ensure device is alerted of existence of a new file
                    galleryAddPic();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * creates a unique file name using the type of image that was created and the time it was
     * created at
     * @param albumName name for the folder in which images should be stored
     */
    public void setCurrentImagePath(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);

        if (file.mkdirs()) {
            Log.e(TAG, "Directory did not exist, has been newly created.");
        }

        // create timestamp capturing the second at which image was created, and add string of image type
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_" + createdImage.getClass().getSimpleName();

        //store the path to use for this image as currentImagePath
        currentImagePath = file + "/" + imageFileName + ".jpg";

        Log.i(TAG, "file path for current image: "+currentImagePath);
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

    /**
     * instantiates a new dataView. Takes the string stored in the image as metadata under image description
     * and passes this as a string extra, along with the strings stored in data strings. In this way,
     * regardless of whether the data is stored in the datastrings or the extra, the dataview can create itself
     * in the same way
     * @param view called by onlclick method of the data button in display image view
     */
    public void data(View view) {

        String data = "";

        if(!imageCreated) {
            try {
                ExifInterface exifInterface = new ExifInterface(currentImagePath);
                data = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(this, DataDisplay.class);
        intent.putStringArrayListExtra("dataStrings", dataStrings);
        intent.putExtra("data",data);
        intent.putExtra("newImageCreated",createNewImage);
        startActivity(intent);

    }

    /**
     * checks to see if the image that is currently being viewed has been saved, and if it has, deletes it.
     * Updates viewpager and saved statuses. If the image was just created, the user can save it again if
     * they change their mind. If not, they are returned to the gallery view.
     * @param view called by the onclick method from the delete button
     */
    public void delete(View view) {

        File file = new File(currentImagePath);

        if (file.exists()) {
            if (file.delete()) {
                Toast.makeText(getApplicationContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
            }
            if(file.exists()){
                Log.e(TAG,"file not deleted");
            }
        }

        if (imageCreated) {

            findViewById(R.id.discardButton).setVisibility(View.INVISIBLE);
            findViewById(R.id.keepButton).setVisibility(View.VISIBLE);
            deleted[viewPager.getCurrentItem()] = true;
            saved[viewPager.getCurrentItem()] = false;
            viewPager.getAdapter().notifyDataSetChanged();

        } else {
            goToGallery(view);
        }

    }

    public void goToGallery(View view) {
        Intent intent = new Intent(this, Gallery.class);
        startActivity(intent);
    }

    /**
     * when back is pressed, if the image was just created then instead of returning to the portrait
     * screen the user will be returned to the home screen.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(imageCreated) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }


    /**
     * sharing is only possible once an image has been saved to external storage. Once it has, this
     * method can be called to create a sharing intent and share the image with any other apps in
     * the user's device that enable image sharing.
     * @param view called by the onclick method from the share button
     */
    public void share(View view) {

        // create new sharing intent to send a file with and flag up to clear the activity when reset
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // find file at current image path
        File file = new File(currentImagePath);

        if(file.exists()) {
            // create uri using file and provider specified in manifest
            Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);

            // grant read and write permission to the sharing intent and then add the uri and datatype
            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            sharingIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            sharingIntent.setDataAndType(uri, "image/*");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

            // begin the sharingintent
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }else{
            Toast.makeText(getApplicationContext(), "Unable to share image", Toast.LENGTH_SHORT).show();
            Log.e(TAG,"file not found at this location: "+currentImagePath);
        }

    }

    private void testImages(){
        //creates a new CSVDataSet which will replace existing data, to allow visual testing of images
        CSVDataSet CSVDataSet = new CSVDataSet(this,true,"dataToUpload.csv");
        dataSets = CSVDataSet.getDataSets();
        dataStrings = new ArrayList<>();
        for (DataSet dataSet : dataSets) {
            dataStrings.add(dataSet.toString());
        }
    }
}
