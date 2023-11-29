package com.modsen.passengerservice.config;

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
}
