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
    private ArrayList<int[]> controls;
    private Random random;

    public Recursion(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);
    }


    @Override
    public void draw(Canvas canvas) {
        this.canvas = canvas;
        canvas.drawColor(Color.WHITE);
        width = getBounds().width();
        height = getBounds().height();

        if (paths == null) {

            controls = new ArrayList<>();
            paths = new ArrayList<>();
            paints = new ArrayList<>();
            random = new Random();


//            setUpForOriginalIdea();

            setUpForLandscapeIdea();
            setUpDrawing();
        }


        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), paints.get(i));
        }


    }

    private void setUpDrawing() {
        int increment = 10;


        for (int i = 0; i < controls.size(); i++) {

            int numberOfLines = random.nextInt(20) + 10;
            paths.set(i, drawRibbon(controls.get(i).clone(), increment, numberOfLines, paths.get(i)));
//                paths.set(i,drawRibbon(controls.get(i).clone(), increment*-1, numberOfLines, paths.get(i)));
        }


        for (Paint paint : paints) {
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(2);
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(100);
        }

        Collections.shuffle(paints);
    }


    private Path drawRibbon(int[] controls, int increment, int numberOfLines, Path path) {

        path.moveTo(controls[0], controls[1]);
        path.cubicTo(controls[2], controls[3], controls[4], controls[5], controls[6], controls[7]);

//
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


    private ArrayList<int[]> newControlsLandscape(ArrayList<int[]> controls, Paint paint, int heightLimit) {

        Random random = new Random();

        for (int i = controls.size(); i < controls.size() + 1; i++) {

            if (controls.get(i - 1)[6] > width) {
                break;
            }

            int x1 = controls.get(i - 1)[6];
            int y1 = controls.get(i - 1)[7];
            int x3;

            do {
                x3 = getEnd(x1);
            } while (x3 <= x1);

            int potentialLength = x3 - x1;

            int controlY1 = y1 - (random.nextInt(heightLimit));
            int controlX1 = random.nextInt(potentialLength) + x1;
            int controlY2 = y1 - (random.nextInt(heightLimit));
            int controlX2 = random.nextInt(potentialLength) + x1;


            controls.add(new int[]{x1, y1, controlX1, controlY1, controlX2, controlY2, x3, y1});
            paints.add(paint);
            paths.add(new Path());

            Paint paintWhite = new Paint();
            paintWhite.setColor(Color.WHITE);

        }

        return controls;
    }

    private int getEnd(int start){
        int end = random.nextInt((int) (width / 3)) + start;
        if(end<0){
            end = 1;
        }
        return end;
    }


    private void setUpForLandscapeIdea() {

        int heightLimit = (int) (height / 3) - 50;
        int start = 0-(random.nextInt(100));
        int end = getEnd(start);
        int centre = (int) ((height / 32));

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        Paint paint4 = new Paint();
        paint4.setColor(context.getResources().getColor(R.color.yellowOchre));
        paints.add(paint4);
        newControlsLandscape(controls, paint4, heightLimit);

        centre = (int) ((height / 32)*2);
        start = 0-(random.nextInt(100));
        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        Paint paint2 = new Paint();
        paint2.setColor(context.getResources().getColor(R.color.coralRed));
        paints.add(paint2);
        newControlsLandscape(controls, paint2, heightLimit);

        centre = (int) ((height / 32)*3);
        start = 0-(random.nextInt(100));
        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        Paint paint3 = new Paint();
        paint3.setColor(context.getResources().getColor(R.color.metallicSeaweed));
        paints.add(paint3);
        newControlsLandscape(controls, paint3, heightLimit);

        centre = (int) ((height / 32)*4);
        start = 0-(random.nextInt(100));
        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        Paint paint5 = new Paint();
        paint5.setColor(context.getResources().getColor(R.color.imperialBlue));
        paints.add(paint5);
        newControlsLandscape(controls, paint5, heightLimit);

//*****************************************************************************************************//

        centre = (int) ((height / 32)*5);
        start = 0-(random.nextInt(100));
//        heightLimit = (int) (height / 4) - 50;
        end = getEnd(start);
        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint4);
        newControlsLandscape(controls, paint4, heightLimit);

        centre = (int) ((height / 32)*6);
        start = 0-(random.nextInt(100));
        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint2);
        newControlsLandscape(controls, paint2, heightLimit);

        centre = (int) ((height / 32)*7);
        start = 0-(random.nextInt(100));
        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint3);
        newControlsLandscape(controls, paint3, heightLimit);

        centre = (int) ((height / 32)*8);
        start = 0-(random.nextInt(100));
        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint5);
        newControlsLandscape(controls, paint5, heightLimit);

