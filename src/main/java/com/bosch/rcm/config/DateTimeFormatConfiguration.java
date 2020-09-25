package com.bosch.rcm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
public class DateTimeFormatConfiguration implements WebMvcConfigurer {
    private final String[] baseApiPaths = new String[]{"/api"};

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        registrar.registerFormatters(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**/*")
            .addResourceLocations("classpath:/static/")
            .resourceChain(true)
            .addResolver(new PathResourceResolver() {
                @Override
                protected Resource getResource(String resourcePath, Resource location) throws IOException {
                    for (String baseApiPath : baseApiPaths) {
                        if (resourcePath.startsWith(baseApiPath) || resourcePath.startsWith(baseApiPath.substring(1))) {
                            return null;
                        }
                    }
                    Resource requestedResource = location.createRelative(resourcePath);
                    return requestedResource.exists() && requestedResource.isReadable() ? requestedResource : new ClassPathResource("/static" +
                        "/index.html");
                }
            });
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
