package com.modsen.passengerservice.integration.kafka;

import com.modsen.passengerservice.dto.message.PassengerRatingMessage;
import com.modsen.passengerservice.integration.TestcontainersBase;
import com.modsen.passengerservice.repository.PassengerRepository;
import com.modsen.passengerservice.service.TestSender;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.time.Duration;

import static com.modsen.passengerservice.util.TestUtils.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
public class KafkaConsumerIT extends TestcontainersBase {

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
