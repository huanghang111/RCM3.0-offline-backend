package com.bosch.rcm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VersionsConfiguration {

    @Value("${version.number}")
    private String version;

    public String getVersion() {
        return version;
    }
}
