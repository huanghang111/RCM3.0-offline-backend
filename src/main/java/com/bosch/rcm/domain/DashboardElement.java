package com.bosch.rcm.domain;

public class DashboardElement {
    private long id;
    private String location;
    private String type;
    private String dataRef;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataRef() {
        return dataRef;
    }

    public void setDataRef(String dataRef) {
        this.dataRef = dataRef;
    }

}
