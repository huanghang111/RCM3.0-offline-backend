package com.bosch.rcm.domain;

import java.util.HashMap;


public class RcmLatestErrorData {
    HashMap<String, Integer> countErrors = new HashMap<>();
    HashMap<String, SignalError> firstErrors = new HashMap<>();
    HashMap<String, Integer> countDataForPieChart = new HashMap<>();

    public HashMap<String, Integer> getCountErrors() {
        return countErrors;
    }

    public void setCountErrors(HashMap<String, Integer> countErrors) {
        this.countErrors = countErrors;
    }

    public HashMap<String, SignalError> getFirstErrors() {
        return firstErrors;
    }

    public void setFirstErrors(HashMap<String, SignalError> firstErrors) {
        this.firstErrors = firstErrors;
    }

    public HashMap<String, Integer> getCountDataForPieChart() {
        return countDataForPieChart;
    }

    public void setCountDataForPieChart(HashMap<String, Integer> countDataForPieChart) {
        this.countDataForPieChart = countDataForPieChart;
    }

    public void resetData(String thresID) {
        if (getFirstErrors().get(thresID) != null) {
            getFirstErrors().replace(thresID, null);
        }
        if (getCountErrors().get(thresID) != null) {
            getCountErrors().replace(thresID, 0);
        }
    }
}
