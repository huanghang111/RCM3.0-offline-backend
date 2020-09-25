package com.bosch.rcm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "signal")
public class Signal extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @NotNull
    @Size(max = 30)
    private String signalId;

    @NotNull
    @Size(max = 100)
    private String name;

    private String descriptions;

    private DataType dataType;

    @Size(max = 20)
    private String unit;

    @Size(max = 50)
    private String digit;

    private String images;

    private SignalPush signalPush;

    private String signalType;

    @JsonIgnore
    private Set<Documents> documents = new HashSet<>();

    @DBRef
    private Calculations calculations;

    @JsonIgnore
    private Set<Links> links = new HashSet<>();

    private String tag;

    @DBRef
    private Set<Threshold> thresholds = new HashSet<>();

    @JsonIgnore
    private Set<RelatedSignal> relatedSignals = new HashSet<>();

    @DBRef
    @JsonIgnore
    private Set<SignalData> signalData = new HashSet<>();

    //For Soft delete, if no init default value, mongoDB will not import this field to document
    private Boolean isDeleted = false;

    //For Intensive
    @Size(max = 50)
    private String triggerType;
    private Integer triggerHour;
    private Integer triggerMinute;
    private String triggerInterval;
    private String triggerTime;

    //For MQ signals
    private String brokerIp;
    private Integer port;
    private Boolean useSSL;
    private String username;
    private String password;
    private String topic;

    //For Boolean signal
    private String displayTrue;
    private String displayFalse;

    //For Fault signal
    @DBRef
    private Set<DiagnoseInstance> diagnoseInstances;

    @DBRef
    private Project project;

    //Change request Jan 2020
    private String subDataType;
    private Integer plcId;
    private Integer plcOrder;
    private Integer cmd;
    private String mode;
    private String interval;
    private Double dataSize;
    private Double dataLength;
    private Double totalLength;


    public Signal() {
    }

    //region GETTER AND SETTER
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSignalId() {
        return signalId;
    }

    public void setSignalId(String signalId) {
        this.signalId = signalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDigit() {
        return digit;
    }

    public void setDigit(String digit) {
        this.digit = digit;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public Integer getTriggerHour() {
        return triggerHour;
    }

    public void setTriggerHour(Integer triggerHour) {
        this.triggerHour = triggerHour;
    }

    public Integer getTriggerMinute() {
        return triggerMinute;
    }

    public void setTriggerMinute(Integer triggerMinute) {
        this.triggerMinute = triggerMinute;
    }

    public String getTriggerInterval() {
        return triggerInterval;
    }

    public void setTriggerInterval(String triggerInterval) {
        this.triggerInterval = triggerInterval;
    }

    public String getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(String triggerTime) {
        this.triggerTime = triggerTime;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Set<Documents> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Documents> documents) {
        this.documents = documents;
    }

    public Set<Links> getLinks() {
        return links;
    }

    public void setLinks(Set<Links> links) {
        this.links = links;
    }

    public Set<Threshold> getThresholds() {
        return thresholds;
    }

    public void setThresholds(Set<Threshold> thresholds) {
        this.thresholds = thresholds;
    }

    public Set<RelatedSignal> getRelatedSignals() {
        return relatedSignals;
    }

    public void setRelatedSignals(Set<RelatedSignal> relatedSignals) {
        this.relatedSignals = relatedSignals;
    }

    public SignalPush getSignalPush() {
        return signalPush;
    }

    public void setSignalPush(SignalPush signalPush) {
        this.signalPush = signalPush;
    }

    public String getSignalType() {
        return signalType;
    }

    public void setSignalType(String signalType) {
        this.signalType = signalType;
    }

    public String getBrokerIp() {
        return brokerIp;
    }

    public void setBrokerIp(String brokerIp) {
        this.brokerIp = brokerIp;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean getUseSSL() {
        return useSSL;
    }

    public void setUseSSL(Boolean useSSL) {
        this.useSSL = useSSL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDisplayTrue() {
        return displayTrue;
    }

    public void setDisplayTrue(String displayTrue) {
        this.displayTrue = displayTrue;
    }

    public String getDisplayFalse() {
        return displayFalse;
    }

    public void setDisplayFalse(String displayFalse) {
        this.displayFalse = displayFalse;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Calculations getCalculations() {
        return calculations;
    }

    public void setCalculations(Calculations calculations) {
        this.calculations = calculations;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Set<SignalData> getSignalData() {
        return signalData;
    }

    public void setSignalData(Set<SignalData> signalData) {
        this.signalData = signalData;
    }

    public Set<DiagnoseInstance> getDiagnoseInstances() {
        return diagnoseInstances;
    }

    public void setDiagnoseInstances(Set<DiagnoseInstance> diagnoseInstances) {
        this.diagnoseInstances = diagnoseInstances;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getPlcId() {
        return plcId;
    }

    public void setPlcId(Integer plcId) {
        this.plcId = plcId;
    }

    public Integer getCmd() {
        return cmd;
    }

    public void setCmd(Integer cmd) {
        this.cmd = cmd;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public Double getDataSize() {
        return dataSize;
    }

    public void setDataSize(Double dataSize) {
        this.dataSize = dataSize;
    }

    public Double getDataLength() {
        return dataLength;
    }

    public void setDataLength(Double dataLength) {
        this.dataLength = dataLength;
    }

    public Double getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(Double totalLength) {
        this.totalLength = totalLength;
    }

    public String getSubDataType() {
        return subDataType;
    }

    public void setSubDataType(String subDataType) {
        this.subDataType = subDataType;
    }

    public Integer getPlcOrder() {
        return plcOrder;
    }

    public void setPlcOrder(Integer plcOrder) {
        this.plcOrder = plcOrder;
    }

    //endregion
}
