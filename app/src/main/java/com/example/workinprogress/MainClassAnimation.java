package com.example.workinprogress;

import android.graphics.Canvas;

import com.example.workinprogress.paintings.shapes.BumpyShape;
import com.example.workinprogress.paintings.shapes.CircleShape;
import com.example.workinprogress.paintings.shapes.CurvedShape;
import com.example.workinprogress.paintings.shapes.LineShape;
import com.example.workinprogress.paintings.shapes.Shape;

import java.util.ArrayList;

public class MainClassAnimation{

    private ArrayList<Shape> shapes;
    private ArrayList<Shape> thinShapes;
    private ArrayList<Shape> thinnestShapes;
    private float width;
    private float height;
    private Canvas canvas;
    private Canvas myCanvas;


    public MainClassAnimation(float width, float height) {
        shapes = new ArrayList<>();
        thinShapes = new ArrayList<>();
        thinnestShapes = new ArrayList<>();
        this.width = width;
        this.height = height;

        createShapes();
        createThinShapes();
        createThinnestShapes();
    }

    private void createShapes(){
        int width1 = (int) (height / 8);
        int startingPoint1 = (int) (height / 12);
        int minimumLength1 = (int) (width1 * 1.2);
        int startingAColour = 255;
        int startingRColour = 0;
        int startingGColour = 0;
        int startingBColour = 0;
        int colourIncrement = 15;
        int colourIncrement2 = 10;

        shapes.add(new CurvedShape(0, startingPoint1, 0, startingPoint1 + width1, true, new int[]{startingAColour, startingRColour, startingGColour, startingBColour}, minimumLength1));
        shapes.add(new CurvedShape(shapes.get(0).getX1End(), shapes.get(0).getY1End(), shapes.get(0).getX2End(), shapes.get(0).getY2End(), false, new int[]{startingAColour -= colourIncrement, startingRColour += colourIncrement, startingGColour += colourIncrement, startingBColour += colourIncrement}, (int) (minimumLength1 * 1.3)));
        shapes.add(new BumpyShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), minimumLength1, new int[]{startingAColour -= colourIncrement, startingRColour += colourIncrement, startingGColour += colourIncrement, startingBColour += colourIncrement}));
        shapes.add(new CurvedShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), false, new int[]{startingAColour -= colourIncrement, startingRColour += colourIncrement, startingGColour += colourIncrement, startingBColour += colourIncrement}, (int) (minimumLength1 * 1.5)));
        shapes.add(new CurvedShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), false, new int[]{startingAColour -= colourIncrement, startingRColour += colourIncrement, startingGColour += colourIncrement, startingBColour += colourIncrement}, (int) (minimumLength1 * 1.2)));
        shapes.add(new CurvedShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), false, new int[]{startingAColour -= colourIncrement, startingRColour += colourIncrement, startingGColour += colourIncrement, startingBColour += colourIncrement}, (int) (minimumLength1 * 1.6)));
        shapes.add(new CurvedShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), false, new int[]{startingAColour -= colourIncrement, startingRColour += colourIncrement, startingGColour += colourIncrement, startingBColour += colourIncrement}, (int) (minimumLength1 * 2)));
        shapes.add(new LineShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), (int) (minimumLength1 * 0.3), new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}));
        shapes.add(new LineShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), (int) (minimumLength1 * 0.3), new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}));
        shapes.add(new LineShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), (int) (minimumLength1 * 0.3), new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}));
        shapes.add(new LineShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), (int) (minimumLength1 * 0.3), new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}));
        shapes.add(new LineShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), (int) (minimumLength1 * 0.3), new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}));

    }

    private void createThinShapes(){

        int width2 = (int) (width / 8);
        int startingPoint2 = (int) (width / 5) * 3;
        int minimumLength2 = (int) (width2 * 1.2);
        int startingAColour = 255;
        int startingRColour = 0;
        int startingGColour = 0;
        int startingBColour = 0;
        int colourIncrement2 = 10;

        thinShapes.add(new LineShape(width2 + startingPoint2, 0, startingPoint2, 0, (int) (minimumLength2 * 0.5), new int[]{startingAColour, startingRColour, startingGColour, startingBColour}));
        thinShapes.add(new LineShape(thinShapes.get(thinShapes.size() - 1).getX1End(), thinShapes.get(thinShapes.size() - 1).getY1End(), thinShapes.get(thinShapes.size() - 1).getX2End(), thinShapes.get(thinShapes.size() - 1).getY2End(), (int) (minimumLength2), new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}));
        thinShapes.add(new LineShape(thinShapes.get(thinShapes.size() - 1).getX1End(), thinShapes.get(thinShapes.size() - 1).getY1End(), thinShapes.get(thinShapes.size() - 1).getX2End(), thinShapes.get(thinShapes.size() - 1).getY2End(), (int) (minimumLength2), new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}));
        thinShapes.add(new LineShape(thinShapes.get(thinShapes.size() - 1).getX1End(), thinShapes.get(thinShapes.size() - 1).getY1End(), thinShapes.get(thinShapes.size() - 1).getX2End(), thinShapes.get(thinShapes.size() - 1).getY2End(), (int) (minimumLength2), new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}));
        thinShapes.add(new LineShape(thinShapes.get(thinShapes.size() - 1).getX1End(), thinShapes.get(thinShapes.size() - 1).getY1End(), thinShapes.get(thinShapes.size() - 1).getX2End(), thinShapes.get(thinShapes.size() - 1).getY2End(), (int) (minimumLength2), new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}));
        thinShapes.add(new LineShape(thinShapes.get(thinShapes.size() - 1).getX1End(), thinShapes.get(thinShapes.size() - 1).getY1End(), thinShapes.get(thinShapes.size() - 1).getX2End(), thinShapes.get(thinShapes.size() - 1).getY2End(), (int) (minimumLength2), new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}));
        thinShapes.add(new LineShape(thinShapes.get(thinShapes.size() - 1).getX1End(), thinShapes.get(thinShapes.size() - 1).getY1End(), thinShapes.get(thinShapes.size() - 1).getX2End(), thinShapes.get(thinShapes.size() - 1).getY2End(), (int) (minimumLength2), new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}));
        thinShapes.add(new CurvedShape(thinShapes.get(thinShapes.size() - 1).getX1End(), thinShapes.get(thinShapes.size() - 1).getY1End(), thinShapes.get(thinShapes.size() - 1).getX2End(), thinShapes.get(thinShapes.size() - 1).getY2End(), false, new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}, (int) (height / 6)));
        thinShapes.add(new CurvedShape(thinShapes.get(thinShapes.size() - 1).getX1End(), thinShapes.get(thinShapes.size() - 1).getY1End(), thinShapes.get(thinShapes.size() - 1).getX2End(), thinShapes.get(thinShapes.size() - 1).getY2End(), false, new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}, (int) (height / 4)));
        thinShapes.add(new CurvedShape(thinShapes.get(thinShapes.size() - 1).getX1End(), thinShapes.get(thinShapes.size() - 1).getY1End(), thinShapes.get(thinShapes.size() - 1).getX2End(), thinShapes.get(thinShapes.size() - 1).getY2End(), false, new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}, (int) (height / 3.2)));
        thinShapes.add(new CurvedShape(thinShapes.get(thinShapes.size() - 1).getX1End(), thinShapes.get(thinShapes.size() - 1).getY1End(), thinShapes.get(thinShapes.size() - 1).getX2End(), thinShapes.get(thinShapes.size() - 1).getY2End(), false, new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}, (int) (height / 6)));
        thinShapes.add(new CurvedShape(thinShapes.get(thinShapes.size() - 1).getX1End(), thinShapes.get(thinShapes.size() - 1).getY1End(), thinShapes.get(thinShapes.size() - 1).getX2End(), thinShapes.get(thinShapes.size() - 1).getY2End(), true, new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}, (int) (height / 5)));
        thinShapes.add(new CurvedShape(thinShapes.get(thinShapes.size() - 1).getX1End(), thinShapes.get(thinShapes.size() - 1).getY1End(), thinShapes.get(thinShapes.size() - 1).getX2End(), thinShapes.get(thinShapes.size() - 1).getY2End(), true, new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}, (int) (height / 6)));
        thinShapes.add(new CurvedShape(thinShapes.get(thinShapes.size() - 1).getX1End(), thinShapes.get(thinShapes.size() - 1).getY1End(), thinShapes.get(thinShapes.size() - 1).getX2End(), thinShapes.get(thinShapes.size() - 1).getY2End(), true, new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}, (int) (height / 5)));
        thinShapes.add(new CurvedShape(thinShapes.get(thinShapes.size() - 1).getX1End(), thinShapes.get(thinShapes.size() - 1).getY1End(), thinShapes.get(thinShapes.size() - 1).getX2End(), thinShapes.get(thinShapes.size() - 1).getY2End(), true, new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}, (int) (height / 3)));
        thinShapes.add(new BumpyShape(thinShapes.get(thinShapes.size() - 1).getX1End(), thinShapes.get(thinShapes.size() - 1).getY1End(), thinShapes.get(thinShapes.size() - 1).getX2End(), thinShapes.get(thinShapes.size() - 1).getY2End(), (int) (height / 3), new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}));
        thinShapes.add(new CircleShape(thinShapes.get(thinShapes.size() - 1).getX1End(), thinShapes.get(thinShapes.size() - 1).getY1End(), thinShapes.get(thinShapes.size() - 1).getX2End(), thinShapes.get(thinShapes.size() - 1).getY2End(), (int) (height / 5), new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}));
        thinShapes.add(new LineShape(thinShapes.get(thinShapes.size() - 1).getX1End(), thinShapes.get(thinShapes.size() - 1).getY1End(), thinShapes.get(thinShapes.size() - 1).getX2End(), thinShapes.get(thinShapes.size() - 1).getY2End(), (int) (height), new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}));
        thinShapes.add(new CurvedShape(thinShapes.get(thinShapes.size() - 1).getX1End(), thinShapes.get(thinShapes.size() - 1).getY1End(), thinShapes.get(thinShapes.size() - 1).getX2End(), thinShapes.get(thinShapes.size() - 1).getY2End(), false, new int[]{startingAColour -= colourIncrement2, startingRColour += colourIncrement2, startingGColour += colourIncrement2, startingBColour += colourIncrement2}, (int) (height / 2)));

    }

    private void createThinnestShapes(){


        int width3 = (int) (width / 60);
        int startingPoint3 = (int) (width / 5);
        int minimumLength3 = (int) (width3 * 6);
        int startingAColour = 255;
        int startingRColour = 0;
        int startingGColour = 0;
        int startingBColour = 0;
        int colourIncrement3 = 10;

        thinnestShapes.add(new LineShape(width3 + startingPoint3, 0, startingPoint3, 0, (int) (minimumLength3 * 0.5), new int[]{startingAColour, startingRColour, startingGColour, startingBColour}));
        thinnestShapes.add(new LineShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), (int) (minimumLength3), new int[]{startingAColour -= colourIncrement3, startingRColour += colourIncrement3, startingGColour += colourIncrement3, startingBColour += colourIncrement3}));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), false, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3 * 0.5)));
        thinnestShapes.add(new LineShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), (int) (minimumLength3*0.5), new int[]{startingAColour -= colourIncrement3, startingRColour += colourIncrement3, startingGColour += colourIncrement3, startingBColour += colourIncrement3}));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), false, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3 * 0.8)));
        thinnestShapes.add(new CircleShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), (int) (minimumLength3*0.5), new int[]{startingAColour -= colourIncrement3, startingRColour += colourIncrement3, startingGColour += colourIncrement3, startingBColour += colourIncrement3}));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), false, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3 * 0.5)));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), false, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3 * 0.5)));
        thinnestShapes.add(new BumpyShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), (int) (minimumLength3), new int[]{startingAColour -= colourIncrement3, startingRColour += colourIncrement3, startingGColour += colourIncrement3, startingBColour += colourIncrement3}));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), false, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3)));
        thinnestShapes.add(new LineShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), (int) (minimumLength3), new int[]{startingAColour -= colourIncrement3, startingRColour += colourIncrement3, startingGColour += colourIncrement3, startingBColour += colourIncrement3}));
        thinnestShapes.add(new CircleShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), (int) (minimumLength3), new int[]{startingAColour -= colourIncrement3, startingRColour += colourIncrement3, startingGColour += colourIncrement3, startingBColour += colourIncrement3}));
        thinnestShapes.add(new BumpyShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), (int) (minimumLength3), new int[]{startingAColour -= colourIncrement3, startingRColour += colourIncrement3, startingGColour += colourIncrement3, startingBColour += colourIncrement3}));
        thinnestShapes.add(new BumpyShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), (int) (minimumLength3), new int[]{startingAColour -= colourIncrement3, startingRColour += colourIncrement3, startingGColour += colourIncrement3, startingBColour += colourIncrement3}));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), false, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3 * 0.5)));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), true, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3 * 0.5)));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), true, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3 * 0.5)));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), true, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3 * 0.5)));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), true, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3 * 0.8)));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), true, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3 * 0.9)));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), true, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3)));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), true, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3*1.3)));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), true, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3*0.3)));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), true, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3*0.25)));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), true, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3*0.25)));
        thinnestShapes.add(new LineShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), (int) (minimumLength3*2), new int[]{startingAColour -= colourIncrement3, startingRColour += colourIncrement3, startingGColour += colourIncrement3, startingBColour += colourIncrement3}));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), false, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3*0.4)));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), true, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3*0.4)));
        thinnestShapes.add(new CircleShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), (int) (minimumLength3*2), new int[]{startingAColour -= colourIncrement3, startingRColour += colourIncrement3, startingGColour += colourIncrement3, startingBColour += colourIncrement3}));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), true, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3*2)));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), true, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3*1.8)));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), true, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3*1.5)));
        thinnestShapes.add(new CurvedShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), true, new int[]{startingAColour, startingRColour, startingGColour, startingBColour},(int) (minimumLength3*1.3)));
        thinnestShapes.add(new BumpyShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), (int) (minimumLength3), new int[]{startingAColour -= colourIncrement3, startingRColour += colourIncrement3, startingGColour += colourIncrement3, startingBColour += colourIncrement3}));
        thinnestShapes.add(new LineShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), (int) (minimumLength3), new int[]{startingAColour -= colourIncrement3, startingRColour += colourIncrement3, startingGColour += colourIncrement3, startingBColour += colourIncrement3}));
        thinnestShapes.add(new BumpyShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), (int) (minimumLength3*2), new int[]{startingAColour -= colourIncrement3, startingRColour += colourIncrement3, startingGColour += colourIncrement3, startingBColour += colourIncrement3}));
        thinnestShapes.add(new LineShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), (int) (minimumLength3), new int[]{startingAColour -= colourIncrement3, startingRColour += colourIncrement3, startingGColour += colourIncrement3, startingBColour += colourIncrement3}));
        thinnestShapes.add(new BumpyShape(thinnestShapes.get(thinnestShapes.size() - 1).getX1End(), thinnestShapes.get(thinnestShapes.size() - 1).getY1End(), thinnestShapes.get(thinnestShapes.size() - 1).getX2End(), thinnestShapes.get(thinnestShapes.size() - 1).getY2End(), (int) (minimumLength3*4), new int[]{startingAColour -= colourIncrement3, startingRColour += colourIncrement3, startingGColour += colourIncrement3, startingBColour += colourIncrement3}));





    }

    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    public ArrayList<Shape> getThinShapes() {
        return thinShapes;
    }

    public ArrayList<Shape> getThinnestShapes() {
        return thinnestShapes;
    }
}