package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.example.workinprogress.DisplayImage;
import com.example.workinprogress.dataSetsAndComponents.DataSet;

import java.util.ArrayList;
import java.util.Random;

public class AutomaticDrawing extends Painting {

    private Canvas canvas;
    private int[][] anglesAndDirections;
    private Paint paint1;
    private Paint paint3;
    private Path path;
    private int[][] positionsToBeDrawn;

    private int[][] testPositionsToBeDrawn;

    public AutomaticDrawing(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);

//        System.out.println(positions.get(0).getResults());
//        System.out.println(locations.get(0).getResults());
//
//        System.out.println(lightDistanceAndSteps.get(2).getResults());

//        testPositionsToBeDrawn = new int[][]{{50, 50, 300, 300}, {300, 300, 150, 400}, {150, 400, 600, 800}, {600, 800, 200, 1000}};

    }


    @Override
    public void draw(Canvas canvas) {
        this.canvas = canvas;
        canvas.drawColor(Color.WHITE);
        width = getBounds().width();
        height = getBounds().height();

        paint1 = new Paint();
        paint1.setColor(Color.BLACK);
        paint1.setStrokeCap(Paint.Cap.ROUND);
        paint1.setStrokeJoin(Paint.Join.ROUND);


        convertPositionsToXYValues();
        testPositionsToBeDrawn = positionsToBeDrawn.clone();
        drawStepsFluidLine();
//        drawLine();

    }


    private void drawStepsFluidLine() {

        path = new Path();
        Path path2 = new Path();
        Path path3 = new Path();

        Paint paint2 = new Paint();

        paint2.setStyle(Paint.Style.STROKE);
        paint2.setColor(Color.GRAY);
        paint2.setStrokeWidth(3);

        paint3 = new Paint();
        paint3.setStrokeWidth(3);

        paint1.setStrokeWidth(1);
        paint1.setStyle(Paint.Style.STROKE);

        boolean increaseWidth = true;

//        path.moveTo(testPositionsToBeDrawn[0][0], testPositionsToBeDrawn[0][1]);
//
//        for (int[] positions : testPositionsToBeDrawn) {
//
//            path.moveTo(positions[0], positions[1]);
//            path.cubicTo(positions[2], positions[1], positions[0], positions[3], positions[2], positions[3]);
//            canvas.drawPath(path, paint1);
//
//            path2.moveTo(positions[0], positions[1]);
//            path2.lineTo(positions[2], positions[1]);
//            path2.lineTo(positions[0], positions[3]);
//            path2.lineTo(positions[2], positions[3]);
//            canvas.drawPath(path2, paint2);
//        }

        path3.moveTo(positionsToBeDrawn[0][0], positionsToBeDrawn[0][1]);

        int lastControlX1 = positionsToBeDrawn[0][2];
        int lastControlY1 = positionsToBeDrawn[0][1];

        int lastControlX2 = positionsToBeDrawn[0][0];
        int lastControlY2 = positionsToBeDrawn[0][1];

        System.out.println("number of lines to draw = "+positionsToBeDrawn.length);

        for (int i = 0; i < positionsToBeDrawn.length; i++) {

            path.moveTo(positionsToBeDrawn[i][0], positionsToBeDrawn[i][1]);

            int originX = positionsToBeDrawn[i][0];
            int originY = positionsToBeDrawn[i][1];

            int destinationX = positionsToBeDrawn[i][2];
            int destinationY = positionsToBeDrawn[i][3];

            int thisControlX1;
            int thisControlY1 = positionsToBeDrawn[i][1];

            int thisControlX2 = positionsToBeDrawn[i][0];
            int thisControlY2 = positionsToBeDrawn[i][3];

            // check where the last line came from and where the next line is going on the x axis
            if (lastControlX2 < originX) {
                if (destinationX < originX) {
                    //line came from left and is going left like this >
                    thisControlX1 = ((originX - destinationX)) + destinationX;
                } else {
                    //line came from left and is going right like this \
                    thisControlX1 = destinationX;
                }
            } else {
                if (destinationX < originX) {
                    thisControlX1 = destinationX;
                    //line came from right and is going left like this /
                } else {
                    thisControlX1 = ((originX - destinationX)) + destinationX;
                    //line came from right and is going right like this <
                }
            }

//            // check where the last line came from and where the next line is going on the y axis
//            if(lastControlY1<originY){
//                if(destinationY<originY){
//                    //line came from
//                    thisControlY2 = ((originY-destinationY))+destinationY;
//                }else{
//                    //line came from left and is going right like this \
//                    thisControlY2 = destinationY;
//                }
//            }else{
//                if(destinationY<originY){
//                    thisControlY2 = destinationY;
//                    //line came from right and is going left like this /
//                }else{
//                    thisControlY2 = ((originY-destinationY))+destinationY;
//                    //line came from right and is going right like this <
//                }
//            }

            lastControlX1 = thisControlX1;
            lastControlX2 = thisControlX2;
            lastControlY1 = thisControlY1;
            lastControlY2 = thisControlY2;

            path.cubicTo(thisControlX1, thisControlY1, thisControlX2, thisControlY2, destinationX, destinationY);
//
//            path2.moveTo(testPositionsToBeDrawn[i][0], testPositionsToBeDrawn[i][1]);
//            path2.lineTo(testPositionsToBeDrawn[i][2], testPositionsToBeDrawn[i][1]);
//            path2.lineTo(testPositionsToBeDrawn[i][0], testPositionsToBeDrawn[i][3]);
//            path2.lineTo(testPositionsToBeDrawn[i][2], testPositionsToBeDrawn[i][3]);
//            canvas.drawPath(path2, paint2);

            canvas.drawPath(path, paint1);

            if (increaseWidth) {
                paint1.setStrokeWidth(paint1.getStrokeWidth() + 1);
                if (paint1.getStrokeWidth() >= 10) {
                    increaseWidth = false;
                }
            } else {
                paint1.setStrokeWidth(paint1.getStrokeWidth() - 1);
                if (paint1.getStrokeWidth() <= 1) {
                    increaseWidth = true;
                }
            }

            path.reset();

            if(i==1000){
                paint1.setAlpha(120);
                paint1.setColor(Color.GRAY);
            }

            if(i==2000){
//                paint1.setAlpha(255);
                paint1.setColor(Color.WHITE);
            }

        }

        convertLightValuesToColour();




//
//        path.cubicTo(300, 50, 100, 400, 400, 400);
//
//        path.cubicTo(600,400,600,800,800,800);
//        canvas.drawPath(path, paint1);
//
//        path.reset();
//        paint1.setColor(Color.GRAY);
//        paint1.setStrokeWidth(1);
//        path.moveTo(50, 50);
//        path.lineTo(300, 50);
//        path.lineTo(100, 400);
//        path.lineTo(400, 400);
//
//        canvas.drawPath(path, paint1);
    }

    private void convertLightValuesToColour() {

        ArrayList<Integer> lightValues = lightDistanceAndSteps.get(2).getScaledResults1();

        int totalValue = 0;
        int totalNumberofValues = (lightValues.size()) - 1;
        int average;
        int newColour;
        int xPosition;
        int yPosition;
        int circleSize;

        for (Integer light : lightValues) {
            totalValue += light;
        }

        totalValue -= lightValues.get(0);

        average = totalValue / totalNumberofValues;

        newColour = (int) (average * (255.0 / DisplayImage.IMAGE_SIZE_SCALAR));

//        System.out.println("totalValue = "+totalValue);
//        System.out.println("totalNumberOfValues = "+totalNumberofValues);
//        System.out.println("average = "+average);
//        System.out.println("newColour = "+newColour);

        paint3.setARGB(150, newColour, newColour, newColour);

        if (newColour < 30) {
            paint3.setAlpha(255);
        } else if (newColour < 60) {
            paint3.setAlpha(200);
        }

//        steps = lightDistanceAndSteps.get(0).getScaledResults1().get(0);
//        distance = lightDistanceAndSteps.get(1).getScaledResults1().get(0);

//        if (steps < width) {
//            xPosition = steps;
//        } else {
//            xPosition = steps % (int) width;
//        }
//
//        if (distance < height) {
//            yPosition = distance;
//        } else {
//            yPosition = distance % (int) height;
//        }
//
//        if (steps >= 10000) {
//            circleSize = 600;
//        } else {
//            circleSize = (int) ((600.0 / 10000) * steps);
//        }


        Random random = new Random();
        canvas.drawCircle((float) random.nextInt((int)width), (float) random.nextInt((int)height), (float) (newColour+20), paint3);


    }

    private void convertPositionsToXYValues() {

//        steps = lightDistanceAndSteps.get(0).getScaledResults1().get(0);

        anglesAndDirections = new int[positions.get(0).getScaledResults1().size()][2];

        System.out.println("size 1: "+positions.get(0).getScaledResults1().size());
        System.out.println("size 2: "+positions.get(0).getScaledResults2().size());
        System.out.println("size 3: "+positions.get(0).getScaledResults3().size());

        // this is where we will store all the x/y coordinates, as x1, y1, x2, y2
        positionsToBeDrawn = new int[anglesAndDirections.length][4];

        for (int i = 0; i < anglesAndDirections.length; i++) {

            // multiply position by 0.1 to get the length of the line and %360 to get the angle
            anglesAndDirections[i][0] = (int) ((positions.get(0).getScaledResults1().get(i)) * 0.5);

            // normalise line lengths by removing 50 from values larger than 50 and converting subsequently negative
            //values back to positives

            anglesAndDirections[i][0] -= 50;
            if (anglesAndDirections[i][0] < 0) {
                anglesAndDirections[i][0] *= -1;
            }

            anglesAndDirections[i][1] = (positions.get(0).getScaledResults2().get(i)) % 360;

//            System.out.println("increment" + anglesAndDirections[i][0]);
//            System.out.println("angle" + anglesAndDirections[i][1]);
        }

        int startX = (int) (Math.round(width / 1.61803398875));
        int startY = startX;

        for (int i = 0; i < anglesAndDirections.length; i++) {

            int length = anglesAndDirections[i][0];
            int angle = anglesAndDirections[i][1];
            int originalAngle = angle;

            int xMultiplier;
            int yMultiplier;

            if (angle > 270) {
                xMultiplier = -1;
                yMultiplier = 1;
                angle -= 270;
            } else if (angle > 180) {
                xMultiplier = -1;
                yMultiplier = 1;
                angle -= 180;
            } else if (angle > 90) {
                xMultiplier = 1;
                yMultiplier = -1;
                angle -= 90;
            } else {
                xMultiplier = 1;
                yMultiplier = 1;
            }

            double angleB = 180 - (angle + 90);

            double lengthOfstopY = (length * Math.sin(Math.toRadians(angle))) / Math.sin(Math.toRadians(90));
            double lengthOfstopX = (length * Math.sin(Math.toRadians(angleB))) / Math.sin(Math.toRadians(90));

            double stopX = lengthOfstopX;
            double stopY = lengthOfstopY;

            stopY *= yMultiplier;
            stopX *= xMultiplier;

            stopX = Math.round((float) stopX) + startX;
            stopY = Math.round((float) stopY) + startY;


            if (stopX > width - 30) {
                stopX = stopX - startX - startX - lengthOfstopX;
                if (stopX < 30) {
                    stopX = 30;
                }
            } else if (stopX < 30) {
                stopX = stopX + startX + startX + lengthOfstopX;
                if (stopX > width - 30) {
                    stopX = width - 30;
                }
            }

            if (stopY > height - 30) {
//                System.out.println(stopY + " too high");
                stopY = stopY - startY - startY - lengthOfstopY;
                if (stopY < 30) {
                    stopY = 30;
                }
//                System.out.println(stopY + " fixed");
            } else if (stopY < 30) {
                stopY = stopY + startY + startY + lengthOfstopY;
                if (stopY > height - 30) {
                    stopY = height - 30;
                }
            }

            positionsToBeDrawn[i][0] = startX;
            positionsToBeDrawn[i][1] = startY;
            positionsToBeDrawn[i][2] = (int) stopX;
            positionsToBeDrawn[i][3] = (int) stopY;

            startX = (int) stopX;
            startY = (int) stopY;

        }


    }

}
