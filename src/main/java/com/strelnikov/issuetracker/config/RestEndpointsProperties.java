package com.strelnikov.issuetracker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "endpoints")
public record RestEndpointsProperties(String privateEndpoints, String publicEndpoints) {
}
