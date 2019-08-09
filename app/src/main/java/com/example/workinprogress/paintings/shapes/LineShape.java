package com.example.workinprogress.paintings.shapes;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;

public class LineShape extends Shape {

    protected int size;

    public LineShape(int x1Start, int y1Start, int x2Start, int y2Start, int size, int[] aRGBColor){

        super(aRGBColor);
        this.x1Start = x1Start;
        this.y1Start = y1Start;
        this.x2Start = x2Start;
        this.y2Start = y2Start;

        this.size = size;

        setStartingDegree();
        setEnds();
    }


    @Override
    public void draw(Canvas canvas) {
        path.moveTo(x1Start, y1Start);
        path.lineTo(x2Start,y2Start);
        path.lineTo(x2End,y2End);
        path.lineTo(x1End,y1End);
        path.lineTo(x1Start,y1Start);
        canvas.drawPath(path, paint1);
    }

    private void setEnds(){
        switch (startingDegree){
            case 0:
                x1End = x1Start;
                y1End = y1Start+size;
                x2End = x2Start;
                y2End = y2Start+size;
                break;
            case 90:
                x1End = x1Start-size;
                y1End = y1Start;
                x2End = x2Start-size;
                y2End = y2Start;
                break;
            case 180:
                x1End = x1Start;
                y1End = y1Start-size;
                x2End = x2Start;
                y2End = y2Start-size;
                break;
            case 270:
                x1End = x1Start+size;
                y1End = y1Start;
                x2End = x2Start+size;
                y2End = y2Start;
                break;
        }
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
