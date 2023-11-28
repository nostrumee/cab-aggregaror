package com.modsen.driverservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka")
public record KafkaProperties(
        String createRideTopic,
        String acceptRideTopic
) {
}
