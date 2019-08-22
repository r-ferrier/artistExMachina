package com.example.workinprogress.paintings.shapes;

import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.ArrayList;

public class BumpyShape extends LineShape {

    private ArrayList<RectF> rectFS;

    /**
     * class to draw a shape that forms a straight line with bumps on it
     *  __________    ,-.,-.,-.,-.
     * |          |  |___________|
     * `-'`-'`-'`-'
     *
     * @param x1Start starting coordinates x1
     * @param y1Start starting coordinates y1
     * @param x2Start starting coordinates x2
     * @param y2Start starting coordinates y2
     * @param size size/length of shape
     * @param aRGBColor colour of shape
     */
    public BumpyShape(int x1Start, int y1Start, int x2Start, int y2Start, int size, int[] aRGBColor) {
        super(x1Start, y1Start, x2Start, y2Start, size, aRGBColor);
    }

    /**
     * creates rectFs which will be used to draw the arcs for the bumps and then draws the shape
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        rectFS = createRectFs(x1End,y1End);
        path.moveTo(x1Start, y1Start);

        path.lineTo(x2Start,y2Start);
        path.lineTo(x2End,y2End);
        path.lineTo(x1End,y1End);

        path.addArc(rectFS.get(0),startingDegree-90,180);
        path.addArc(rectFS.get(1),startingDegree-90,180);
        path.addArc(rectFS.get(2),startingDegree-90,180);
        path.addArc(rectFS.get(3),startingDegree-90,180);

        canvas.drawPath(path, paint1);
    }

    /**
     * helper method to create a list of rectangles the app can use to draw arcs
     * @param startingX the starting x coord for line of bumps
     * @param startingY the starting y coord for line of bumps
     * @return returns a list of rectangles
     */
    protected ArrayList<RectF> createRectFs(float startingX, float startingY){

        float increment = ((float)size)/4;
        ArrayList<RectF> rectFS = new ArrayList<>();

        //create first rectangle using starting points
        rectFS.add(createRectF(startingX,startingY));

        //now check angle of shape to determine which direction to travel in for remaining rectangles
        switch (startingDegree){
            case 0:
                rectFS.add(createRectF(startingX,startingY-increment));
                rectFS.add(createRectF(startingX,startingY-(increment*2)));
                rectFS.add(createRectF(startingX,startingY-(increment*3)));
                break;
            case 90:
                rectFS.add(createRectF(startingX+increment,startingY));
                rectFS.add(createRectF(startingX+(increment*2),startingY));
                rectFS.add(createRectF(startingX+(increment*3),startingY));
                break;
            case 180:
                rectFS.add(createRectF(startingX,startingY+increment));
                rectFS.add(createRectF(startingX,startingY+(increment*2)));
                rectFS.add(createRectF(startingX,startingY+(increment*3)));
                break;
            case 270:
                rectFS.add(createRectF(startingX-increment,startingY));
                rectFS.add(createRectF(startingX-(increment*2),startingY));
                rectFS.add(createRectF(startingX-(increment*3),startingY));
                break;
        }
        return rectFS;
    }

    /**
     * helper method to create single rectangle if starting point is known
     * @param startingX the starting x coord for this rectangle
     * @param startingY the starting y coord for this rectangle
     * @return newly created rectangle
     */
    private RectF createRectF(float startingX, float startingY){

        RectF rectF = new RectF();

        switch (startingDegree){

            case 0:
                rectF.left = startingX-((float)size/8);
                rectF.top = startingY-((float)size/4);
                rectF.right = startingX+((float)size/8);
                rectF.bottom = startingY;
                break;
            case 90:
                rectF.left = startingX;
                rectF.top = startingY-((float)size/8);
                rectF.right = startingX+((float)size/4);
                rectF.bottom = startingY+((float)size/8);
                break;
            case 180:
                rectF.left = startingX-((float)size/8);
                rectF.top = startingY;
                rectF.right = startingX+((float)size/8);
                rectF.bottom = startingY+((float)size/4);
                break;
            case 270:
                rectF.left = startingX-((float)size/4);
                rectF.top= startingY-((float)size/8);
                rectF.right = startingX;
                rectF.bottom = startingY+((float)size/8);
                break;
        }

        return rectF;
    }
}
