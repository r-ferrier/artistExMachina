package com.example.workinprogress.dataSetsAndComponents;

import android.content.Context;
import android.hardware.SensorManager;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataSetTest {

    private DataSet dataSet;
    private float dataPoint;
    private String nameTest;
    private int numberOfPoints;
    private float max;
    private float min;
    private LightData lightData;
    private PositionData positionData;

    @Before
    public void setUp() throws Exception {

        dataPoint = 10;
        nameTest = "test";
        numberOfPoints = 1;
        max = 20;
        min = 0;
        lightData = new LightData(nameTest, dataPoint, max, min, false);
        positionData = new PositionData(nameTest, dataPoint,dataPoint,dataPoint,max, min);

        dataSet = new DataSet() {
            @Override
            public void setScaledResults() {
            }
        };

        dataSet.setDataTypeName(nameTest);
        dataSet.numberOfDataPointsInEachSet = numberOfPoints;
        dataSet.max = max;
        dataSet.min = min;

        dataSet.addResult(lightData);
        dataSet.addResult(positionData);

    }

    @Test
    public void test_getDataTypeName() {
        assertEquals("dataType name is not stored correctly", nameTest, dataSet.getDataTypeName());

    }

    @Test
    public void test_add_and_getResults() {
        assertEquals("dataSet is not storing results as expected", dataSet.getResults().get(0).getResults().get(0), dataPoint);
    }


    @Test
    public void getNumberOfDataPointsInEachSet() {
        assertEquals("dataset is storing values with a different number of points",dataSet.getResults().get(0).getNumberOfDataPointsInSet(),dataSet.getNumberOfDataPointsInEachSet());
       assertEquals("dataset is storing values with a different number of points than it has specified",dataSet.getResults().size(),1);
        assertEquals("dataset is not storing number of data points accurately",dataSet.getResults().get(0).getNumberOfDataPointsInSet(),numberOfPoints);

    }

    @Test
    public void getMax() {
        assertTrue("dataset is not storing maximum value accurately",dataSet.getMax()==max);
        max = Float.MAX_VALUE;
        dataSet.max = max;
        assertTrue("dataset is not storing maximum value accurately when maximum is largest possible float",dataSet.getMax()==max);
    }

    @Test
    public void getMin() {
        assertTrue("dataset is not storing minimum value accurately",dataSet.getMin()==min);
        min = Float.MIN_VALUE;
        dataSet.min = min;
        assertTrue("dataset is not storing minimum value accurately when minimum is smallest possible float",dataSet.getMin()==min);
    }
}