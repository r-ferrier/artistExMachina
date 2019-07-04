package com.example.workinprogress;

public class Position {

    private float xAxis;
    private float yAxis;
    private float zAxis;

    private String xAxisString;
    private String yAxisString;
    private String zAxisString;


    public Position(float x, float y, float z){

        this.xAxis = x;
        this.yAxis = y;
        this.zAxis = z;

        this.xAxisString = String.valueOf(x);
        this.yAxisString = String.valueOf(y);
        this.zAxisString = String.valueOf(z);

    }


    public float getxAxis() {
        return xAxis;
    }

    public float getyAxis() {
        return yAxis;
    }

    public float getzAxis() {
        return zAxis;
    }

    public String getxAxisString() {
        return xAxisString;
    }

    public String getyAxisString() {
        return yAxisString;
    }

    public String getzAxisString() {
        return zAxisString;
    }
}
