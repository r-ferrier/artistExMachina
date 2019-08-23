package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.example.workinprogress.R;
import com.example.workinprogress.dataSetsAndComponents.DataSet;
import com.example.workinprogress.dataSetsAndComponents.SingularPointDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * creates a drawing in an abstract landscape style, using some of the techniques explored in kineticArt
 */
public class Landscape extends KineticArt {

    private Canvas canvas;
    private ArrayList<Path> paths;
    private ArrayList<Paint> paints;
    private ArrayList<int[]> controls;
    private Random random;
    private int numberOfPaintOptions = 9;
    private int sizeRange = 500;
    private int sizeCounter = 0;
    private String TAG = "Landscape class info";

    private int canvasColor;


    public Landscape(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);

        random = new Random();
        setNewData();
        setPaints();
        choosePaints();
    }

    /**
     * decides how many and then which paints to choose from the options. Removes paints at random
     * from chosen selection and then adds in one opaque black or white paint object.
     */
    private void choosePaints() {

        for (Paint paint: paintsOptions) {
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(100);
        }

        Paint opaquePaint = new Paint();
        int numberOfOptions;
        if (averageLightValue < 2500) {

            canvasColor = Color.BLACK;
            opaquePaint.setColor(canvasColor);

            if (averageLightValue < 1000) {
                numberOfOptions = 2;
            } else {
                numberOfOptions = 4;
            }
        } else {
            canvasColor = Color.WHITE;
            opaquePaint.setColor(canvasColor);

            if (averageLightValue < 10000) {
              numberOfOptions = 6;
            } else {
               numberOfOptions = 8;
            }
        }
        removePaintsFromOptions(opaquePaint,numberOfOptions);
    }

    /**
     * helper method to remove paints from choices and set one colour opaque
     * @param opaquePaint black or white Paint object
     * @param numberOfOptions total number of paints drawing should have
     */
    private void removePaintsFromOptions(Paint opaquePaint, int numberOfOptions){
        for(int i = numberOfPaintOptions; i>numberOfOptions; i--){
            paintsOptions.remove(random.nextInt(i));
        }
        paintsOptions.add(opaquePaint);
    }

    /**
     * sets canvas colour, width and height. Checks if drawing has been created yet, if not, sets
     * up drawing. Then draws every path.
     * @param canvas passed by view
     */
    @Override
    public void draw(Canvas canvas) {
        this.canvas = canvas;
        this.canvas.drawColor(canvasColor);
        width = getBounds().width();
        height = getBounds().height();

        if (paths == null) {

            controls = new ArrayList<>();
            paths = new ArrayList<>();
            paints = new ArrayList<>();

            setUpForLandscape();
            setUpDrawing();
        }

        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), paints.get(i));
        }
    }

    /**
     * creates a path and one of each colour paint for every control array, then shuffle ths order of
     * the paints, and uses the draw ribbon method to modify and save each path
     */
    private void setUpDrawing() {

        for(int[] control: controls){
            paths.add(new Path());
            paints.addAll(paintsOptions);
        }
        Collections.shuffle(paints);

        for (int i = 0; i < controls.size(); i++) {
            paths.set(i, drawRibbon(controls.get(i).clone(), paths.get(i)));
        }
    }

    /**
     * draws a ribbon for each path
     * @param controls bezier curve controls
     * @param path path to draw to
     * @return drawn path
     */
    private Path drawRibbon(int[] controls, Path path) {
        path.moveTo(controls[0], controls[1]);
        path.cubicTo(controls[2], controls[3], controls[4], controls[5], controls[6], controls[7]);
        return path;
    }

    /**
     * takes in an arraylist of controls and returns it with many more controls added to create a mountain range
     * @param controls controls for each bezier curve 'mountain'
     * @param heightLimit limit to the height of each mountain
     * @return returns a list of controls that will enable a line representing a range of mountains to be drawn
     */
    private ArrayList<int[]> fillOutNewMountainRange(ArrayList<int[]> controls, int heightLimit) {

        Random random = new Random();

        for (int i = controls.size(); i < controls.size() + 1; i++) {

            if (controls.get(i - 1)[6] > width) {
                break;
            }

            int x1 = controls.get(i - 1)[6];
            int y1 = controls.get(i - 1)[7];
            int x3;

            do {
                x3 = getEnd(x1, heightLimit);
            } while (x3 <= x1);

            int potentialLength = x3 - x1;

            int controlY1 = y1 - (getHeightLimitedSizeForMountain(heightLimit));
            int controlX1 = random.nextInt(potentialLength) + x1;
            int controlY2 = y1 - (getHeightLimitedSizeForMountain(heightLimit));
            int controlX2 = random.nextInt(potentialLength) + x1;

            controls.add(new int[]{x1, y1, controlX1, controlY1, controlX2, controlY2, x3, y1});
        }

        return controls;
    }

    /**
     * helper method to get the end of each line. ensures end is visible onscreen.
     * @param start x coordinate for beginning of line
     * @param heightLimit maximum height for each curve
     * @return end point for this line
     */
    private int getEnd(int start, int heightLimit) {
        int end = random.nextInt(heightLimit) + start;
        if (end < 1) {
            end = 1;
        }
        return end;
    }

    /**
     * helper method to create an individual height limit for a mountain within the limit imposed
     * @param heightLimit maximum height for each curve
     * @return size to use for this mountain
     */
    private int getHeightLimitedSizeForMountain(int heightLimit){

        int sizeToUse = (int)((sizes.get(sizeCounter++))*((float)heightLimit/sizeRange));
        sizeToUse *= 2;

        if(sizeCounter>=sizes.size()){
            sizeCounter = 0;
        }
        return sizeToUse;
    }

    /**
     * set up method to add a mountain range for each number of loops
     */
    private void setUpForLandscape() {
        int start;
        int end;
        int centre;
        int heightLimit = (int) (height / numberOfLoops)*6;

        for (int i = 0; i < numberOfLoops; i++) {
                centre = (int) ((height / (numberOfLoops) * (i+1)));
                start = 0 - (random.nextInt(100));
                end = getEnd(start, heightLimit);

                for(int j = 0; j<4;j++) {
                    controls.add(new int[]{start, centre, start + random.nextInt(end),
                            centre - getHeightLimitedSizeForMountain(heightLimit), start + random.nextInt(end),
                            centre - getHeightLimitedSizeForMountain(heightLimit), end, centre});
                    fillOutNewMountainRange(controls, heightLimit);
                }
        }
    }
}
