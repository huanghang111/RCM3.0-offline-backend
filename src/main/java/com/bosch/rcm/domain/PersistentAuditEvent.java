package com.bosch.rcm.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "persistent_audit_event")
public class PersistentAuditEvent extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Field("event_id")
    private String id;
    @NotNull
    private String principal;
    @Field("event_date")
    private Instant auditEventDate;
    @Field("event_type")
    private String auditEventType;
    private Map<String, String> data = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public Instant getAuditEventDate() {
        return auditEventDate;
    }

    public void setAuditEventDate(Instant auditEventDate) {
        this.auditEventDate = auditEventDate;
    }

    public String getAuditEventType() {
        return auditEventType;
    }

    public void setAuditEventType(String auditEventType) {
        this.auditEventType = auditEventType;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersistentAuditEvent)) {
            return false;
        }
        return id != null && id.equals(((PersistentAuditEvent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PersistentAuditEvent{" +
            "principal='" + principal + '\'' +
            ", auditEventDate=" + auditEventDate +
            ", auditEventType='" + auditEventType + '\'' +
            '}';
    }
}
