package com.bosch.rcm.domain.dto;

import com.bosch.rcm.domain.Threshold;

import java.time.Instant;

public class ErrorDataDTO {
    private String id;
    private String name;
    private Object values;
    private Instant timestamp;
    private boolean ack;
    private Threshold threshold;
    private Integer digit;
    private String dataType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValues() {
        return values;
    }

    public void setValues(Object values) {
        this.values = values;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isAck() {
        return ack;
    }

    public void setAck(boolean ack) {
        this.ack = ack;
    }

    public Threshold getThreshold() {
        return threshold;
    }

    public void setThreshold(Threshold threshold) {
        this.threshold = threshold;
    }

    public Integer getDigit() {
        return digit;
    }

    public void setDigit(Integer digit) {
        this.digit = digit;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
