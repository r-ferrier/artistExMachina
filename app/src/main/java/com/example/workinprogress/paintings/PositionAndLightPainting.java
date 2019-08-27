package com.example.workinprogress.paintings;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.example.workinprogress.R;
import com.example.workinprogress.dataSetsAndComponents.DataSet;
import com.example.workinprogress.dataSetsAndComponents.SingularPointDataSet;

import java.util.ArrayList;

public class PositionAndLightPainting extends Painting {

    private String TAG = "PositionAndLightPainting info";
    protected ArrayList<Integer> lightValues;
    protected ArrayList<Integer> positionValues1;
    protected ArrayList<Integer> positionValues2;
    protected ArrayList<Integer> positionValues3;



    /**
     * stores datasets in their own datapolint specific arraylists.
     *
     * @param context  context is required to enable this class to access view elements. Must be passed
     *                 as a variable because the class needs to extend drawable.
     * @param dataSets arrayList of any data to be used by the application to draw images
     */
    public PositionAndLightPainting(Context context, ArrayList<DataSet> dataSets) {
        super(context, dataSets);
        setData();
    }

    public void setData(){

        for(SingularPointDataSet d: singularPointDataSets){
            if (d.getDataTypeName().equals(context.getString(R.string.data_type_light))){
                lightValues = (ArrayList<Integer>)d.getScaledResults1().clone();
            }
        }

        if(lightValues==null){
            Log.e(TAG,"No light data has been received, check data types");
        }

        positionValues1 = (ArrayList<Integer>)(threePointsDataSets.get(0).getScaledResults1()).clone();
        positionValues2 = (ArrayList<Integer>)(threePointsDataSets.get(0).getScaledResults2()).clone();
        positionValues3 = (ArrayList<Integer>)(threePointsDataSets.get(0).getScaledResults3()).clone();

        if(positionValues1==null){
            Log.e(TAG,"No position data has been received, check data types");
        }
    }

    /**
     * takes any sorted array and removes duplicate values from it. If the array is shorter than 10,
     * adds in 0s until it is at least 10 in length.
     * @param sortedArray any arraylist of integers but must be in sorted order, ascending or descending
     * @return arraylist of unique integers in sorted order
     */
    ArrayList<Integer> setUniqueValueSortedArrays(ArrayList<Integer> sortedArray) {

        for (int i = 0; i < sortedArray.size() - 1; i++) {
            while (sortedArray.get(i + 1).equals(sortedArray.get(i))) {
                sortedArray.remove(i + 1);
                if(sortedArray.size()-1==i){
                    break;
                }
            }
        }
        while (sortedArray.size() < 10) {
            sortedArray.add(0);
        }
        return sortedArray;
    }

    int findLargestMovementBiasedAverage(ArrayList<Integer> reverseSortedArray,String dataType){

        float totalHighestValues = 0;
        int countOfHighestValues = 0;
        float totalAllValues = 0;
        int countAllValues = reverseSortedArray.size();

        for(int i = 0; i<4;i++){
            if(i<countAllValues){
                totalHighestValues+=reverseSortedArray.get(i);
                countOfHighestValues++;
            }else{
                break;
            }
        }

        for(int i = 0; i<countAllValues; i++){
            totalAllValues+=reverseSortedArray.get(i);
            System.out.println(reverseSortedArray.get(i));
        }

        float averageOfFourHighestValues = (totalHighestValues/countOfHighestValues);
        System.out.println(averageOfFourHighestValues);
        int averageOfAllValues = (int)(totalAllValues/countAllValues);
        System.out.println(averageOfAllValues);

        Log.i(TAG,"new average "+dataType+" created: "+(averageOfAllValues+averageOfFourHighestValues)/2);

        return (int)(averageOfAllValues+averageOfFourHighestValues)/2;

    }

}
