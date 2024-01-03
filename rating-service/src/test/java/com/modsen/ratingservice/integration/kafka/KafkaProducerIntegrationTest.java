package com.modsen.ratingservice.integration.kafka;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.modsen.ratingservice.dto.message.DriverRatingMessage;
import com.modsen.ratingservice.dto.message.PassengerRatingMessage;
import com.modsen.ratingservice.entity.RideStatus;
import com.modsen.ratingservice.integration.IntegrationTestBase;
import com.modsen.ratingservice.repository.DriverRatingRepository;
import com.modsen.ratingservice.repository.PassengerRatingRepository;
import com.modsen.ratingservice.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;

import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.modsen.ratingservice.util.TestUtils.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@RequiredArgsConstructor
public class KafkaProducerIntegrationTest extends IntegrationTestBase {

    private static KafkaConsumer<String, Object> consumer;

    private final WireMockServer mockServer;
    private final RatingService ratingService;
    private final DriverRatingRepository driverRatingRepository;
    private final PassengerRatingRepository passengerRatingRepository;

    @BeforeAll
    static void beforeAll() {
        consumer = getKafkaConsumer(kafka.getBootstrapServers());
        consumer.subscribe(List.of(
                DRIVER_RATING_TOPIC_NAME,
                PASSENGER_RATING_TOPIC_NAME
        ));
    }

    @BeforeEach
    void setUp() {
        var rideResponse = getRideResponse(RideStatus.FINISHED);
        mockServer.stubFor(get(GET_RIDE_BY_ID_PATH).willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBody(fromObjectToString(rideResponse)))
        );
    }

    @AfterEach
    void tearDown() {
        mockServer.resetAll();
    }

    @Test
    void rateDriver_shouldSendDriverRatingMessageToDriverRatingTopic() {
        var ratingRequest = getDriverRatingRequest();
        ratingService.rateDriver(ratingRequest);

        var averageRating = driverRatingRepository.findDriverRating(DEFAULT_ID);
        var expected = DriverRatingMessage.builder()
                .driverId(DEFAULT_ID)
                .rating(averageRating.setScale(2, RoundingMode.HALF_UP).doubleValue())
                .build();

        await()
                .atMost(10, SECONDS)
                .until(() -> {
                    ConsumerRecords<String, Object> records = consumer.poll(Duration.ofMillis(100));
                    if (records.isEmpty()) {
                        return false;
                    }

                    var actual = (DriverRatingMessage) records.iterator().next().value();

                    assertThat(actual).isEqualTo(expected);
                    return true;
                });
    }

    @Test
    void ratePassenger_shouldSendPassengerRatingMessageToPassengerRatingTopic() {
        var ratingRequest = getPassengerRatingRequest();
        ratingService.ratePassenger(ratingRequest);

        var averageRating = passengerRatingRepository.findPassengerRating(DEFAULT_ID);
        var expected = PassengerRatingMessage.builder()
                .passengerId(DEFAULT_ID)
                .rating(averageRating.setScale(2, RoundingMode.HALF_UP).doubleValue())
                .build();

        await()
                .atMost(10, SECONDS)
                .until(() -> {
                    ConsumerRecords<String, Object> records = consumer.poll(Duration.ofMillis(100));
                    if (records.isEmpty()) {
                        return false;
                    }

                    var actual = (PassengerRatingMessage) records.iterator().next().value();

                    assertThat(actual).isEqualTo(expected);
                    return true;
                });
    }

}
