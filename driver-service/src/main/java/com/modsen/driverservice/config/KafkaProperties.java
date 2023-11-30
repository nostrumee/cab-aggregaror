package com.modsen.driverservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka")
public record KafkaProperties(
        String createRideTopicName,
        String acceptRideTopicName,
        String updateDriverRatingTopicName
) {
}
