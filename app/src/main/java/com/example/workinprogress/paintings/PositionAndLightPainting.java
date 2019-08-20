package com.example.workinprogress.paintings;

import android.content.Context;
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
}
