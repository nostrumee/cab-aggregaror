package com.modsen.notificationservice.util;

import com.modsen.notificationservice.dto.RideStatus;
import com.modsen.notificationservice.dto.message.RideStatusMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.math.BigDecimal;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {
    public static final long ID = 1L;
    public static final String START_POINT = "Start point";
    public static final String DESTINATION_POINT = "Destination point";
    public static final String EMAIL = "nostrumee@gmail.com";
    public static final String FIRST_NAME = "John";
    public static final BigDecimal ESTIMATED_COST = new BigDecimal("6.3");

    public static final String KAFKA_IMAGE_NAME = "confluentinc/cp-kafka:7.3.3";
    public static final String RIDE_STATUS_TOPIC_NAME = "ride-status-topic";

    public static KafkaProducer<String, Object> getKafkaProducer(String bootstrapServers) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaProducer<>(properties);
    }

    public static RideStatusMessage getRideStatusMessage() {
        return RideStatusMessage.builder()
                .rideId(ID)
                .status(RideStatus.FINISHED)
                .startPoint(START_POINT)
                .destinationPoint(DESTINATION_POINT)
                .passengerEmail(EMAIL)
                .passengerFirstName(FIRST_NAME)
                .estimatedCost(ESTIMATED_COST)
                .build();
    }
}
