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


public class Landscape extends Painting {

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


    private int averageLightValue;
    private int averageSize;
    private int canvasColor;


    public Landscape(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);

        random = new Random();
        setData();
        setPaints();

    }


    private void setData() {

        ArrayList<Integer> lightValues = ((SensorSingularPointDataSet) lightDistanceAndSteps.get(2)).getScaledResults2();
        sizes = positions.get(0).getScaledResults1();
        sizes.addAll(positions.get(0).getScaledResults2());
        sizes.addAll(positions.get(0).getScaledResults3());

        for(int i = 0; i<sizes.size();i++){
            sizes.set(i,sizes.get(i)-500);
            if(sizes.get(i)<0){
                sizes.set(i,sizes.get(i)*-1);
            }
        }

        averageLightValue = getAverage(lightValues);
        averageSize = getAverage(sizes);


        System.out.println("average light: " + averageLightValue);
        System.out.println("average size: " + averageSize);

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

//        int lightValueForARGB = 255-(int)((double)averageLightValue/(1000.0/255));

        Paint opaquePaint = new Paint();
        int numberOfOptions;
//        canvasColor = Color.argb(255,lightValueForARGB,lightValueForARGB,lightValueForARGB);
//        opaquePaint.setColor(canvasColor);

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

//        paintsOptions.get(random.nextInt(paintsOptions.size())).setAlpha(255);
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

//            setUpForOriginalIdea();
            setUpForLandscapeIdea();
//            setUpForOriginalIdea();
            setUpDrawing();
        }

        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), paints.get(i));
        }


    }

    private void setUpDrawing() {
        int increment = 10;

        for(int[] control: controls){
            paths.add(new Path());
            paints.addAll(paintsOptions);
        }

        Collections.shuffle(paints);

        for (int i = 0; i < controls.size(); i++) {

            int numberOfLines = random.nextInt(20) + 10;
            paths.set(i, drawRibbon(controls.get(i).clone(), increment, numberOfLines, paths.get(i)));
//                paths.set(i,drawRibbon(controls.get(i).clone(), increment*-1, numberOfLines, paths.get(i)));
        }

        System.out.println("number of controls: "+controls.size());

    }


    private Path drawRibbon(int[] controls, int increment, int numberOfLines, Path path) {

        path.moveTo(controls[0], controls[1]);
        path.cubicTo(controls[2], controls[3], controls[4], controls[5], controls[6], controls[7]);


//        for (int i = 0; i < numberOfLines; i++) {
//
//            path.moveTo(controls[0], controls[1]);
//            path.cubicTo(controls[2], controls[3], controls[4], controls[5], controls[6], controls[7]);
//
//            controls[2] += increment;
//            controls[4] += increment;
//        }

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

            int controlY1 = y1 - (getheightLimitedSizeForMountain(heightLimit));
            int controlX1 = random.nextInt(potentialLength) + x1;
            int controlY2 = y1 - (getheightLimitedSizeForMountain(heightLimit));
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

    private int getheightLimitedSizeForMountain(int heightLimit){

        int sizeToUse = (int)((sizes.get(sizeCounter++))*((float)heightLimit/sizeRange));
        sizeToUse *= 2;

        if(sizeCounter>=sizes.size()){
            sizeCounter = 0;
        }
        System.out.println("sizeToUse: "+sizeToUse);
        return sizeToUse;
    }

    private void addMountainRange(int start, int centre, int end, int heightLimit) {

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - getheightLimitedSizeForMountain(heightLimit), start + random.nextInt(end),
                centre - getheightLimitedSizeForMountain(heightLimit), end, centre});

        fillOutNewMountainRange(controls, heightLimit);
    }


    private void setUpForLandscapeIdea() {

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

//    private void setUpForOriginalIdea() {
//        int widthLimit = (int) (width / 2);
//        int heightStart = 50;
//        int heightEnd = (int) ((height) - 50);
//        int centre = (int) (width / 2);
//
//
//        controls.add(new int[]{centre, heightStart, centre + (random.nextInt(widthLimit)),
//                random.nextInt(heightEnd), centre + (random.nextInt(widthLimit)),
//                random.nextInt(heightEnd), centre, heightEnd});
//        paths.add(new Path());
//        Paint paint4 = new Paint();
//        paint4.setColor(context.getResources().getColor(R.color.yellowOchre));
//        paints.add(paint4);
////            newControls(controls,paint4);
//
//        controls.add(new int[]{centre, heightStart, centre + (random.nextInt(widthLimit)),
//                random.nextInt(heightEnd), centre + (random.nextInt(widthLimit)),
//                random.nextInt(heightEnd), centre, heightEnd});
//        paths.add(new Path());
//        Paint paint2 = new Paint();
//        paint2.setColor(context.getResources().getColor(R.color.coralRed));
//        paints.add(paint2);
////            newControls(controls,paint2);
//
//
//        controls.add(new int[]{centre, heightStart, centre + (random.nextInt(widthLimit)),
//                random.nextInt(heightEnd), centre + (random.nextInt(widthLimit)),
//                random.nextInt(heightEnd), centre, heightEnd});
//        paths.add(new Path());
//        Paint paint3 = new Paint();
//        paint3.setColor(context.getResources().getColor(R.color.coralRed));
//        paints.add(paint3);
////            newControls(controls,paint3);
//
//        controls.add(new int[]{centre, heightStart, centre + (random.nextInt(widthLimit)),
//                random.nextInt(heightEnd), centre + (random.nextInt(widthLimit)),
//                random.nextInt(heightEnd), centre, heightEnd});
//        paths.add(new Path());
//        Paint paint5 = new Paint();
//        paint5.setColor(context.getResources().getColor(R.color.imperialBlue));
//        paints.add(paint5);
////            newControls(controls,paint5);
//
//
////            controls.add(new int[]{centre, heightStart, centre - (random.nextInt(widthLimit)), random.nextInt(heightEnd), centre + (random.nextInt(widthLimit)), random.nextInt(heightEnd), centre,heightEnd, Color.GREEN});
////
////
////            controls.add(new int[]{centre, heightStart, centre - (random.nextInt(widthLimit)), random.nextInt(heightEnd), centre + (random.nextInt(widthLimit)), random.nextInt(heightEnd),centre, heightEnd, Color.BLUE});
////
////
////            controls.add(new int[]{centre, heightStart, centre - (random.nextInt(widthLimit)), random.nextInt(heightEnd),centre + (random.nextInt(widthLimit)), random.nextInt(heightEnd), centre, heightEnd, Color.RED});
//
//
//    }
//
//    private ArrayList<int[]> newControls(ArrayList<int[]> controls, Paint paint) {
//
//        Random random = new Random();
//
//        for (int i = 1; i < 3; i++) {
//
//            int x1 = controls.get(i - 1)[6];
//            int y1 = controls.get(i - 1)[7];
//
//            int y3 = y1 + (int) ((height - 50) / 3);
//
//            int potentialLength = y3 - y1;
//
//            int controlX1 = x1 + (random.nextInt((int) (width / 2)));
//            int controlY1 = random.nextInt(potentialLength) + y1;
//            int controlX2 = x1 - (random.nextInt((int) (width / 2)));
//            int controlY2 = random.nextInt(potentialLength) + y1;
//
//
//            controls.add(new int[]{x1, y1, controlX1, controlY1, controlX2, controlY2, x1, y3});
//            paints.add(paint);
//            paths.add(new Path());
//
//        }
//
//        return controls;
//    }


}
