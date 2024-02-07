package com.modsen.rideservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.rideservice.dto.message.AcceptRideMessage;
import com.modsen.rideservice.dto.message.DriverStatusMessage;
import com.modsen.rideservice.dto.message.RideStatusMessage;
import com.modsen.rideservice.dto.request.CreateRideRequest;
import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.dto.response.ErrorResponse;
import com.modsen.rideservice.dto.response.PassengerResponse;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.entity.DriverStatus;
import com.modsen.rideservice.entity.Ride;
import com.modsen.rideservice.entity.RideStatus;
import feign.Request;
import feign.Request.Body;
import feign.Request.HttpMethod;
import feign.Response;
import lombok.experimental.UtilityClass;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static com.modsen.rideservice.util.UriPaths.*;

@UtilityClass
public class TestUtils {

    public final LocalDateTime NOW = LocalDateTime.now();

    public final long DEFAULT_ID = 1L;
    public final long CREATED_RIDE_ID = 1L;
    public final long REJECTED_RIDE_ID = 2L;
    public final long ACCEPTED_RIDE_ID = 3L;
    public final long STARTED_RIDE_ID = 4L;
    public final long FINISHED_RIDE_ID = 5L;
    
    public final UUID DEFAULT_PASSENGER_ID = UUID.fromString("d6f3c9d1-de66-45ee-beb9-f371fa3a6378");
    public final UUID DEFAULT_DRIVER_ID = UUID.fromString("d3849c45-a4f6-4e2a-8289-6b662076fabf");

    public final String START_POINT = "Default start point";
    public final String DESTINATION_POINT = "Default destination point";
    public final LocalDateTime CREATED_DATE = NOW.plusMinutes(1);
    public final LocalDateTime ACCEPTED_DATE = NOW.plusMinutes(2);
    public final LocalDateTime START_DATE = NOW.plusMinutes(3);
    public final LocalDateTime FINISH_DATE = NOW.plusMinutes(4);
    public final BigDecimal ESTIMATED_COST = new BigDecimal("6.7");

    public final String FIRST_NAME = "John";
    public final String LAST_NAME = "Doe";
    public final String EMAIL = "johndoe@example.com";
    public final String PHONE = "123-45-67";
    public final double RATING = 5.0;

    public final int VALID_PAGE = 1;
    public final int VALID_SIZE = 2;
    public final String VALID_ORDER_BY = "id";
    public final long TOTAL = 5L;
    public final long NON_EXISTING_ID = 256;
    public final long INVALID_PASSENGER_ID = -1;

    public final int INVALID_PAGE = -1;
    public final int INVALID_SIZE = -1;
    public final String INVALID_ORDER_BY = "invalidOrderBy";
    public final String PAGE_PARAM_OF_INVALID_TYPE = "invalidType";

    public final String PAGE_PARAM_NAME = "page";
    public final String SIZE_PARAM_NAME = "size";
    public final String ORDER_BY_PARAM_NAME = "order_by";
    public final String ID_PARAM_NAME = "id";
    public final String PASSENGER_ID_PARAM_NAME = "passengerId";
    public final String DRIVER_ID_PARAM_NAME = "driverId";
    public final String RIDE_ID_PARAM_NAME = "rideId";

    public final String PASSENGER_ID_VALIDATION_MESSAGE_KEY = "passenger-id.positive";
    public final String START_POINT_VALIDATION_MESSAGE_KEY = "start-point.not.blank";
    public final String DESTINATION_POINT_VALIDATION_MESSAGE_KEY = "destination-point.not.blank";

    public final String PASSENGER_ID_FIELD_NAME = "passengerId";
    public final String START_POINT_FIELD_NAME = "startPoint";
    public final String DESTINATION_POINT_FIELD_NAME = "destinationPoint";

    public final String GET_PASSENGER_BY_ID_PATH = String.format("%s/%s", PASSENGER_SERVICE_BASE_PATH, DEFAULT_ID);
    public final String GET_DRIVER_BY_ID_PATH = String.format("%s/%s", DRIVER_SERVICE_BASE_PATH, DEFAULT_ID);

