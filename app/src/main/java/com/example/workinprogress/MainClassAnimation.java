package com.example.workinprogress;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.example.workinprogress.paintings.shapes.BumpyShape;
import com.example.workinprogress.paintings.shapes.CurvedShape;
import com.example.workinprogress.paintings.shapes.LineShape;
import com.example.workinprogress.paintings.shapes.Shape;

import java.util.ArrayList;

public class MainClassAnimation extends Drawable implements Runnable {

    private ArrayList<Shape> shapes;
    private float width;
    private float height;
    private Canvas canvas;
    private Canvas myCanvas;


    public MainClassAnimation() {
        shapes = new ArrayList<>();
    }

    @Override
    public void draw(Canvas canvas) {

        width = canvas.getWidth();
        height = canvas.getHeight();

        myCanvas = canvas;

        shapes.add(new CurvedShape(0, 0, 0, (int) (height / 4), true, new int[]{255, 230, 50, 20}, (int) (height / 2)));
        shapes.add(new CurvedShape(shapes.get(0).getX1End(), shapes.get(0).getY1End(), shapes.get(0).getX2End(), shapes.get(0).getY2End(), false, new int[]{255, 230, 150, 20}, (int) (height / 2)));
        shapes.add(new BumpyShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), (int) (height), new int[]{255, 230, 200, 20}));
        shapes.add(new CurvedShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), false, new int[]{255, 240, 220, 20}, (int) (height / 1.5)));
        shapes.add(new CurvedShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), false, new int[]{255, 200, 220, 20}, (int) (height / 3)));
        shapes.add(new CurvedShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), false, new int[]{255, 180, 220, 40}, (int) (height / 2.5)));
        shapes.add(new CurvedShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), false, new int[]{255, 180, 220, 70}, (int) (height / 3.5)));
        shapes.add(new LineShape(shapes.get(shapes.size() - 1).getX1End(), shapes.get(shapes.size() - 1).getY1End(), shapes.get(shapes.size() - 1).getX2End(), shapes.get(shapes.size() - 1).getY2End(), (int) (height), new int[]{155, 130, 200, 130}));


//        Thread thread = new Thread(this);
//        thread.start();

        for (Shape shape: shapes){
                shape.draw(myCanvas);
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

    @Override
    public void run() {

        for (Shape shape : shapes) {

            shape.draw(myCanvas);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
