package com.modsen.notificationservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

@ConfigurationProperties(prefix = "spring.mail")
public record MailProperties(
        String host,
        int port,
        String username,
        String password,
        Properties properties
) {
}
