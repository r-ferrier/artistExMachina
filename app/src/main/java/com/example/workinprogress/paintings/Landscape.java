package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.example.workinprogress.R;
import com.example.workinprogress.dataSetsAndComponents.DataSet;
import com.example.workinprogress.dataSetsAndComponents.SingularPointDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Landscape extends PositionAndLightPainting {

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
    private String TAG = "Landscape class info";


    private int averageLightValue;
    private int averageSize;
    private int canvasColor;


    public Landscape(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);

        random = new Random();
        manipulateData();
        setPaints();
    }

    /**
     * manipulates and reformats the existing datasets for this image
     */
    private void manipulateData() {
        sizes = (ArrayList<Integer>)positionValues1.clone();
        sizes.addAll((ArrayList<Integer>)positionValues2.clone());
        sizes.addAll((ArrayList<Integer>)positionValues3.clone());

        for(int i = 0; i<sizes.size();i++){
            sizes.set(i,sizes.get(i)-500);
            if(sizes.get(i)<0){
                sizes.set(i,sizes.get(i)*-1);
            }
        }

        averageLightValue = getAverage(lightValues);
        averageSize = getAverage(sizes);

        if(sizes.size()<=20) {
            numberOfLoops = 3;
        }else if(sizes.size()<=50){
            numberOfLoops = 5;
        }else if(sizes.size()<=100){
            numberOfLoops = 24;
        }else if(sizes.size()<=200){
            numberOfLoops = 32;
        }else{
            numberOfLoops = 64;
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

        for(int i = 0; i<numberOfPaintOptions; i++){
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

        for (Paint paint: paintsOptions) {
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(100);
        }

        choosePaints();
    }

    // method to decide which and how many colours to use from the available options
    private void choosePaints() {

        Paint opaquePaint = new Paint();
        int numberOfOptions;
        if (averageLightValue < 2500) {

            canvasColor = Color.BLACK;
            opaquePaint.setColor(canvasColor);

            if (averageLightValue < 1000) {
                numberOfOptions = 2;
            } else {
                numberOfOptions = 4;
            }
        } else {
            canvasColor = Color.WHITE;
            opaquePaint.setColor(canvasColor);

            if (averageLightValue < 10000) {
              numberOfOptions = 6;
            } else {
               numberOfOptions = 8;
            }
        }
        removePaintsFromOptions(opaquePaint,numberOfOptions);
    }

    // helper method to remove paints from choices and set one colour opaque
    private void removePaintsFromOptions(Paint opaquePaint, int numberOfOptions){
        for(int i = numberOfPaintOptions; i>numberOfOptions; i--){
            paintsOptions.remove(random.nextInt(i));
        }
        paintsOptions.add(opaquePaint);
    }

    @Override
    public void draw(Canvas canvas) {
        this.canvas = canvas;
        this.canvas.drawColor(canvasColor);
        width = getBounds().width();
        height = getBounds().height();

        if (paths == null) {

            controls = new ArrayList<>();
            paths = new ArrayList<>();
            paints = new ArrayList<>();

            setUpForLandscape();
            setUpDrawing();
        }

        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), paints.get(i));
        }
    }

    private void setUpDrawing() {

        for(int[] control: controls){
            paths.add(new Path());
            paints.addAll(paintsOptions);
        }
        Collections.shuffle(paints);

        for (int i = 0; i < controls.size(); i++) {
            paths.set(i, drawRibbon(controls.get(i).clone(), paths.get(i)));
        }
    }

    private Path drawRibbon(int[] controls, Path path) {
        path.moveTo(controls[0], controls[1]);
        path.cubicTo(controls[2], controls[3], controls[4], controls[5], controls[6], controls[7]);
        return path;
    }

    private ArrayList<int[]> fillOutNewMountainRange(ArrayList<int[]> controls, int heightLimit) {

        Random random = new Random();

        for (int i = controls.size(); i < controls.size() + 1; i++) {

            if (controls.get(i - 1)[6] > width) {
                break;
            }

            int x1 = controls.get(i - 1)[6];
            int y1 = controls.get(i - 1)[7];
            int x3;

            do {
                x3 = getEnd(x1, heightLimit);
            } while (x3 <= x1);

            int potentialLength = x3 - x1;

            int controlY1 = y1 - (getHeightLimitedSizeForMountain(heightLimit));
            int controlX1 = random.nextInt(potentialLength) + x1;
            int controlY2 = y1 - (getHeightLimitedSizeForMountain(heightLimit));
            int controlX2 = random.nextInt(potentialLength) + x1;


            controls.add(new int[]{x1, y1, controlX1, controlY1, controlX2, controlY2, x3, y1});
        }

        return controls;
    }

    private int getEnd(int start, int heightLimit) {
        int end = random.nextInt(heightLimit) + start;
        if (end < 1) {
            end = 1;
        }
        return end;
    }

    private int getHeightLimitedSizeForMountain(int heightLimit){

        int sizeToUse = (int)((sizes.get(sizeCounter++))*((float)heightLimit/sizeRange));
        sizeToUse *= 2;

        if(sizeCounter>=sizes.size()){
            sizeCounter = 0;
        }
        return sizeToUse;
    }

    private void addMountainRange(int start, int centre, int end, int heightLimit) {
        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - getHeightLimitedSizeForMountain(heightLimit), start + random.nextInt(end),
                centre - getHeightLimitedSizeForMountain(heightLimit), end, centre});
        fillOutNewMountainRange(controls, heightLimit);
    }

    private void setUpForLandscape() {
        int start;
        int end;
        int centre;
        int heightLimit = (int) (height / numberOfLoops)*6;

        for (int i = 0; i < numberOfLoops; i++) {
                centre = (int) ((height / (numberOfLoops) * (i+1)));
                start = 0 - (random.nextInt(100));
                end = getEnd(start, heightLimit);

                for(int j = 0; j<4;j++) {
                    addMountainRange(start, centre, end, heightLimit);
                }
        }
    }
}
