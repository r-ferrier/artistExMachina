package com.example.workinprogress.dataSetsAndComponents;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * DatasetPoints are used to store the individual sets of data for points in time or events that will
 * be used to create
 * @param <e> dataset points can be created from any type of number but must implement this interface.
 */
public interface DataSetPoint<e extends Number> extends Serializable {


   String toString();

   int getNumberOfDataPointsInSet();

   String getDataTypeName();

   ArrayList<e> getResults();

   ArrayList<e> getScaledResults();


}
