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

import java.util.ArrayList;
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
//            drawALoadOfShapes((int) (width / 2), (int) (height / 2), (int) (width / 2)+(averageSize/2), (int) (height / 2),setSingleChannelColours(0),averageSize/2);
//            drawALoadOfShapes((int) (width / 2)+(averageSize/2), (int) (height / 2), (int) (width / 2)+(averageSize/2)+(averageSize/2), (int) (height / 2),setSingleChannelColours(0));
            drawALoadOfShapes((int) (width / 2)-(averageSize/4), (int) (height / 2), (int) (width / 2)-(averageSize/2)-(averageSize/10), (int) (height / 2),setSingleChannelColours(0),(averageSize/10));
            drawALoadOfShapes((int) (width / 2), (int) (height / 2), (int) (width / 2)+(averageSize/8), (int) (height / 2),setSingleChannelColours(1),(averageSize/8));
            drawALoadOfShapes((int) (width / 2), (int) (height / 2), (int) (width / 2)+(averageSize/16), (int) (height / 2),setSingleChannelColours(2),(averageSize/28));
            drawALoadOfShapes((int) (width / 2) , (int) (height / 2), (int) (width / 2)+(averageSize/32), (int) (height / 2),setSingleChannelColours(3),(averageSize/40));
        } else {

            for (Shape shape : shapes) {
                shape.draw(canvas);
            }
        }

    }

    private ArrayList<int[]> setSingleChannelColours(int channel){

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
        ArrayList<Integer> xPositions = positions.get(0).getScaledResults3();
        ArrayList<Integer> yPositions = positions.get(0).getScaledResults2();
        ArrayList<Integer> sizes = positions.get(0).getScaledResults1();

        Collections.sort(lightValues);


        colourValuesArray = new ArrayList<>();

        Random random = new Random();

        int[] colours = new int[]{255, 255, 255, 255};
        colourValuesArray.add(colours);

        for(int i = 1; i<lightValues.size();i++){
            lightValues.set(i,lightValues.get(i)* (40000 / 255));
            colours = colourValuesArray.get(i-1).clone();
            colours[random.nextInt(4)]=lightValues.get(i);
            colourValuesArray.add(colours);
        }

        numberOfShapes = (xPositions.size())/2;

        int totalSize = 0;

        int highestValue = 0;
        int secondHighestValue = 0;
        int thirdHighestValue = 0;
        int fourthHighestValue = 0;

        for(Integer size: sizes){
            size-=500;
            if(size<0){
                size*=-1;
            }

            totalSize+=size;
            if(size>highestValue){
                fourthHighestValue = thirdHighestValue;
                thirdHighestValue = secondHighestValue;
                secondHighestValue = highestValue;
                highestValue = size;
            }else if(size>secondHighestValue){
                fourthHighestValue = thirdHighestValue;
                thirdHighestValue = secondHighestValue;
                secondHighestValue = size;
            }else if(size>thirdHighestValue){
                fourthHighestValue = thirdHighestValue;
                thirdHighestValue = size;
            }else if(size>fourthHighestValue){
                fourthHighestValue = size;
            }
        }


        averageSize = (((highestValue+secondHighestValue+thirdHighestValue+fourthHighestValue)/4)+(totalSize/numberOfShapes))/2;
        averageSize = (500-averageSize);

        System.out.println("average size: "+averageSize);

    }

    private void drawALoadOfShapes(int startX1, int startY1, int startX2, int startY2, ArrayList<int[]> colourValuesArrayForLoop, int shapeWidth) {

        int x1Start = startX1;
        int x2Start = startX2;
        int y1Start = startY1;
        int y2Start = startY2;


        Random random = new Random();

        for (int i = 0; i < numberOfShapes; i++) {

            int j = i;
            int[] coloursForThisLoop = new int[4];

            while (colourValuesArray.size() <= j) {
                j -= colourValuesArray.size();
            }
            coloursForThisLoop = colourValuesArray.get(j);

            Shape shapeInLoop;

            int lineLength = (random.nextInt(10000/numberOfShapes))+shapeWidth;

            System.out.println(lineLength+" line length");


            int choice = random.nextInt(16);

            if (choice<=3) {
                shapeInLoop = new LineShape(x1Start, y1Start, x2Start, y2Start, lineLength, coloursForThisLoop);
            } else if (choice <=8) {
                shapeInLoop = new CurvedShape(x1Start, y1Start, x2Start, y2Start, random.nextBoolean(), coloursForThisLoop, lineLength);
            } else if (choice <=11){
                shapeInLoop = new CircleShape(x1Start, y1Start, x2Start, y2Start, lineLength, coloursForThisLoop);
            }else {
                shapeInLoop = new BumpyShape(x1Start, y1Start, x2Start, y2Start, lineLength, coloursForThisLoop);
            }
            shapeInLoop.draw(canvas);

            x1Start = shapeInLoop.getX1End();
            y1Start = shapeInLoop.getY1End();
            x2Start = shapeInLoop.getX2End();
            y2Start = shapeInLoop.getY2End();

//            System.out.println("starting points for shapeinloop: x1 = " + x1Start + ", x2 = " + x2Start + ", y1 = " + y1Start + ", y2 = " + y2Start);

            shapes.add(shapeInLoop);
        }
    }


}
