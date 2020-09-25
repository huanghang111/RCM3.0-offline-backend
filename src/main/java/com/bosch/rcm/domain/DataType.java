package com.bosch.rcm.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@org.springframework.data.mongodb.core.mapping.Document(collection = "dataType")
public class DataType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Size(max = 50)
    private String name;

    public DataType() {

    }

    public DataType(@NotNull @Size(max = 50) String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
