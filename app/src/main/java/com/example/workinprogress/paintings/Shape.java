package com.example.workinprogress.paintings;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public abstract class Shape extends Drawable {

    protected int x1End;
    protected int y1End;
    protected int x2End;
    protected int y2End;

    protected int x1Start;
    protected int y1Start;
    protected int x2Start;
    protected int y2Start;

    protected int startingDegree;
    protected int width;

    protected Paint paint1;

    public Shape(int[] aRGBColor){
        paint1 = new Paint();
        paint1.setARGB(aRGBColor[0],aRGBColor[1],aRGBColor[2],aRGBColor[3]);
    }

    public void setStartingDegree() {

        if (y1Start > y2Start) {
            startingDegree = 90;
            width = y1Start - y2Start;
            return;
        } else if (y2Start > y1Start) {
            startingDegree = 270;
            width = y2Start - y1Start;
            return;
        }
        if (x1Start > x2Start) {
            startingDegree = 0;
            width = x1Start - x2Start;
            return;
        } else if (x1Start < x2Start) {
            startingDegree = 180;
            width = x2Start - x1Start;
            return;
        }
    }

    public int getX1End() {
        return x1End;
    }

    public int getY1End() {
        return y1End;
    }

    public int getX2End() {
        return x2End;
    }

    public int getY2End() {
        return y2End;
    }


}
