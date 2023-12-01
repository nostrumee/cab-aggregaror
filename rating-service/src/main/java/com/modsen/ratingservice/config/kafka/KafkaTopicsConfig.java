package com.modsen.ratingservice.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {

    @Bean
    public NewTopic passengerRatingTopic(KafkaProperties kafkaProperties) {
        return TopicBuilder.name(kafkaProperties.passengerRatingTopicName())
                .build();
    }

    @Bean
    public NewTopic driverRatingTopic(KafkaProperties kafkaProperties) {
        return TopicBuilder.name(kafkaProperties.driverRatingTopicName())
                .build();
    }
}
