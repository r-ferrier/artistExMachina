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
import java.util.Random;


public class Recursion extends Painting {

    private Paint paint1;
    private Canvas canvas;
    private Path path;
    private ArrayList<Path> paths;
    private ArrayList<Paint> paints;
    private ArrayList<int[]> controls;
    private Random random;
    private Paint[] paintsOptions;
    private int numberOfLoops;
    private Paint[] paintsChoice;

    private int averageLightValue;
    private int averageSize;
    private int canvasColor;


    public Recursion(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);

        random = new Random();
        setData();
        setPaints();



    }


    private void setData() {

        ArrayList<Integer> lightValues = ((SensorSingularPointDataSet) lightDistanceAndSteps.get(2)).getScaledResults2();
        ArrayList<Integer> sizes = positions.get(0).getScaledResults1();
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

        if(averageSize<=50){
            numberOfLoops = 12;
        }else if(averageSize<=60){
            numberOfLoops = 24;
        }else if(averageSize<=80){
            numberOfLoops = 32;
        }else{
            numberOfLoops = 64;
        }


    }

    private int getAverage(ArrayList<Integer> values) {
        int total = 0;

        for (int i = 0; i < values.size(); i++) {
            total += values.get(i);
        }

        return total / values.size();
    }

    private void setPaints() {

        paintsOptions = new Paint[]{new Paint(), new Paint(), new Paint(), new Paint(), new Paint(), new Paint(), new Paint(), new Paint(), new Paint()};

        paintsOptions[0].setColor(context.getResources().getColor(R.color.yellowOchre));
        paintsOptions[1].setColor(context.getResources().getColor(R.color.coralRed));
        paintsOptions[2].setColor(context.getResources().getColor(R.color.metallicSeaweed));
        paintsOptions[3].setColor(context.getResources().getColor(R.color.imperialBlue));
        paintsOptions[4].setColor(context.getResources().getColor(R.color.juneBudYellow));
        paintsOptions[5].setColor(context.getResources().getColor(R.color.iguanaGreen));
        paintsOptions[6].setColor(context.getResources().getColor(R.color.rosyBrown));
        paintsOptions[7].setColor(context.getResources().getColor(R.color.purpleNavy));
        paintsOptions[8].setColor(context.getResources().getColor(R.color.purplePineapple));


        for (int i = 0; i < paintsOptions.length; i++) {
            paintsOptions[i].setStyle(Paint.Style.FILL);
            paintsOptions[i].setAlpha(100);
        }

        choosePaints();

    }

    private void choosePaints() {

        Paint opaquePaint = new Paint();

        if (averageLightValue < 2500) {

            canvasColor = Color.BLACK;
            opaquePaint.setColor(canvasColor);

            if (averageLightValue < 500) {
                paintsChoice = new Paint[]{paintsOptions[random.nextInt(8)],
                        paintsOptions[random.nextInt(8)], paintsOptions[random.nextInt(8)],opaquePaint};
            } else {
                paintsChoice = new Paint[]{paintsOptions[random.nextInt(8)],
                        paintsOptions[random.nextInt(8)], paintsOptions[random.nextInt(8)],
                        paintsOptions[random.nextInt(8)],opaquePaint};
            }
        } else {

            canvasColor = Color.WHITE;
            opaquePaint.setColor(canvasColor);

            if (averageLightValue < 10000) {
                paintsChoice = new Paint[]{paintsOptions[random.nextInt(8)],
                        paintsOptions[random.nextInt(8)], paintsOptions[random.nextInt(8)],
                        paintsOptions[random.nextInt(8)], paintsOptions[random.nextInt(8)],opaquePaint};
            } else {
                paintsChoice = new Paint[]{paintsOptions[random.nextInt(8)],
                        paintsOptions[random.nextInt(8)], paintsOptions[random.nextInt(8)],
                        paintsOptions[random.nextInt(8)], paintsOptions[random.nextInt(8)],
                        paintsOptions[random.nextInt(8)],opaquePaint};
                canvasColor = Color.WHITE;
            }

        }

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
            paints.add(paintsChoice[random.nextInt(paintsChoice.length)]);
        }


        for (int i = 0; i < controls.size(); i++) {

            int numberOfLines = random.nextInt(20) + 10;
            paths.set(i, drawRibbon(controls.get(i).clone(), increment, numberOfLines, paths.get(i)));
//                paths.set(i,drawRibbon(controls.get(i).clone(), increment*-1, numberOfLines, paths.get(i)));
        }

        System.out.println("number of controls: "+controls.size());



//        Collections.shuffle(paints);
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

            int controlY1 = y1 - (random.nextInt(heightLimit));
            int controlX1 = random.nextInt(potentialLength) + x1;
            int controlY2 = y1 - (random.nextInt(heightLimit));
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

    private void addMountainRange(int start, int centre, int end, int heightLimit) {

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});

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
