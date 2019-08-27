package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

import com.example.workinprogress.R;
import com.example.workinprogress.dataSetsAndComponents.DataSet;
import com.example.workinprogress.paintings.shapes.BumpyShape;
import com.example.workinprogress.paintings.shapes.CircleShape;
import com.example.workinprogress.paintings.shapes.CurvedShape;
import com.example.workinprogress.paintings.shapes.LineShape;
import com.example.workinprogress.paintings.shapes.Shape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Creates an image composed of a series of drawable objects, Shapes, in a variety of colours. Each Shape
 * has two ends which join up to other shapes to create long chains of shapes which can then be placed
 * on the canvas. The ending position and direction of each shape dictates where the next shape will be placed.
 */
public class AbstractShapes extends PositionAndLightPainting {


    private Canvas canvas;
    private ArrayList<Shape> shapes  = new ArrayList<>();
    private ArrayList<int[]> colourValuesArray = new ArrayList<>();
    private int numberOfShapes;
    private int averageSize;
    private Random random = new Random();
    private int shapesToBeChosen;

    private int startX1 = 0;
    private int startY1 = 0;
    private int startX2 = 0;
    private int startY2 = 0;

    private int startingDegreeChoice = random.nextInt(8);

    private int[] lineWidths;
    private int minimumumAlpha;
    private int maximumAlpha;

