package com.bosch.rcm.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "threshold")
public class Threshold implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(max = 50)
    private String values;

    @NotNull
    @Size(max = 20)
    private String level;


    @NotNull
    @Size(max = 100)
    private String errorMessages;

    //@DBRef
    private Set<RelatedSignal> relatedSignals = new HashSet<>();

    public Threshold(@NotNull @Size(max = 50) String values, @NotNull @Size(max = 20) String level, @NotNull @Size(max = 100) String errorMessages) {
        this.values = values;
        this.level = level;
        this.errorMessages = errorMessages;
    }

    public Threshold() {
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(String errorMessages) {
        this.errorMessages = errorMessages;
    }

    public Set<RelatedSignal> getRelatedSignals() {
        return relatedSignals;
    }

    public void setRelatedSignals(Set<RelatedSignal> relatedSignals) {
        this.relatedSignals = relatedSignals;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
