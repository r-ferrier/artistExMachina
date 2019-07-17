package com.example.workinprogress.dataSetsAndComponents;

import java.io.Serializable;
import java.util.ArrayList;

public interface DataSetPoint<e extends Number> extends Serializable {


   String toString();

   int getNumberOfDataPointsInSet();

   String getDataTypeName();

   ArrayList<e> getResults();

}