    public final String PASSENGER_NOT_FOUND_MESSAGE = "Passenger with id 1 was not found";
    public final String DRIVER_NOT_FOUND_MESSAGE = "Passenger with id 1 was not found";

    public final String POSTGRES_IMAGE_NAME = "postgres:15-alpine";
    public final String KAFKA_IMAGE_NAME = "confluentinc/cp-kafka:7.3.3";

    public final String ACCEPT_RIDE_TOPIC_NAME = "accept-ride-topic";
    public final String CREATE_RIDE_TOPIC_NAME = "create-ride-topic";
    public final String RIDE_STATUS_TOPIC_NAME = "ride-status-topic";
    public final String DRIVER_STATUS_TOPIC_NAME = "driver-status-topic";

    public final String DRIVER_SERVICE_NAME = "driver-service";
    public final String PASSENGER_SERVICE_NAME = "passenger-service";

    public Ride getCreatedRide() {
        return Ride.builder()
                .id(CREATED_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .startPoint(START_POINT)
                .destinationPoint(DESTINATION_POINT)
                .status(RideStatus.CREATED)
                .createdDate(CREATED_DATE)
                .estimatedCost(ESTIMATED_COST)
                .build();
    }

    public Ride getRejectedRide() {
        return Ride.builder()
                .id(REJECTED_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .startPoint(START_POINT)
                .destinationPoint(DESTINATION_POINT)
                .status(RideStatus.REJECTED)
                .createdDate(CREATED_DATE)
                .estimatedCost(ESTIMATED_COST)
                .build();
    }

    public Ride getAcceptedRide() {
        return Ride.builder()
                .id(ACCEPTED_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .startPoint(START_POINT)
                .destinationPoint(DESTINATION_POINT)
                .status(RideStatus.ACCEPTED)
                .createdDate(CREATED_DATE)
                .acceptedDate(ACCEPTED_DATE)
                .estimatedCost(ESTIMATED_COST)
                .build();
    }

    public Ride getStartedRide() {
        return Ride.builder()
                .id(STARTED_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .startPoint(START_POINT)
                .destinationPoint(DESTINATION_POINT)
                .status(RideStatus.STARTED)
                .createdDate(CREATED_DATE)
                .acceptedDate(ACCEPTED_DATE)
                .startDate(START_DATE)
                .estimatedCost(ESTIMATED_COST)
                .build();
    }

    public Ride getFinishedRide() {
        return Ride.builder()
                .id(FINISHED_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .startPoint(START_POINT)
                .destinationPoint(DESTINATION_POINT)
                .status(RideStatus.FINISHED)
                .createdDate(CREATED_DATE)
                .acceptedDate(ACCEPTED_DATE)
                .startDate(START_DATE)
                .finishDate(FINISH_DATE)
                .estimatedCost(ESTIMATED_COST)
                .build();
    }

    public Ride getNotSavedRide() {
        return Ride.builder()
                .passengerId(DEFAULT_PASSENGER_ID)
                .startPoint(START_POINT)
                .destinationPoint(DESTINATION_POINT)
                .status(RideStatus.CREATED)
                .createdDate(CREATED_DATE)
                .estimatedCost(ESTIMATED_COST)
                .build();
    }

    public RideResponse getCreatedRideResponse() {
        return RideResponse.builder()
                .id(CREATED_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .startPoint(START_POINT)
                .destinationPoint(DESTINATION_POINT)
                .status(RideStatus.CREATED)
                .createdDate(CREATED_DATE)
                .estimatedCost(ESTIMATED_COST)
                .build();
    }

    public RideResponse getRejectedRideResponse() {
        return RideResponse.builder()
                .id(REJECTED_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .startPoint(START_POINT)
                .destinationPoint(DESTINATION_POINT)
                .status(RideStatus.REJECTED)
                .createdDate(CREATED_DATE)
                .estimatedCost(ESTIMATED_COST)
                .build();
    }

    public RideResponse getAcceptedRideResponse() {
        return RideResponse.builder()
                .id(ACCEPTED_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .startPoint(START_POINT)
                .destinationPoint(DESTINATION_POINT)
                .status(RideStatus.ACCEPTED)
                .createdDate(CREATED_DATE)
                .acceptedDate(ACCEPTED_DATE)
                .estimatedCost(ESTIMATED_COST)
                .build();
    }

    public RideResponse getStartedRideResponse() {
        return RideResponse.builder()
                .id(STARTED_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .startPoint(START_POINT)
                .destinationPoint(DESTINATION_POINT)
                .status(RideStatus.STARTED)
                .createdDate(CREATED_DATE)
                .acceptedDate(ACCEPTED_DATE)
                .startDate(START_DATE)
                .estimatedCost(ESTIMATED_COST)
                .build();
    }

    public RideResponse getFinishedRideResponse() {
        return RideResponse.builder()
                .id(FINISHED_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .startPoint(START_POINT)
                .destinationPoint(DESTINATION_POINT)
                .status(RideStatus.FINISHED)
                .createdDate(CREATED_DATE)
                .acceptedDate(ACCEPTED_DATE)
                .startDate(START_DATE)
                .finishDate(FINISH_DATE)
                .estimatedCost(ESTIMATED_COST)
                .build();
    }

    public DriverResponse getDriverResponse() {
        return DriverResponse.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .phone(PHONE)
                .rating(RATING)
                .build();
    }

    public PassengerResponse getPassengerResponse() {
        return PassengerResponse.builder()
                .firstName(FIRST_NAME)
                .email(EMAIL)
                .build();
    }

    public CreateRideRequest getCreateRideRequest() {
        return CreateRideRequest.builder()
                .passengerId(DEFAULT_PASSENGER_ID)
                .startPoint(START_POINT)
                .destinationPoint(DESTINATION_POINT)
                .build();
    }

    public DriverStatusMessage getDriverStatusMessage(DriverStatus status) {
        return DriverStatusMessage.builder()
                .driverId(DEFAULT_DRIVER_ID)
                .status(status)
                .build();
    }

    public AcceptRideMessage getAcceptRideMessage(UUID driverId) {
        return AcceptRideMessage.builder()
                .rideId(DEFAULT_ID)
                .driverId(driverId)
                .build();
    }

    public RideStatusMessage getRideStatusMessage(RideStatus status) {
        return RideStatusMessage.builder()
                .rideId(DEFAULT_ID)
                .status(status)
                .passengerEmail(EMAIL)
                .passengerFirstName(FIRST_NAME)
                .startPoint(START_POINT)
                .destinationPoint(DESTINATION_POINT)
                .estimatedCost(ESTIMATED_COST)
                .build();
    }

    public List<Ride> getRideList() {
        return List.of(
                getCreatedRide(),
                getRejectedRide(),
                getAcceptedRide(),
                getStartedRide(),
                getFinishedRide()
        );
    }

    public List<Ride> getRidesHistoryList() {
        return Collections.singletonList(getFinishedRide());
    }

    public List<RideResponse> getRidesHistoryResponseList() {
        return Collections.singletonList(getFinishedRideResponse());
    }

    public List<RideResponse> getRideResponseList() {
        return List.of(
                getCreatedRideResponse(),
                getRejectedRideResponse(),
                getAcceptedRideResponse(),
                getStartedRideResponse(),
                getFinishedRideResponse()
        );
    }

    public Page<Ride> getRidePage() {
        return new PageImpl<>(getRideList());
    }

    public Page<Ride> getRidesHistoryPage() {
        return new PageImpl<>(getRidesHistoryList());
    }

    public PageRequest getPageRequest(int page, int size, String orderBy) {
        return PageRequest.of(page - 1, size, Sort.by(orderBy));
    }

    public KafkaProducer<String, Object> getKafkaProducer(String bootstrapServers) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaProducer<>(properties);
    }

    public KafkaConsumer<String, Object> getKafkaConsumer(String bootstrapServers) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(JsonDeserializer.TYPE_MAPPINGS, "rideStatusMessage:" + RideStatusMessage.class.getName());
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new KafkaConsumer<>(properties);
    }

    public Response getResponseWithErrorCode(int status, String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return Response.builder()
                    .status(status)
                    .reason("")
                    .headers(new HashMap<>())
                    .request(Request.create(HttpMethod.GET, "", new HashMap<>(), Body.empty(), null))
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

    public <T> String fromObjectToString(T object) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
