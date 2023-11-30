package com.modsen.passengerservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka")
public record KafkaProperties(
        String updatePassengerRatingTopicName
) {
}
