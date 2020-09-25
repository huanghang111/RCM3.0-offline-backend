package com.bosch.rcm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SignalPush {

    WECHAT("wechat", "WeChat"),
    EMAIL("email", "Email"),
    SMS("sms", "SMS");

    private String key;
    private String value;

    SignalPush(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
