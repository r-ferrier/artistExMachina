package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.workinprogress.DisplayImage;
import com.example.workinprogress.dataSetsAndComponents.DataSet;
import com.example.workinprogress.dataSetsAndComponents.SingularPointDataSet;
import com.example.workinprogress.dataSetsAndComponents.UnscaledSingleEntryDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class AlbersImage extends Painting {

    private Paint paint1;
    private Canvas canvas;
    private float range = DisplayImage.IMAGE_SIZE_SCALAR;
    private int maximumSquareSize = 800;
    private int[][] coloursValues;
    private float colourRange = 255;
    private float positionsSquareSizeRange = maximumSquareSize;
    private ArrayList<Integer> shapeSize = new ArrayList<>();
    private int[][] canvasPositions;
    private int[][] newCanvasPositions;
    private int numberOfSquares;
    private ArrayList<Integer> newShapeSize;

    public AlbersImage(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);

        paint1 = new Paint();

        paint1.setARGB(10, 255, 200, 90);
//        System.out.println("in the albers image -------------------------------------------------");

        System.out.println(positions.get(0).getScaledResults1());
        System.out.println(positions.get(0).getScaledResults2());
        System.out.println(positions.get(0).getScaledResults3());

        setColoursValues();
        numberOfSquares = lightDistanceAndSteps.get(0).getScaledResults1().get(0);

    }


    public void draw(Canvas canvas) {

        this.canvas = canvas;
        canvas.drawColor(Color.WHITE);
        width = getBounds().width();
        height = getBounds().height();

        if(canvasPositions==null) {
            setCanvasPositions();
        }

        if(shapeSize.size()==0) {
            shapeSize.addAll(setSquareSizes(positions.get(0).getScaledResults1()));
            shapeSize.addAll(setSquareSizes(positions.get(0).getScaledResults2()));
            shapeSize.addAll(setSquareSizes(positions.get(0).getScaledResults3()));
//            Collections.sort(shapeSize, Collections.reverseOrder());
        }

        String canvasPositionsString = "";
        String coloursString = "";

        for (int i = 0; i < canvasPositions.length; i++) {
            canvasPositionsString += "[x: " + canvasPositions[i][0] + ", y: " + canvasPositions[i][1] + "], ";
        }

        for (int i = 0; i < coloursValues.length; i++) {
            coloursString += "[A: " + coloursValues[i][0] + ", R: " + coloursValues[i][1] + ",G: " + coloursValues[i][2] + ",B: " + coloursValues[i][3] + "], ";
        }

//        System.out.println("shape positions" + canvasPositionsString);
//        System.out.println("shape sizes scaled" + shapeSize.toString());
//        System.out.println(" colours" + coloursString);

        firstEverythingExperiment();

    }

    private void firstEverythingExperiment() {

        drawImage(canvasPositions, coloursValues, shapeSize);
//        setWhiteSquare();
    }

    private void drawImage(int[][] canvasPositions, int[][] coloursValues, ArrayList<Integer> shapeSize) {
        for (int i = 0; i < canvasPositions.length; i++) {
//
            int j = i;
            while (j >= coloursValues.length) {
                j -= coloursValues.length;
            }

            paint1.setARGB(coloursValues[j][0], coloursValues[j][1], coloursValues[j][2], coloursValues[j][3]);

            //set canvas positions so image sits centrally
            int x1 = canvasPositions[i][0] - (shapeSize.get(i) / 2);
            int y1 = canvasPositions[i][1] - (shapeSize.get(i) / 2);
            int x2 = canvasPositions[i][0] + (shapeSize.get(i) / 2);
            int y2 = canvasPositions[i][1] + (shapeSize.get(i) / 2);

            canvas.drawRect(x1, y1, x2, y2, paint1);

        }
    }


    private void setWhiteSquare(){

        int distance = 0;
        int numberOflocationsVisited = 1;
        distance = (int)((UnscaledSingleEntryDataSet)lightDistanceAndSteps.get(1)).getScaledResults1().get(0);

//        System.out.println("distance" + distance + "---------------------");

        if(distance>0) {

            numberOflocationsVisited += locations.get(0).getScaledResults1().size();

            Paint paint2 = new Paint();
            paint2.setARGB(150, 240, 240,240);
            int x1;
            int y1;

            if(distance>width){
                x1 = (int) (distance%width);
            }else{
                x1 = (int) (width%distance);
            }

            if(distance>height){
                y1 = (int) (distance%height);
            }else{
                y1 = (int) (height%distance);
            }

            int x2 = x1 + 200;
            int y2 = y1 + 30;

//            System.out.println("distance%width" + x1 + "---------------------");
//            System.out.println("distance%height" + y1 + "---------------------");
//            System.out.println("locations visited" + numberOflocationsVisited + "---------------------");

            for (int i = 0; i < numberOflocationsVisited; i++) {

                y1 += 50;
                y2 += 50;

                canvas.drawRect(x1, y1, x2, y2, paint2);
//                System.out.println("canvasdrawRect"+x1+" "+y1+" "+x2+" "+y2+" ");
            }
        }
    }


    private ArrayList<Integer> setSquareSizes(ArrayList<Integer> positions) {

//        System.out.println("original shape sizes: "+positions.toString());

        // make everything a positive between 0 and 500
        for (int i = 0; i < positions.size(); i++) {
            positions.set(i, positions.get(i) - 500);
            if (positions.get(i) < 0) {
                positions.set(i, positions.get(i) * -1);
            }
        }

//        System.out.println("shape sizes 0-500: "+positions.toString());
        //find the largest and smallest values in the range
        int biggestShape = 0;
        int smallestShape = 500;
        for (Integer i : positions) {
            if (i > biggestShape) {
                biggestShape = i;
            } else if (i < smallestShape) {
                smallestShape = i;
            }
        }

//        System.out.println("biigest shape as biggest shape: "+biggestShape);
//        System.out.println("smallest shape: "+smallestShape);


        float currentRange = biggestShape - smallestShape;
        float newRange = biggestShape;
        float scalar = newRange/currentRange;
//        System.out.println("new range: "+newRange);
//        System.out.println("scalar: "+scalar);
        for (int i = 0; i < positions.size(); i++) {
            //find the range and then multiply everything to fit within the new desired range
            positions.set(i, (int)((positions.get(i)- smallestShape) * scalar));

            //now multiply everything over 50 by itself/50 to increase larger shape sizes
            positions.set(i, (int)(positions.get(i)*((float)positions.get(i)/50)));
        }

//        System.out.println("scaled shapes1: "+positions.toString());

        Collections.sort(positions, Collections.reverseOrder());

//        System.out.println("final scaling: "+positions.toString());

        return positions;
    }



    private void setColoursValues() {

        //create new random + minimum colour value
        Random random = new Random();
        int minimumValue = 55;

        //create colour range
        float colourScalar = (colourRange - minimumValue) / DisplayImage.IMAGE_SIZE_SCALAR;

        //get correct array
        for (SingularPointDataSet dataSet : lightDistanceAndSteps) {
            if (dataSet.getScaledResults1().size() > 1) {

                //instantiate array to correct length
                coloursValues = new int[dataSet.getScaledResults1().size()][4];

                coloursValues[0] = new int[]
                        {dataSet.getScaledResults1().get(dataSet.getScaledResults1().size()-1),
                        dataSet.getScaledResults1().get(dataSet.getScaledResults1().size()-2),
                        dataSet.getScaledResults1().get(dataSet.getScaledResults1().size()-3),
                        dataSet.getScaledResults1().get(dataSet.getScaledResults1().size()-4)};

                //populate 1st position in each array with scaled values
                for (int i = 0; i < coloursValues.length; i++) {
                    coloursValues[i][0] = dataSet.getScaledResults1().get(i);
                    coloursValues[i][0] = (int) ((coloursValues[i][0] * colourScalar) + minimumValue);
                    System.out.println("scaled colours" + Arrays.toString(coloursValues[i]));
                }
//                System.out.println("length of colours"+coloursValues.length);

                //populate all four position with colours found at previous position and one channel changed at random
                for (int i = 1; i < coloursValues.length; i++) {

                    //take value from current position
                    int newValue = coloursValues[i][0];

                    // create new array using array found at last position and insert value into one of its slots at random
                    int[] colourArray = coloursValues[i - 1].clone();
                    System.out.println("i-1: " + Arrays.toString(coloursValues[i - 1]));

                    colourArray[random.nextInt(4)] = newValue;

                    colourArray[0] -= minimumValue;

                    // ensure alpha channel is visible
                    if (colourArray[0] < 20) {
                        colourArray[0] = 20;
                    }

                    colourArray = invisibleColours(colourArray.clone());

                    //store array again at position i
                    coloursValues[i] = colourArray;
                    System.out.println("i: " + Arrays.toString(coloursValues[i]));
                }
                break;
            }
        }
    }

    private int[] invisibleColours(int[] colourArray) {
        int numberOfInvisibleChannels = 0;

        for (int j = 1; j < 4; j++) {
            if (colourArray[j] < 20) {
                numberOfInvisibleChannels++;
            }
        }
        if (numberOfInvisibleChannels > 2) {
            int highestValueChannel = 1;
            for (int j = 1; j < 4; j++) {
                if (colourArray[j] > highestValueChannel) {
                    highestValueChannel = j;
                }
            }
            colourArray[highestValueChannel] += 70;
            colourArray[0] = 20;
        }
        return colourArray;
    }

    private void setCanvasPositions() {

        int numberOfPositions = positions.get(0).getScaledResults1().size();

        canvasPositions = new int[numberOfPositions*3][2];

        float xScalar = (width / range);
        float yScalar = (height / range);


        for (int i = 0; i < numberOfPositions; i++) {
            float xSize = (positions.get(0).getScaledResults1().get(i) * xScalar);
            float ySize = (positions.get(0).getScaledResults2().get(i) * yScalar);

//            if(xSize>(width/2)){
//                xSize+=((xSize-(width/2))*2);
//            }else{
//                xSize-=(((xSize-(width/2))*-1)*2);
//            }
//
//            if(ySize>(height/2)){
//                ySize+=((ySize-(height/2))*2);
//            }else{
//                ySize-=(((ySize-(height/2))*-1)*2);
//            }

            canvasPositions[i][0] = Math.round(xSize);
            canvasPositions[i][1] = Math.round(ySize);

            canvasPositions[i+numberOfPositions][0] = Math.round(xSize);
            canvasPositions[i+numberOfPositions][1] = Math.round(ySize);

            canvasPositions[i+(numberOfPositions*2)][0] = Math.round(xSize);
            canvasPositions[i+(numberOfPositions*2)][1] = Math.round(ySize);
        }
    }
}
