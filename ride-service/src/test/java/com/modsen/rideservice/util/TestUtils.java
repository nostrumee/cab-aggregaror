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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@UtilityClass
public class TestUtils {

    private final LocalDateTime NOW = LocalDateTime.now();

    public final long DEFAULT_ID = 1L;

    public final long CREATED_RIDE_ID = 1L;
    public final long REJECTED_RIDE_ID = 2L;
    public final long ACCEPTED_RIDE_ID = 3L;
    public final long STARTED_RIDE_ID = 4L;
    public final long FINISHED_RIDE_ID = 5L;

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

    public final int INVALID_PAGE = -1;
    public final int INVALID_SIZE = -1;
    public final String INVALID_ORDER_BY = "invalidOrderBy";
    public final String PAGE_PARAM_OF_INVALID_TYPE = "invalidType";

    public final String PAGE_PARAM_NAME = "page";
    public final String SIZE_PARAM_NAME = "size";
    public final String ORDER_BY_PARAM_NAME = "order_by";
    public final String ID_PARAM_NAME = "id";

    public final String GET_RIDE_BY_ID_PATH = "/api/v1/rides/{id}";
    public final String GET_RIDE_PAGE_PATH = "/api/v1/rides";
    public final String CREATE_RIDE_PATH = "/api/v1/rides";
    public final String DELETE_RIDE_PATH = "/api/v1/rides/{id}";
    public final String START_RIDE_PATH = "/api/v1/rides/{id}/start";
    public final String FINISH_RIDE_PATH = "/api/v1/rides/{id}/finish";
    public final String GET_DRIVER_PROFILE_PATH = "/api/v1/rides/{id}/driver";

    public final String PASSENGER_NOT_FOUND_MESSAGE = "Passenger with id 1 was not found";
    public final String DRIVER_NOT_FOUND_MESSAGE = "Passenger with id 1 was not found";

    public final String POSTGRES_IMAGE_NAME = "postgres:15-alpine";
    public final String KAFKA_IMAGE_NAME = "confluentinc/cp-kafka:7.3.3";

    public Ride getCreatedRide() {
        return Ride.builder()
                .id(CREATED_RIDE_ID)
                .passengerId(DEFAULT_ID)
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
                .passengerId(DEFAULT_ID)
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
                .passengerId(DEFAULT_ID)
                .driverId(DEFAULT_ID)
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
                .passengerId(DEFAULT_ID)
                .driverId(DEFAULT_ID)
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
                .passengerId(DEFAULT_ID)
                .driverId(DEFAULT_ID)
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
                .passengerId(DEFAULT_ID)
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
                .passengerId(DEFAULT_ID)
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
                .passengerId(DEFAULT_ID)
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
                .passengerId(DEFAULT_ID)
                .driverId(DEFAULT_ID)
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
                .passengerId(DEFAULT_ID)
                .driverId(DEFAULT_ID)
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
                .passengerId(DEFAULT_ID)
                .driverId(DEFAULT_ID)
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
                .id(DEFAULT_ID)
                .firstName(FIRST_NAME)
                .email(EMAIL)
                .build();
    }

    public CreateRideRequest getCreateRideRequest() {
        return CreateRideRequest.builder()
                .passengerId(DEFAULT_ID)
                .startPoint(START_POINT)
                .destinationPoint(DESTINATION_POINT)
                .build();
    }

    public DriverStatusMessage getDriverStatusMessage(DriverStatus status) {
        return DriverStatusMessage.builder()
                .driverId(DEFAULT_ID)
                .status(status)
                .build();
    }

    public AcceptRideMessage getAcceptRideMessage(Long driverId) {
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

    public Response getResponseWithErrorCode(int status, String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
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
    }

}
