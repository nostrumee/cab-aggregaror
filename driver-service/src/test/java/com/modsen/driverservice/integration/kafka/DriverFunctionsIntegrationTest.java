package com.modsen.driverservice.integration.kafka;

import com.modsen.driverservice.dto.message.DriverRatingMessage;
import com.modsen.driverservice.dto.message.DriverStatusMessage;
import com.modsen.driverservice.entity.DriverStatus;
import com.modsen.driverservice.integration.IntegrationTestBase;
import com.modsen.driverservice.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.modsen.driverservice.util.TestUtils.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


@RequiredArgsConstructor
public class DriverFunctionsIntegrationTest extends IntegrationTestBase {

    private static KafkaProducer<String, Object> producer;
    private final DriverRepository driverRepository;

    @BeforeAll
    static void beforeAll() {
        producer = getKafkaProducer(kafka.getBootstrapServers());
    }

    @AfterAll
    static void afterAll() {
        producer.flush();
        producer.close();
    }

    @Test
    void updateDriverRating_shouldUpdateDriverRating_whenMessageConsumed() {
        var ratingMessage = DriverRatingMessage.builder()
                .driverId(DEFAULT_ID)
                .rating(OTHER_RATING)
                .build();
        ProducerRecord<String, Object> record = new ProducerRecord<>(
                DRIVER_RATING_TOPIC_NAME,
                ratingMessage
        );
        producer.send(record);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    var driver = driverRepository.findById(ratingMessage.driverId()).get();
                    var actual = driver.getRating();
                    assertThat(actual).isEqualTo(ratingMessage.rating());
                });
    }

    @Test
    void updateDriverStatus_shouldUpdateDriverStatus_whenMessageConsumed() {
        var statusMessage = DriverStatusMessage.builder()
                .driverId(DEFAULT_ID)
                .status(DriverStatus.UNAVAILABLE)
                .build();
        ProducerRecord<String, Object> record = new ProducerRecord<>(
                DRIVER_STATUS_TOPIC_NAME,
                statusMessage
        );
        producer.send(record);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    var driver = driverRepository.findById(statusMessage.driverId()).get();
                    var actual = driver.getStatus();
                    assertThat(actual).isEqualTo(statusMessage.status());
                });
    }
}
