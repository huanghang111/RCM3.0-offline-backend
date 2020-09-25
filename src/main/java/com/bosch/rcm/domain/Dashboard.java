package com.bosch.rcm.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;


@Document(collection = "dashboard")
public class Dashboard extends AbstractAuditingEntity implements Serializable {

    @Id
    @NotNull
    private long id;

    @NotNull
    @Size(min = 1, max = 256)
    private String name;

    private Integer slideIndex;

    private Integer delayTime;

    private boolean addSlide;

    private List<DashboardElement> elements;

    private String image;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSlideIndex() {
        return slideIndex;
    }

    public void setSlideIndex(Integer slideIndex) {
        this.slideIndex = slideIndex;
    }

    public Integer getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
    }

    public boolean isAddSlide() {
        return addSlide;
    }

    public void setAddSlide(boolean addSlide) {
        this.addSlide = addSlide;
    }

    public List<DashboardElement> getElements() {
        return elements;
    }

    public void setElements(List<DashboardElement> elements) {
        this.elements = elements;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
