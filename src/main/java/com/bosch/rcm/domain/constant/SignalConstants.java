package com.bosch.rcm.domain.constant;

import org.springframework.stereotype.Component;

@Component
public final class SignalConstants {
    public static final String THRESHOLD_MIN_MIN = "Too_Low";
    public static final String THRESHOLD_MIN = "Low";
    public static final String THRESHOLD_MAX_MAX = "Too_High";
    public static final String THRESHOLD_MAX = "High";
    public static final String THRESHOLD_BOOL = "";
    public static final String THRESHOLD_VALUE_TRUE = "True";
    public static final String THRESHOLD_VALUE_FALSE = "False";
    public static final String PUSH_EMAIL = "EMAIL";
    public static final String PUSH_WECHAT = "WECHAT";
    public static final String PUSH_SMS = "SMS";
    public static final String SIGNAL_TYPE_NORMAL = "Normal";
    public static final String SIGNAL_TYPE_FAULT = "Fault";
    public static final String DATA_TYPE_INTENSIVE = "INTENSIVE";
    public static final String DATA_TYPE_INT16 = "INT16";
    public static final String DATA_TYPE_INT32 = "INT32";
    public static final String DATA_TYPE_FLOAT = "FLOAT";
    public static final String DATA_TYPE_BOOL = "BOOL";
    public static final String DATA_TYPE_CATALOG = "CATALOG";
    public static final String SUB_TYPE_INTENSIVE_INT16 = "INT16[]";
    public static final String SUB_TYPE_INTENSIVE_INT32 = "INT32[]";
    public static final String SUB_TYPE_INTENSIVE_FLOAT = "FLOAT[]";
    public static final String FAULT_SHEET = "Fault";
    public static final String TCP_SHEET = "TCP";
    public static final String MQTT_SHEET = "MQTT";
    public static final String RELATED_INTENSIVE = "intensive";
    public static final String RELATED_POINT = "point";
    public static final String RELATED_TIME_10S = "10";
    public static final String RELATED_TIME_1M = "60";
    public static final String RELATED_TIME_1H = "3600";
    public static final String RELATED_TIME_1D = "86400";
    public static final String RELATED_TIME_1W = "604800";
    public static final String EXPORT_TYPE_EXCEL = "excel";
    public static final String EXPORT_TYPE_JSON = "json";
    public static final String TRIGGER_METHOD_MANUAL = "MANUAL";
    public static final String TRIGGER_METHOD_AUTO = "AUTOMATIC";
    public static final String TRIGGER_INTERVAL_10_MIN = "10 min";
    public static final String TRIGGER_INTERVAL_1_HOUR = "1 hour";
    public static final String TRIGGER_INTERVAL_2_HOUR = "2 hour";
    public static final String TRIGGER_INTERVAL_8_HOUR = "8 hour";
    public static final String TRIGGER_INTERVAL_1_DAY = "1 day";
    public static final String MODE_CONT = "Continuous";
    public static final String MODE_TRIG = "Trigger";
    public static final String SIGNAL_SETTING = "DONE";
}
