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
    private int maximumSquareSize = 800;
    private int[][] coloursValues;
    private float colourRange = 255;
    private float positionsSquareSizeRange = maximumSquareSize;
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
        numberOfSquares = lightDistanceAndSteps.get(0).getScaledResults1().get(0);
    }

    public void draw(Canvas canvas) {

        this.canvas = canvas;
        canvas.drawColor(Color.WHITE);
        width = getBounds().width();
        height = getBounds().height();

        if(canvasPositions==null) {
            setCanvasPositions();
            setSquareSizes();
//            printOutInfo();
        }

        drawImage();
    }

    private void drawImage() {
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

    private void setSquareSizes() {
        shapeSize = new ArrayList<>();
        for (float f : positions.get(0).getScaledResults3()) {
            float zSize = f;
            shapeSize.add(Math.round(zSize));
        }
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
        float scalar = (newRange/currentRange);
        for (int i = 0; i < shapeSize.size(); i++) {
            shapeSize.set(i, (int)(shapeSize.get(i) * scalar) - smallestShape);
        }
//        System.out.println("shape sizes unscaled"+Arrays.toString(shapeSize));
        float sizeScalar = maximumSquareSize / positionsSquareSizeRange;
        for (int i = 0; i < shapeSize.size(); i++) {
            shapeSize.set(i, (int) (sizeScalar * shapeSize.get(i)) + 10);
        }
        Collections.sort(shapeSize, Collections.reverseOrder());
//        Collections.shuffle(shapeSize);
        newShapeSize = (ArrayList<Integer>) shapeSize.clone();
        for (int i = 0; i < numberOfSquares; i++) {
            int j;
            for (j = 0; j < shapeSize.size(); j++) {
                newShapeSize.add(shapeSize.get(j) + 10);
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
        for (SingularPointDataSet dataSet : lightDistanceAndSteps) {
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
        canvasPositions = new int[positions.get(0).getScaledResults1().size()][2];
        float xScalar = width / range;
        float yScalar = height / range;
        for (int i = 0; i < positions.get(0).getScaledResults1().size(); i++) {
            float xSize = (positions.get(0).getScaledResults1().get(i) * xScalar);
            float ySize = (positions.get(0).getScaledResults2().get(i) * yScalar);
            canvasPositions[i][0] = Math.round(xSize);
            canvasPositions[i][1] = Math.round(ySize);
        }
//       shuffleCanvasPositions();
//        setNewCanvasPositions();
    }

    private void shuffleCanvasPositions() {
        ArrayList<int[]> canvasPositions2 = new ArrayList<>();

        for (int[] intArray : canvasPositions) {
            canvasPositions2.add(intArray.clone());
        }

        Collections.shuffle(canvasPositions2);

        for (int i = 0; i < canvasPositions2.size(); i++) {
            canvasPositions[i][0] = canvasPositions2.get(i)[0];
            canvasPositions[i][1] = canvasPositions2.get(i)[1];
        }
    }


    private void setNewCanvasPositions() {
        newCanvasPositions = new int[numberOfSquares][2];
        int increment = 100;
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

                //on the second loop increase values on the right hand side by 100 and decrease left hand side by 100
            } else if (loopNumber == 1) {

                int j;
                for (j = 0; j < canvasPositions.length; j++) {
                    if (j + i < newCanvasPositions.length) {
                        newCanvasPositions[j + i][0] = canvasPositions[j][0] + (canvasPositions[j][0] + 1) / 2;
                        newCanvasPositions[j + i][1] = canvasPositions[j][1];
                    }
                }
                i += j - 1;
                increment += 100;
                loopNumber++;
            } else if (loopNumber == 2) {
                int j;
                for (j = 0; j < canvasPositions.length; j++) {
                    if (j + i < newCanvasPositions.length) {
                        newCanvasPositions[j + i][1] = canvasPositions[j][1] + (canvasPositions[j][1] + 1) / 2;
                        newCanvasPositions[j + i][0] = canvasPositions[j][0];
                    }
                }
                i += j - 1;
                increment += 100;
                loopNumber++;
            } else if (loopNumber == 3) {
                int j;
                for (j = 0; j < canvasPositions.length; j++) {
                    if (j + i < newCanvasPositions.length) {
                        newCanvasPositions[j + i][0] = canvasPositions[j][0] - (canvasPositions[j][0] + 1) / 2;
                        newCanvasPositions[j + i][1] = canvasPositions[j][1];
                    }
                }
                i += j - 1;
                increment += 100;
                loopNumber++;
            } else if (loopNumber == 4) {
                int j;
                for (j = 0; j < canvasPositions.length; j++) {
                    if (j + i < newCanvasPositions.length) {
                        newCanvasPositions[j + i][1] = canvasPositions[j][1] - (canvasPositions[j][1] + 1) / 2;
                        newCanvasPositions[j + i][0] = canvasPositions[j][0];
                    }
                }
                i += j - 1;
                increment += 100;
                loopNumber = 1;
            }

        }

    }
}