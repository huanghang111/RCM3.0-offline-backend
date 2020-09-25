package com.bosch.rcm.service.dto;

import com.bosch.rcm.domain.Calculations;
import com.bosch.rcm.domain.SignalData;

public class SignalCalculatedDTO {
    private String sigElementName;
    private Calculations calculations;
    private SignalData sigElementData;

    public SignalCalculatedDTO(String sigElementName, Calculations calculations) {
        this.sigElementName = sigElementName;
        this.calculations = calculations;
    }

    public String getSigElementName() {
        return sigElementName;
    }

    public void setSigElementName(String sigElementName) {
        this.sigElementName = sigElementName;
    }

    public Calculations getCalculations() {
        return calculations;
    }

    public void setCalculations(Calculations calculations) {
        this.calculations = calculations;
    }

    public SignalData getSigElementData() {
        return sigElementData;
    }

    public void setSigElementData(SignalData sigElementData) {
        this.sigElementData = sigElementData;
    }
}
