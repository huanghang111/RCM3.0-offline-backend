package com.bosch.rcm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Document(collection = "diagnose-step")
public class DiagnoseStep implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @Field("desc")
    private String description;

    @Field("index")
    private Integer index;

    private LinkedHashSet<DiagnosePicture> diagnosePictures = new LinkedHashSet<>();

    @DBRef
    @JsonIgnore
    private DiagnoseInstance diagnoseInstance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public LinkedHashSet<DiagnosePicture> getDiagnosePictures() {
        return diagnosePictures;
    }

    public void setDiagnosePictures(LinkedHashSet<DiagnosePicture> diagnosePictures) {
        this.diagnosePictures = diagnosePictures;
    }

    public DiagnoseInstance getDiagnoseInstance() {
        return diagnoseInstance;
    }

    public void setDiagnoseInstance(DiagnoseInstance diagnoseInstance) {
        this.diagnoseInstance = diagnoseInstance;
    }
}
