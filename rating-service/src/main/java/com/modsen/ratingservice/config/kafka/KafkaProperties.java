package com.modsen.ratingservice.config.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka")
public record KafkaProperties(
        String bootstrapServers,
        String updatePassengerRatingTopicName,
        String updateDriverRatingTopicName
) {
}
