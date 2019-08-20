package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.workinprogress.DisplayImage;
import com.example.workinprogress.dataSetsAndComponents.DataSet;
import com.example.workinprogress.dataSetsAndComponents.SingularPointDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class AlbersImage extends Painting {

    private Paint paint1;
    private Canvas canvas;
    private float range = DisplayImage.IMAGE_SIZE_SCALAR;
    private int maximumSquareSize = 2000;
    private int[][] coloursValues;
    private float colourRange = 255;
    private float positionsSquareSizeRange = 1000;
    private ArrayList<Integer> shapeSize;
    private int[][] canvasPositions;
    private int[][] newCanvasPositions;
    private int numberOfSquares;
    private ArrayList<Integer> newShapeSize;

    public AlbersImage(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);

        paint1 = new Paint();

        paint1.setARGB(10, 255, 200, 90);

        setColoursValues();
        numberOfSquares = threePointsDataSets.get(0).getScaledResults1().size()*8;

    }


    public void draw(Canvas canvas) {

        this.canvas = canvas;
        canvas.drawColor(Color.WHITE);
        width = getBounds().width();
        height = getBounds().height();

        if(canvasPositions==null) {
            setCanvasPositions();
            setSquareSizes();
        }

        firstEverythingExperiment();
    }

    private void firstEverythingExperiment() {

        drawImage(newCanvasPositions, coloursValues, newShapeSize);

    }

    private void drawImage(int[][] canvasPositions, int[][] coloursValues, ArrayList<Integer> shapeSize) {
        for (int i = 0; i < canvasPositions.length; i++) {
//
            int j = i;
            while (j >= coloursValues.length) {
                j -= coloursValues.length;
            }

            paint1.setARGB(coloursValues[j][0], coloursValues[j][1], coloursValues[j][2], coloursValues[j][3]);

            //set canvas threePointsDataSets so image sits centrally
            int x1 = canvasPositions[i][0] - (shapeSize.get(i) / 2);
            int y1 = canvasPositions[i][1] - (shapeSize.get(i) / 2);
            int x2 = canvasPositions[i][0] + (shapeSize.get(i) / 2);
            int y2 = canvasPositions[i][1] + (shapeSize.get(i) / 2);

            canvas.drawRect(x1, y1, x2, y2, paint1);
        }
    }



    private void setSquareSizes() {
        shapeSize = new ArrayList<>();

        //add a list of all scaled results to shapeSizes
        for (float f : threePointsDataSets.get(0).getScaledResults3()) {
            float zSize = f;
            shapeSize.add(Math.round(zSize));
        }

//        set results to be >0, <500
        for (int i = 0; i < shapeSize.size(); i++) {

            shapeSize.set(i, shapeSize.get(i) - 500);

            if (shapeSize.get(i) < 0) {
                shapeSize.set(i, shapeSize.get(i) * -1);
            }

            if (shapeSize.get(i) > positionsSquareSizeRange) {
                positionsSquareSizeRange = i;
            }
        }

        int biggestShape = 0;
        int smallestShape = 1000;

        //find biggest and smallest value in shape sizes
        for (Integer i : shapeSize) {
            if (i > biggestShape) {
                biggestShape = i;
            } else if (i < smallestShape) {
                smallestShape = i;
            }
        }

        float currentRange = biggestShape - smallestShape;
        float newRange = biggestShape;

        float scalar = currentRange / newRange;

//        System.out.println("scalar: "+scalar);

        //multiply sizes by scalar
        for (int i = 0; i < shapeSize.size(); i++) {
            shapeSize.set(i, (int)((shapeSize.get(i) * scalar) - smallestShape));
        }

        float sizeScalar = maximumSquareSize / positionsSquareSizeRange;

        for (int i = 0; i < shapeSize.size(); i++) {
            shapeSize.set(i, (int) (sizeScalar * shapeSize.get(i))+10);
        }

        Collections.sort(shapeSize, Collections.reverseOrder());
//        Collections.shuffle(shapeSize);

        newShapeSize = (ArrayList<Integer>) shapeSize.clone();

        for (int i = 0; i < numberOfSquares; i++) {

            int j;
            for (j = 0; j < shapeSize.size(); j++) {
                newShapeSize.add(shapeSize.get(j));
            }
            i += j - 1;
        }



    }

    private void setColoursValues() {

        //create new random + minimum colour value
        Random random = new Random();
        int minimumValue = 55;

        //create colour range
        float colourScalar = (colourRange - minimumValue) / DisplayImage.IMAGE_SIZE_SCALAR;

        //get correct array
        for (SingularPointDataSet dataSet : singularPointDataSets) {
            if (dataSet.getScaledResults1().size() > 1) {

                //instantiate array to correct length
                coloursValues = new int[dataSet.getScaledResults1().size()][4];

                //populate 1st position in each array with scaled values
                for (int i = 0; i < coloursValues.length; i++) {
                    coloursValues[i][0] = dataSet.getScaledResults1().get(i);
                    coloursValues[i][0] = (int) ((coloursValues[i][0] * colourScalar) + minimumValue);
                }
//                System.out.println("length of colours"+coloursValues.length);

                //populate all four position with colours found at previous position and one channel changed at random
                for (int i = 1; i < coloursValues.length; i++) {

                    //take value from current position
                    int newValue = coloursValues[i][0];

                    // create new array using array found at last position and insert value into one of its slots at random
                    int[] colourArray = coloursValues[i - 1].clone();
                    colourArray[random.nextInt(4)] = newValue;

                    colourArray[0] -= minimumValue;

                    // ensure alpha channel is visible
                    if (colourArray[0] < 20) {
                        colourArray[0] = 20;
                    }

                    colourArray = invisibleColours(colourArray.clone());

                    //store array again at position i
                    coloursValues[i] = colourArray;
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

        Random random = new Random();

        canvasPositions = new int[threePointsDataSets.get(0).getScaledResults1().size()*8][2];

        float xScalar = width / range;
        float yScalar = height / range;
        int j = 0;

        for (int i = 0; i < canvasPositions.length; i+=8) {

            float xSize = (threePointsDataSets.get(0).getScaledResults1().get(j) * xScalar);
            float ySize = (threePointsDataSets.get(0).getScaledResults2().get(j) * yScalar);
            j++;

            int a1 = Math.round(xSize);
            int a2 = Math.round(ySize);
            int b1 = a1+random.nextInt(50);
            int b2 = a2+random.nextInt(50);
            int c1 = a1-random.nextInt(50);
            int c2 = a2-random.nextInt(50);
            int d1 = b1+random.nextInt(50);
            int d2 = b2+random.nextInt(50);
            int e1 = c1-random.nextInt(100);
            int e2 = c2-random.nextInt(100);
            int f1 = d1+random.nextInt(100);
            int f2 = d2+random.nextInt(100);
            int g1 = e1-random.nextInt(150);
            int g2 = e2-random.nextInt(150);
            int h1 = f1+random.nextInt(200);
            int h2 = f2+random.nextInt(200);


            canvasPositions[i][0] = h1;
            canvasPositions[i][1] = h2;
            canvasPositions[i+1][0] = g1;
            canvasPositions[i+1][1] =  g2;
            canvasPositions[i+2][0] =  f1;
            canvasPositions[i+2][1] =  f2;
            canvasPositions[i+3][0] =  e1;
            canvasPositions[i+3][1] =  e2;
            canvasPositions[i+4][0] =  d1;
            canvasPositions[i+4][1] =  d2;
            canvasPositions[i+5][0] =  c1;
            canvasPositions[i+5][1] =  c2;
            canvasPositions[i+6][0] =  b1;
            canvasPositions[i+6][1] =  b2;
            canvasPositions[i+7][0] =  a1;
            canvasPositions[i+7][1] =  a2;

        }

        setNewCanvasPositions();
    }



    private void setNewCanvasPositions() {
        newCanvasPositions = new int[numberOfSquares][2];
        int loopNumber = 0;

        for (int i = 0; i < numberOfSquares; i++) {

            if (loopNumber == 0) {

                //add in the existing array on the first loop
                int j;
                for (j = 0; j < canvasPositions.length; j++) {
                    newCanvasPositions[j][0] = canvasPositions[j][0];
                    newCanvasPositions[j][1] = canvasPositions[j][1];

                }
                i += j - 1;
                loopNumber++;

                //on the second loop increase x values by 1.5
            } else if (loopNumber == 1) {

                int j;
                for (j = 0; j < canvasPositions.length; j++) {
                    if (j + i < newCanvasPositions.length) {
                        newCanvasPositions[j + i][0] = canvasPositions[j][0] + (canvasPositions[j][0] + 1) / 2;
                        newCanvasPositions[j + i][1] = canvasPositions[j][1];
                    }
                }
                i += j - 1;
                loopNumber++;

                // on the third loop increase y values by 1.5
            } else if (loopNumber == 2) {
                int j;
                for (j = 0; j < canvasPositions.length; j++) {
                    if (j + i < newCanvasPositions.length) {
                        newCanvasPositions[j + i][1] = canvasPositions[j][1] + (canvasPositions[j][1] + 1) / 2;
                        newCanvasPositions[j + i][0] = canvasPositions[j][0];
                    }
                }
                i += j - 1;
                loopNumber++;
                //on the fourth loop decrease x values by 1.5
            } else if (loopNumber == 3) {
                int j;
                for (j = 0; j < canvasPositions.length; j++) {
                    if (j + i < newCanvasPositions.length) {
                        newCanvasPositions[j + i][0] = canvasPositions[j][0] - (canvasPositions[j][0] + 1) / 2;
                        newCanvasPositions[j + i][1] = canvasPositions[j][1];
                    }
                }
                i += j - 1;
                loopNumber++;
                // on the fifth loop decrease y values by 1.5 then return loop counter to 1 to begin again at 2nd loop
            } else if (loopNumber == 4) {
                int j;
                for (j = 0; j < canvasPositions.length; j++) {
                    if (j + i < newCanvasPositions.length) {
                        newCanvasPositions[j + i][1] = canvasPositions[j][1] - (canvasPositions[j][1] + 1) / 2;
                        newCanvasPositions[j + i][0] = canvasPositions[j][0];
                    }
                }
                i += j - 1;
                loopNumber = 1;
            }

        }

    }


}
