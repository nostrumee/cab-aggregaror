package com.modsen.rideservice.config.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka")
public record KafkaProperties(
        String group,
        String bootstrapServers,
        String createRideTopic,
        String acceptRideTopic
) {
}
