package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.example.workinprogress.dataSetsAndComponents.DataSet;
import com.example.workinprogress.dataSetsAndComponents.DataSetPoint;
import com.example.workinprogress.dataSetsAndComponents.SingularPointDataSet;
import com.example.workinprogress.dataSetsAndComponents.ThreePointsDataSet;
import com.example.workinprogress.dataSetsAndComponents.TwoPointsDataSet;

import java.util.ArrayList;

public class Painting extends Drawable {

    protected ArrayList<SingularPointDataSet> lightDistanceAndSteps = new ArrayList<>();
    protected ArrayList<TwoPointsDataSet> locations = new ArrayList<>();
    protected ArrayList<ThreePointsDataSet> positions = new ArrayList<>();
    protected Context context;

    protected float width;
    protected float height;

    public Painting(Context context, ArrayList<DataSet> dataSets){

        for(DataSet dataSet: dataSets){

            System.out.println(dataSet.getNumberOfDataPointsInEachSet()+dataSet.getDataTypeName()+"-----------------------");
            if(dataSet.getNumberOfDataPointsInEachSet()==1){
                lightDistanceAndSteps.add((SingularPointDataSet)dataSet);
            }else if(dataSet.getNumberOfDataPointsInEachSet()==2){
                locations.add((TwoPointsDataSet)dataSet);
            }else if(dataSet.getNumberOfDataPointsInEachSet()==3){
                positions.add((ThreePointsDataSet)dataSet);

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

    public Bitmap createBitmap(){

        Bitmap  bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);

        return bitmap;
    }
}
