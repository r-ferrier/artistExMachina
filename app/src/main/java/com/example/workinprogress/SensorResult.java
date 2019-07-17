package com.example.workinprogress;

import android.hardware.Sensor;

import java.io.Serializable;
import java.util.ArrayList;

public class SensorResult<f extends Number, e extends ResultValuesAppendable> implements Serializable {

    public static int MAX_LIGHT_LEVEL = 100;

    private float max;
    private float min;
    private final String name;

    private ArrayList<e> resultsObjects;
    private ArrayList<f> resultsNumbers;

    private ArrayList<Integer> scaledResultsNumbers = new ArrayList<>();

    private boolean resultsAreInObjectForm;

    /**
     * Constructors for this class must all set the results list, set a maximum and minimum value, and
     * set one of the three results form booleans to true to declare which type of result should be returned.
     * They must also declare whether or not the result said needs to be scaled.
     *
     * @param sensor
     * @param resultsObjects
     */

    public SensorResult(Sensor sensor, ArrayList<e> resultsObjects, String name) {
        setUpForObjectSets(resultsObjects);
        setUpForSensorSets(sensor);
        this.name = name;
    }

    public SensorResult(ArrayList<f> resultsNumbers, Sensor sensor,String name) {
        setUpForNumericSets(resultsNumbers);
        setUpForSensorSets(sensor);
        this.name = name;
    }

    public SensorResult(ArrayList<e> resultsObjects, boolean scalable, String name) {
        setUpForObjectSets(resultsObjects);
        toScaleOrNotToScale(scalable);
        this.name = name;
    }

    public SensorResult(boolean scalable, ArrayList<f> resultsNumbers, String name) {
        setUpForNumericSets(resultsNumbers);
        toScaleOrNotToScale(scalable);
        this.name = name;
    }


    public SensorResult(ArrayList<e> resultsObjects, float max, float min, String name) {
        setUpForObjectSets(resultsObjects);
        setUpForSetsWithKnownMaxAndMinRange(max, min);
        this.name = name;
    }

    public SensorResult(float max, float min, ArrayList<f> resultsNumbers, String name) {
        setUpForNumericSets(resultsNumbers);
        setUpForSetsWithKnownMaxAndMinRange(max, min);
        this.name = name;
    }


    public void setUpForNumericSets(ArrayList<f> resultsNumbers) {
        this.resultsNumbers = resultsNumbers;
        resultsAreInObjectForm = false;
    }

    public void setUpForObjectSets(ArrayList<e> resultsObjects) {
        this.resultsObjects = resultsObjects;
        resultsAreInObjectForm = true;
    }


    public void toScaleOrNotToScale(boolean scalable) {

        if (scalable) {
            setMaxAndMinWithoutKnownRange();
            scaleResults();
        } else {
            doNotScaleResults();
        }

    }

    public void setUpForSensorSets(Sensor sensor) {

        this.max = sensor.getMaximumRange();

        if (sensor.getType() == (Sensor.TYPE_LIGHT)) {
            this.min = 0;

        } else if ((sensor.getType() == (Sensor.TYPE_ACCELEROMETER))) {
            this.min = max * -1;

        } else {
            setMaxAndMinWithoutKnownRange();
        }
        scaleResults();

    }

    private void setUpForSetsWithKnownMaxAndMinRange(float max, float min) {
        this.max = max;
        this.min = min;
        scaleResults();
    }

    private void setMaxAndMinWithoutKnownRange() {

        if (!resultsAreInObjectForm) {

            for (f result : resultsNumbers) {

                if ((Float) result > this.max) {
                    this.max = (Float) result;
                } else if ((Float) result < this.min) {
                    this.min = (Float) result;
                }
            }

        }

    }


    public void scaleResults() {

        if (resultsAreInObjectForm) {
            for (e result : resultsObjects) {
                result.setScaledResults();
            }
        } else {

            float range = max - min;
            float scalar = MAX_LIGHT_LEVEL / range;

            System.out.println(scalar);

            for (f result : resultsNumbers) {
                scaledResultsNumbers.add(Math.round((Float) result * scalar));
            }
        }
    }

    public void doNotScaleResults() {
        for (f result : resultsNumbers) {

            if(result instanceof Integer) {
                scaledResultsNumbers.add((Integer)result);
            }else{
                scaledResultsNumbers.add(Math.round((Float)result));
            }
        }

        System.out.println("unscaled result: "+scaledResultsNumbers.get(0));
    }

    public ArrayList<e> getResultsObjects() {
        return resultsObjects;
    }

    public ArrayList<Integer> getResultsNumbers() {
        return scaledResultsNumbers;
    }

    public String getName(){
        return name;
    }

    public String getResultsAsString(){

        StringBuilder sb = new StringBuilder();

        sb.append(getName()+"\n");

        if(resultsAreInObjectForm){
            for (e result: resultsObjects){
                sb.append(result.toString()+"\n");
            }
        }else{

            for(int i=0;i<scaledResultsNumbers.size();i++){
                sb.append(scaledResultsNumbers.get(i)+"\n");
            }
        }

        return sb.toString();

    }

}
