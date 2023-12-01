package com.modsen.driverservice.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicsConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public NewTopic createRideTopic() {
        return TopicBuilder.name(kafkaProperties.createRideTopicName())
                .build();
    }

    @Bean
    public NewTopic acceptRideTopic() {
        return TopicBuilder.name(kafkaProperties.acceptRideTopicName())
                .build();
    }

    @Bean
    public NewTopic driverRatingTopic() {
        return TopicBuilder.name(kafkaProperties.driverRatingTopicName())
                .build();
    }

    @Bean
    public NewTopic driverStatusTopic() {
        return TopicBuilder.name(kafkaProperties.driverStatusTopicName())
                .build();
    }
}
