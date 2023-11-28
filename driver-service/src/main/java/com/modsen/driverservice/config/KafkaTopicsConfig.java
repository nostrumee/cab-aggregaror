package com.modsen.driverservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {

    @Bean
    public NewTopic createRideTopic(KafkaProperties kafkaProperties) {
        return TopicBuilder.name(kafkaProperties.createRideTopic())
                .build();
    }

    @Bean
    public NewTopic acceptRideTopic(KafkaProperties kafkaProperties) {
        return TopicBuilder.name(kafkaProperties.acceptRideTopic())
                .build();
    }
}
