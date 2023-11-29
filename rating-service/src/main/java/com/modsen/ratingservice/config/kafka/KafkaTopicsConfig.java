package com.modsen.ratingservice.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {

    @Bean
    public NewTopic updatePassengerRatingTopic(KafkaProperties kafkaProperties) {
        return TopicBuilder.name(kafkaProperties.updatePassengerRatingTopic())
                .build();
    }

    @Bean
    public NewTopic updateDriverRatingTopic(KafkaProperties kafkaProperties) {
        return TopicBuilder.name(kafkaProperties.updateDriverRatingTopic())
                .build();
    }
}
