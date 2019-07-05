package com.example.workinprogress;


import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class CreateImage extends Drawable {

    private final Paint redPaint;
    private final Paint bluePaint;
    private final Paint greenPaint;
    private final Paint orangePaint;

    private Context context;
    private int[] lightColours;
    private int[] lightColours2;
    private int[] lightColours3;
    private int firstLightFigure;
    private Canvas canvas;
    private final int lightRange = 40000;
    private int textSize = 25;

    private ArrayList<Float> lightLevels;
    private ArrayList<double[]> locations;
    private ArrayList<Position> positions;

    public CreateImage(Context context, ArrayList<Float> lightLevels,
                       ArrayList<double[]> locations, ArrayList<Position> positions){

        this.locations = locations;
        this.lightLevels = lightLevels;
        this.positions = positions;
        this.context = context;

        redPaint = new Paint();

        bluePaint = new Paint();
        bluePaint.setARGB(255,40,60,150);
        bluePaint.setTextSize(textSize);

        orangePaint = new Paint();
        orangePaint.setARGB(255,150,190,0);
        orangePaint.setTextSize(textSize);

        greenPaint = new Paint();
        greenPaint.setARGB(255,40,180,70);
        greenPaint.setTextSize(textSize);

//        setLightColours();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void draw(Canvas canvas) {

        this.canvas = canvas;

        int x = 60;
        int y = 30;

        for(Float lightLevel: lightLevels){

            String lightLevelAsString = lightLevel+"";
            canvas.drawText(lightLevelAsString,x,y,greenPaint);
            y+=textSize+1;
            Log.i("lightlevels",lightLevelAsString);
        }

        y = 30;

        for (double[] location: locations){

            String longitude = location[0]+"";
            String latitude = location[1]+"";
            String longAndLat = "lat: "+latitude+" long: "+longitude;

            canvas.drawText(longAndLat,x+120,y,orangePaint);
            y+=textSize+1;
            Log.i("location",longAndLat);
        }

        y = 30;

        for(Position position: positions){

            String positionString = "x: "+position.getxAxisString()+" y: "
                    +position.getyAxisString()+" z: "+position.getzAxisString();

            canvas.drawText(positionString,x+550,y,bluePaint);
            y+=textSize+1;
            Log.i("position",positionString);
        }





//        int[] bounds1 = new int[]{300,30,800,1000};
//        int[] bounds2 = new int[]{100,400,900,1200};
//        int[] bounds3 = new int[]{850,1050,1050,1250};

//        lightLevels(lightColours,lightBounds()).draw(canvas);
//        lightLevels(lightColours2,bounds2).draw(canvas);
//        lightLevels(lightColours3,bounds3).draw(canvas);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private GradientDrawable lightLevels(int[] lightColours,int[] bounds){

        Log.i("bounds",""+bounds[0]+" "+bounds[1]+" "+bounds[2]+" "+bounds[3]);
        Log.i("colours",""+lightColours[0]+" "+lightColours[1]);

        GradientDrawable shapeGradient = new GradientDrawable();
        shapeGradient.setShape(GradientDrawable.OVAL);
        shapeGradient.setColors(lightColours);
        shapeGradient.setBounds(bounds[0],bounds[1],bounds[2],bounds[3]);
        return shapeGradient;
    }


//    private void setLightColours(){
//
//        int firstLightFigure = (int)Double.parseDouble(light); //grab the value of the light
//
//        int colourRange = 0x0FFFFFFF; //store the range of colours as an int
//        int startingPoint = 0x80000000;
//
//        int colourRangeForUse = colourRange - startingPoint;
//
//        int lightMultiplier = colourRangeForUse/lightRange;
//
//        firstLightFigure *= lightMultiplier;
//        firstLightFigure += startingPoint;
//        int secondLightFigure = (int)(Math.random()*400000)*lightMultiplier;
//
//        lightColours = new int[]{firstLightFigure,secondLightFigure};
//        lightColours2 = new int[]{(firstLightFigure+colourRange)%firstLightFigure,(secondLightFigure+colourRange)%secondLightFigure};
//        lightColours3 = new int[]{colourRange-firstLightFigure,colourRange-secondLightFigure};
//
//    }

//    private int[] lightBounds(){
//
//        Log.i("lightboundsMethod","hello i'm in here");
//
//        int firstLightFigure = (lightRange-(int)Double.parseDouble(light)-5000);
//
//        double widthRange = canvas.getWidth();
//        double heightRange = canvas.getHeight();
//
//        Log.i("ranges",""+widthRange+" "+heightRange);
//        Log.i("ranges",""+(widthRange/(double)lightRange)*(double)firstLightFigure);
//        Log.i("ranges","first light figure "+firstLightFigure);
//
//
//        int x1 = (int)((widthRange/(double)lightRange)*(double)firstLightFigure);
//        int y1 = (int)((heightRange/(double)lightRange)*(double)firstLightFigure);
//
//        Log.i("x2 and y2",""+(Math.random()*(widthRange-x1)));
//
//        int x2 = x1+(int)(Math.random()*(widthRange-x1));
//        int y2 = y1+(int)(Math.random()*(heightRange-x1));
//
//        Log.i("integers",""+x1+"  "+y1+"  "+x2+"  "+y2);
//
//        return new int[]{x1,y1,x2,y2};
//
//    }


    @Override
    public void setAlpha(int alpha) {
        // This method is required
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        // This method is required
    }

    @Override
    public int getOpacity() {
        // Must be PixelFormat.UNKNOWN, TRANSLUCENT, TRANSPARENT, or OPAQUE
        return PixelFormat.OPAQUE;
    }
}