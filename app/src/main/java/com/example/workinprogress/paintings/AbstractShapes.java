package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.workinprogress.dataSetsAndComponents.DataSet;
import com.example.workinprogress.dataSetsAndComponents.SensorSingularPointDataSet;
import com.example.workinprogress.paintings.shapes.BumpyShape;
import com.example.workinprogress.paintings.shapes.CircleShape;
import com.example.workinprogress.paintings.shapes.CurvedShape;
import com.example.workinprogress.paintings.shapes.LineShape;
import com.example.workinprogress.paintings.shapes.Shape;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class AbstractShapes extends Painting {


    private Canvas canvas;
    private ArrayList<Shape> shapes;
    private Paint paint1;
    private ArrayList<int[]> colourValuesArray;
    private ArrayList<Integer> lightValues;
    private int numberOfShapes;
    private int averageSize;

    private int startX1 = 0;
    private int startY1 = 0;
    private int startX2 = 0;
    private int startY2 = 0;

    private int[] lineWidths;

    public AbstractShapes(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);
        shapes = new ArrayList<>();
    }

    public void draw(Canvas canvas) {

        this.canvas = canvas;
        canvas.drawColor(Color.BLACK);
        width = getBounds().width();
        height = getBounds().height();

        getDataForDrawingShapes();


        if (shapes.size() < 1) {
            if (averageSize > 200) {
//            drawALoadOfShapes((int) (width / 2), (int) (height / 2), (int) (width / 2)+(averageSize/2), (int) (height / 2),setSingleChannelColours(0),averageSize/2);
//            drawALoadOfShapes((int) (width / 2)+(averageSize/2), (int) (height / 2), (int) (width / 2)+(averageSize/2)+(averageSize/2), (int) (height / 2),setSingleChannelColours(0));
                setStartingPositions(0);
                drawALoadOfShapes(setSingleChannelColours(0));
                drawALoadOfShapes(setSingleChannelColours(1));
            } else {
                setStartingPositions(0);
                drawALoadOfShapes(setSingleChannelColours(0));
                drawALoadOfShapes(setSingleChannelColours(1));
                drawALoadOfShapes(setSingleChannelColours(2));
                drawALoadOfShapes(setSingleChannelColours(3));
            }
        } else {

            for (Shape shape : shapes) {
                shape.draw(canvas);
            }
        }

    }

    private void setStartingPositions(int i) {
        startX1 = startX2;
        startY1 = (int) height;
        startX2 = startX1 + lineWidths[i];
        startY2 = (int) height;

        System.out.println("startx1 and linewidth: " + lineWidths[i]);

        System.out.println("startx1: " + startX1 + " starty1: " + startY1 + " startx2: " + startX2 + " starty2: " + startY2);

    }

    private ArrayList<int[]> setSingleChannelColours(int channel) {

        ArrayList<int[]> coloursValues = new ArrayList<>();

        for (Integer lightValue : lightValues) {

            int[] colours = new int[]{255, 255, 255, 255};
            lightValue *= (40000 / 255);
            colours[channel] = lightValue;
            coloursValues.add(colours);
        }

        return coloursValues;

    }

    private void getDataForDrawingShapes() {

        lightValues = ((SensorSingularPointDataSet) lightDistanceAndSteps.get(2)).getScaledResults2();
        ArrayList<Integer> sizes = positions.get(0).getScaledResults1();

        Collections.shuffle(lightValues);



        colourValuesArray = new ArrayList<>();

        Random random = new Random();

        int[] colours = new int[]{90, 20, 20, 20};
        colourValuesArray.add(colours);

        for (int i = 1; i < lightValues.size(); i++) {

            int colourNumber = (255-(int)(lightValues.get(i)*(255.0/40000.0)));

            System.out.println("scalar "+255.0/40000.0);
            lightValues.set(i,colourNumber);
            colours = colourValuesArray.get(i - 1).clone();



//            if(colourNumber>colours[1]){
//                colours[1] = colourNumber;
//            }else if(colourNumber>colours[2]){
//                colours[2] = colourNumber;
//            }else if(colourNumber>colours[0]){
//                colours[0] = colourNumber;
//            }else if(colourNumber>colours[3]){
//                colours[3] = colourNumber;
//            }else{
//                colours[1] = colourNumber;
//            }



            colours[random.nextInt(4)] = lightValues.get(i);

            if(lightValues.get(i)>230){
                colours[random.nextInt(3)+1]-= 100;
            }





            colourValuesArray.add(colours);

            System.out.println(Arrays.toString(colours));
        }

        numberOfShapes = (sizes.size()) / 2;

        int totalSize = 0;

        int highestValue = 0;
        int secondHighestValue = 0;
        int thirdHighestValue = 0;
        int fourthHighestValue = 0;

        for (Integer size : sizes) {
            size -= 500;
            if (size < 0) {
                size *= -1;
            }

            totalSize += size;
            if (size > highestValue) {
                fourthHighestValue = thirdHighestValue;
                thirdHighestValue = secondHighestValue;
                secondHighestValue = highestValue;
                highestValue = size;
            } else if (size > secondHighestValue) {
                fourthHighestValue = thirdHighestValue;
                thirdHighestValue = secondHighestValue;
                secondHighestValue = size;
            } else if (size > thirdHighestValue) {
                fourthHighestValue = thirdHighestValue;
                thirdHighestValue = size;
            } else if (size > fourthHighestValue) {
                fourthHighestValue = size;
            }
        }


        averageSize = (((highestValue + secondHighestValue + thirdHighestValue + fourthHighestValue) / 4) + (totalSize / numberOfShapes)) / 2;
        averageSize = (500 - averageSize);


        //setting lineWidths

        double sizeUpperBounds;
        double sizeLowerBounds;

        if(averageSize>400){
            sizeLowerBounds = (double)averageSize/6;
            sizeUpperBounds = (double)averageSize/1.5-sizeLowerBounds;
        }else if(averageSize>300){
            sizeLowerBounds = (double)averageSize/16;
            sizeUpperBounds = (double)averageSize/2-sizeLowerBounds;
        }else if(averageSize>200){
            sizeLowerBounds = (double)averageSize/50;
            sizeUpperBounds = (double)averageSize/8-sizeLowerBounds;
        }else{
            sizeLowerBounds = (double)averageSize/100;
            sizeUpperBounds = (double)averageSize/32-sizeLowerBounds;
        }

        lineWidths = new int[2000];

        for(int i = 0;i<lineWidths.length;i++){
            lineWidths[i] = random.nextInt((int)sizeUpperBounds)+(int)sizeLowerBounds;
        }

        System.out.println("average size: " + averageSize);
    }

    private void drawALoadOfShapes(ArrayList<int[]> colourValuesArrayForLoop) {

        int loopStartX1 = startX1;
        int loopStartY1 = startY1;
        int loopStartX2 = startX2;
        int loopStartY2 = startY2;

        Random random = new Random();

        int lineWidthIndex = 0;

        for (int i = 0; i < numberOfShapes; i++) {



            int j = i;
            int[] coloursForThisLoop = new int[4];

            while (colourValuesArray.size() <= j) {
                j -= colourValuesArray.size();
            }
            coloursForThisLoop = colourValuesArray.get(j);

            Shape shapeInLoop;

            int lineLength = (random.nextInt(10000 / numberOfShapes)) + lineWidths[lineWidthIndex];

            System.out.println(lineLength + " line length");


            int choice = random.nextInt(16);

            if (choice <= 3) {
                shapeInLoop = new LineShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, lineLength, coloursForThisLoop);
            } else if (choice <= 8) {
                shapeInLoop = new CurvedShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, random.nextBoolean(), coloursForThisLoop, (int)(lineLength*1.5));
            } else if (choice <= 11) {
                shapeInLoop = new CircleShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, lineLength, coloursForThisLoop);
            } else if (choice <= 14) {
                shapeInLoop = new BumpyShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, lineLength, coloursForThisLoop);
            } else {
                shapeInLoop = new CurvedShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, random.nextBoolean(), coloursForThisLoop,(int)(lineLength*1.5));
            }

            shapeInLoop.draw(canvas);

            loopStartX1 = shapeInLoop.getX1End();
            loopStartY1 = shapeInLoop.getY1End();
            loopStartX2 = shapeInLoop.getX2End();
            loopStartY2 = shapeInLoop.getY2End();

//            System.out.println("loopstartx1: " + loopStartX1 + " loopstarty1: " + loopStartY1 + " loopstartx2: " + loopStartX2 + " loopstarty2: " + loopStartY2);

            if (loopStartX1 < 0 || loopStartX1 > width || loopStartY1 < 0 || loopStartY1 > height || loopStartX2 < 0 || loopStartX2 > width || loopStartY2 < 0 || loopStartY2 > height) {

                lineWidthIndex++;
                setStartingPositions(lineWidthIndex);

                loopStartX1 = startX1;
                loopStartY1 = startY1;
                loopStartX2 = startX2;
                loopStartY2 = startY2;

                if (startX1 > width) break;

//                System.out.println("CHANGING startx1: " + startX1 + " starty1: " + startY1 + " startx2: " + startX2 + " starty2: " + startY2);
            }


//            System.out.println("starting points for shapeinloop: x1 = " + x1Start + ", x2 = " + x2Start + ", y1 = " + y1Start + ", y2 = " + y2Start);

            shapes.add(shapeInLoop);
        }


    }


}
