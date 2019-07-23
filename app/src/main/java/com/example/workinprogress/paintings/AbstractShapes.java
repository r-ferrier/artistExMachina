package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.workinprogress.dataSetsAndComponents.DataSet;

import java.util.ArrayList;

public class AbstractShapes extends Painting {


    private Canvas canvas;
    private Paint paint1;

    public AbstractShapes(Context context, ArrayList<DataSet> dataSets) {

        super(context, dataSets);



    }

    public void draw(Canvas canvas) {

        this.canvas = canvas;
        canvas.drawColor(Color.WHITE);
        width = getBounds().width();
        height = getBounds().height();

        paint1 = new Paint();
        paint1.setColor(Color.BLACK);
        paint1.setStrokeCap(Paint.Cap.ROUND);
        paint1.setStrokeJoin(Paint.Join.ROUND);

        CurvedShape curvedShape = new CurvedShape(600,400,600,560);
        curvedShape.draw(canvas);

//        canvas.drawCircle(200,200,200,paint1);

    }

}
