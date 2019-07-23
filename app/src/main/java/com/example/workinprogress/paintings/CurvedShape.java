package com.example.workinprogress.paintings;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class CurvedShape extends Drawable {

    private int x1End;
    private int y1End;
    private int x2End;
    private int y2End;

    private int x1Start;
    private int y1Start;
    private int x2Start;
    private int y2Start;

    private int arcWidth;
    private int size = 400;
    private int startingDegree;

    public CurvedShape(int x1Start, int y1Start, int x2Start, int y2Start) {

        this.x1Start = x1Start;
        this.x2Start = x2Start;
        this.y1Start = y1Start;
        this.y2Start = y2Start;


        // find out which orientation the coordinates are in
        if (y1Start > y2Start) {
            startingDegree = 270;
            arcWidth = y1Start-y2Start;
        } else if (y2Start > y1Start) {
            startingDegree = 90;
            arcWidth = y2Start-y1Start;
        }

        if (x1Start > x2Start) {
            startingDegree = 0;
            arcWidth = x1Start-x2Start;
        } else if (x1Start < x2Start) {
            startingDegree = 180;
            arcWidth = x2Start-x1Start;
        }

    }


    @Override
    public void draw(Canvas canvas) {

        Paint paint1 = new Paint();
        paint1.setColor(Color.MAGENTA);
        Path path = new Path();
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(5);

        path.moveTo(x1Start, y1Start);

        int rectCoordinatesLeft;
        int rectCoordinatesTop;
        int rectCoordinatesRight;
        int rectCoordinatesBottom;

        int rect2CoordinatesLeft;
        int rect2CoordinatesTop;
        int rect2CoordinatesRight;
        int rect2CoordinatesBottom;

        if (startingDegree == 0) {
            rectCoordinatesLeft = x1Start - size;
            rect2CoordinatesLeft = rectCoordinatesLeft;
            rectCoordinatesTop = y1Start;
            rect2CoordinatesTop = rectCoordinatesTop;
            rectCoordinatesRight = x1Start;
            rect2CoordinatesRight = rectCoordinatesRight-arcWidth;
            rectCoordinatesBottom = y1Start + size;
            rect2CoordinatesBottom = rectCoordinatesBottom-arcWidth;
        } else if (startingDegree == 90) {
            rectCoordinatesLeft = x1Start - size;
            rect2CoordinatesLeft = rectCoordinatesLeft+arcWidth;
            rectCoordinatesTop = y1Start - size;
            rect2CoordinatesTop = rectCoordinatesTop-arcWidth;
            rectCoordinatesRight = x1Start+size;
            rect2CoordinatesRight = rectCoordinatesRight+arcWidth;
            rectCoordinatesBottom = y1Start+size;
            rect2CoordinatesBottom = rectCoordinatesBottom-arcWidth;
        } else if (startingDegree == 180) {
            rectCoordinatesLeft = x1Start;
            rect2CoordinatesLeft = rectCoordinatesLeft+arcWidth;
            rectCoordinatesTop = y1Start - size;
            rect2CoordinatesTop = rectCoordinatesTop+arcWidth;
            rectCoordinatesRight = x1Start + size;
            rect2CoordinatesRight = rectCoordinatesRight;
            rectCoordinatesBottom = y1Start;
            rect2CoordinatesBottom = rectCoordinatesBottom;
        } else {
            rectCoordinatesLeft = x1Start;
            rect2CoordinatesLeft = rectCoordinatesLeft;
            rectCoordinatesTop = y1Start;
            rect2CoordinatesTop = rectCoordinatesTop+arcWidth;
            rectCoordinatesRight = x1Start+size;
            rect2CoordinatesRight = rectCoordinatesRight-arcWidth;
            rectCoordinatesBottom = y1Start+size;
            rect2CoordinatesBottom = rectCoordinatesBottom;
        }

        System.out.println("Rectangle: "+rectCoordinatesLeft+", "+rectCoordinatesTop+", "+rectCoordinatesRight+", "+rectCoordinatesBottom);


        RectF rectF = new RectF(rectCoordinatesLeft,rectCoordinatesTop,rectCoordinatesRight,rectCoordinatesBottom);

        path.addArc(rectF,startingDegree,90);

        System.out.println("starting degree = "+startingDegree);

        if(startingDegree==0){
            path.lineTo(rectCoordinatesLeft,rectCoordinatesBottom-arcWidth);
        }else if(startingDegree==90){
            path.lineTo(rectCoordinatesLeft+arcWidth,rectCoordinatesTop+size);
        }else if(startingDegree==180){
            path.lineTo(rectCoordinatesRight,rectCoordinatesTop+arcWidth);
        }else{
            path.lineTo(rectCoordinatesRight-arcWidth,rectCoordinatesBottom);
        }

        RectF rect2 = new RectF(rect2CoordinatesLeft,rect2CoordinatesTop,rect2CoordinatesRight,rect2CoordinatesBottom);

        path.addArc(rect2,startingDegree,-90);

        canvas.drawPath(path,paint1);

//        canvas.drawCircle(500, 500, 200, paint1);
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
