package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.example.workinprogress.dataSetsAndComponents.DataSet;
import com.example.workinprogress.dataSetsAndComponents.SingularPointDataSet;
import com.example.workinprogress.dataSetsAndComponents.ThreePointsDataSet;
import com.example.workinprogress.dataSetsAndComponents.TwoPointsDataSet;

import java.util.ArrayList;

/**
 * Painting class should be extended to create different types of painting. It implements drawable
 * so any painting will return a drawable object. The abstract parent class will take care of storing
 * the data into arrays that the child classes can then access, and provide them with a method to
 * use to convert their outputs into bitmaps.
 */
public abstract class Painting extends Drawable {

    protected ArrayList<SingularPointDataSet> singularPointDataSets = new ArrayList<>();
    protected ArrayList<TwoPointsDataSet> twoPointsDataSets = new ArrayList<>();
    protected ArrayList<ThreePointsDataSet> threePointsDataSets = new ArrayList<>();
    protected Context context;

    protected float width;
    protected float height;

    /**
     * stores datasets in their own datapolint specific arraylists.
     * @param context context is required to enable this class to access view elements. Must be passed
     *                as a variable because the class needs to extend drawable.
     * @param dataSets arrayList of any data to be used by the application to draw images
     */
    public Painting(Context context, ArrayList<DataSet> dataSets){

        for(DataSet dataSet: dataSets){
            if(dataSet.getNumberOfDataPointsInEachSet()==1){
                singularPointDataSets.add((SingularPointDataSet)dataSet);
            }else if(dataSet.getNumberOfDataPointsInEachSet()==2){
                twoPointsDataSets.add((TwoPointsDataSet)dataSet);
            }else if(dataSet.getNumberOfDataPointsInEachSet()==3){
                threePointsDataSets.add((ThreePointsDataSet)dataSet);
            }
        }

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

        return (int)width;
    }

    private int getHeight() {
        return (int)height;
    }

    /**
     * creates a bitmap of the draw() method
     * @return bitmap version of output created when draw() is called
     */
    public Bitmap createBitmap(){

        Bitmap  bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);

        return bitmap;
    }
}
