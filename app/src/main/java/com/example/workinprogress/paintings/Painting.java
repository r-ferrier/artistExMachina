package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.example.workinprogress.Position;

import java.util.ArrayList;

public class Painting extends Drawable {

    protected ArrayList<Float> lightLevels;
    protected ArrayList<double[]> locations;
    protected ArrayList<Position> positions;
    protected int steps;
    protected float distance;
    protected Context context;

    public Painting(Context context, ArrayList<Float> lightLevels,
                    ArrayList<double[]> locations, ArrayList<Position> positions, int steps, float distance){
        this.locations = locations;
        this.lightLevels = lightLevels;
        this.positions = positions;
        this.steps = steps;
        this.distance = distance;
        this.context = context;
    }


    @Override
    public void draw(Canvas canvas) {

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
