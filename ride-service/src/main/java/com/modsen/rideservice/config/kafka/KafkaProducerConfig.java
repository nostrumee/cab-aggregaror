package com.modsen.rideservice.config.kafka;

import com.modsen.rideservice.dto.message.RideStatusMessage;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.messaging.MessageChannel;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public IntegrationFlow sendToCreateRideTopicFlow() {
        return f -> f.channel(createRideChannel())
                .handle(Kafka.outboundChannelAdapter(kafkaTemplate())
                        .messageKey(m -> m.getHeaders().get(IntegrationMessageHeaderAccessor.SEQUENCE_NUMBER))
                        .topic(kafkaProperties.createRideTopicName()));
    }

    @Bean
    public IntegrationFlow sendToRideStatusTopicFlow() {
        return f -> f.channel(rideStatusChannel())
                .handle(Kafka.outboundChannelAdapter(kafkaTemplate())
                        .messageKey(m -> m.getHeaders().get(IntegrationMessageHeaderAccessor.SEQUENCE_NUMBER))
                        .topic(kafkaProperties.rideStatusTopicName()));
    }

    @Bean
    public IntegrationFlow sendToDriverStatusTopicFlow() {
        return f -> f.channel(driverStatusChannel())
                .handle(Kafka.outboundChannelAdapter(kafkaTemplate())
                        .messageKey(m -> m.getHeaders().get(IntegrationMessageHeaderAccessor.SEQUENCE_NUMBER))
                        .topic(kafkaProperties.driverStatusTopicName()));
    }

    @Bean
    public MessageChannel createRideChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel rideStatusChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel driverStatusChannel() {
        return new DirectChannel();
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaConfigs());
    }

    @Bean
    public Map<String, Object> kafkaConfigs() {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.bootstrapServers(),
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
                JsonSerializer.TYPE_MAPPINGS, "rideStatusMessage:" + RideStatusMessage.class.getName()
        );
    }
}
