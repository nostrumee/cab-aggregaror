package com.modsen.notificationservice.config.kafka;

import com.modsen.notificationservice.service.RideStatusMessageToEmailTransformer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.Map;

import static com.modsen.notificationservice.util.KafkaTypeMappings.*;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public IntegrationFlow consumeFromKafka(
            ConsumerFactory<String, String> consumerFactory,
            RideStatusMessageToEmailTransformer transformer,
            JavaMailSender mailSender,
            ChannelInterceptor channelInterceptor
    ) {
        return IntegrationFlow.from(Kafka.messageDrivenChannelAdapter(consumerFactory, kafkaProperties.rideStatusTopicName()))
                .intercept(channelInterceptor)
                .transform(transformer)
                .handle(Mail.outboundAdapter(mailSender))
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
                JsonDeserializer.TRUSTED_PACKAGES, "*",
                ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.groupId(),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                JsonDeserializer.TYPE_MAPPINGS, JSON_DESERIALIZER_TYPE_MAPPING
        );
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setObservationEnabled(true);
        return factory;
    }
}
