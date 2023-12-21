package com.modsen.rideservice.integration.kafka;

import com.modsen.rideservice.config.kafka.KafkaProperties;
import com.modsen.rideservice.entity.RideStatus;
import com.modsen.rideservice.integration.IntegrationTestBase;
import com.modsen.rideservice.repository.RideRepository;
import com.modsen.rideservice.service.PassengerService;
import com.modsen.rideservice.service.SendMessageHandler;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;

import static com.modsen.rideservice.util.TestUtils.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


@RequiredArgsConstructor
public class KafkaConsumerIntegrationTest extends IntegrationTestBase {

    private static KafkaProducer<String, Object> producer;
    private final RideRepository rideRepository;
    private final KafkaProperties kafkaProperties;

    @MockBean
    private SendMessageHandler sendMessageHandler;

    @MockBean
    private PassengerService passengerService;

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
    void acceptRide_shouldBeCalled_whenMessageConsumed() {
        var acceptRideMessage = getAcceptRideMessage(DEFAULT_ID);

        ProducerRecord<String, Object> record = new ProducerRecord<>(
                ACCEPT_RIDE_TOPIC_NAME,
                acceptRideMessage
        );
        producer.send(record);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    var ride = rideRepository.findById(CREATED_RIDE_ID).get();
                    assertThat(ride.getStatus()).isEqualTo(RideStatus.ACCEPTED);
                    assertThat(ride.getDriverId()).isEqualTo(acceptRideMessage.driverId());
                    assertThat(ride.getAcceptedDate()).isNotNull();
                });
    }
}
