package com.modsen.ratingservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.ratingservice.dto.message.DriverRatingMessage;
import com.modsen.ratingservice.dto.message.PassengerRatingMessage;
import com.modsen.ratingservice.dto.request.DriverRatingRequest;
import com.modsen.ratingservice.dto.request.PassengerRatingRequest;
import com.modsen.ratingservice.dto.response.ErrorResponse;
import com.modsen.ratingservice.dto.response.RideResponse;
import com.modsen.ratingservice.dto.response.ValidationErrorResponse;
import com.modsen.ratingservice.entity.DriverRating;
import com.modsen.ratingservice.entity.PassengerRating;
import com.modsen.ratingservice.entity.RideStatus;
import feign.Request;
import feign.Response;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import static com.modsen.ratingservice.util.ErrorMessages.VALIDATION_FAILED_MESSAGE;
import static com.modsen.ratingservice.util.UriPaths.RIDE_SERVICE_BASE_PATH;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {

    public static final long DEFAULT_ID = 1L;
    public static final UUID DEFAULT_PASSENGER_ID = UUID.fromString("d6f3c9d1-de66-45ee-beb9-f371fa3a6378");
    public static final UUID DEFAULT_DRIVER_ID = UUID.fromString("d3849c45-a4f6-4e2a-8289-6b662076fabf");
    public static final int DEFAULT_RATING = 3;
    public static BigDecimal UPDATED_RATING = new BigDecimal("3.0");

    public static final int INVALID_RATING = -1;
    public static final long INVALID_RIDE_ID = -1;

    public static final String RATING_VALIDATION_MESSAGE_KEY = "rating.valid";
    public static final String RIDE_ID_VALIDATION_MESSAGE_KEY = "ride-id.valid";
    public static final String RATING_FIELD_NAME = "rating";
    public static final String RIDE_ID_FIELD_NAME = "rideId";

    public static final String RIDE_NOT_FOUND_MESSAGE = "Ride with id 1 was not found";

    public static final String GET_RIDE_BY_ID_PATH = String.format(
            "%s/%s",
            RIDE_SERVICE_BASE_PATH,
            DEFAULT_ID
    );

    public static final String POSTGRES_IMAGE_NAME = "postgres:15-alpine";
    public static final String KAFKA_IMAGE_NAME = "confluentinc/cp-kafka:7.3.3";

    public static final String DRIVER_RATING_TOPIC_NAME = "driver-rating-topic";
    public static final String PASSENGER_RATING_TOPIC_NAME = "passenger-rating-topic";

    public static final String RIDE_SERVICE_NAME = "ride-service";

    public static PassengerRatingRequest getPassengerRatingRequest() {
        return PassengerRatingRequest.builder()
                .rideId(DEFAULT_ID)
                .rating(DEFAULT_RATING)
                .build();
    }

    public static DriverRatingRequest getDriverRatingRequest() {
        return DriverRatingRequest.builder()
                .rideId(DEFAULT_ID)
                .rating(DEFAULT_RATING)
                .build();
    }

    public static RideResponse getRideResponse(RideStatus status) {
        return RideResponse.builder()
                .passengerId(DEFAULT_PASSENGER_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .status(status)
                .build();
    }

    public static PassengerRating getPassengerRating() {
        return PassengerRating.builder()
                .id(DEFAULT_ID)
                .rideId(DEFAULT_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .rating(DEFAULT_RATING)
                .build();
    }

    public static DriverRating getDriverRating() {
        return DriverRating.builder()
                .id(DEFAULT_ID)
                .rideId(DEFAULT_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .rating(DEFAULT_RATING)
                .build();
    }

    public static PassengerRatingMessage getPassengerRatingMessage() {
        return PassengerRatingMessage.builder()
                .passengerId(DEFAULT_PASSENGER_ID)
                .rating(3.0)
                .build();
    }

    public static DriverRatingMessage getDriverRatingMessage() {
        return DriverRatingMessage.builder()
                .driverId(DEFAULT_DRIVER_ID)
                .rating(3.0)
                .build();
    }

    public static ValidationErrorResponse getValidationErrorResponse(
            String rideIdValidationMessageKey,
            String ratingValidationMessageKey
    ) {
        return ValidationErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(VALIDATION_FAILED_MESSAGE)
                .errors(Map.of(
                        RIDE_ID_FIELD_NAME, rideIdValidationMessageKey,
                        RATING_FIELD_NAME, ratingValidationMessageKey
                ))
                .build();
    }

    public static KafkaConsumer<String, Object> getKafkaConsumer(String bootstrapServers) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new KafkaConsumer<>(properties);
    }

    public static Response getResponseWithErrorCode(int status, String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return Response.builder()
                    .status(status)
                    .reason("")
                    .headers(new HashMap<>())
                    .request(Request.create(Request.HttpMethod.GET, "", new HashMap<>(), Request.Body.empty(), null))
                    .body(objectMapper.writeValueAsBytes(
                            ErrorResponse.builder()
                                    .status(status)
                                    .message(message)
                                    .build()
                    ))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String fromObjectToString(T object) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
