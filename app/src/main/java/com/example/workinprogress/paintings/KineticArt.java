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
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class KineticArt extends Painting {

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


    private int averageLightValue;
    private int averageSize;
    private int canvasColor;

    private int[] positionsForPlotting = new int[10];


    public KineticArt(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);
        random = new Random();


    }


    private void setData() {

        ArrayList<Integer> lightValues = ((SensorSingularPointDataSet) lightDistanceAndSteps.get(2)).getScaledResults2();
        sizes = positions.get(0).getScaledResults1();
        sizes.addAll(positions.get(0).getScaledResults2());
        sizes.addAll(positions.get(0).getScaledResults3());

        for (int i = 0; i < sizes.size(); i++) {
            sizes.set(i, sizes.get(i) - 500);
            if (sizes.get(i) < 0) {
                sizes.set(i, sizes.get(i) * -1);
            }
        }

        averageLightValue = getAverage(lightValues);
        averageSize = getAverage(sizes);


        System.out.println("average light: " + averageLightValue);
        System.out.println("average size: " + averageSize);

        if (sizes.size() <= 50) {
            numberOfLoops = 12;
        } else if (sizes.size() <= 100) {
            numberOfLoops = 24;
        } else if (sizes.size() <= 200) {
            numberOfLoops = 32;
        } else {
            numberOfLoops = 64;
        }

        setHighestAndLowestValueArrays();

    }

    private ArrayList<Integer> setUniqueValueArrays(ArrayList<Integer> sortedArray) {

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

    private void setHighestAndLowestValueArrays() {

        highestPositionValues = (ArrayList<Integer>) sizes.clone();

        Collections.sort(highestPositionValues, Collections.reverseOrder());
        setUniqueValueArrays(highestPositionValues);

        positionsForPlotting[0] = (int)(width/2)-(highestPositionValues.get(0)*2);
        positionsForPlotting[1] = (int)(height/2)-(highestPositionValues.get(1)*2);
        positionsForPlotting[2] = (int)(width/2)+(highestPositionValues.get(2)*2);
        positionsForPlotting[3] = (int)(height/2)-(highestPositionValues.get(3)*2);
        positionsForPlotting[4] = (int)(width/2)+(highestPositionValues.get(4)*2);
        positionsForPlotting[5] = (int)(height/2)+(highestPositionValues.get(5)*2);
        positionsForPlotting[6] = (int)(width/2)-(highestPositionValues.get(6)*2);
        positionsForPlotting[7] = (int)(height/2)+(highestPositionValues.get(7)*2);
        positionsForPlotting[8] = (int)(width/2)-(highestPositionValues.get(0)*2);
        positionsForPlotting[9] = (int)(height/2)-(highestPositionValues.get(1)*2);

        System.out.println("xy values: "+ Arrays.toString(positionsForPlotting));
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

        if (averageLightValue < 2500) {
            if (averageLightValue < 10) {
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
                numberOfOptions = 7;
                increment = 20;
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

        setData();


        if (paths == null) {

            controls = new ArrayList<>();
            paths = new ArrayList<>();
            paints = new ArrayList<>();
            setPaints();

            setUpForOriginalIdea();
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



        for (int[] control : controls) {
            paths.add(new Path());
            paints.addAll(paintsOptions);
        }

        for (Paint paint : paints) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
        }

//        Collections.shuffle(paints);

        int newAverage = (int)((double)(highestPositionValues.get(0)+highestPositionValues.get(1)+highestPositionValues.get(2)+highestPositionValues.get(3))/4);
        int combiAverage = (newAverage+averageSize)/2;
        System.out.println("new average: "+newAverage+" old average: "+averageSize+" comnbi average: "+(newAverage+averageSize)/2);

        for (int i = 0; i < controls.size(); i++) {

            int numberOfLines = random.nextInt(combiAverage/5);
            if(numberOfLines>40){
                numberOfLines = 40;
            }

            paths.add(drawRibbon(controls.get(i).clone(), increment, numberOfLines, new Path()));
            paths.set(i, drawRibbon(controls.get(i).clone(), increment * -1, numberOfLines, paths.get(i)));
        }

        System.out.println("number of controls: " + controls.size());

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

    private void setUpForOriginalIdea() {

        createPaths();
        createPaths();
        createPaths();
        createPaths();
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


            controls.add(new int[]{x1, y1, controlX1, controlY1, controlX2, controlY2, x3, y3});

            System.out.println("coords: "+x1+" "+y1+" "+x3+" "+y3);

        }
    }


}
