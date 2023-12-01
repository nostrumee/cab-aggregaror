package com.modsen.notificationservice.config.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka")
public record KafkaProperties(
        String groupId,
        String bootstrapServers,
        String rideStatusTopicName
) {
}
