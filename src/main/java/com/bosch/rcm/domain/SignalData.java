package com.bosch.rcm.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

@Document(collection = "signaldata")
public class SignalData implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(max = 50)
    @Indexed(name = "indexed_name", direction = IndexDirection.DESCENDING)
    private String name;

    @NotNull
    private Object values;

    @NotNull
    @Indexed(name = "indexed_time", direction = IndexDirection.DESCENDING)
    private Instant timestamp;

    //@Indexed(name = "indexed_order", direction = IndexDirection.DESCENDING)
    private long order;

    public String getId() {
        return id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOrder() {
        return order;
    }

    public void setOrder(long order) {
        this.order = order;
    }
}
