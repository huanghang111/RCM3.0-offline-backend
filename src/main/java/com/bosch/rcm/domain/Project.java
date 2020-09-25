package com.bosch.rcm.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Size(max = 50)
    private String projectId;

    @NotNull
    @Size(max = 100)
    private String name;

    private Instant time;

    private String image;

    private String address;

    @DBRef
    private Set<Dashboard> dashboards = new HashSet<>();

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Dashboard> getDashboards() {
        return dashboards;
    }

    public void setDashboards(Set<Dashboard> dashboards) {
        this.dashboards = dashboards;
    }
}
