package com.bosch.rcm.config;

import io.github.jhipster.config.JHipsterConstants;
import org.springframework.boot.SpringApplication;

import java.util.HashMap;
import java.util.Map;

public final class DefaultProfileUtil {
    private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";

    private DefaultProfileUtil() {
    }

    /**
     * Set a default to use when no profile is configured.
     *
     * @param app the Spring application.
     */
    public static void addDefaultProfile(SpringApplication app) {
        Map<String, Object> defProperties = new HashMap<>();
        /*
         * The default profile to use when no other profiles are defined
         * This cannot be set in the application.yml file.
         * See https://github.com/spring-projects/spring-boot/issues/1219
         */
        defProperties.put(SPRING_PROFILE_DEFAULT, JHipsterConstants.SPRING_PROFILE_DEVELOPMENT);
        app.setDefaultProperties(defProperties);
    }
}
