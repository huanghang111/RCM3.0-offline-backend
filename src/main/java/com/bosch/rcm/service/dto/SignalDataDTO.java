package com.bosch.rcm.service.dto;

import com.bosch.rcm.domain.SignalData;

public class SignalDataDTO {
    private String id;
    private SignalData lastOccurrence;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SignalData getLastOccurrence() {
        return lastOccurrence;
    }

    public void setLastOccurrence(SignalData lastOccurrence) {
        this.lastOccurrence = lastOccurrence;
    }
}
