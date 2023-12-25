package com.modsen.rideservice.integration.kafka;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.modsen.rideservice.dto.message.CreateRideMessage;
import com.modsen.rideservice.dto.message.DriverStatusMessage;
import com.modsen.rideservice.dto.message.RideStatusMessage;
import com.modsen.rideservice.entity.DriverStatus;
import com.modsen.rideservice.entity.RideStatus;
import com.modsen.rideservice.integration.IntegrationTestBase;
import com.modsen.rideservice.service.RideService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.modsen.rideservice.util.TestUtils.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@RequiredArgsConstructor
public class KafkaProducerIntegrationTest extends IntegrationTestBase {

    private static KafkaProducer<String, Object> producer;
    private static KafkaConsumer<String, Object> consumer;

    private final WireMockServer mockServer;
    private final RideService rideService;

    @BeforeAll
    static void beforeAll() {
        producer = getKafkaProducer(kafka.getBootstrapServers());
        consumer = getKafkaConsumer(kafka.getBootstrapServers());
        consumer.subscribe(List.of(
                RIDE_STATUS_TOPIC_NAME,
                DRIVER_STATUS_TOPIC_NAME,
                CREATE_RIDE_TOPIC_NAME
        ));
    }

    @BeforeEach
    void setUp() {
        var passenger = getPassengerResponse();
        mockServer.stubFor(get(GET_PASSENGER_BY_ID_PATH).willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBody(fromObjectToString(passenger)))
        );
    }

    @AfterEach
    void tearDown() {
        mockServer.resetAll();
    }

    @AfterAll
    static void afterAll() {
        producer.flush();
        producer.close();
        consumer.close();
    }

    @Test
    void createRide_shouldSendCreateRideMessageToCreateRideTopic() {
        var createRequest = getCreateRideRequest();
        rideService.createRide(createRequest);

        await()
                .atMost(10, SECONDS)
                .until(() -> {
                    ConsumerRecords<String, Object> records = consumer.poll(Duration.ofMillis(100));
                    if (records.isEmpty()) {
                        return false;
                    }

                    var actual = (CreateRideMessage) records.iterator().next().value();

                    assertThat(actual.rideId()).isNotNull();
                    return true;
                });
    }

    @Test
    void acceptRide_shouldSendAcceptedRideStatusMessageToRideStatusTopic() {
        var acceptRideMessage = getAcceptRideMessage(DEFAULT_ID);
        rideService.acceptRide(acceptRideMessage);

        await()
                .atMost(10, SECONDS)
                .until(() -> {
                    ConsumerRecords<String, Object> records = consumer.poll(Duration.ofMillis(100));
                    if (records.isEmpty()) {
                        return false;
                    }

                    var actual = (RideStatusMessage) records.iterator().next().value();

                    assertThat(actual.rideId()).isEqualTo(acceptRideMessage.rideId());
                    assertThat(actual.status()).isEqualTo(RideStatus.ACCEPTED);
                    return true;
                });
    }

    @Test
    void startRide_shouldSendStartedRideStatusMessageToRideStatusTopic() {
        rideService.startRide(ACCEPTED_RIDE_ID);

        await()
                .atMost(10, SECONDS)
                .until(() -> {
                    ConsumerRecords<String, Object> records = consumer.poll(Duration.ofMillis(100));
                    if (records.isEmpty()) {
                        return false;
                    }

                    var actual = (RideStatusMessage) records.iterator().next().value();

                    assertThat(actual.rideId()).isEqualTo(ACCEPTED_RIDE_ID);
                    assertThat(actual.status()).isEqualTo(RideStatus.STARTED);
                    return true;
                });
    }

    @Test
    void finishRide_shouldSendRideAndDriverStatusMessages() {
        rideService.finishRide(STARTED_RIDE_ID);

        await()
                .atMost(10, SECONDS)
                .until(() -> {
                    ConsumerRecords<String, Object> records = consumer.poll(Duration.ofMillis(100));
                    if (records.count() < 2) {
                        return false;
                    }

                    records.forEach(record -> {
                        switch (record.topic()) {
                            case RIDE_STATUS_TOPIC_NAME -> {
                                var actual = (RideStatusMessage) record.value();
                                assertThat(actual.rideId()).isEqualTo(STARTED_RIDE_ID);
                                assertThat(actual.status()).isEqualTo(RideStatus.FINISHED);
                            }
                            case DRIVER_STATUS_TOPIC_NAME -> {
                                var actual = (DriverStatusMessage) record.value();
                                assertThat(actual.status()).isEqualTo(DriverStatus.AVAILABLE);
                            }
                        }
                    });

                    return true;
                });
    }
}
