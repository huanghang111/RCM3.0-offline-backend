package com.bosch.rcm.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

@Document(collection = "cacheData")
public class CacheData extends AbstractAuditingEntity {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    private HashMap<String, Integer> countErrors = new HashMap<>();

    private HashMap<String, SignalError> firstErrors = new HashMap<>();

    private HashMap<String, Integer> countDataForPieChart = new HashMap<>();

    public CacheData() {
    }

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
}