//*****************************************************************************************************//

        centre = (int) ((height / 32)*9);
        start = 0-(random.nextInt(100));
//        heightLimit = (int) (height / 4) - 50;
        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint4);
        newControlsLandscape(controls, paint4, heightLimit);

        centre = (int) ((height / 32)*10);
        start = 0-(random.nextInt(100));
        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint2);
        newControlsLandscape(controls, paint2, heightLimit);

        centre = (int) ((height / 32)*11);
        start = 0-(random.nextInt(100));
        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint3);
        newControlsLandscape(controls, paint3, heightLimit);

        centre = (int) ((height / 32)*12);
        start = 0-(random.nextInt(100));
        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint5);
        newControlsLandscape(controls, paint5, heightLimit);

//*****************************************************************************************************//

        centre = (int) ((height / 32)*13);
        start = 0-(random.nextInt(100));
//        heightLimit = (int) (height / 2) - 50;

        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint4);
        newControlsLandscape(controls, paint4, heightLimit);

        centre = (int) ((height / 32)*14);
        start = 0-(random.nextInt(100));
        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint2);
        newControlsLandscape(controls, paint2, heightLimit);

        centre = (int) ((height / 32)*15);
        start = 0-(random.nextInt(100));
        end = getEnd(start);


        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint3);
        newControlsLandscape(controls, paint3, heightLimit);

        centre = (int) ((height / 32)*16);
        start = 0-(random.nextInt(100));
        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint5);
        newControlsLandscape(controls, paint5, heightLimit);


        //*****************************************************************************************************//

        centre = (int) ((height / 32)*17);
        start = 0-(random.nextInt(100));
//        heightLimit = (int) (height / 2) - 50;

        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint4);
        newControlsLandscape(controls, paint4, heightLimit);

        centre = (int) ((height / 32)*18);
        start = 0-(random.nextInt(100));
        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint2);
        newControlsLandscape(controls, paint2, heightLimit);

        centre = (int) ((height / 32)*19);
        start = 0-(random.nextInt(100));
        end = getEnd(start);


        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint3);
        newControlsLandscape(controls, paint3, heightLimit);

        centre = (int) ((height / 32)*20);
        start = 0-(random.nextInt(100));
        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint5);
        newControlsLandscape(controls, paint5, heightLimit);

        //*****************************************************************************************************//

        centre = (int) ((height / 32)*21);
        start = 0-(random.nextInt(100));
//        heightLimit = (int) (height / 2) - 50;

        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint4);
        newControlsLandscape(controls, paint4, heightLimit);

        centre = (int) ((height / 32)*22);
        start = 0-(random.nextInt(100));
        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint2);
        newControlsLandscape(controls, paint2, heightLimit);

        centre = (int) ((height / 32)*23);
        start = 0-(random.nextInt(100));
        end = getEnd(start);


        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint3);
        newControlsLandscape(controls, paint3, heightLimit);

        centre = (int) ((height / 32)*24);
        start = 0-(random.nextInt(100));
        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint5);
        newControlsLandscape(controls, paint5, heightLimit);

        //*****************************************************************************************************//

        centre = (int) ((height / 32)*25);
        start = 0-(random.nextInt(100));
