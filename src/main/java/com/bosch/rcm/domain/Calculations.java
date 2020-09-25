package com.bosch.rcm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@org.springframework.data.mongodb.core.mapping.Document(collection = "calculations")
public class Calculations implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(max = 100)
    private String newSignalName;

    @DBRef
    @JsonIgnore
    private Signal signalOne;

    private Method methodTwo;

    @DBRef
    @JsonIgnore
    private Signal signalTwo;

    private Method methodOne;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNewSignalName() {
        return newSignalName;
    }

    public void setNewSignalName(String newSignalName) {
        this.newSignalName = newSignalName;
    }

    public Signal getSignalOne() {
        return signalOne;
    }

    public void setSignalOne(Signal signalOne) {
        this.signalOne = signalOne;
    }

    public Method getMethodTwo() {
        return methodTwo;
    }

    public void setMethodTwo(Method methodTwo) {
        this.methodTwo = methodTwo;
    }

    public Signal getSignalTwo() {
        return signalTwo;
    }

    public void setSignalTwo(Signal signalTwo) {
        this.signalTwo = signalTwo;
    }

    public Method getMethodOne() {
        return methodOne;
    }

    public void setMethodOne(Method methodOne) {
        this.methodOne = methodOne;
    }
}
