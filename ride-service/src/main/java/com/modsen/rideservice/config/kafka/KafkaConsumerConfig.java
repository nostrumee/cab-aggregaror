package com.modsen.rideservice.config.kafka;

import com.modsen.rideservice.service.ReceiveMessageHandler;
import com.modsen.rideservice.service.RideService;
import com.modsen.rideservice.service.impl.ReceiveMessageHandlerImpl;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.Map;

import static com.modsen.rideservice.util.IntegrationProperties.*;
import static com.modsen.rideservice.util.KafkaTypeMappings.*;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ReceiveMessageHandler receiveMessageHandler(RideService rideService) {
        return new ReceiveMessageHandlerImpl(rideService);
    }

    @Bean
    public IntegrationFlow consumeFromKafka(
            ConsumerFactory<String, String> consumerFactory,
            RideService rideService,
            ChannelInterceptor observationPropagationChannelInterceptor
    ) {
        return IntegrationFlow.from(Kafka.messageDrivenChannelAdapter(consumerFactory, kafkaProperties.acceptRideTopicName()))
                .intercept(observationPropagationChannelInterceptor)
                .handle(receiveMessageHandler(rideService), HANDLE_ACCEPT_RIDE_METHOD_NAME)
                .get();
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.bootstrapServers(),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.groupId(),
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                JsonDeserializer.TYPE_MAPPINGS, JSON_DESERIALIZER_TYPE_MAPPINGS,
                JsonDeserializer.TRUSTED_PACKAGES, "*"
        );
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.getContainerProperties().setObservationEnabled(true);
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
