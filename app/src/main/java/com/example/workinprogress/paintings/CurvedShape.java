package com.example.workinprogress.paintings;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
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
    private int size = 200;
    private int startingDegree;

    public CurvedShape(int x1Start, int y1Start, int x2Start, int y2Start) {

        this.x1Start = x1Start;
        this.x2Start = x2Start;
        this.y1Start = y1Start;
        this.y2Start = y2Start;

        // find out which orientation the coordinates are in
        setStartingDegree();
    }

    @Override
    public void draw(Canvas canvas) {

        Paint paint1 = new Paint();
        paint1.setColor(Color.MAGENTA);
        Path path = new Path();
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(5);


        path.moveTo(x1Start, y1Start);

        RectF outerLine = createRectangle(size,x1Start,y1Start);
        RectF innerLine = createRectangle(size-arcWidth,x2Start,y2Start);

        setEnds(outerLine);

        path.addArc(outerLine,startingDegree,90);
        path.lineTo(x2End,y2End);
        path.addArc(innerLine,startingDegree+90,-90);
        path.lineTo(x1Start,y1Start);

        canvas.drawPath(path,paint1);
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


    private void setEnds(RectF rectF){
        switch (startingDegree){
            case 0:
                x1End = (int)rectF.left+size;
                y1End = (int)rectF.bottom;
                x2End = x1End;
                y2End = y1End - arcWidth;
            case 90:
                x1End = (int)rectF.left;
                y1End = (int)rectF.top+size;
                x2End = x1End+arcWidth;
                y2End = y1End;
            case 180:
                x1End = (int)rectF.right-size;
                y1End = (int)rectF.top;
                x2End = x1End;
                y2End = y1End+arcWidth;
            case 270:
                x1End = (int)rectF.right;
                y1End = (int)rectF.bottom-size;
                x2End = x1End - arcWidth;
                y2End = y1End;
        }
    }

    private void setStartingDegree(){
        if (y1Start > y2Start) {
            startingDegree = 90;
            arcWidth = y1Start-y2Start;
        } else if (y2Start > y1Start) {
            startingDegree = 270;
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


    private RectF createRectangle(int size, int x1Start, int y1Start){

        RectF rectf = new RectF();

        switch (startingDegree){
            case 0:
                rectf.left = x1Start - size - size;
                rectf.top = y1Start - size;
                rectf.right = x1Start;
                rectf.bottom = y1Start + size;
                break;
            case 90:
                rectf.left = x1Start - size;
                rectf.top = y1Start - size - size;
                rectf.right = x1Start+size;
                rectf.bottom = y1Start;
                break;
            case 180:
                rectf.left = x1Start;
                rectf.top = y1Start - size;
                rectf.right = x1Start + size + size;
                rectf.bottom = y1Start + size;
                break;
            case 270:
                rectf.left = x1Start - size;
                rectf.top = y1Start;
                rectf.right = x1Start+size;
                rectf.bottom = y1Start+size + size;
                break;
        }
//        System.out.println("Rectangle: "+rectCoordinatesLeft+", "+rectCoordinatesTop+", "+rectCoordinatesRight+", "+rectCoordinatesBottom);
        return rectf;

    }

}
