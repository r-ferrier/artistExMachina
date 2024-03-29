package com.example.workinprogress.paintings.shapes;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.RectF;

public class CurvedShape extends Shape {

    protected int size;
    protected int sweepingAngle;
    private RectF outerLine;
    private RectF innerLine;
    public Canvas canvas;

    protected boolean clockwiseOrientation;

    /**
     * class to draw a curved shape.
     *
     *  __         .---.      __    .---.
     * |  |       /  __|     |  |   |__  `\
     * \  `---.  |  |    .---'  |      |  |
     * `._____|  |__|    |_____,'      |__|
     *
     * constructor changes starts around depending on orientation and sets sweeping angle for curve.
     * @param x1Start starting coordinates x1
     * @param y1Start starting coordinates y1
     * @param x2Start starting coordinates x2
     * @param y2Start starting coordinates y2
     * @param clockwiseOrientation whether the shape is to turn left or right
     * @param aRGBColor colour of shape
     * @param size size/length of shape
     */
    public CurvedShape(int x1Start, int y1Start, int x2Start, int y2Start, boolean clockwiseOrientation, int[] aRGBColor, int size) {

        super(aRGBColor);
        this.size = size;

        this.clockwiseOrientation = clockwiseOrientation;

        if (clockwiseOrientation) {
            this.x1Start = x1Start;
            this.y1Start = y1Start;
            this.x2Start = x2Start;
            this.y2Start = y2Start;
            sweepingAngle = 90;
        } else {
            this.x2Start = x1Start;
            this.y2Start = y1Start;
            this.x1Start = x2Start;
            this.y1Start = y2Start;
            sweepingAngle = -90;
        }

        // find out which orientation the coordinates are in
        setStartingDegree();

        //create rectangles for the outer and inner curves
        outerLine = createRectangle(size, this.x1Start, this.y1Start);
        innerLine = createRectangle(size - width, this.x2Start, this.y2Start);

        //set ends
        setEnds(outerLine);
    }

    /**
     * draws the curved shape to canvas using information set by constructor
     * @param canvas passed by view
     */
    @Override
    public void draw(Canvas canvas) {

        this.canvas = canvas;

        path.moveTo(x1Start, y1Start);
        path.addArc(outerLine, startingDegree, sweepingAngle);

        if (clockwiseOrientation) {
            path.lineTo(x2End, y2End);
        } else {
            path.lineTo(x1End, y1End);
        }

        path.addArc(innerLine, startingDegree + sweepingAngle, sweepingAngle * -1);
        path.lineTo(x1Start, y1Start);

        canvas.drawPath(path, paint1);
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

    /**
     * helper method to setEnds of a shape
     * @param rectF rectangle created to form the outer curve
     * @return returns an array containing newly set ends
     */
    protected int[] setEnds(RectF rectF) {

        switch (startingDegree) {
            case 0:
                if (clockwiseOrientation) {
                    x1End = (int) (rectF.left + size);
                    y1End = (int) rectF.bottom;
                    x2End = x1End;
                    y2End = y1End - width;
                } else {
                    x2End = (int) (rectF.left + size);
                    y2End = (int) rectF.top;
                    x1End = x2End;
                    y1End = y2End + width;
                }
                return new int[]{x1End, y1End, x2End, y2End};
            case 90:
                if (clockwiseOrientation) {
                    x1End = (int) (rectF.left);
                    y1End = (int) (rectF.top + size);
                    x2End = x1End + width;
                    y2End = y1End;
                } else {
                    x2End = (int) (rectF.right);
                    y2End = (int) (rectF.top + size);
                    x1End = x2End - width;
                    y1End = y2End;
                }
                return new int[]{x1End, y1End, x2End, y2End};
            case 180:
                if(clockwiseOrientation) {
                    x1End = (int) (rectF.right - size);
                    y1End = (int) rectF.top;
                    x2End = x1End;
                    y2End = y1End + width;
                }else{
                    x2End = (int) (rectF.right - size);
                    y2End = (int) rectF.bottom;
                    x1End = x2End;
                    y1End = y2End - width;
                }
                return new int[]{x1End, y1End, x2End, y2End};
            case 270:
                if (clockwiseOrientation) {
                    x1End = (int) rectF.right;
                    y1End = (int) (rectF.bottom - size);
                    x2End = x1End - width;
                    y2End = y1End;
                } else {
                    x2End = (int) rectF.left;
                    y2End = (int) (rectF.bottom - size);
                    x1End = x2End + width;
                    y1End = y2End;
                }
                return new int[]{x1End, y1End, x2End, y2End};
        }
        return new int[]{0, 0, 0, 0};
    }

    /**
     * helper method to create outer and inner curve rectangles
     * @param size size of the shape
     * @param x1Start starting x coord for this rectangle
     * @param y1Start starting y coord for this rectangle
     * @return
     */
    protected RectF createRectangle(int size, int x1Start, int y1Start) {

        RectF rectf = new RectF();

        switch (startingDegree) {
            case 0:
                rectf.left = x1Start - size - size;
                rectf.top = y1Start - size;
                rectf.right = x1Start;
                rectf.bottom = y1Start + size;
                break;
            case 90:
                rectf.left = x1Start - size;
                rectf.top = y1Start - size - size;
                rectf.right = x1Start + size;
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
                rectf.right = x1Start + size;
                rectf.bottom = y1Start + size + size;
                break;
        }
        return rectf;
    }

}
