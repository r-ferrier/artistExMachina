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

    public AbstractShapes(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);
    }

    public void draw(Canvas canvas) {

        this.canvas = canvas;
        canvas.drawColor(Color.BLACK);
        width = getBounds().width();
        height = getBounds().height();

        if (shapes.size() < 1) {

            setFirstStartingPosition();
            getDataForDrawingShapes();

            if (numberOfShapes < 50) {
                setStartingPositions(0);
                drawALoadOfShapes();
                drawALoadOfShapes();
            } else {
                setStartingPositions(0);
                drawALoadOfShapes();
                drawALoadOfShapes();
                drawALoadOfShapes();
                drawALoadOfShapes();
            }
        } else {

            for (Shape shape : shapes) {
                shape.draw(canvas);
            }
        }
    }

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

    private void setStartingPositions(int i) {

        switch (startingDegreeChoice) {
            case 0:
                startX1 = startX2+10;
                startY1 = (int) height;
                startX2 = startX1 + lineWidths[i];
                startY2 = (int) height;
                break;
            case 1:
                startX1 = 0;
                startY1 = startY2+10;
                startX2 = 0;
                startY2 = startY1 + lineWidths[i];
                break;
            case 2:
                startX2 = startX1-10;
                startY1 = (int) height;
                startX1 = startX2 - lineWidths[i];
                startY2 = (int) height;
                break;
            case 3:
                startX1 = 0;
                startY2 = startY1-10;
                startX2 = 0;
                startY1 = startY2 - lineWidths[i];
                break;
            case 4:
                startX1 = startX2-10;
                startY1 = 0;
                startX2 = startX1 - lineWidths[i];
                startY2 = 0;
                break;
            case 5:
                startX1 = (int) width;
                startY1 = startY2-10;
                startX2 = (int) width;
                startY2 = startY1 - lineWidths[i];
                break;
            case 6:
                startX2 = startX1+10;
                startY1 = 0;
                startX1 = startX2 + lineWidths[i];
                startY2 = 0;
                break;
            case 7:
                startX1 = (int) width;
                startY2 = startY1+10;
                startX2 = (int) width;
                startY1 = startY2 + lineWidths[i];
                break;
        }
    }

    private void chooseColours(int[] startingColour) {

        ArrayList<int[]> temporaryHoldingArray = new ArrayList<>();
        Random random = new Random();
        temporaryHoldingArray.add(startingColour);

        for (int i = 1; i < lightValues.size(); i++) {

            int[] colour = temporaryHoldingArray.get(i - 1).clone();
            int channelToChange = random.nextInt(3) + 1;
            int multiplier = 1;
            int increment = lightValues.get(i);

            if (colour[channelToChange] > 255 - increment) {
                multiplier = -1;
            }
            colour[channelToChange] += increment * multiplier;
            temporaryHoldingArray.add(colour);
        }

        colourValuesArray.addAll(temporaryHoldingArray);
    }

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
            sizeUpperBounds = (double) averageSize / 1.5 - sizeLowerBounds;
            numberOfColours = 2;
            shapesToBeChosen = 1;
        } else if (numberOfShapes < 200) {
            sizeLowerBounds = (double) averageSize / 64;
            sizeUpperBounds = (double) averageSize / 10 - sizeLowerBounds;
            numberOfColours = 4;
            shapesToBeChosen = 2;
        } else if (numberOfShapes < 400) {
            sizeLowerBounds = (double) averageSize / 100;
            sizeUpperBounds = (double) averageSize / 20 - sizeLowerBounds;
            numberOfColours = 6;
            shapesToBeChosen = 3;
        } else {
            sizeLowerBounds = (double) averageSize / 300;
            sizeUpperBounds = (double) averageSize / 40 - sizeLowerBounds;
            numberOfColours = 9;
            shapesToBeChosen = 4;
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

    protected ArrayList<Integer> setUniqueValueSortedArrays(ArrayList<Integer> sortedArray) {

        for (int i = 0; i < sortedArray.size() - 1; i++) {
            while (sortedArray.get(i + 1) == sortedArray.get(i)) {
                sortedArray.remove(i + 1);
                if(sortedArray.size()-1==i){
                    break;
                }
            }
        }

        while (sortedArray.size() < 10) {
            sortedArray.add(0);
        }

        return sortedArray;

    }

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
//            if (choice <= 3) {
//                shape = new LineShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, widthRestrictedlineLength, coloursForThisLoop);
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

    private void drawALoadOfShapes() {

        int loopStartX1 = startX1;
        int loopStartY1 = startY1;
        int loopStartX2 = startX2;
        int loopStartY2 = startY2;

        Random random = new Random();

        int lineWidthIndex = 0;

        for (int i = 0; i < numberOfShapes; i++) {

            int j = i;
            int[] coloursForThisLoop;

            while (colourValuesArray.size() <= j) {
                j -= colourValuesArray.size();
            }
            coloursForThisLoop = colourValuesArray.get(j);

            int widthRestrictedlineLength = (random.nextInt(10000 / numberOfShapes)) + lineWidths[lineWidthIndex];

            Shape shapeInLoop = setShape(loopStartX1, loopStartY1, loopStartX2, loopStartY2, widthRestrictedlineLength, coloursForThisLoop);

            shapeInLoop.draw(canvas);
            shapes.add(shapeInLoop);

            loopStartX1 = shapeInLoop.getX1End();
            loopStartY1 = shapeInLoop.getY1End();
            loopStartX2 = shapeInLoop.getX2End();
            loopStartY2 = shapeInLoop.getY2End();

            if ((loopStartX1 < 0 && loopStartX2<0)|| (loopStartX1 > width && loopStartX2>width) || (loopStartY1 < 0 && loopStartY2<0)|| (loopStartY1 > height && loopStartY2>height)) {

                lineWidthIndex++;

                setStartingPositions(lineWidthIndex);

                loopStartX1 = startX1;
                loopStartY1 = startY1;
                loopStartX2 = startX2;
                loopStartY2 = startY2;

                if (startX1 > width || startX1 < 0 || startY1 > height || startY1 < 0 || startX2 > width || startX2 < 0 || startY2 > height || startY2 < 0)
                    break;
            }
          //  shapes.add(shapeInLoop);
        }
    }
}
