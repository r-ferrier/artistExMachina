package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.example.workinprogress.DisplayImage;
import com.example.workinprogress.dataSetsAndComponents.DataSet;

import java.util.ArrayList;
import java.util.Random;

public class AutomaticDrawing extends PositionAndLightPainting {

    private Canvas canvas;
    private int[][] anglesAndDirections;
    private Paint paint1;
    private Path path;
    private int[][] positionsToBeDrawn;
    private ArrayList<float[]>shapesInformation;
    private ArrayList<Paint>shapesPaints;

    /**
     * AutomaticDrawing creates a series of bezier curves and shapes, inspired by Surrealist automatic drawings
     * and alexander calder
     * @param context
     * @param dataSets
     */
    public AutomaticDrawing(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);
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
        drawStepsFluidLine();

        if(shapesInformation==null){
            drawShapes();
        }

        for(int i = 0; i<shapesInformation.size(); i++){
            canvas.drawCircle(shapesInformation.get(i)[0],shapesInformation.get(i)[1],shapesInformation.get(i)[2],shapesPaints.get(i));
        }
    }


    private void drawStepsFluidLine() {

        path = new Path();

        paint1.setStrokeWidth(1);
        paint1.setStyle(Paint.Style.STROKE);

        boolean increaseWidth = true;

        int lastControlX2 = positionsToBeDrawn[0][0];

        for (int i = 0; i < positionsToBeDrawn.length; i++) {

            path.moveTo(positionsToBeDrawn[i][0], positionsToBeDrawn[i][1]);

            int originX = positionsToBeDrawn[i][0];

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

            lastControlX2 = thisControlX2;

            path.cubicTo(thisControlX1, thisControlY1, thisControlX2, thisControlY2, destinationX, destinationY);

            canvas.drawPath(path, paint1);

            //increase and decrease width of line as drawing is made to mimic pen strokes
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
        }

    }

    private void drawShapes() {

        int totalValue = 0;
        int totalNumberofValues = (lightValues.size()) - 1;
        int average;
        int newColour;
        Random random = new Random();
        int numberOfShapes = random.nextInt(6);
        shapesInformation = new ArrayList<>();
        shapesPaints = new ArrayList<>();
        int paintAlpha;

        for (Integer light : lightValues) {
            totalValue += light;
        }

        totalValue -= lightValues.get(0);
        average = totalValue / totalNumberofValues;

        newColour = (int) (average * (255.0 / DisplayImage.IMAGE_SIZE_SCALAR));


        if (newColour < 30) {
            paintAlpha = 50;
        } else if (newColour < 60) {
            paintAlpha = 100;
        } else if(newColour < 120){
            paintAlpha = 150;
        }else{
            paintAlpha = 200;
        }

        for(int i = 0; i<numberOfShapes;i++) {

            //set the paint colour
            int[] colours = new int[]{0,0,0};
            colours[random.nextInt(3)]=(random.nextInt(newColour));
            colours[random.nextInt(3)]=(random.nextInt(newColour));
            Paint paint = new Paint();
            paint.setARGB(paintAlpha,colours[0],colours[1],colours[2]);

            //set the shape size
            float shapeSize = random.nextInt(newColour-10)+10;

            //set the coordinates so that the shape will be inside the border
            float x = random.nextInt((int)((width-shapeSize)-20))+10;
            float y = random.nextInt((int)((height-shapeSize)-20))+10;

            //add the new shape information to lists for drawing from
            shapesInformation.add(new float[]{x,y,shapeSize});
            shapesPaints.add(paint);
        }

    }

    private void convertPositionsToXYValues() {

        anglesAndDirections = new int[positionValues1.size()][2];

        // this is where we will store all the x/y coordinates, as x1, y1, x2, y2
        positionsToBeDrawn = new int[anglesAndDirections.length][4];

        for (int i = 0; i < anglesAndDirections.length; i++) {

            // multiply position by 0.1 to get the length of the line and %360 to get the angle
            anglesAndDirections[i][0] = (int) ((positionValues1.get(i)) * 0.5);

            // normalise line lengths by removing 50 from values larger than 50 and converting subsequently negative
            //values back to positives

            anglesAndDirections[i][0] -= 50;
            if (anglesAndDirections[i][0] < 0) {
                anglesAndDirections[i][0] *= -1;
            }

            anglesAndDirections[i][1] = (positionValues2.get(i)) % 360;
        }

        int startX = (int) (Math.round(width / 1.61803398875));
        int startY = startX;

        for (int i = 0; i < anglesAndDirections.length; i++) {
            int length = anglesAndDirections[i][0];
            int angle = anglesAndDirections[i][1];

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
                stopY = stopY - startY - startY - lengthOfstopY;
                if (stopY < 30) {
                    stopY = 30;
                }
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
