package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.example.workinprogress.dataSetsAndComponents.DataSet;

import java.util.ArrayList;
import java.util.Random;

public class ColoursExperiment extends PositionAndLightPainting {

    private Paint paint;
    private Paint textPaint;
    private Paint paint2;
    private Paint paint3;
    private Paint paint4;
    private Paint paint5;
    private Paint paint6;
    private Paint paint7;
    private Paint paint8;


    /**
     * stores datasets in their own datapolint specific arraylists.
     *
     * @param context  context is required to enable this class to access view elements. Must be passed
     *                 as a variable because the class needs to extend drawable.
     * @param dataSets arrayList of any data to be used by the application to draw images
     */
    public ColoursExperiment(Context context, ArrayList<DataSet> dataSets) {

        super(context, dataSets);
        paint = new Paint();
        paint2 = new Paint();
        paint3 = new Paint();
        paint4 = new Paint();
        paint5 = new Paint();
        paint6 = new Paint();
        paint7 = new Paint();
        paint8 = new Paint();
        textPaint = new Paint();
        textPaint.setTextSize(40);

    }

    public void draw(Canvas canvas) {

        canvas.drawColor(Color.WHITE);

        width = canvas.getWidth();
        height = canvas.getHeight();

        int xStart = 20;
        int yStart = 70;

        int xLimit = canvas.getWidth()-50;
        int yLimit = canvas.getHeight()-50;

        Random random = new Random();

        int[] lights = new int[]{200,234,244,73,45,233,210,120,178,156,190,150,174,238,221,179,110,85,203,221,212};
        int[] lights2 = new int[]{10,234,14,73,45,183,53,200,34,120,75,223,29,245,82,176,200,76,146,43,188,66,32,3};
        int[] lights3 = new int[]{5,220,34,235,23,45,63,42,13,25,47,62,123,87,85,34,152,134,22,54,44,32,22,16,78,65,32,4};


        ArrayList<int[]> colourValues = new ArrayList<>();
        ArrayList<int[]> colourValues2 = new ArrayList<>();
        ArrayList<int[]> colourValues3 = new ArrayList<>();
        ArrayList<int[]> colourValues4 = new ArrayList<>();
        ArrayList<int[]> colourValues5 = new ArrayList<>();
        ArrayList<int[]> colourValues6 = new ArrayList<>();
        ArrayList<int[]> colourValues7 = new ArrayList<>();
        ArrayList<int[]> colourValues8 = new ArrayList<>();

        colourValues.add(new int[]{255,0,0,0});
        colourValues2.add(new int[]{255,0,0,0});
        colourValues3.add(new int[]{255,255,255,255});
        colourValues4.add(new int[]{255,255,255,255});
        colourValues5.add(new int[]{0,0,0,0});
        colourValues6.add(new int[]{255,0,0,0});
        colourValues7.add(new int[]{255,0,0,0});
        colourValues8.add(new int[]{255,0,0,0});


        // canvas.drawText("1",50,40,textPaint);
        canvas.drawText(" a",120,40,textPaint);
        canvas.drawText(" b",240,40,textPaint);
        canvas.drawText(" c",340,40,textPaint);
        canvas.drawText(" d",440,40,textPaint);
        canvas.drawText(" e",540,40,textPaint);
        canvas.drawText(" f",640,40,textPaint);
        canvas.drawText(" g",740,40,textPaint);
        canvas.drawText(" h",840,40,textPaint);
        canvas.drawText(" i",940,40,textPaint);

        int colourCounter = 1;


        for (int i = 0; i<lights.length; i++){

//            int colourLightValue = (int)(lightValues.get(i)*(255.0/10000));

            int colourLightValue = lights3[i];

        //    canvas.drawText(lightValues.get(i)+"",xStart,yStart+40,textPaint);
            canvas.drawText(""+colourLightValue,xStart+100,yStart+40,textPaint);

            //changing colour by order, one channel at a time, colours start at 0,0,0,0
            int[] coloursArray = colourValues.get(i).clone();
            coloursArray[colourCounter] = colourLightValue;
            paint.setARGB(coloursArray[0],coloursArray[1],coloursArray[2],coloursArray[3]);
            colourValues.add(coloursArray);
            canvas.drawRect(new RectF(xStart+220,yStart,xStart+270,yStart+50),paint);


            //changing colour by randomly selected channel, one at a time, colours start at 255,0,0,0
            int[] coloursArray2 = colourValues2.get(i).clone();
            coloursArray2[random.nextInt(3)+1] = colourLightValue;
            paint2.setARGB(coloursArray2[0],coloursArray2[1],coloursArray2[2],coloursArray2[3]);
            colourValues2.add(coloursArray2);
            canvas.drawRect(new RectF(xStart+320,yStart,xStart+370,yStart+50),paint2);


            //changing colour by order, one channel at a time, colours start at 255,255,255,255 and value is inverted through subtraction from 255
            int[] coloursArray3 = colourValues3.get(i).clone();
            coloursArray3[colourCounter] = (255-colourLightValue);
            paint3.setARGB(coloursArray3[0],coloursArray3[1],coloursArray3[2],coloursArray3[3]);
            colourValues3.add(coloursArray3);
            canvas.drawRect(new RectF(xStart+420,yStart,xStart+470,yStart+50),paint3);


            //changing colour by randomly selected channel, one at a time, colours start at 255,255,255,255 and value is inverted through subtraction from 255
            int[] coloursArray4 = colourValues4.get(i).clone();
            coloursArray4[random.nextInt(4)] = (255-colourLightValue);
            paint4.setARGB(coloursArray4[0],coloursArray4[1],coloursArray4[2],coloursArray4[3]);
            colourValues4.add(coloursArray4);
            canvas.drawRect(new RectF(xStart+520,yStart,xStart+570,yStart+50),paint4);

            //changing colour by altering alpha channel only
            int[] coloursArray5 = colourValues5.get(i).clone();
            coloursArray5[0] = colourLightValue;
            paint5.setARGB(coloursArray5[0],coloursArray5[1],coloursArray5[2],coloursArray5[3]);
            colourValues5.add(coloursArray5);
            canvas.drawRect(new RectF(xStart+620,yStart,xStart+670,yStart+50),paint5);

            //changing colour by altering red channel only
            int[] coloursArray6 = colourValues6.get(i).clone();
            coloursArray6[1] = colourLightValue;
            paint6.setARGB(coloursArray6[0],coloursArray6[1],coloursArray6[2],coloursArray6[3]);
            colourValues6.add(coloursArray6);
            canvas.drawRect(new RectF(xStart+720,yStart,xStart+770,yStart+50),paint6);

            //changing colour by altering green channel only
            int[] coloursArray7 = colourValues7.get(i).clone();
            coloursArray7[2] = colourLightValue;
            paint7.setARGB(coloursArray7[0],coloursArray7[1],coloursArray7[2],coloursArray7[3]);
            colourValues7.add(coloursArray7);
            canvas.drawRect(new RectF(xStart+820,yStart,xStart+870,yStart+50),paint7);

            //changing colour by altering blue channel only
            int[] coloursArray8 = colourValues8.get(i).clone();
            coloursArray8[3] = colourLightValue;
            paint8.setARGB(coloursArray8[0],coloursArray8[1],coloursArray8[2],coloursArray8[3]);
            colourValues8.add(coloursArray8);
            canvas.drawRect(new RectF(xStart+920,yStart,xStart+970,yStart+50),paint8);

            yStart+=50;


            if(colourCounter>=3){
                colourCounter=1;
            }else{
                colourCounter++;
            }

        }




    }




}
