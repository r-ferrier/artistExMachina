package com.example.workinprogress;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

public class ImageSavedorDeleted extends Drawable {

    boolean saved;

    public ImageSavedorDeleted(boolean saved){
        this.saved = saved;
    }

    @Override
    public void draw(Canvas canvas) {

        Paint background = new Paint();
        Paint text  = new Paint();

        background.setARGB(100,235,235,235);

        canvas.drawColor(background.getColor());

        if(saved) {

        text.setTextSize(60);
        text.setARGB(255,120,180,30);
        text.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));

            canvas.drawText("Image saved to gallery",canvas.getWidth()/2-200,canvas.getHeight()/2,text);
        }else{
            text.setTextSize(60);
            text.setARGB(255,240,120,30);
            text.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));

            canvas.drawText("Image deleted",canvas.getWidth()/2-200,canvas.getHeight()/2,text);
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
