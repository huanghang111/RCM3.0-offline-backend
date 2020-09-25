package com.bosch.rcm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "diagnose-instance")
public class DiagnoseInstance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @NotNull
    @Field("cal_output")
    private Double calculationOutput;

    private String description;

    @DBRef
    private Set<DiagnoseStep> diagnoseSteps = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getCalculationOutput() {
        return calculationOutput;
    }

    public void setCalculationOutput(Double calculationOutput) {
        this.calculationOutput = calculationOutput;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<DiagnoseStep> getDiagnoseSteps() {
        return diagnoseSteps;
    }

    public void setDiagnoseSteps(Set<DiagnoseStep> diagnoseSteps) {
        this.diagnoseSteps = diagnoseSteps;
    }
}
