package com.bosch.rcm.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "diagnose")
public class Diagnose implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    private Calculations calculations;

    @DBRef
    private Signal outputSignal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Calculations getCalculations() {
        return calculations;
    }

    public void setCalculations(Calculations calculations) {
        this.calculations = calculations;
    }

    public Signal getOutputSignal() {
        return outputSignal;
    }

    public void setOutputSignal(Signal outputSignal) {
        this.outputSignal = outputSignal;
    }


}
