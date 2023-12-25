package com.modsen.driverservice.integration.kafka;

import com.modsen.driverservice.dto.message.AcceptRideMessage;
import com.modsen.driverservice.dto.message.CreateRideMessage;
import com.modsen.driverservice.entity.DriverStatus;
import com.modsen.driverservice.integration.IntegrationTestBase;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.util.PropertiesUtil;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;

import static com.modsen.driverservice.util.TestUtils.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


@RequiredArgsConstructor
public class RideFunctionsIntegrationTest extends IntegrationTestBase {

    private static KafkaProducer<String, Object> producer;
    private static KafkaConsumer<String, Object> consumer;

    private final DriverRepository driverRepository;

    @BeforeAll
    static void beforeAll() {
        producer = getKafkaProducer(kafka.getBootstrapServers());
        consumer = getKafkaConsumer(kafka.getBootstrapServers());
        consumer.subscribe(Collections.singletonList(
                PropertiesUtil.get(ACCEPT_RIDE_TOPIC_NAME_KEY)
        ));
    }

    @AfterAll
    static void afterAll() {
        producer.flush();
        producer.close();
        consumer.close();
    }

    @Test
    void acceptRideOrder_shouldAssignDriverToRide_whenMessageConsumed() {
        String createRideTopicName = PropertiesUtil.get(CREATE_RIDE_TOPIC_NAME_KEY);

        var createRideMessage = new CreateRideMessage(DEFAULT_ID);
        ProducerRecord<String, Object> record = new ProducerRecord<>(
                createRideTopicName,
                createRideMessage
        );
        producer.send(record);

        await()
                .atMost(10, SECONDS)
                .until(() -> {
                    ConsumerRecords<String, Object> records = consumer.poll(Duration.ofMillis(100));

                    if (records.isEmpty()) {
                        return false;
                    }

                    var acceptRideMessage = (AcceptRideMessage) records.iterator().next().value();
                    var driver = driverRepository.findById(acceptRideMessage.driverId()).get();

                    assertThat(driver.getStatus()).isEqualTo(DriverStatus.UNAVAILABLE);
                    assertThat(acceptRideMessage.rideId()).isEqualTo(createRideMessage.rideId());

                    return true;
                });
    }
}
