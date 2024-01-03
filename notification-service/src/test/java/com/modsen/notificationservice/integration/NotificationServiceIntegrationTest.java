package com.modsen.notificationservice.integration;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.modsen.notificationservice.util.MailParams.SUBJECT;
import static com.modsen.notificationservice.util.TestUtils.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class NotificationServiceIntegrationTest extends IntegrationTestBase {

    private static KafkaProducer<String, Object> producer;

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
    void emailShouldBeSent_whenRideStatusMessageConsumed() {
        var rideStatusMessage = getRideStatusMessage();
        ProducerRecord<String, Object> record = new ProducerRecord<>(
                RIDE_STATUS_TOPIC_NAME,
                rideStatusMessage
        );
        producer.send(record);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    assertThat(greenMail.getReceivedMessages().length).isEqualTo(1);
                    var receivedMessage = greenMail.getReceivedMessages()[0];
                    assertThat(receivedMessage.getSubject())
                            .isEqualTo(String.format(SUBJECT, rideStatusMessage.rideId()));

                    assertThat(receivedMessage.getAllRecipients().length).isEqualTo(1);
                    assertThat(receivedMessage.getAllRecipients()[0].toString()).isEqualTo(EMAIL);
                });
    }
}
