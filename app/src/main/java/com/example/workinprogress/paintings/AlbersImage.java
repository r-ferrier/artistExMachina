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

/**
 * AlbersImage class draws a series of squares in different colours and opacities on top of one another,
 * inspired by the work of Josef Albers
 */
public class AlbersImage extends PositionAndLightPainting {

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

    /**
     * constructor sets colour values and the number of squares to be created (8 times the number of movements)
     * @param context
     * @param dataSets
     */
    public AlbersImage(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);

        paint1 = new Paint();
        //this colour has been chosen as the starting point
        paint1.setARGB(10, 0, 0, 0);

        setColoursValues();
        numberOfSquares = positionValues1.size()*8;
    }

    /**
     * First sets canvas background and height and width, as with all paintings.
     * Checks if drawing has been made before. If not, sets up drawing. Then draws.
     * @param canvas
     */
    public void draw(Canvas canvas) {

        this.canvas = canvas;
        canvas.drawColor(Color.WHITE);
        width = getBounds().width();
        height = getBounds().height();

        if(canvasPositions==null) {
            setCanvasPositions();
            setSquareSizes();
        }
        drawImage(canvasPositions, coloursValues, newShapeSize);
    }

    /**
     * method to actually do the drawing. Takes in an arraylist of x,y coordinates, colours, and sizes.
     * Uses these to generate the squares for the drawing.
     * @param canvasPositions x,y coordinates for position of each square
     * @param coloursValues colours for each square
     * @param shapeSize size for each square
     */
    private void drawImage(int[][] canvasPositions, int[][] coloursValues, ArrayList<Integer> shapeSize) {

        //for each position, set paint to the next available colour, draw square of the next size at the next position
        for (int i = 0; i < canvasPositions.length; i++) {
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

    /**
     * method to set up the array of square sizes
     */
    private void setSquareSizes() {
        shapeSize = new ArrayList<>();

        //add a list of all scaled results to shapeSizes
        for (float f : threePointsDataSets.get(0).getScaledResults3()) {
            float zSize = f;
            shapeSize.add(Math.round(zSize));
        }

        //set results to be >0, <500
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

        //multiply sizes by scalar
        for (int i = 0; i < shapeSize.size(); i++) {
            shapeSize.set(i, (int)((shapeSize.get(i) * scalar) - smallestShape));
        }

        float sizeScalar = maximumSquareSize / positionsSquareSizeRange;

        for (int i = 0; i < shapeSize.size(); i++) {
            shapeSize.set(i, (int) (sizeScalar * shapeSize.get(i))+10);
        }

        Collections.sort(shapeSize, Collections.reverseOrder());
        newShapeSize = (ArrayList<Integer>) shapeSize.clone();

        for (int i = 0; i < numberOfSquares; i++) {
            int j;
            for (j = 0; j < shapeSize.size(); j++) {
                newShapeSize.add(shapeSize.get(j));
            }
            i += j - 1;
        }
    }

    /**
     * method to set up the array of possible colours
     */
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

    /**
     * helper method to check for and adjust any invisible colours
     * @param colourArray array of colours to check
     * @return modified colour array with any invisible colours changed
     */
    private int[] invisibleColours(int[] colourArray) {
        int numberOfInvisibleChannels = 0;
        //check how many channels are at less than 20
        for (int j = 1; j < 4; j++) {
            if (colourArray[j] < 20) {
                numberOfInvisibleChannels++;
            }
        }
        //if more than 2 channels are less than 20, the colour may be invisible. find the highest
        //value out of RGB and increase this by 70 to prevent this. Reset the alpha to 20 to offset
        //the sudden jump in colour
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

    /**
     * method to set the position for each square, using the accelerometer data
     */
    private void setCanvasPositions() {

        Random random = new Random();

        canvasPositions = new int[numberOfSquares][2];

        float xScalar = width / range;
        float yScalar = height / range;
        int j = 0;

        //creates a list of position integers for each accelerometer reading. Each takes the x as its
        //x coord, the y as its y coord, and then then adds in 6 positions placed around this randomly
        //to create some noise
        for (int i = 0; i < canvasPositions.length; i+=8) {

            float xSize = (positionValues1.get(j) * xScalar);
            float ySize = (positionValues2.get(j) * yScalar);
            j++;

            int a1 = Math.round(xSize);
            int a2 = Math.round(ySize);
            int b1 = (a1-50)+random.nextInt(100);
            int b2 = (a2-50)+random.nextInt(100);
            int c1 = (a1-50)+random.nextInt(100);
            int c2 = (a2-50)+random.nextInt(100);
            int d1 = (b1-50)+random.nextInt(100);
            int d2 = (b2-50)+random.nextInt(100);
            int e1 = (c1-100)+random.nextInt(200);
            int e2 = (c2-100)+random.nextInt(200);
            int f1 = (d1-100)+random.nextInt(200);
            int f2 = (d2-100)+random.nextInt(200);
            int g1 = (e1-150)+random.nextInt(300);
            int g2 = (e2-150)+random.nextInt(300);
            int h1 = (f1-200)+random.nextInt(400);
            int h2 = (f2-200)+random.nextInt(400);

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
    }
}
