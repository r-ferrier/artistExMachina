package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.workinprogress.dataSetsAndComponents.DataSet;

import java.util.ArrayList;

public class AutomaticDrawing extends Painting {

    private Canvas canvas;
    private int steps;
    private int[][] anglesAndDirections;
    private Paint paint1;

    public AutomaticDrawing(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);

        System.out.println(positions.get(0).getResults());
        System.out.println(locations.get(0).getResults());

        System.out.println(lightDistanceAndSteps.get(0).getResults());
        System.out.println(lightDistanceAndSteps.get(1).getResults());
        System.out.println(lightDistanceAndSteps.get(2).getResults());

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

        drawStepsLine();


    }

    private void drawStepsLine(){

        steps = lightDistanceAndSteps.get(0).getScaledResults1().get(0);

        anglesAndDirections = new int[positions.get(0).getScaledResults1().size()][2];

        for(int i = 0; i< anglesAndDirections.length;i++){

            // multiply position by 0.1 to get the length of the line and %360 to get the angle
            anglesAndDirections[i][0] = (int)((positions.get(0).getScaledResults1().get(i))*0.5);

            // normalise line lengths by removing 50 from values larger than 50 and converting subsequently negative
            //values back to positives

            anglesAndDirections[i][0]-=50;
            if(anglesAndDirections[i][0]<0){
                anglesAndDirections[i][0]*=-1;
            }

            anglesAndDirections[i][1] = (positions.get(0).getScaledResults2().get(i))%360;

            System.out.println("increment" + anglesAndDirections[i][0]);
            System.out.println("angle"+ anglesAndDirections[i][1]);
        }

        int startX = (int)(Math.round(width/1.61803398875));
        int startY = startX;






        for(int i = 0; i<anglesAndDirections.length; i++){

            int length = anglesAndDirections[i][0];
            int angle = anglesAndDirections[i][1];
            int originalAngle = angle;

            int xMultiplier;
            int yMultiplier;

            if(angle>270){
                xMultiplier=-1;
                yMultiplier=1;
                angle-=270;
            }else if(angle>180){
                xMultiplier=-1;
                yMultiplier=1;
                angle-=180;
            }else if(angle>90){
                xMultiplier=1;
                yMultiplier=-1;
                angle-=90;
            }else{
                xMultiplier=1;
                yMultiplier=1;
            }

            double angleB = 180 - (angle+90);

            double lengthOfstopY =(length * Math.sin(Math.toRadians(angle)))/Math.sin(Math.toRadians(90));
            double lengthOfstopX =(length*Math.sin(Math.toRadians(angleB)))/Math.sin(Math.toRadians(90));

            double stopX = lengthOfstopX;
            double stopY = lengthOfstopY;

            stopY*=yMultiplier;
            stopX*=xMultiplier;

            stopX = Math.round((float)stopX)+startX;
            stopY = Math.round((float)stopY)+startY;


            if(stopX>width){
                stopX=stopX-startX-startX-lengthOfstopX;
                if(stopX<30){
                    stopX = 30;
                }
            }else if(stopX<30){
                stopX= stopX+startX+startX+lengthOfstopX;
                if(stopX>width){
                    stopX = width - 30;
                }
            }

            if(stopY>height){
                System.out.println(stopY+" too high");
                stopY= stopY-startY-startY-lengthOfstopY;
                if(stopY<0){
                    stopY = 0;
                }
                System.out.println(stopY+" fixed");
            }else if(stopY<0){
                stopY= stopY+ startY+startY+lengthOfstopY;
                if(stopY>height){
                    stopY = height;
                }
            }


            canvas.drawLine(startX,startY,(int)stopX,(int)stopY,paint1);

            System.out.println("length = "+length+", angle = "+originalAngle+", height = "+height+" width = "+width);
            System.out.println("start x = "+startX+", start y = "+startY+", stop x = "+stopX+", stop y = "+stopY);

            startX = (int)stopX;
            startY = (int)stopY;

            if(paint1.getStrokeWidth()<20) {
                paint1.setStrokeWidth(paint1.getStrokeWidth() + 1);

            }else{
                paint1.setStrokeWidth(1);
            }









        }


    }
}
