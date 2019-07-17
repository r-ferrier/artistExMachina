package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.example.workinprogress.Location;
import com.example.workinprogress.Position;

import java.util.ArrayList;

public class Painting extends Drawable {

    protected ArrayList<Integer> lightLevels;
    protected ArrayList<Location> locations;
    protected ArrayList<Position> positions;
    protected ArrayList<Integer> steps;
    protected ArrayList<Integer> distance;
    protected Context context;

    protected int width;
    protected int height;

    public Painting(Context context, ArrayList<Integer> lightLevels,
                    ArrayList<Location> locations, ArrayList<Position> positions, ArrayList<Integer> steps, ArrayList<Integer> distance){
        this.locations = locations;
        this.lightLevels = lightLevels;
        this.positions = positions;
        this.steps = steps;
        this.distance = distance;
        this.context = context;
    }


    @Override
    public void draw(Canvas canvas) {
        width = getBounds().width();
        height = getBounds().height();

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

    private int getWidth() {

        return width;
    }

    private int getHeight() {
        return height;
    }

    public Bitmap createBitmap(){


        Bitmap  bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);

        return bitmap;
    }
}
