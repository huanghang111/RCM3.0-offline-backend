package com.bosch.rcm.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Document(collection = "links")
public class Links implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 50)
    private String values;

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public Links(@NotNull @Size(max = 50) String values) {
        this.values = values;
    }
}
