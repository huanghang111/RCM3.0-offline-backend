package com.bosch.rcm.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Document(collection = "method")
public class Method implements Serializable {
    private static final long serialVersionUID = 1L;

    @Size(max = 50)
    @Id
    private String name;

    @Size(max = 50)
    private String value;

    @Size(max = 50)
    private String type;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Method)) {
            return false;
        }
        return Objects.equals(name, ((Method) o).name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Method{" +
            "name='" + name + '\'' +
            "value='" + value + '\'' +
            "type='" + type + '\'' +
            "}";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
