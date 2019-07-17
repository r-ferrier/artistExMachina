package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.workinprogress.dataSetsAndComponents.DataSetPoint;
import com.example.workinprogress.DisplayImage;

import java.util.ArrayList;
import java.util.Random;

public class AlbersImage extends Painting {

    private Paint paint1;
    private Paint paint2;
    private Paint paint3;
    private Paint paint4;
    private Paint paint5;
    private Canvas canvas;

//    private int maximumLightValue = 1000;


    public AlbersImage(Context context, ArrayList<Integer> lightLevels, ArrayList<DataSetPoint> locations, ArrayList<DataSetPoint> positions, ArrayList<Integer> steps, ArrayList<Integer> distance) {
        super(context, lightLevels, locations, positions, steps, distance);

        paint1 = new Paint();
        paint2 = new Paint();
        paint3 = new Paint();
        paint4 = new Paint();

        System.out.println(lightLevels);

    }


    public void draw(Canvas canvas){

        this.canvas = canvas;
        canvas.drawColor(Color.WHITE);
        width = getBounds().width();
        height = getBounds().height();

        firstLightLevelsExperiment();
        secondLightLevelsExperiment();


    }


    public void firstLightLevelsExperiment(){


        int xStartingPoint = 0;
        int yStartingPoint;
        int highestLightLevel = 0;

        Random random = new Random();
        paint1.setARGB(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));


        for(Integer lightLevel: lightLevels) {

            // following code limits colour selection options
            if ((lightLevel * (255 / DisplayImage.IMAGE_SIZE_SCALAR)) > highestLightLevel) {
                highestLightLevel = lightLevel * (255 / DisplayImage.IMAGE_SIZE_SCALAR);
            }


            if (highestLightLevel > 256) {
                highestLightLevel = 256;
            }
        }

        for(Integer lightLevel: lightLevels){

            //set height as linked to lightlevel
            yStartingPoint = (height/ DisplayImage.IMAGE_SIZE_SCALAR)*(lightLevel);
            //draw rectangle at hightlevel
            canvas.drawRect(xStartingPoint,yStartingPoint,xStartingPoint+150,yStartingPoint+150,paint1);
            //reposition to the right for next lightlevel
            xStartingPoint+=20;

            paint1.setARGB(random.nextInt(highestLightLevel), random.nextInt(highestLightLevel), random.nextInt(highestLightLevel), random.nextInt(highestLightLevel));
            //colour selection + opacity limited to the highest lightvalue - can choose from a random assortment of colours up to but not over that light amount
        }

        xStartingPoint = 0;


        //repeat but lower down

        for(Integer lightLevel: lightLevels){

            yStartingPoint = ((height/ DisplayImage.IMAGE_SIZE_SCALAR)*(lightLevel))+100;
            canvas.drawRect(xStartingPoint,yStartingPoint,xStartingPoint+150,yStartingPoint+150,paint1);
            xStartingPoint+=20;
            paint1.setARGB(random.nextInt(256), random.nextInt(256), random.nextInt(256), random.nextInt(256));
        }

    }

    public void secondLightLevelsExperiment(){

        int xStartingPoint = 0;
        int yStartingPoint;
        int lightColourLevel = 0;
        Random random = new Random();
        int[] lightColourLevels = new int[]{0,0,0,0};


        for(Integer lightLevel: lightLevels){

            //set colour as linked to lightlevel
            lightColourLevel = lightLevel*(255/DisplayImage.IMAGE_SIZE_SCALAR);

            //randomly assign this colour level to one channel
            lightColourLevels[random.nextInt(4)] = lightColourLevel;
            //set colour to current colours held in lightlevels array
            paint1.setARGB(lightColourLevels[0], lightColourLevels[1], lightColourLevels[2], lightColourLevels[3]);

            //set height as linked to lightlevel
            yStartingPoint = (height/ DisplayImage.IMAGE_SIZE_SCALAR)*(lightLevel);
            //draw rectangle at hightlevel
            canvas.drawRect(xStartingPoint,yStartingPoint,xStartingPoint+350,yStartingPoint+350,paint1);
            //reposition to the right for next lightlevel
            xStartingPoint+=20;

        }

        xStartingPoint = 0;



    }


}
