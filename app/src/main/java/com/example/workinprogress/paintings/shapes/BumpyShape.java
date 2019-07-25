package com.example.workinprogress.paintings.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import java.util.ArrayList;

public class BumpyShape extends LineShape {

    public BumpyShape(int x1Start, int y1Start, int x2Start, int y2Start, int size, int[] aRGBColor) {
        super(x1Start, y1Start, x2Start, y2Start, size, aRGBColor);
    }

    @Override
    public void draw(Canvas canvas) {

        ArrayList<RectF> rectFS = createRectFs(x1End,y1End);





        Path path = new Path();
        paint1.setStyle(Paint.Style.FILL);
//        paint1.setStrokeWidth(5);

        path.moveTo(x1Start, y1Start);

        path.lineTo(x2Start,y2Start);
        path.lineTo(x2End,y2End);
        path.lineTo(x1End,y1End);

        path.addArc(rectFS.get(0),startingDegree-90,180);
        path.addArc(rectFS.get(1),startingDegree-90,180);
        path.addArc(rectFS.get(2),startingDegree-90,180);
        path.addArc(rectFS.get(3),startingDegree-90,180);


//        path.lineTo(x1Start,y1Start);

        canvas.drawPath(path, paint1);

//        System.out.println("starting points: x1 = " + x1Start + ", x2 = " + x2Start + ", y1 = " + y1Start + ", y2 = " + y2Start);
//        System.out.println("ending points: x1 = " + x1End + ", x2 = " + x2End + ", y1 = " + y1End + ", y2 = " + y2End);
//        System.out.println("width: "+width);
//        System.out.println("starting degree: "+startingDegree);

    }

    private ArrayList<RectF> createRectFs(float startingX, float startingY){

        float increment = ((float)size)/4;

        ArrayList<RectF> rectFS = new ArrayList<>();

        rectFS.add(createRectF(startingX,startingY));

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