//        heightLimit = (int) (height / 2) - 50;

        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint4);
        newControlsLandscape(controls, paint4, heightLimit);

        centre = (int) ((height / 32)*26);
        start = 0-(random.nextInt(100));
        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint2);
        newControlsLandscape(controls, paint2, heightLimit);

        centre = (int) ((height / 32)*27);
        start = 0-(random.nextInt(100));
        end = getEnd(start);


        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint3);
        newControlsLandscape(controls, paint3, heightLimit);

        centre = (int) ((height / 32)*28);
        start = 0-(random.nextInt(100));
        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint5);
        newControlsLandscape(controls, paint5, heightLimit);

        //*****************************************************************************************************//

        centre = (int) ((height / 32)*29);
        start = 0-(random.nextInt(100));
//        heightLimit = (int) (height / 2) - 50;

        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint4);
        newControlsLandscape(controls, paint4, heightLimit);

        centre = (int) ((height / 32)*30);
        start = 0-(random.nextInt(100));
        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint2);
        newControlsLandscape(controls, paint2, heightLimit);

        centre = (int) ((height / 32)*31);
        start = 0-(random.nextInt(100));
        end = getEnd(start);


        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint3);
        newControlsLandscape(controls, paint3, heightLimit);

        centre = (int) ((height / 32)*32);
        start = 0-(random.nextInt(100));
        end = getEnd(start);

        controls.add(new int[]{start, centre, start + random.nextInt(end),
                centre - random.nextInt(heightLimit), start + random.nextInt(end),
                centre - random.nextInt(heightLimit), end, centre});
        paths.add(new Path());
        paints.add(paint5);
        newControlsLandscape(controls, paint5, heightLimit);


    }

    private void setUpForOriginalIdea() {
        int widthLimit = (int) (width / 2);
        int heightStart = 50;
        int heightEnd = (int) ((height) - 50);
        int centre = (int) (width / 2);


        controls.add(new int[]{centre, heightStart, centre + (random.nextInt(widthLimit)),
                random.nextInt(heightEnd), centre + (random.nextInt(widthLimit)),
                random.nextInt(heightEnd), centre, heightEnd});
        paths.add(new Path());
        Paint paint4 = new Paint();
        paint4.setColor(context.getResources().getColor(R.color.yellowOchre));
        paints.add(paint4);
//            newControls(controls,paint4);

        controls.add(new int[]{centre, heightStart, centre + (random.nextInt(widthLimit)),
                random.nextInt(heightEnd), centre + (random.nextInt(widthLimit)),
                random.nextInt(heightEnd), centre, heightEnd});
        paths.add(new Path());
        Paint paint2 = new Paint();
        paint2.setColor(context.getResources().getColor(R.color.coralRed));
        paints.add(paint2);
//            newControls(controls,paint2);


        controls.add(new int[]{centre, heightStart, centre + (random.nextInt(widthLimit)),
                random.nextInt(heightEnd), centre + (random.nextInt(widthLimit)),
                random.nextInt(heightEnd), centre, heightEnd});
        paths.add(new Path());
        Paint paint3 = new Paint();
        paint3.setColor(context.getResources().getColor(R.color.coralRed));
        paints.add(paint3);
//            newControls(controls,paint3);

        controls.add(new int[]{centre, heightStart, centre + (random.nextInt(widthLimit)),
                random.nextInt(heightEnd), centre + (random.nextInt(widthLimit)),
                random.nextInt(heightEnd), centre, heightEnd});
        paths.add(new Path());
        Paint paint5 = new Paint();
        paint5.setColor(context.getResources().getColor(R.color.imperialBlue));
        paints.add(paint5);
//            newControls(controls,paint5);


//            controls.add(new int[]{centre, heightStart, centre - (random.nextInt(widthLimit)), random.nextInt(heightEnd), centre + (random.nextInt(widthLimit)), random.nextInt(heightEnd), centre,heightEnd, Color.GREEN});
//
//
//            controls.add(new int[]{centre, heightStart, centre - (random.nextInt(widthLimit)), random.nextInt(heightEnd), centre + (random.nextInt(widthLimit)), random.nextInt(heightEnd),centre, heightEnd, Color.BLUE});
//
//
//            controls.add(new int[]{centre, heightStart, centre - (random.nextInt(widthLimit)), random.nextInt(heightEnd),centre + (random.nextInt(widthLimit)), random.nextInt(heightEnd), centre, heightEnd, Color.RED});


    }


}
