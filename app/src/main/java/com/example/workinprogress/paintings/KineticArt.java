package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.example.workinprogress.R;
import com.example.workinprogress.dataSetsAndComponents.DataSet;
import com.example.workinprogress.dataSetsAndComponents.SensorSingularPointDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class KineticArt extends AbstractShapes {

    private Canvas canvas;
    private ArrayList<Path> paths;
    private ArrayList<Paint> paints;
    private ArrayList<int[]> controls;
    private Random random;
    private ArrayList<Paint> paintsOptions;
    private int numberOfLoops;
    private int numberOfPaintOptions = 9;
    private ArrayList<Integer> sizes;
    private int sizeRange = 500;
    private int sizeCounter = 0;
    private int paintsCounter = 0;
    ArrayList<Integer> highestPositionValues;
    ArrayList<Integer> lowestPositionValues;
    private int increment;
    private int manyMovements = 200;


    private int averageLightValue;
    private int averageSize;
    private int canvasColor;

    private int[] positionsForPlotting = new int[20];


    public KineticArt(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);
        random = new Random();
    }

    private void setNewData() {


        ArrayList<Integer> lightValues = (ArrayList<Integer>)((SensorSingularPointDataSet) singularPointDataSets.get(2)).getScaledResults1().clone();
        sizes = (ArrayList<Integer>) threePointsDataSets.get(0).getScaledResults1().clone();
        sizes.addAll((ArrayList<Integer>) threePointsDataSets.get(0).getScaledResults2().clone());
        sizes.addAll((ArrayList<Integer>) threePointsDataSets.get(0).getScaledResults3().clone());

        for (int i = 0; i < sizes.size(); i++) {
            sizes.set(i, sizes.get(i) - 500);
            if (sizes.get(i) < 0) {
                sizes.set(i, sizes.get(i) * -1);
            }
        }

        averageLightValue = getAverage(lightValues);
        averageSize = getAverage(sizes);


        if (sizes.size() <= 50) {
            numberOfLoops = 8;
        } else if (sizes.size() <= 100) {
            numberOfLoops = 12;
        } else if (sizes.size() <= manyMovements) {
            numberOfLoops = 24;
        } else {
            numberOfLoops = 32;
        }

        setHighestAndLowestValueArrays();

    }

    private void setHighestAndLowestValueArrays() {

        highestPositionValues = (ArrayList<Integer>) sizes.clone();

        Collections.sort(highestPositionValues, Collections.reverseOrder());
        highestPositionValues = setUniqueValueSortedArrays(highestPositionValues);

        int possibleWidthValues = (int)(width/2);
        int possibleHeightValues = (int)(height/2);

        positionsForPlotting[0] = possibleWidthValues-(highestPositionValues.get(0)*2);
        positionsForPlotting[1] = possibleHeightValues-(highestPositionValues.get(highestPositionValues.size()-1)*2);
        positionsForPlotting[2] = possibleWidthValues+(highestPositionValues.get(1)*2);
        positionsForPlotting[3] = possibleHeightValues-(highestPositionValues.get(highestPositionValues.size()-2)*2);
        positionsForPlotting[4] = possibleWidthValues+(highestPositionValues.get(2)*2);
        positionsForPlotting[5] = possibleHeightValues+(highestPositionValues.get(highestPositionValues.size()-3)*2);
        positionsForPlotting[6] = possibleWidthValues-(highestPositionValues.get(3)*2);
        positionsForPlotting[7] = possibleHeightValues+(highestPositionValues.get(highestPositionValues.size()-4)*2);
        positionsForPlotting[8] = positionsForPlotting[0];
        positionsForPlotting[9] = positionsForPlotting[1];

        positionsForPlotting[10] = possibleHeightValues-(highestPositionValues.get(highestPositionValues.size()-1)*2);
        positionsForPlotting[11] = possibleWidthValues-(highestPositionValues.get(0)*2);
        positionsForPlotting[12] = possibleHeightValues-(highestPositionValues.get(highestPositionValues.size()-2)*2);
        positionsForPlotting[13] = possibleWidthValues+(highestPositionValues.get(1)*2);
        positionsForPlotting[14] = possibleHeightValues+(highestPositionValues.get(highestPositionValues.size()-3)*2);
        positionsForPlotting[15] = possibleWidthValues+(highestPositionValues.get(2)*2);
        positionsForPlotting[16] = possibleHeightValues+(highestPositionValues.get(highestPositionValues.size()-4)*2);
        positionsForPlotting[17] = possibleWidthValues-(highestPositionValues.get(3)*2);
        positionsForPlotting[18] = positionsForPlotting[10];
        positionsForPlotting[19] = positionsForPlotting[11];


        for(int i = 0; i<positionsForPlotting.length;i++){
            if(positionsForPlotting[i]%2==0){
                if(positionsForPlotting[i]>possibleWidthValues) {
                    positionsForPlotting[i] -= random.nextInt(500);
                }else{
                    positionsForPlotting[i] += random.nextInt(500);
                }
            }
            else{
                if(positionsForPlotting[i]>possibleHeightValues) {
                    positionsForPlotting[i] += random.nextInt(500);
                }else{
                    positionsForPlotting[i] -= random.nextInt(500);
                }
            }
        }

    }

    //helper method to return average of any array of Integers
    private int getAverage(ArrayList<Integer> values) {
        int total = 0;

        for (int i = 0; i < values.size(); i++) {
            total += values.get(i);
        }
        return total / values.size();
    }

    // method to create all the available paint options and set their transparencies nominally to 100.
    // Calls on a second method to build final array of paint colours.
    private void setPaints() {

        paintsOptions = new ArrayList<>();

        for (int i = 0; i < numberOfPaintOptions; i++) {
            paintsOptions.add(new Paint());
        }

        paintsOptions.get(0).setColor(context.getResources().getColor(R.color.yellowOchre));
        paintsOptions.get(1).setColor(context.getResources().getColor(R.color.coralRed));
        paintsOptions.get(2).setColor(context.getResources().getColor(R.color.metallicSeaweed));
        paintsOptions.get(3).setColor(context.getResources().getColor(R.color.imperialBlue));
        paintsOptions.get(4).setColor(context.getResources().getColor(R.color.juneBudYellow));
        paintsOptions.get(5).setColor(context.getResources().getColor(R.color.iguanaGreen));
        paintsOptions.get(6).setColor(context.getResources().getColor(R.color.rosyBrown));
        paintsOptions.get(7).setColor(context.getResources().getColor(R.color.purpleNavy));
        paintsOptions.get(8).setColor(context.getResources().getColor(R.color.purplePineapple));

        choosePaints();
    }

    // method to decide which and how many colours to use from the available options
    private void choosePaints() {

        canvasColor = Color.WHITE;

        int numberOfOptions;


        if (averageLightValue < 1500) {
            if (averageLightValue < 200) {
                numberOfOptions = 0;
                increment = 5;
            } else {
                numberOfOptions = 3;
                increment = 5;
            }
        } else {
            if (averageLightValue < 10000) {
                numberOfOptions = 6;
                increment = 10;
            } else {
                numberOfOptions = 9;
                increment = 20;
            }
        }

        for(Paint paint:paintsOptions){
            int color = paint.getColor();
            if(random.nextBoolean()) {
                paint.setColor(color + averageLightValue);
            }else{
                paint.setColor(color - averageLightValue);
            }
        }

        removePaintsFromOptions(numberOfOptions);
    }

    // helper method to remove paints from choices and set one colour opaque
    private void removePaintsFromOptions(int numberOfOptions) {

        if (numberOfOptions == 0) {
            paintsOptions = new ArrayList<>();
            paintsOptions.add(new Paint());
            paintsOptions.get(paintsOptions.size() - 1).setColor(Color.BLACK);
        } else {
            for (int i = numberOfPaintOptions; i > numberOfOptions; i--) {
                paintsOptions.remove(random.nextInt(i));
            }
        }
    }


    @Override
    public void draw(Canvas canvas) {

        this.canvas = canvas;
        this.canvas.drawColor(canvasColor);

        width = getBounds().width();
        height = getBounds().height();

        setNewData();

        if (paths == null) {
            setUpDrawing();
        }

        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), paints.get(paintsCounter++));
            if (paintsCounter > paints.size() - 1) {
                paintsCounter = 0;
            }
        }
    }

    private void setUpDrawing() {

        controls = new ArrayList<>();
        paths = new ArrayList<>();
        paints = new ArrayList<>();
        setPaints();

        for(int i = 0; i<4;i++){
            createPaths();
        }

        for (int[] control : controls) {
            paths.add(new Path());
            paints.addAll(paintsOptions);
        }

        for (Paint paint : paints) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
        }
        int loopSize;

        if(sizes.size()<manyMovements){
            loopSize = controls.size()/2;
        }else{
            loopSize = controls.size();
        }

        for (int i = 0; i < loopSize; i++) {
            int numberOfLines = random.nextInt(numberOfLoops);
            if(numberOfLines>40){
                numberOfLines = 40;
            }

            paths.add(drawRibbon(controls.get(i).clone(), increment, numberOfLines, new Path()));
            paths.set(i, drawRibbon(controls.get(i).clone(), increment * -1, numberOfLines, paths.get(i)));
        }
    }

    private Path drawRibbon(int[] controls, int increment, int numberOfLines, Path path) {

        path.moveTo(controls[0], controls[1]);
        path.cubicTo(controls[2], controls[3], controls[4], controls[5], controls[6], controls[7]);

        for (int i = 0; i < numberOfLines; i++) {

            path.moveTo(controls[0], controls[1]);
            path.cubicTo(controls[2], controls[3], controls[4], controls[5], controls[6], controls[7]);

            controls[3] -= increment;
            controls[5] -= increment;
            controls[2] += increment;
            controls[4] += increment;
        }

        return path;
    }

    private void createPaths(){


        for (int i = 0; i < positionsForPlotting.length - 3; i += 2) {

            int x1 = positionsForPlotting[i];
            int y1 = positionsForPlotting[i + 1];

            int x3 = positionsForPlotting[i + 2];
            int y3 = positionsForPlotting[i + 3];


            int controlX1 = x1 + (random.nextInt((int) ((width / 2)-50)))+50;
            int controlY1 = y1 + (random.nextInt((int) ((height / 2)-50)))+50;
            int controlX2 = x1 - (random.nextInt((int) ((width / 2)-50)))+50;
            int controlY2 = y1 - (random.nextInt((int) ((height / 2)-50)))+50;

            // check controls do not go over border if final point is within border
                if(controlX1>width-30){
                    controlX1=(int)width-30;
                }
                if(controlX2>width-30){
                    controlX2=(int)width-30;
                }
                if(controlX1<30){
                    controlX1=30;
                }
                if(controlX2<30){
                    controlX2=30;
                }
                if(controlY1>height-30){
                    controlY1=(int)height-30;
                }
                if(controlY2>height-30){
                    controlY2=(int)height-30;
                }
                if(controlY1<30){
                    controlY1=30;
                }
                if(controlY2<30){
                    controlY2=30;
                }
            controls.add(new int[]{x1, y1, controlX1, controlY1, controlX2, controlY2, x3, y3});
        }
    }


}
