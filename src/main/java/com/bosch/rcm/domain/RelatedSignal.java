package com.bosch.rcm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Document(collection = "related-signal")
public class RelatedSignal implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 10)
    private String type;

    @NotNull
    @Size(max = 50)
    private String timing;

    @DBRef
    @JsonIgnore
    private Signal relatedSignal;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public Signal getRelatedSignal() {
        return relatedSignal;
    }

    public void setRelatedSignal(Signal relatedSignal) {
        this.relatedSignal = relatedSignal;
    }

    public String getRelatedSignalId() {
        return this.relatedSignal != null ? this.relatedSignal.getSignalId() : null;
    }
}
