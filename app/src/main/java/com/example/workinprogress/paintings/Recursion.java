package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.example.workinprogress.R;
import com.example.workinprogress.dataSetsAndComponents.DataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Recursion extends Painting {

    private Paint paint1;
    private Canvas canvas;
    private Path path;
    private ArrayList<Path> paths;
    private ArrayList<Paint> paints;

    public Recursion(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);
    }


    @Override
    public void draw(Canvas canvas) {
        this.canvas = canvas;
        canvas.drawColor(Color.WHITE);
        width = getBounds().width();
        height = getBounds().height();

//        paint1 = new Paint();

//        paint1.setColor(Color.BLACK);
//        paint1.setStrokeCap(Paint.Cap.ROUND);
//        paint1.setStrokeJoin(Paint.Join.ROUND);
//        paint1.setStrokeWidth(2);
//        paint1.setStyle(Paint.Style.STROKE);

        if (paths == null) {

//            path = new Path();


//        int x1 = 500;
//        int y1  =0;

//        int y3 = 300;

            int[] controls2 = new int[]{600, 350, 750, 420, 510, 375};

            ArrayList<int[]> controls = new ArrayList<>();
            paths = new ArrayList<>();
            paints = new ArrayList<>();


//        drawRibbon(controls2.clone(),x1,y1,path,10);
//        drawRibbon(controls2,x1,y1,path,-10);

            Random random = new Random();


            controls.add(new int[]{(int) (width / 2), 50, (int) (width / 2) - (random.nextInt(800)), random.nextInt((int) (height / 3)), (int) (width / 2) + (random.nextInt(800)), random.nextInt((int) (height / 3)), (int) (width / 2), (int) (height / 3)});
            paths.add(new Path());
            Paint paint4 = new Paint();
            paint4.setColor(context.getResources().getColor(R.color.yellowOchre));
            paints.add(paint4);
            newControls(controls,paint4);

            controls.add(new int[]{(int) (width / 2), 50, (int) (width / 2) - (random.nextInt(800)), random.nextInt((int) (height / 3)), (int) (width / 2) + (random.nextInt(800)), random.nextInt((int) (height / 3)), (int) (width / 2), (int) (height / 3)});
            paths.add(new Path());
            Paint paint2 = new Paint();
            paint2.setColor(context.getResources().getColor(R.color.coralRed));
            paints.add(paint2);
            newControls(controls,paint2);


            controls.add(new int[]{(int) (width / 2), 50, (int) (width / 2) - (random.nextInt(800)), random.nextInt((int) (height / 3)), (int) (width / 2) + (random.nextInt(800)), random.nextInt((int) (height / 3)), (int) (width / 2), (int) (height / 3)});
            paths.add(new Path());
            Paint paint3 = new Paint();
            paint3.setColor(context.getResources().getColor(R.color.metallicSeaweed));
            paints.add(paint3);
            newControls(controls,paint3);

            controls.add(new int[]{(int) (width / 2), 50, (int) (width / 2) - (random.nextInt(800)), random.nextInt((int) (height / 3)), (int) (width / 2) + (random.nextInt(800)), random.nextInt((int) (height / 3)), (int) (width / 2), (int) (height / 3)});
            paths.add(new Path());
            Paint paint5 = new Paint();
            paint5.setColor(context.getResources().getColor(R.color.imperialBlue));
            paints.add(paint5);
            newControls(controls,paint5);

            int widthLimit = 3200;
            int heightStart = 50-500;
            int heightEnd = (int)(height-50)+500;
            int centre = (int) (width/2);


//            controls.add(new int[]{centre, heightStart, centre - (random.nextInt(widthLimit)), random.nextInt(heightEnd), centre + (random.nextInt(widthLimit)), random.nextInt(heightEnd), centre,heightEnd, Color.GREEN});
//
//
//            controls.add(new int[]{centre, heightStart, centre - (random.nextInt(widthLimit)), random.nextInt(heightEnd), centre + (random.nextInt(widthLimit)), random.nextInt(heightEnd),centre, heightEnd, Color.BLUE});
//
//
//            controls.add(new int[]{centre, heightStart, centre - (random.nextInt(widthLimit)), random.nextInt(heightEnd),centre + (random.nextInt(widthLimit)), random.nextInt(heightEnd), centre, heightEnd, Color.RED});


            int increment = 10;



            for (int i =0; i<controls.size(); i++) {


                int numberOfLines = random.nextInt(20) + 10;

                paths.set(i,drawRibbon(controls.get(i).clone(), increment, numberOfLines, paths.get(i)));
                paths.set(i,drawRibbon(controls.get(i).clone(), increment*-1, numberOfLines, paths.get(i)));

            }


//        controls.add(setControls(new int[]{600,0,350,0,x1,y3},x1,y1));
//        controls.add(setControls(new int[]{600,0,350,0,x1,y3},200,400));
//
//


        }

        for(Paint paint: paints){
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(2);
            paint.setStyle(Paint.Style.STROKE);
        }

        Collections.shuffle(paints);

        for(int i = 0; i<paths.size(); i++){
            canvas.drawPath(paths.get(i),paints.get(i));
        }





    }


    private Path drawRibbon(int[] controls, int increment, int numberOfLines, Path path) {

        for (int i = 0; i < numberOfLines; i++) {

            path.moveTo(controls[0], controls[1]);
            path.cubicTo(controls[2], controls[3], controls[4], controls[5], controls[6], controls[7]);

//            controls[0]+= increment;
            controls[2] += increment;
            controls[4] += increment;
//            controls[6]+= increment;

//            paint1.setAlpha(20);

        }

        return path;
    }

    private ArrayList<int[]> newControls(ArrayList<int[]> controls, Paint paint) {

        Random random = new Random();

        for (int i = 1; i < 3; i++) {

            int x1 = controls.get(i - 1)[6];
            int y1 = controls.get(i - 1)[7];

            int y3 = y1 + (int) ((height - 50) / 3);

            int potentialLength = y3 - y1;

            int controlX1 = x1 + (random.nextInt((int) (width / 2)));
            int controlY1 = random.nextInt(potentialLength) + y1;
            int controlX2 = x1 - (random.nextInt((int) (width / 2)));
            int controlY2 = random.nextInt(potentialLength) + y1;


            controls.add(new int[]{x1, y1, controlX1, controlY1, controlX2, controlY2, x1, y3});
            paints.add(paint);
            paths.add(new Path());

        }

        return controls;
    }


}