    public AbstractShapes(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);
    }

    /**
     * First sets canvas background and height and width, as with all paintings. Then checks to see if
     * this object has already been created. If it hasn't, it creates the required amount of shapes before
     * drawing them onto the canvas.
     * @param canvas passed by view
     */
    public void draw(Canvas canvas) {

        this.canvas = canvas;
        canvas.drawColor(Color.BLACK);
        width = getBounds().width();
        height = getBounds().height();

        if (shapes.size() < 1) {

            // helper methods to set up data for creating shapes with
            setFirstStartingPosition();
            getDataForDrawingShapes();

            // if only a few movements, draw fewer shapes
            if (numberOfShapes < 50) {
                setStartingPositions(0);
                createShapes();
                createShapes();
            } else {
                setStartingPositions(0);
                createShapes();
                createShapes();
                createShapes();
                createShapes();
            }
        } else {

            for (Shape shape : shapes) {
                shape.draw(canvas);
            }
        }
    }

    /**
     * checks the starting degree that was randomly set for this painting when it was constructed.
     * Once it knows the starting degree choice, it can set what the starting positions should be
     * for the x and y coordinates for the first shape to be placed on the canvas.
     */
    private void setFirstStartingPosition() {

        if (startingDegreeChoice == 0 || startingDegreeChoice == 4) {
            startX2 = (int) ((double) width / 1.61803398875);
        } else if (startingDegreeChoice == 1 || startingDegreeChoice == 5) {
            startY2 = (int) ((double) width / 1.61803398875) ;
        } else if (startingDegreeChoice == 2 || startingDegreeChoice == 6) {
            startX1 = (int) (width - ((double) width / 1.61803398875));
        } else {
            startY1 = (int)(height - ((double) height / 1.61803398875));
        }
    }

    /**
     * used to set the starting position for a piece. Takes in the number of lines created so far so that
     * it can use this and the original starting angle to increment the current starting positions.
     * @param numberOfLinesSoFar number of lines created so far
     */
    private void setStartingPositions(int numberOfLinesSoFar) {

        //needs to know the direction and position of the original piece from startingdegreechoice
        switch (startingDegreeChoice) {
            case 0:
                startX1 = startX2+10;
                startY1 = (int) height;
                startX2 = startX1 + lineWidths[numberOfLinesSoFar];
                startY2 = (int) height;
                break;
            case 1:
                startX1 = 0;
                startY1 = startY2+10;
                startX2 = 0;
                startY2 = startY1 + lineWidths[numberOfLinesSoFar];
                break;
            case 2:
                startX2 = startX1-10;
                startY1 = (int) height;
                startX1 = startX2 - lineWidths[numberOfLinesSoFar];
                startY2 = (int) height;
                break;
            case 3:
                startX1 = 0;
                startY2 = startY1-10;
                startX2 = 0;
                startY1 = startY2 - lineWidths[numberOfLinesSoFar];
                break;
            case 4:
                startX1 = startX2-10;
                startY1 = 0;
                startX2 = startX1 - lineWidths[numberOfLinesSoFar];
                startY2 = 0;
                break;
            case 5:
                startX1 = (int) width;
                startY1 = startY2-10;
                startX2 = (int) width;
                startY2 = startY1 - lineWidths[numberOfLinesSoFar];
                break;
            case 6:
                startX2 = startX1+10;
                startY1 = 0;
                startX1 = startX2 + lineWidths[numberOfLinesSoFar];
                startY2 = 0;
                break;
            case 7:
                startX1 = (int) width;
                startY2 = startY1+10;
                startX2 = (int) width;
                startY1 = startY2 + lineWidths[numberOfLinesSoFar];
                break;
        }
    }

    /**
     * takes in a starting colour and then increments one of its channels up and down within a set
     * range to create different colours.
     * @param startingColour array of four positions containing ARGB values for the starting colour
     */
    private void chooseColours(int[] startingColour) {

        ArrayList<int[]> temporaryHoldingArray = new ArrayList<>();
        Random random = new Random();
        temporaryHoldingArray.add(startingColour);
        int multiplier = 1;
        int alphamultiplier = 1;
        int channelToChange = random.nextInt(3) + 1;

        for (int i = 1; i < lightValues.size(); i++) {

            //get most recently added ARGB values
            int[] colour = temporaryHoldingArray.get(i - 1).clone();
            //decide which channel to change this time

            int increment = lightValues.get(i);

            //if the increment will push the value over 255, minus it instead. Keep it minus until
            //it's going to push it under 255
            if (colour[channelToChange]+increment>255) {
                multiplier = -1;
            }else if (colour[channelToChange]-increment<0){
                multiplier = 1;
            }

            //if the increment will push the value over the maximum allowed, minus it instead. Keep it minus until
            //it's going to push it under the minimum allowed
            if (colour[0]+increment>maximumAlpha){
                alphamultiplier = -1;
            }else if (colour[0]-increment< minimumumAlpha){
                alphamultiplier = 1;
            }

            //increase/decrease the alpha and the chosen colour by the increment
            colour[channelToChange] += (increment * multiplier);
            colour[0] += (increment * alphamultiplier);

            //add the new colour to the array
            temporaryHoldingArray.add(colour);
        }

        colourValuesArray.addAll(temporaryHoldingArray);
    }


    /**
     * used to transform the data specifically for drawing shapes.
     */
    private void getDataForDrawingShapes() {

        //create new list for sizes
        ArrayList<Integer> sizes = (ArrayList<Integer>)positionValues1.clone();

        //create a new random and set the number of shapes
        Random random = new Random();
        numberOfShapes = (sizes.size());

        //shuffle lightvalues
        Collections.shuffle(lightValues);

        //create Arraylist of possible colours
        ArrayList<Integer> colours = new ArrayList<>();
        colours.add(context.getResources().getColor(R.color.juneBudYellow));
        colours.add(context.getResources().getColor(R.color.yellowOchre));
        colours.add(context.getResources().getColor(R.color.iguanaGreen));
        colours.add(context.getResources().getColor(R.color.imperialBlue));
        colours.add(context.getResources().getColor(R.color.dustyBlue));
        colours.add(context.getResources().getColor(R.color.coralRed));
        colours.add(context.getResources().getColor(R.color.metallicSeaweed));
        colours.add(context.getResources().getColor(R.color.purplePineapple));
        colours.add(context.getResources().getColor(R.color.rosyBrown));
        colours.add(context.getResources().getColor(R.color.purpleNavy));

        //create Arraylist of int[] containing colours broken down into their numbers
        ArrayList<int[]> coloursAsNumbers = new ArrayList<>();

        for(int i = 0; i<colours.size();i++){

            int alpha = 150;
            int red = Color.red(colours.get(i));
            int green = Color.green(colours.get(i));
            int blue = Color.blue(colours.get(i));

            coloursAsNumbers.add(new int[]{alpha,red,green,blue});
        }

        //shuffle the colours so that a random selection will be chosen
        Collections.shuffle(coloursAsNumbers);

        //find the total of all size values once converted to positive
        int totalSize = 0;

        for (Integer size : sizes) {
            size -= 500;
            if (size < 0) {
                size *= -1;
            }
            totalSize+=size;
        }

        //remove duplicate values from the list of sizes and sort array in size order
        ArrayList<Integer> highestSizes = setUniqueValueSortedArrays((ArrayList<Integer>)sizes.clone());

        //create an average size that is biased towards the highest sizes
        averageSize = (((highestSizes.get(0)+highestSizes.get(1)+highestSizes.get(2)+highestSizes.get(3)) / 4) + (totalSize / numberOfShapes)) / 2;
        averageSize = (500 - averageSize);

        //used to set lineWidths
        double sizeUpperBounds;
        double sizeLowerBounds;
        int numberOfColours;

        if (numberOfShapes < 80) {
            sizeLowerBounds = (double) averageSize / 220;
            sizeUpperBounds = (double) (averageSize / 1.5) - sizeLowerBounds;
            numberOfColours = 2;
            shapesToBeChosen = 1;
        } else if (numberOfShapes < 200) {
            sizeLowerBounds = (double) averageSize / 64;
            sizeUpperBounds = (double) (averageSize / 10) - sizeLowerBounds;
            numberOfColours = 4;
            shapesToBeChosen = 2;
        } else if (numberOfShapes < 400) {
            sizeLowerBounds = (double) averageSize / 100;
            sizeUpperBounds = (double) (averageSize / 20) - sizeLowerBounds;
            numberOfColours = 6;
            shapesToBeChosen = 3;
        } else {

            sizeLowerBounds = (double) averageSize / 300;
            sizeUpperBounds = (double) averageSize / 40 - sizeLowerBounds;
            numberOfColours = 9;
            shapesToBeChosen = 4;
        }

        if(sizeUpperBounds<1)sizeUpperBounds = 1;

        //find the average lightlevel, convert the lightlevels to a scale of 255, and set the alpha values accordingly
        int lightTotal = 0;
        int lightCount = lightValues.size();
        for(int i = 0; i<lightValues.size(); i++){
            lightValues.set(i,(int)(lightValues.get(i)*0.255));
            lightTotal+=lightValues.get(i);
        }
        minimumumAlpha = lightTotal/lightCount;
        maximumAlpha = 127+minimumumAlpha;

        //if minimum alpha is greater than 125, set it to 125
        if (minimumumAlpha>125){
            minimumumAlpha = 125;
            maximumAlpha = 255;
        }

        //use the choosecolours method with randomly selected colours the number of times dictated by the algorithm
        for (int i = 0; i<numberOfColours; i++){
            chooseColours(coloursAsNumbers.get(i));
        }

        lineWidths = new int[2000];

        for (int i = 0; i < lineWidths.length; i++) {
            lineWidths[i] = random.nextInt((int) sizeUpperBounds) + (int) sizeLowerBounds;
        }
    }

    /**
     * Method to create a new shape. Generates a random number and then chooses a shape from the
     * selection allowed set by shapesToBeChosen.
     * @param loopStartX1 starting point x1
     * @param loopStartY1 starting point y1
     * @param loopStartX2 starting point x2
     * @param loopStartY2 starting point y2
     * @param widthRestrictedlineLength length of shape
     * @param coloursForThisLoop colour of shape
     * @return Shape object to be added to canvas
     */
    private Shape setShape(int loopStartX1, int loopStartY1, int loopStartX2, int loopStartY2,
                           int widthRestrictedlineLength, int[] coloursForThisLoop) {

        Shape shape;

        int choice = random.nextInt(20);

        if (shapesToBeChosen == 1) {
            if (choice <= 7) {
                shape = new CurvedShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, random.nextBoolean(), coloursForThisLoop, (int) (widthRestrictedlineLength * 1.5));
            } else if (choice <= 14) {
                shape = new CircleShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, widthRestrictedlineLength, coloursForThisLoop);
            } else {
                shape = new BumpyShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, widthRestrictedlineLength, coloursForThisLoop);
            }
        } else if (shapesToBeChosen == 2) {
            if (choice <= 3) {
                shape = new LineShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, widthRestrictedlineLength, coloursForThisLoop);
            } else if (choice <= 10) {
                shape = new CurvedShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, random.nextBoolean(), coloursForThisLoop, (int) (widthRestrictedlineLength * 1.5));
            } else if (choice <= 14) {
                shape = new CircleShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, widthRestrictedlineLength, coloursForThisLoop);
            } else {
                shape = new BumpyShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, widthRestrictedlineLength, coloursForThisLoop);
            }
        } else if (shapesToBeChosen == 3) {
           if(choice<=8){
                shape = new CurvedShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, random.nextBoolean(), coloursForThisLoop, (int) (widthRestrictedlineLength * 1.5));
            } else {
                shape = new CircleShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, widthRestrictedlineLength, coloursForThisLoop);
            }
        } else {
            if (choice <= 2) {
                shape = new LineShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, widthRestrictedlineLength, coloursForThisLoop);
            } else if (choice <= 12) {
                shape = new CurvedShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, random.nextBoolean(), coloursForThisLoop, (int) (widthRestrictedlineLength * 1.5));
            } else if(choice<=16){
                shape = new CircleShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, widthRestrictedlineLength, coloursForThisLoop);
            }else {
                shape = new BumpyShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, widthRestrictedlineLength, coloursForThisLoop);
            }
        }
        return shape;
    }

    /**
     * Method to create all of the necessary shapes for the canvas
     */
    private void createShapes() {

        //begins with new starting positions, a random, and sets the number of lines drawn to 0
        int loopStartX1 = startX1;
        int loopStartY1 = startY1;
        int loopStartX2 = startX2;
        int loopStartY2 = startY2;
        Random random = new Random();
        int numberOfLinesSoFar = 0;

        //loops for the number of shapes
        for (int i = 0; i < numberOfShapes; i++) {

            int j = i;
            int[] coloursForThisLoop;

            //sets j to loop over the colourvalues array within this loop and select the colours for the next shape
            while (colourValuesArray.size() <= j) {
                j -= colourValuesArray.size();
            }
            coloursForThisLoop = colourValuesArray.get(j);

            // checks which type of drawing is being made and limits the potential length of each shape accordingly
            int widthRestrictedlineLength;
            if (shapesToBeChosen==4) {
                widthRestrictedlineLength = (random.nextInt(30000 / numberOfShapes)) + lineWidths[numberOfLinesSoFar];
            }else if(shapesToBeChosen==3){
                widthRestrictedlineLength = (random.nextInt(5000 / numberOfShapes)) + lineWidths[numberOfLinesSoFar];
            } else{
                widthRestrictedlineLength = (random.nextInt(10000 / numberOfShapes)) + lineWidths[numberOfLinesSoFar];
            }

            //creates new shape using newly set length and colour
            Shape shapeInLoop = setShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, widthRestrictedlineLength, coloursForThisLoop);

            //draws shape and adds it to list.
            // *IMPORTANT* Shape MUST be drawn within this loop in order to get back the correct end points for the next shape
            shapeInLoop.draw(canvas);
            shapes.add(shapeInLoop);

            //gets ends from newly drawn shape
            loopStartX1 = shapeInLoop.getX1End();
            loopStartY1 = shapeInLoop.getY1End();
            loopStartX2 = shapeInLoop.getX2End();
            loopStartY2 = shapeInLoop.getY2End();

            //if the ends of the last shape/start of the next are BOTH outside the border, start a new line
            if ((loopStartX1 < 0 && loopStartX2<0)|| (loopStartX1 > width && loopStartX2>width) || (loopStartY1 < 0 && loopStartY2<0)|| (loopStartY1 > height && loopStartY2>height)) {
               //increment linecounter and start new line with new starting position
                numberOfLinesSoFar++;
                setStartingPositions(numberOfLinesSoFar);
                loopStartX1 = startX1;
                loopStartY1 = startY1;
                loopStartX2 = startX2;
                loopStartY2 = startY2;

                //if the new start is also now offscreen, break and return from these loops.
                if (startX1 > width || startX1 < 0 || startY1 > height || startY1 < 0 || startX2 > width || startX2 < 0 || startY2 > height || startY2 < 0)
                    break;
            }
        }
    }
}
