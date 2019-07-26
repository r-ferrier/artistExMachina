package com.example.workinprogress.paintings.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class SquareShape extends CurvedShape {
    public SquareShape(int x1Start, int y1Start, int x2Start, int y2Start, boolean clockwiseOrientation, int[] aRGBColor, int size) {
        super(x1Start, y1Start, x2Start, y2Start, clockwiseOrientation, aRGBColor, size);
    }


    @Override
    public void draw(Canvas canvas) {

        Path path = new Path();
        paint1.setStyle(Paint.Style.FILL);
//        paint1.setStrokeWidth(5);
//
//
        RectF outerLine = createRectangle(size, x1Start, y1Start);
//        RectF innerLine = createRectangle(size - width, x2Start, y2Start);
//
        setEnds(outerLine);
//
//
//        path.moveTo(x1End,y1Start);
//
        canvas.drawRect(y2Start,x2End,y1Start,x1End,paint1);

//        path.addArc(outerLine, startingDegree, sweepingAngle);
//        if(clockwiseOrientation) {
//            path.lineTo(x2End, y2End);
//        }else{
//            path.lineTo(x1End, y1End);
//        }
//        path.addArc(innerLine, startingDegree + sweepingAngle, sweepingAngle * -1);
//        path.lineTo(x1Start, y1Start);
//
//        canvas.drawPath(path, paint1);



//        System.out.println("ending points: x1 = " + x1End + ", x2 = " + x2End + ", y1 = " + y1End + ", y2 = " + y2End);
    }


}
