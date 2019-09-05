package com.example.workinprogress.dataSetsAndComponents.classesForTestingOutputs;

import android.content.Context;

import com.example.workinprogress.R;
import com.example.workinprogress.dataSetsAndComponents.DataSet;
import com.example.workinprogress.dataSetsAndComponents.DataSetPoint;
import com.example.workinprogress.dataSetsAndComponents.LightData;
import com.example.workinprogress.dataSetsAndComponents.PositionData;
import com.example.workinprogress.dataSetsAndComponents.SingularPointDataSet;
import com.example.workinprogress.dataSetsAndComponents.ThreePointsDataSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class can be used in conjunction with the displayimage class to specifically create false data sets to test images with
 */
public class CSVDataSet {

    private ArrayList<DataSet> dataSets;
    private SingularPointDataSet singularPointDataSet;
    private ThreePointsDataSet threePointsDataSet;

    //following variables for randomly generated data parameters
    private int numberOfMovements = 1000;
    private int numberOfLightChanges = 120;
    private int maximumLight = 5000;
    private int minimumLight =  000;
    private int maximumMovements = 125;
    private int minimumMovements = -125;

    //following variables used for all data
    private int lightMax = 40000;
    private int lightMin = 0;
    private int movementMax = 158;
    private int movementMin = -158;
    private String dataType;
    private String fileName;
    private Context context;


    public CSVDataSet(Context context, boolean CSVData, String fileName){

        dataSets = new ArrayList<>();
        this.context = context;
        this.dataType = context.getString(R.string.data_type_light);
        this.fileName = fileName;

        setSingularPointDataSet();
        setThreePointsDataSet();

        //chooses whether to use data from a CSV or a randomly generated method
        if(CSVData){
            addDataFromCsv();
        }else{
            addRandomlyGeneratedData();
        }

        dataSets.add(singularPointDataSet);
        dataSets.add(threePointsDataSet);

        for(DataSet dataSet: dataSets){
            dataSet.setScaledResults();
        }
    }

    private void addRandomlyGeneratedData(){
        addRandomMovementData();
        addRandomLightData();
    }

    private void addRandomMovementData(){

        Random random = new Random();

        for(int i = 0; i<numberOfMovements;i++){

            int result1 = random.nextInt(maximumMovements-minimumMovements)+minimumMovements;
            int result2 = random.nextInt(maximumMovements-minimumMovements)+minimumMovements;
            int result3 = random.nextInt(maximumMovements-minimumMovements)+minimumMovements;
            threePointsDataSet.addResult(new PositionData("test",(float)result1,(float)result2,(float)result3,movementMax,movementMin));
        }
    }

    private void addRandomLightData(){

        Random random = new Random();

        for(int i = 0; i<numberOfLightChanges;i++){
            int result = random.nextInt(maximumLight-minimumLight)+minimumLight;

            singularPointDataSet.addResult(new LightData(dataType,(float)result,lightMax,lightMin,false));
        }
    }


    private void setSingularPointDataSet(){

        singularPointDataSet = new SingularPointDataSet(dataType) {
            @Override
            public ArrayList<Integer> getScaledResults1() {
                return scaledResults1;
            }

            @Override
            public void setScaledResults() {
                scaledResults1 = new ArrayList<>();
                for (DataSetPoint dataSetPoint : getResults()) {
                    scaledResults1.add(Math.round((float) dataSetPoint.getScaledResults().get(0)));
                }

            }
            public String getAverageString(){

                float total = 0;
                int count = 0;

                for (DataSetPoint dataSetPoint:getResults()){
                    count++;
                    total+= (Float)dataSetPoint.getScaledResults().get(0);
                }
                return total/count+"";
            }
        };
    }

    private void setThreePointsDataSet(){
        threePointsDataSet = new ThreePointsDataSet("test") {
            @Override
            public ArrayList<Integer> getScaledResults1() {
                return scaledResults1;
            }

            @Override
            public ArrayList<Integer> getScaledResults2() {
                return scaledResults2;
            }

            @Override
            public ArrayList<Integer> getScaledResults3() {
                return scaledResults3;
            }

            @Override
            public void setScaledResults() {
                scaledResults1 = new ArrayList<>();
                scaledResults2 = new ArrayList<>();
                scaledResults3 = new ArrayList<>();

                for (DataSetPoint dataSetPoint : getResults()) {
                    scaledResults1.add(applyScaling((float) dataSetPoint.getScaledResults().get(0)));
                    scaledResults2.add(applyScaling((float) dataSetPoint.getScaledResults().get(1)));
                    scaledResults3.add(applyScaling((float) dataSetPoint.getScaledResults().get(2)));
                }

            }

            private Integer applyScaling(float dataSetPoint){
                return Math.round(dataSetPoint);
            }

            @Override
            public String getAverageString(){

                float totalX = 0;
                float totalY = 0;
                float totalZ = 0;

                int count = 0;

                for (DataSetPoint dataSetPoint:getResults()){
                    count++;
                    float x = (Float)dataSetPoint.getResults().get(0);
                    float y = (Float)dataSetPoint.getResults().get(1);
                    float z = (Float)dataSetPoint.getResults().get(2);
                    if(x<0){
                        totalX-=x;
                    }else{
                        totalX+=x;
                    }
                    if(y<0){
                        totalY-=y;
                    }else{
                        totalY+=y;
                    }
                    if(z<0){
                        totalZ-=z;
                    }else{
                        totalZ+=z;
                    }
                }
                return "x: "+totalX/count+"y: "+totalY/count+"z: "+totalZ/count;
            }
        };
    }


    public ArrayList<DataSet> getDataSets() {
        return dataSets;
    }

    /**
     * method to read in and use the data from a csv file. Csv file must have following format:
     * Row 1: headers (ignored)
     * Rows >1: column 1 (optional) light data, column 2 accelerometer x coordinate,
     * column 3 accelerometer y coordinate, column 4 accelerometer z coordinate
     * Csv file must be stored in assets folder
     */
    public void addDataFromCsv(){

        List<String> csvList = new ArrayList<>();
        try {
            //reads in any file stored in assets
            InputStream in = context.getResources().getAssets().open(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String thisLine;
            //read in line by line and add lines to a list of strings
            while ((thisLine = br.readLine()) != null) {
                csvList.add(thisLine);
            }

            //remove first line
            csvList.remove(0);

                //for each line, split it up and add the right columns to the dataSets
                for (String s : csvList) {
                    String[] word = s.split(","); //splits the string at every comma
                    if(word.length>3){
                        singularPointDataSet.addResult(new LightData(dataType,Float.parseFloat(word[0]),lightMax,lightMin,false));
                        threePointsDataSet.addResult(new PositionData("test",Float.parseFloat(word[1]),Float.parseFloat(word[2]),Float.parseFloat(word[3]),movementMax,movementMin));
                    }else if (word.length>2){
                        threePointsDataSet.addResult(new PositionData("test",Float.parseFloat(word[0]),Float.parseFloat(word[1]),Float.parseFloat(word[2]),movementMax,movementMin));
                    }else{
                        singularPointDataSet.addResult(new LightData(dataType,Float.parseFloat(word[0]),lightMax,lightMin,false));
                    }
                }

            br.close();

        } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException fileProblem) {
            fileProblem.printStackTrace();
        }

    }
}
