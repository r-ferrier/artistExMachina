package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.workinprogress.DisplayImage;
import com.example.workinprogress.dataSetsAndComponents.DataSet;
import com.example.workinprogress.dataSetsAndComponents.SingularPointDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class AlbersImage extends Painting {

    private Paint paint1;
    private Canvas canvas;
    private float range = DisplayImage.IMAGE_SIZE_SCALAR;
    private int maximumSquareSize = 800;
    private int[][] coloursValues;
    private float colourRange = 255;
    private float positionsSquareSizeRange = maximumSquareSize;
    private ArrayList<Integer> shapeSize;
    private int[][] canvasPositions;
    private int numberOfSquares;

    public AlbersImage(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);

        paint1 = new Paint();

        paint1.setARGB(10, 255, 200, 90);
        System.out.println("in the albers image -------------------------------------------------");

        System.out.println(positions.get(0).getScaledResults1());
        System.out.println(positions.get(0).getScaledResults2());
        System.out.println(positions.get(0).getScaledResults3());

        setColoursValues();
        setSquareSizes();
        numberOfSquares = lightDistanceAndSteps.get(0).getScaledResults1().get(0);

    }


    public void draw(Canvas canvas) {

        this.canvas = canvas;
        canvas.drawColor(Color.WHITE);
        width = getBounds().width();
        height = getBounds().height();

        setCanvasPositions();

        String canvasPositionsString = "";
        String coloursString = "";

        for(int i = 0; i<canvasPositions.length;i++){
            canvasPositionsString += "[x: "+canvasPositions[i][0]+", y: "+canvasPositions[i][1]+"], ";
        }

        for(int i = 0; i<coloursValues.length;i++){
            coloursString += "[A: "+coloursValues[i][0]+", R: "+coloursValues[i][1]+",G: "+coloursValues[i][2]+",B: "+coloursValues[i][3]+"], ";
        }

        System.out.println("shape positions"+canvasPositionsString);
        System.out.println("shape sizes scaled"+shapeSize.toString());
        System.out.println(" colours"+coloursString);

        firstEverythingExperiment();


        // to be used to toggle alpha functionality
//        paint1.setARGB(40,20,200,150);
//        canvas.drawRect(50,50,150,150, paint1);
//        canvas.drawRect(25,25,110,200, paint1);
    }

    private void firstEverythingExperiment() {

//        for (int i = 0; i < numberOfSquares; i++) {
////
//            int j = i;
//            while (j >= coloursValues.length) {
//                j -= coloursValues.length;
//            }
//
//            paint1.setARGB(coloursValues[j][0],coloursValues[j][1],coloursValues[j][2],coloursValues[j][3]);
//
//            j = i;
//            while (j >= canvasPositions.length) {
//                j -= canvasPositions.length;
//            }
//
//            //set canvas positions so image sits centrally
//            int x1 = canvasPositions[j][0] - (shapeSize.get(j)/2);
//            int y1 = canvasPositions[j][1] - (shapeSize.get(j)/2);
//            int x2 = canvasPositions[j][0] + (shapeSize.get(j)/2);
//            int y2 = canvasPositions[j][1] + (shapeSize.get(j)/2);
//
//            canvas.drawRect(x1, y1, x2,y2 , paint1);
//
////            System.out.println(canvasPositions[j][0]+" "+canvasPositions[j][1]+" "+shapeSize[j]+" "+shapeSize[j]+" canvas positions");
////            System.out.println(coloursValues.length);
////            Log.i("colours",Arrays.toString(coloursValues[j]));
//        }

        int[][] newCanvasPositions = canvasPositions.clone();
        int [][] newColoursValues = coloursValues.clone();
        ArrayList<Integer> newShapeSize = (ArrayList<Integer>)shapeSize.clone();
        int newNumberOfSquares = numberOfSquares;

        while(newNumberOfSquares>0){

            drawImage(newCanvasPositions,newColoursValues,newShapeSize);

            for(int i = 0; i<newCanvasPositions.length;i++){

                if(newCanvasPositions[i][0]>width/2){
                    newCanvasPositions[i][0]+=100;
                    newCanvasPositions[i][1]-=100;
                } else {
                    newCanvasPositions[i][0] -=100;
                }newCanvasPositions[i][1] +=100;

            }

           for(int i = 0; i<newShapeSize.size();i++){
               newShapeSize.set(i,newShapeSize.get(i)+10);
           }

           newNumberOfSquares -= canvasPositions.length;

        }



    }

    private void drawImage(int[][] canvasPositions, int[][]coloursValues, ArrayList<Integer> shapeSize){
        for (int i = 0; i < canvasPositions.length; i++) {
//
            int j = i;
            while (j >= coloursValues.length) {
                j -= coloursValues.length;
            }

            paint1.setARGB(coloursValues[j][0],coloursValues[j][1],coloursValues[j][2],coloursValues[j][3]);

            //set canvas positions so image sits centrally
            int x1 = canvasPositions[i][0] - (shapeSize.get(i)/2);
            int y1 = canvasPositions[i][1] - (shapeSize.get(i)/2);
            int x2 = canvasPositions[i][0] + (shapeSize.get(i)/2);
            int y2 = canvasPositions[i][1] + (shapeSize.get(i)/2);

            canvas.drawRect(x1, y1, x2,y2 , paint1);

//            System.out.println(canvasPositions[j][0]+" "+canvasPositions[j][1]+" "+shapeSize[j]+" "+shapeSize[j]+" canvas positions");
//            System.out.println(coloursValues.length);
//            Log.i("colours",Arrays.toString(coloursValues[j]));
        }
    }


    private void setSquareSizes(){
        shapeSize = new ArrayList<>();

        for (float f: positions.get(0).getScaledResults3()) {
            float zSize = f;
            shapeSize.add(Math.round(zSize));
        }

        for (int i=0; i<shapeSize.size();i++) {

            shapeSize.set(i,shapeSize.get(i)-500);

            if(shapeSize.get(i)<0){
                shapeSize.set(i,shapeSize.get(i)*-1);
            }

            if (shapeSize.get(i) > positionsSquareSizeRange) {
                positionsSquareSizeRange = i;
            }
        }

        int biggestShape = 0;
        int smallestShape = 1000;

        //find biggest and smallest value in shape sizes
        for(Integer i: shapeSize){
            if(i>biggestShape){
                biggestShape=i;
            }else if(i<smallestShape){
                smallestShape=i;
            }
        }

        float currentRange = biggestShape-smallestShape;
        float newRange = biggestShape;

        Integer scalar = (int)(currentRange/newRange);

        for(int i=0; i<shapeSize.size();i++){
            shapeSize.set(i,(shapeSize.get(i)*scalar)-smallestShape);
        }

//        System.out.println("shape sizes unscaled"+Arrays.toString(shapeSize));
        float sizeScalar = maximumSquareSize / positionsSquareSizeRange;

        for (int i=0; i<shapeSize.size();i++) {
            shapeSize.set(i,(int)(sizeScalar*i)+10);
        }

        Collections.sort(shapeSize,Collections.reverseOrder());
    }

    private void setColoursValues(){

        //create new random + minimum colour value
        Random random = new Random();
        int minimumValue = 55;

        //create colour range
        float colourScalar = (colourRange-minimumValue) / DisplayImage.IMAGE_SIZE_SCALAR;

        //get correct array
        for (SingularPointDataSet dataSet : lightDistanceAndSteps) {
            if (dataSet.getScaledResults1().size() > 1) {

                //instantiate array to correct length
                coloursValues = new int[dataSet.getScaledResults1().size()][4];

                //populate 1st position in each array with scaled values
                for (int i = 0; i < coloursValues.length; i++) {
                    coloursValues[i][0] = dataSet.getScaledResults1().get(i);
                    coloursValues[i][0] = (int)((coloursValues[i][0]*colourScalar)+minimumValue);
                    System.out.println("scaled colours"+Arrays.toString(coloursValues[i]));
                }
//                System.out.println("length of colours"+coloursValues.length);

                //populate all four position with colours found at previous position and one channel changed at random
                for(int i = 1; i < coloursValues.length; i++){

                    //take value from current position
                    int newValue = coloursValues[i][0];

                    // create new array using array found at last position and insert value into one of its slots at random
                    int[] colourArray = coloursValues[i-1].clone();
                    System.out.println("i-1: "+Arrays.toString(coloursValues[i-1]));
                    colourArray[random.nextInt(4)] = newValue;

                    colourArray[0]-= minimumValue;

                    // ensure alpha channel is visible
                    if(colourArray[0]<20){
                        colourArray[0]=20;
                    }

                    colourArray = invisibleColours(colourArray.clone());

                    //store array again at position i
                    coloursValues[i] = colourArray;
                    System.out.println("i: "+Arrays.toString(coloursValues[i]));
                }
                break;
            }
        }
    }

    private int[] invisibleColours(int[] colourArray){
        int numberOfInvisibleChannels = 0;

        for(int j = 1; j<4;j++){
            if(colourArray[j]<20){
                numberOfInvisibleChannels++;
            }
        }
        if(numberOfInvisibleChannels>2){
            int highestValueChannel = 1;
            for(int j = 1; j<4; j++){
                if(colourArray[j]>highestValueChannel){
                    highestValueChannel=j;
                }
            }
            colourArray[highestValueChannel]+=70;
            colourArray[0]=20;
        }
        return colourArray;
    }

    private void setCanvasPositions(){
        canvasPositions = new int[positions.get(0).getScaledResults1().size()][2];

        float xScalar = width / range;
        float yScalar = height / range;

        for (int i = 0; i < positions.get(0).getScaledResults1().size(); i++) {
            float xSize = (positions.get(0).getScaledResults1().get(i) * xScalar);
            float ySize = (positions.get(0).getScaledResults2().get(i) * yScalar);
            canvasPositions[i][0] = Math.round(xSize);
            canvasPositions[i][1] = Math.round(ySize);
        }
    }

}
