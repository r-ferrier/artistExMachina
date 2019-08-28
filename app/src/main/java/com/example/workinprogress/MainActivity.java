package com.example.workinprogress;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.workinprogress.paintings.shapes.Shape;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int animationDuration1 = 100;
    private int animationDuration2 = 120;
    private int animationDuration3 = 5000;
    private int animationDuration4 = 6000;
    private int animationPause1 = 3000;
    private int animationPause2 = 2000;
    private ObjectAnimator moveButtons;
    private static final int REQUEST_OAUTH = 1;
    private boolean allPermissionsGranted = false;

    /**
     * On creation the mainActivity will set up content, request any necessary permissions, collect aggregate
     * data and create an animation for its home screen
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runAnimations();
        requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_OAUTH: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = true;
                } else {
                    allPermissionsGranted = false;
                    Toast.makeText(MainActivity.this, "You must accept permissions to use this app.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission
                (this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission
                (this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions
                    (this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else{
            allPermissionsGranted=true;
        }

    }



    /**
     * Method to force animation of buttons to end on click so that users do not have to wait for animation
     * to complete when not opening app for the first time
     * @param view called by onClick method of the layout
     */
    public void stopAnimation(View view){
        moveButtons.cancel();
        View buttonsLayout = findViewById(R.id.buttonPanel);
        buttonsLayout.setTranslationY(0);
    }

    /**
     * Calls on helper methods that are used to generate animations for the home screen
     */
    private void runAnimations() {
        crossfadeText();
        moveButtonsUpFromBottom();
        drawShapes();
    }

    /**
     * translates buttons from outside the screen at the bottom to their set position onscreen
     */
    private void moveButtonsUpFromBottom() {
        View buttonsLayout = findViewById(R.id.buttonPanel);
        moveButtons = ObjectAnimator.ofFloat(buttonsLayout, "translationY", 1000f, 0f, 30f);
        buttonsLayout.setVisibility(View.VISIBLE);
        moveButtons.setDuration(animationDuration4);
        moveButtons.start();
    }

    /**
     * fades title text from black to white
     */
    private void crossfadeText() {
        TextView titleText = findViewById(R.id.titleText);
        TextView titleText2 = findViewById(R.id.titleText2);

        titleText.setVisibility(View.GONE);
        int longAnimationDuration = animationDuration3;

        titleText.setAlpha(0f);
        titleText.setVisibility(View.VISIBLE);
        titleText.animate().alpha(1f).setDuration(longAnimationDuration).setListener(null);
        titleText2.animate().alpha(0f).setDuration(longAnimationDuration).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                titleText2.setVisibility(View.GONE);
            }
        });
    }

    /**
     * creates animated background from lists of shapes obtained from MainClassAnimation class
     */
    private void drawShapes() {

        FrameLayout animatedShapesDrawing = findViewById(R.id.animationPortion);
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewTreeObserver observer = animatedShapesDrawing.getViewTreeObserver();

        //animation cannot be completed until layout has finished so listener added to determine when
        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //as soon as layout is complete, listener is removed
                        animatedShapesDrawing.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        //animation can now be constructed and run using the width and height of the layout
                        MainClassAnimation mainClassAnimation = new MainClassAnimation(animatedShapesDrawing.getWidth(), animatedShapesDrawing.getHeight());

                        ArrayList<ImageView> shapeImageViews = inflateViewsForAnimation(inflater, mainClassAnimation.getShapes());
                        ArrayList<ImageView> thinShapeImageViews = inflateViewsForAnimation(inflater, mainClassAnimation.getThinShapes());
                        ArrayList<ImageView> thinnestShapeImageViews = inflateViewsForAnimation(inflater, mainClassAnimation.getThinnestShapes());

                        //three threads created, each one calls helper method with different shape arrays after a small pause
                        Thread shapesThread = new Thread(() -> {
                            try {
                                Thread.sleep(animationPause1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            runnableTask(mainClassAnimation.getShapes(), shapeImageViews, animatedShapesDrawing,animationDuration1);
                        });

                        Thread thinShapesThread = new Thread(() -> {
                            try {
                                Thread.sleep(animationPause2);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            runnableTask(mainClassAnimation.getThinShapes(), thinShapeImageViews, animatedShapesDrawing,animationDuration1);

                        });

                        Thread thinnestShapesThread = new Thread(() -> runnableTask(mainClassAnimation.getThinnestShapes(), thinnestShapeImageViews, animatedShapesDrawing, animationDuration2));

                        //run threads
                        shapesThread.start();
                        thinShapesThread.start();
                        thinnestShapesThread.start();
                    }
                });
    }

    /**
     * helper method for drawshapes method, used to place shapes on background one by one. This method
     * is not called by the main thread so it calls sleep first, before using a handler to post tasks back onto
     * the main thread in order to update the views
     * @param shapes list of drawables
     * @param shapeImageViews list of imageviews into which drawables should be inserted
     * @param animatedShapesDrawing
     * @param sleepDuration
     */
    private void runnableTask(ArrayList<Shape> shapes, ArrayList<ImageView> shapeImageViews, FrameLayout animatedShapesDrawing, int sleepDuration) {
        for (int i = 0; i < shapes.size(); i++) {

            try {
                Thread.sleep(sleepDuration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //get the imageview and shape and declare them as final so they can be accessed by the Handler
            final ImageView imageView = shapeImageViews.get(i);
            final Shape shape = shapes.get(i);

            new Handler(Looper.getMainLooper()).post(
                    () -> {
                        //runnable to add new view to the frameLayout and add the drawable to that view
                        animatedShapesDrawing.addView(imageView);
                        imageView.setImageDrawable(shape);
                    }
            );
        }
    }

    /**
     * helper method for the drawShapes method, creates a new imageview for each shape. Imageview is the size of
     * the full screen and will be placed into a frame layout so that many can be layed on top of each other
     * @param inflater LayoutInflater
     * @param shapes ArrayList of Shapes
     * @return
     */
    private ArrayList<ImageView> inflateViewsForAnimation(LayoutInflater inflater, ArrayList<Shape> shapes) {
        ArrayList<ImageView> shapeImageViews = new ArrayList<>();

        for (Shape shape : shapes) {
            shapeImageViews.add((ImageView) inflater.inflate(R.layout.single_image, null));
        }
        return shapeImageViews;
    }

    public void beginShortPortraitActivity(View view) {
        if(allPermissionsGranted) {
            Intent intent = new Intent(this, ShortPortrait.class);
            startActivity(intent);
        }else{
            requestPermissions();
        }
    }

    public void beginGalleryActivity(View view) {
        if(allPermissionsGranted) {
            Intent intent = new Intent(this, Gallery.class);
            startActivity(intent);
        }else{
            requestPermissions();
        }
    }

    public void beginAboutActivity(View view) {
        if(allPermissionsGranted) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
        }else{
            requestPermissions();
        }
    }


}
