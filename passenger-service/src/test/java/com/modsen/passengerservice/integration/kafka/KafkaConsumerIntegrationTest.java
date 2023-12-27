package com.modsen.passengerservice.integration.kafka;

import com.modsen.passengerservice.dto.message.PassengerRatingMessage;
import com.modsen.passengerservice.integration.IntegrationTestBase;
import com.modsen.passengerservice.repository.PassengerRepository;
import com.modsen.passengerservice.service.TestSender;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;

import java.time.Duration;

import static com.modsen.passengerservice.util.TestUtils.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@RequiredArgsConstructor
public class KafkaConsumerIntegrationTest extends IntegrationTestBase {

    private final PassengerRepository passengerRepository;
    private final TestSender testSender;

    @Test
    void updatePassengerRating_shouldUpdatePassengerRating_whenMessageConsumed() {
        var ratingMessage = PassengerRatingMessage.builder()
                .passengerId(DEFAULT_ID)
                .rating(NEW_RATING)
                .build();

        testSender.sendMessage(
                kafka.getBootstrapServers(),
                PASSENGER_RATING_TOPIC_NAME,
                ratingMessage
        );
        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    var passenger = passengerRepository.findById(DEFAULT_ID).get();
                    var actual = passenger.getRating();
                    assertThat(actual).isEqualTo(NEW_RATING);
                });
    }
}
