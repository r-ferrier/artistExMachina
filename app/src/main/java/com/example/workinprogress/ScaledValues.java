package com.example.workinprogress;

import android.content.Context;

import java.util.ArrayList;

public class ScaledValues {

    private ArrayList<Float> lightLevels;
    private ArrayList<double[]> locations;
    private ArrayList<Position> positions;
    private int steps;
    private float distance;


    public ScaledValues(ArrayList<Float> lightLevels,
                        ArrayList<double[]> locations, ArrayList<Position> positions, int steps, float distance){

        this.locations = locations;
        this.lightLevels = lightLevels;
        this.positions = positions;
        this.steps = steps;
        this.distance = distance;

    }


    @Override
    public String toString() {

        String string = "";
        StringBuilder stringBuilder = new StringBuilder(string);

        stringBuilder.append("light levels:\n");
        for(Float f: lightLevels){
            stringBuilder.append(f+"\n");
        }
        stringBuilder.append("\n");

        stringBuilder.append("locations:\n");
        for(double[] d: locations){
            stringBuilder.append(d[0]+", "+d[1]+"\n");
        }
        stringBuilder.append("\n");

        stringBuilder.append("positions:\n");
        for(Position p: positions){
            stringBuilder.append(p.getxAxisString()+", "+p.getyAxisString()+", "+p.getzAxisString()+"\n");
        }
        stringBuilder.append("\n");

        stringBuilder.append("steps:\n"+steps+"\n");
        stringBuilder.append("distance:\n"+distance+"\n");

        return stringBuilder.toString();

    }

    public ArrayList<Float> getLightLevels() {
        return lightLevels;
    }

    public ArrayList<double[]> getLocations() {
        return locations;
    }

    public ArrayList<Position> getPositions() {
        return positions;
    }

    public int getSteps() {
        return steps;
    }

    public float getDistance() {
        return distance;
    }
}
