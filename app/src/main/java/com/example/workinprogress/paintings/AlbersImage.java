package com.example.workinprogress.paintings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.workinprogress.DisplayImage;
import com.example.workinprogress.dataSetsAndComponents.DataSet;
import com.example.workinprogress.dataSetsAndComponents.SingularPointDataSet;
import com.google.android.gms.common.util.Hex;

import java.util.ArrayList;

public class AlbersImage extends Painting {

    private Paint paint1;
    private Paint paint2;
    private Paint paint3;
    private Paint paint4;
    private Paint paint5;
    private Canvas canvas;
    private float range = DisplayImage.IMAGE_SIZE_SCALAR;
    private int maximumSquareSize = 300;
    private int[] coloursValues;
    private int colourRange = 16777216;
    private int colourG = 65535;
    private int colourB = 255;

//    private int maximumLightValue = 1000;


    public AlbersImage(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);

        paint1 = new Paint();
        paint2 = new Paint();
        paint3 = new Paint();
        paint4 = new Paint();

        paint1.setARGB(10,255,200,90);
        System.out.println("in the albers image -------------------------------------------------");
        System.out.println(positions.get(0).getScaledResults1());
        System.out.println(positions.get(0).getScaledResults2());
        System.out.println(positions.get(0).getScaledResults3());

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


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void firstEverythingExperiment() {


                int[][] canvasPositions = new int[positions.get(0).getScaledResults1().size()][2];
                int[] shapeSize = new int[positions.get(0).getScaledResults1().size()];

//                System.out.println(positions.get(0).getScaledResults1().size()+" positiions size ------ ------ ------ ------ ------ -------");

                float xScalar = width/range;
                float yScalar = height/range;
                float sizeScalar = maximumSquareSize/range;

                for (int i = 0; i < positions.get(0).getScaledResults1().size(); i++) {
//                    System.out.println("in the positions loop"+i+"-------------------------------------------------");
//                    System.out.println(positions.get(0).getScaledResults1().get(i)+"scaled results-----");

                    float xSize = (positions.get(0).getScaledResults1().get(i)*xScalar);
                    float ySize = (positions.get(0).getScaledResults2().get(i)*yScalar);
                    canvasPositions[i][0] = Math.round(xSize);
                    canvasPositions[i][1] = Math.round(ySize);

                    float zSize = (positions.get(0).getScaledResults3().get(i))*sizeScalar;
                  shapeSize[i] = Math.round(zSize);
//                    shapeSize[i] = 10;
                }

            //    positions.remove(positions.get(0));

        float colourScalar = colourRange/range;
//
        for(SingularPointDataSet dataSet: lightDistanceAndSteps){
            if(dataSet.getScaledResults1().size()>1){

                coloursValues = new int[dataSet.getScaledResults1().size()];

                for(int i = 0; i<coloursValues.length;i++){
                    coloursValues[i]=dataSet.getScaledResults1().get(i);
                    coloursValues[i]*=colourScalar;
                }
                lightDistanceAndSteps.remove(dataSet);
                break;
            }
        }

        if (coloursValues==null){
            coloursValues = new int[]{230,10,50,19,180};
        }

                int steps = lightDistanceAndSteps.get(0).getScaledResults1().get(0);
//                System.out.println(steps+" steps-------------------------------------------------");

                for(int i = 0; i<steps; i++){

            int j = i;

            while (j>=coloursValues.length){
                j -= coloursValues.length;
            }

            Color color = Color.valueOf(coloursValues[j]);
//                    System.out.println(color.toArgb());

                    String rgb = color.toArgb()+"";
//
//                    String r = "0x"+rgb.charAt(0)+rgb.charAt(1)+"";
//                    String g = "0x"+rgb.charAt(2)+rgb.charAt(3)+"";
//                    String b = "0x"+rgb.charAt(4)+rgb.charAt(5)+"";

            paint1.setARGB(50, (int)(color.red()*100),(int)(color.green()*100),(int)(color.blue()*100));





//                    System.out.println(color.red()+"red "+color.blue()+" blue "+color.green()+" green");


                    j = i;

                    while (j>=canvasPositions.length){
                        j -= canvasPositions.length;
                    }
                    canvas.drawRect(canvasPositions[j][0],canvasPositions[j][1],canvasPositions[j][0]+shapeSize[j],canvasPositions[j][1]+shapeSize[j],paint1);
//                    System.out.println(canvasPositions[j][0]+" "+canvasPositions[j][1]+" "+shapeSize[j]+" "+shapeSize[j]+" canvas positions");


                }
        }

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
//            yStartingPoint = (height/ DisplayImage.IMAGE_SIZE_SCALAR)*(lightLevel);
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
