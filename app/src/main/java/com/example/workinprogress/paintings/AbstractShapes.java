package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.workinprogress.dataSetsAndComponents.DataSet;

import java.util.ArrayList;
import java.util.Random;

public class AbstractShapes extends Painting {


    private Canvas canvas;
    private ArrayList<Shape> shapes;
    private Paint paint1;

    public AbstractShapes(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);
        shapes = new ArrayList<>();
    }

    public void draw(Canvas canvas) {

        this.canvas = canvas;
        canvas.drawColor(Color.BLACK);
        width = getBounds().width();
        height = getBounds().height();

        paint1 = new Paint();
        paint1.setColor(Color.BLACK);
        paint1.setStrokeCap(Paint.Cap.ROUND);
        paint1.setStrokeJoin(Paint.Join.ROUND);
//
//        CurvedShape curvedShape = new CurvedShape(500,700,500,750,true);
//        curvedShape.draw(canvas);

        getDataForDrawingShapes();


        if(shapes.size()<1){
            drawALoadOfShapes((int)(width/2),(int)(height/2),(int)(width/2)+10,(int)(height/2));
            drawALoadOfShapes((int)(width/2)+10,(int)(height/2),(int)(width/2),(int)(height/2));
            drawALoadOfShapes((int)(width/2),(int)(height/2),(int)(width/2)+10,(int)(height/2));
            drawALoadOfShapes((int)(width/2)+10,(int)(height/2),(int)(width/2),(int)(height/2));
        }else {

            for (Shape shape : shapes) {
                shape.draw(canvas);
            }
        }

//        CurvedShape curvedShape2 = new CurvedShape(curvedShape.getX1End(),curvedShape.getY1End(),curvedShape.getX2End(),curvedShape.getY2End(),1);
//        curvedShape2.draw(canvas);
//
////        CurvedShape curvedShape3 = new CurvedShape(curvedShape2.getX1End(),curvedShape2.getY1End(),curvedShape2.getX2End(),curvedShape2.getY2End(),false);
////        curvedShape3.draw(canvas);
//
//        CurvedShape curvedShape4 = new CurvedShape(curvedShape2.getX1End(),curvedShape2.getY1End(),curvedShape2.getX2End(),curvedShape2.getY2End(),1);
//        curvedShape4.draw(canvas);


//        canvas.drawCircle(200,200,200,paint1);

    }

    private void getDataForDrawingShapes(){

        ArrayList <Integer> lightValues = lightDistanceAndSteps.get(2).getScaledResults1();
        ArrayList<Integer> xPositions = positions.get(0).getScaledResults3();
        ArrayList<Integer> yPositions = positions.get(0).getScaledResults2();

    }

    private void drawALoadOfShapes(int startX1, int startY1, int startX2, int startY2){

        int x1Start = startX1;
        int x2Start = startX2;
        int y1Start = startY1;
        int y2Start = startY2;


        Random random = new Random();

        for(int i = 0;i<100;i++){

            Shape shapeInLoop;

            int lineLength = random.nextInt(50);

            if(random.nextBoolean()){
                shapeInLoop = new LineShape(x1Start,y1Start,x2Start,y2Start,lineLength);
            }else{
                shapeInLoop = new CurvedShape(x1Start,y1Start,x2Start,y2Start,random.nextBoolean());
            }

            shapeInLoop.draw(canvas);

            x1Start = shapeInLoop.getX1End();
            y1Start = shapeInLoop.getY1End();
            x2Start = shapeInLoop.getX2End();
            y2Start = shapeInLoop.getY2End();

            System.out.println("starting points for shapeinloop: x1 = " + x1Start + ", x2 = " + x2Start + ", y1 = " + y1Start + ", y2 = " + y2Start);

            shapes.add(shapeInLoop);
        }
    }





}
