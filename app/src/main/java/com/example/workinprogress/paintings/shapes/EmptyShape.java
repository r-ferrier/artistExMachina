package com.example.workinprogress.paintings.shapes;

import android.graphics.Canvas;

import java.util.Random;

public class EmptyShape extends LineShape {

    private int randomChoice;

    public EmptyShape(int x1Start, int y1Start, int x2Start, int y2Start, int size, int[] aRGBColor) {
        super(x1Start, y1Start, x2Start, y2Start, size, aRGBColor);

        randomChoice = new Random().nextInt(4);
    }

    public void draw(Canvas canvas) {


//        draw nothing




    }


    private void setEnds(){
        switch (randomChoice){
            case 0:
                x1End = x1Start;
                y1End = y1Start+size;
                x2End = x2Start;
                y2End = y2Start+size;
                break;
            case 1:
                x1End = x1Start-size;
                y1End = y1Start;
                x2End = x2Start-size;
                y2End = y2Start;
                break;
            case 2:
                x1End = x1Start;
                y1End = y1Start-size;
                x2End = x2Start;
                y2End = y2Start-size;
                break;
            case 3:
                x1End = x1Start+size;
                y1End = y1Start;
                x2End = x2Start+size;
                y2End = y2Start;
                break;
        }
    }

}
