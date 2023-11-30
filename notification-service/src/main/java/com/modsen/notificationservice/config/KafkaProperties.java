package com.modsen.notificationservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka")
public record KafkaProperties(
        String groupId,
        String bootstrapServers,
        String rideStatusTopicName
) {
}
