package com.example.workinprogress.paintings.shapes;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;

public class CircleShape extends LineShape {

    private int centreX;
    private int centreY;


    /**
     * class to draw a shape that is a circle. Extends line shape as travelling in only 1 direction.
     *
     *    ,---..
     *  /       \
     * |         |
     *  \       /
     *   `-----'
     *
     * constructor finds the centre of the circle
     * @param x1Start starting coordinates x1
     * @param y1Start starting coordinates y1
     * @param x2Start starting coordinates x2
     * @param y2Start starting coordinates y2
     * @param size size/length of shape
     * @param aRGBColor colour of shape
     */
    public CircleShape(int x1Start, int y1Start, int x2Start, int y2Start, int size, int[] aRGBColor) {
        super(x1Start, y1Start, x2Start, y2Start, size, aRGBColor);
        findCentre();
    }

    /**
     * draws circle around its centre
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(centreX,centreY,(float)(size/3),paint1);
    }

    /**
     * helper method to find centre, using centre of starting positions
     */
    private void findCentre() {
        if (x1Start > x2Start) {
            centreX = ((x1Start - x2Start) / 2) + x2Start;
        } else if (x2Start > x1Start) {
            centreX = ((x2Start - x1Start) / 2) + x1Start;
        } else {
            if (x1End > x1Start) {
                centreX = ((x1End - x1Start) / 2) + x1Start;
            } else if (x1Start > x1End) {
                centreX = ((x1Start - x1End) / 2) + x1End;
            }
        }

        if (y1Start > y2Start) {
            centreY = ((y1Start - y2Start) / 2) + y2Start;
        } else if (y2Start > y1Start) {
            centreY = ((y2Start - y1Start) / 2) + y1Start;
        } else {
            if (y1End > y1Start) {
                centreY = ((y1End - y1Start) / 2) + y1Start;
            } else if (y1Start > y1End) {
                centreY = ((y1Start - y1End) / 2) + y1End;
            }
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
