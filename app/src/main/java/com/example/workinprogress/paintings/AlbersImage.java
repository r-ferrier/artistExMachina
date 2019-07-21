package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.workinprogress.DisplayImage;
import com.example.workinprogress.dataSetsAndComponents.DataSet;
import com.example.workinprogress.dataSetsAndComponents.SingularPointDataSet;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class AlbersImage extends Painting {

    private Paint paint1;
    private Paint paint2;
    private Paint paint3;
    private Paint paint4;
    private Paint paint5;
    private Canvas canvas;
    private float range = DisplayImage.IMAGE_SIZE_SCALAR;
    private int maximumSquareSize = 600;
    private int[][] coloursValues;
    private int colourRange = 255;
    private int colourG = 65535;
    private int colourB = 255;
    private float positionsSquareSizeRange;
    private int[][] savedColoursValues;

//    private int maximumLightValue = 1000;


    public AlbersImage(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);

        paint1 = new Paint();
        paint2 = new Paint();
        paint3 = new Paint();
        paint4 = new Paint();

        paint1.setARGB(10, 255, 200, 90);
        System.out.println("in the albers image -------------------------------------------------");
        System.out.println(positions.get(0).getScaledResults1());
        System.out.println(positions.get(0).getScaledResults2());
        System.out.println(positions.get(0).getScaledResults3());

        setColoursValues();

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void draw(Canvas canvas) {

        this.canvas = canvas;
        canvas.drawColor(Color.WHITE);
        width = getBounds().width();
        height = getBounds().height();

//        firstLightLevelsExperiment();
//        secondLightLevelsExperiment();
        firstEverythingExperiment();

//        paint1.setARGB(40,20,200,150);
//
//        canvas.drawRect(50,50,150,150, paint1);
//        canvas.drawRect(25,25,110,200, paint1);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void firstEverythingExperiment() {

        int[][] canvasPositions = new int[positions.get(0).getScaledResults1().size()][2];
        int[] shapeSize = new int[positions.get(0).getScaledResults1().size()];

        float xScalar = width / range;
        float yScalar = height / range;

        for (int i = 0; i < positions.get(0).getScaledResults1().size(); i++) {
//                    System.out.println("in the positions loop"+i+"-------------------------------------------------");
//                    System.out.println(positions.get(0).getScaledResults1().get(i)+"scaled results-----");

            float xSize = (positions.get(0).getScaledResults1().get(i) * xScalar);
            float ySize = (positions.get(0).getScaledResults2().get(i) * yScalar);
            canvasPositions[i][0] = Math.round(xSize);
            canvasPositions[i][1] = Math.round(ySize);

            float zSize = (positions.get(0).getScaledResults3().get(i));
            shapeSize[i] = Math.round(zSize);
        }

        for (int i = 0; i < shapeSize.length; i++) {

            shapeSize[i]-=500;
            if(shapeSize[i]<0){
                shapeSize[i]*=-1;
            }

            if (shapeSize[i] > positionsSquareSizeRange) {
                positionsSquareSizeRange = shapeSize[i];
            }
        }

        System.out.println("shape sizes unscaled"+Arrays.toString(shapeSize));

        float sizeScalar = maximumSquareSize / positionsSquareSizeRange;

        for (int i = 0; i < shapeSize.length; i++) {
            shapeSize[i] = (int)(sizeScalar*shapeSize[i]);
        }

        System.out.println("shape sizes scaled"+Arrays.toString(shapeSize));


        //    positions.remove(positions.get(0));




//






//        if (coloursValues == null) {
//            coloursValues = new int[]{230, 10, 50, 19, 180};
//        }

        int steps = lightDistanceAndSteps.get(0).getScaledResults1().get(0);
//                System.out.println(steps+" steps-------------------------------------------------");




//        paint1.setARGB(colourArray[0], colourArray[1], colourArray[2], colourArray[3]);

        for (int i = 0; i < steps; i++) {

            int j = i;

            System.out.println(coloursValues.length);

            while (j >= coloursValues.length) {
                j -= coloursValues.length;
            }


            paint1.setARGB(coloursValues[j][0],coloursValues[j][1],coloursValues[j][2],coloursValues[j][3]);
//            paint1.setARGB(random.nextInt(255),random.nextInt(256),random.nextInt(256),random.nextInt(256));


            Log.i("colours",Arrays.toString(coloursValues[j]));
            System.out.println("j"+j);
            System.out.println("i"+i);

            j = i;

            while (j >= canvasPositions.length) {
                j -= canvasPositions.length;
            }

            //set canvas positions so image sits centrally
            int x1 = canvasPositions[j][0] - (shapeSize[j]/2);
            int y1 = canvasPositions[j][1] - (shapeSize[j]/2);
            int x2 = canvasPositions[j][0] + (shapeSize[j]/2);
            int y2 = canvasPositions[j][1] + (shapeSize[j]/2);

            canvas.drawRect(x1, y1, x2,y2 , paint1);
//                    System.out.println(canvasPositions[j][0]+" "+canvasPositions[j][1]+" "+shapeSize[j]+" "+shapeSize[j]+" canvas positions");


        }
    }


    public void setColoursValues(){

        //create new random
        Random random = new Random();

        //create colour range
        float colourScalar = (float)255 / DisplayImage.IMAGE_SIZE_SCALAR;

        //get correct array
        for (SingularPointDataSet dataSet : lightDistanceAndSteps) {
            if (dataSet.getScaledResults1().size() > 1) {

                //instantiate array to correct length
                coloursValues = new int[dataSet.getScaledResults1().size()][4];

                //populate 1st position in each array with scaled values
                for (int i = 0; i < coloursValues.length; i++) {
                    coloursValues[i][0] = dataSet.getScaledResults1().get(i);
                    coloursValues[i][0] *= colourScalar;
                    System.out.println("scaled colours"+Arrays.toString(coloursValues[i]));
                }

                System.out.println("length of colours"+coloursValues.length);


                //populate all four position with colours found at previous position and one channel changed at random
                for(int i = 1; i < coloursValues.length; i++){

                    //take value from current position
                    int newValue = coloursValues[i][0];

                    // create new array using array found at last position and insert value into one of its slots at random
                    int[] colourArray = coloursValues[i-1].clone();
                    System.out.println("i-1: "+Arrays.toString(coloursValues[i-1]));
                    colourArray[random.nextInt(4)] = newValue;

                    // ensure alpha channel is visible
                    if(colourArray[0]<20){
                        colourArray[0]=20;
                    }

                    colourArray = invisibleColours(colourArray.clone());

                    //store array again at position i
                    coloursValues[i] = colourArray;
                    System.out.println("i: "+Arrays.toString(coloursValues[i]));
                }
                break;
            }
        }
    }

    public int[] invisibleColours(int[] colourArray){

        int numberOfInvisibleChannels = 0;

        for(int j = 1; j<4;j++){
            if(colourArray[j]<20){
                numberOfInvisibleChannels++;
            }
        }

        if(numberOfInvisibleChannels>2){

            System.out.println("invisible channels");

            int highestValueChannel = 1;

            for(int j = 1; j<4; j++){
                if(colourArray[j]>highestValueChannel){
                    highestValueChannel=j;
                }
            }

            colourArray[highestValueChannel]+=70;
            colourArray[0]=20;
        }

        return colourArray;

    }



//    public void thirdLightLevelsExperiment(int[] colourArray, Integer lightColourLevel) {
//
//
//
//
//        }
//    }

    //to hold all the x/y positions + shape sizes


//    public void firstLightLevelsExperiment(){
//
//
//        int xStartingPoint = 0;
//        int yStartingPoint;
//        int highestLightLevel = 0;
//
//        Random random = new Random();
//        paint1.setARGB(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
//
//
//        for(Integer lightLevel: lightLevels) {
//
//            // following code limits colour selection options
//            if ((lightLevel * (255 / DisplayImage.IMAGE_SIZE_SCALAR)) > highestLightLevel) {
//                highestLightLevel = lightLevel * (255 / DisplayImage.IMAGE_SIZE_SCALAR);
//            }
//
//
//            if (highestLightLevel > 256) {
//                highestLightLevel = 256;
//            }
//        }
//
//        for(Integer lightLevel: lightLevels){
//
//            //set height as linked to lightlevel
//            yStartingPoint = (height/ DisplayImage.IMAGE_SIZE_SCALAR)*(lightLevel);
//            //draw rectangle at hightlevel
//            canvas.drawRect(xStartingPoint,yStartingPoint,xStartingPoint+150,yStartingPoint+150,paint1);
//            //reposition to the right for next lightlevel
//            xStartingPoint+=20;
//
//            paint1.setARGB(random.nextInt(highestLightLevel), random.nextInt(highestLightLevel), random.nextInt(highestLightLevel), random.nextInt(highestLightLevel));
//            //colour selection + opacity limited to the highest lightvalue - can choose from a random assortment of colours up to but not over that light amount
//        }
//
//        xStartingPoint = 0;
//
//
//        //repeat but lower down
//
//        for(Integer lightLevel: lightLevels){
//
//            yStartingPoint = ((height/ DisplayImage.IMAGE_SIZE_SCALAR)*(lightLevel))+100;
//            canvas.drawRect(xStartingPoint,yStartingPoint,xStartingPoint+150,yStartingPoint+150,paint1);
//            xStartingPoint+=20;
//            paint1.setARGB(random.nextInt(256), random.nextInt(256), random.nextInt(256), random.nextInt(256));
//        }
//
//    }

//    public void secondLightLevelsExperiment(){
//
//        int xStartingPoint = 0;
//        int yStartingPoint;
//        int lightColourLevel = 0;
//        Random random = new Random();
//        int[] lightColourLevels = new int[]{0,0,0,0};
//
//
//        for(Integer lightLevel: lightLevels){
//
//            //set colour as linked to lightlevel
//            lightColourLevel = lightLevel*(255/DisplayImage.IMAGE_SIZE_SCALAR);
//
//            //randomly assign this colour level to one channel
//            lightColourLevels[random.nextInt(4)] = lightColourLevel;
//            //set colour to current colours held in lightlevels array
//            paint1.setARGB(lightColourLevels[0], lightColourLevels[1], lightColourLevels[2], lightColourLevels[3]);
//
//            //set height as linked to lightlevel
//            yStartingPoint = (int)((height/ DisplayImage.IMAGE_SIZE_SCALAR)*(lightLevel));
//            //draw rectangle at hightlevel
//            canvas.drawRect(xStartingPoint,yStartingPoint,xStartingPoint+350,yStartingPoint+350,paint1);
//            //reposition to the right for next lightlevel
//            xStartingPoint+=20;
//
//        }
//
//        xStartingPoint = 0;
//
//
//
//    }


}
