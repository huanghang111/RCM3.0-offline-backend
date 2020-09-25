package com.bosch.rcm.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Document(collection = "diagnose-picture")
public class DiagnosePicture implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    private String name;

    @Field("content-bin")
    private String contentBin;

    @Field("index")
    private Integer index;

    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentBin() {
        return contentBin;
    }

    public void setContentBin(String contentBin) {
        this.contentBin = contentBin;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DiagnosePicture(String id, String name, String contentBin, Integer index, String type) {
        this.id = id;
        this.name = name;
        this.contentBin = contentBin;
        this.index = index;
        this.type = type;
    }
}
