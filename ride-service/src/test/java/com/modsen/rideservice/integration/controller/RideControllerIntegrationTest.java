package com.modsen.rideservice.integration.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.modsen.rideservice.dto.request.CreateRideRequest;
import com.modsen.rideservice.dto.response.*;
import com.modsen.rideservice.entity.RideStatus;
import com.modsen.rideservice.integration.IntegrationTestBase;
import com.modsen.rideservice.mapper.RideMapper;
import com.modsen.rideservice.repository.RideRepository;
import com.modsen.rideservice.service.MessageService;
import com.modsen.rideservice.service.SendMessageHandler;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.modsen.rideservice.util.ErrorMessages.*;
import static com.modsen.rideservice.util.TestUtils.*;
import static com.modsen.rideservice.util.UriPaths.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;


@RequiredArgsConstructor
public class RideControllerIntegrationTest extends IntegrationTestBase {

    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    private final MessageService messageService;

    private final WireMockServer mockServer;

    @MockBean
    private SendMessageHandler sendMessageHandler;

    @LocalServerPort
    private int port;

    @AfterEach
    void tearDown() {
        mockServer.resetAll();
    }

    @Test
    void getRidesPage_shouldReturnRidesPageResponse_whenValidParamsPassed() {
        var ridesPage = rideRepository.findAll(
                PageRequest.of(VALID_PAGE - 1, VALID_SIZE, Sort.by(VALID_ORDER_BY))
        );
        var rides = rideMapper.fromEntityListToResponseList(ridesPage.getContent());
        var expected = RidePageResponse.builder()
                .rides(rides)
                .pageNumber(VALID_PAGE)
                .total(TOTAL)
                .build();

        var actual = given()
                .port(port)
                .params(Map.of(
                        PAGE_PARAM_NAME, VALID_PAGE,
                        SIZE_PARAM_NAME, VALID_SIZE,
                        ORDER_BY_PARAM_NAME, VALID_ORDER_BY
                ))
                .when()
                .get(RIDE_SERVICE_BASE_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RidePageResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("getInvalidParamsForGetRidesPageTest")
    void getRidesPage_shouldReturnBadRequestResponse_whenInvalidPageOrSizePassed(int page, int size) {
        var expected = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(INVALID_PAGE_PARAMETERS_MESSAGE)
                .build();

        var actual = given()
                .port(port)
                .params(Map.of(
                        PAGE_PARAM_NAME, page,
                        SIZE_PARAM_NAME, size,
                        ORDER_BY_PARAM_NAME, VALID_ORDER_BY
                ))
                .when()
                .get(RIDE_SERVICE_BASE_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getRidesPage_shouldReturnBadRequestResponse_whenInvalidOrderByParamPassed() {
        String errorMessage = getInvalidSortingParameterMessage();
        var expected = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .build();

        var actual = given()
                .port(port)
                .params(Map.of(
                        PAGE_PARAM_NAME, VALID_PAGE,
                        SIZE_PARAM_NAME, VALID_SIZE,
                        ORDER_BY_PARAM_NAME, INVALID_ORDER_BY
                ))
                .when()
                .get(RIDE_SERVICE_BASE_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getRidesPage_shouldReturnBadRequestResponse_whenPageTypeNotMatch() {
        given()
                .port(port)
                .params(Map.of(
                        PAGE_PARAM_NAME, PAGE_PARAM_OF_INVALID_TYPE,
                        SIZE_PARAM_NAME, VALID_SIZE,
                        ORDER_BY_PARAM_NAME, VALID_ORDER_BY
                ))
                .when()
                .get(RIDE_SERVICE_BASE_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo(INVALID_PARAMETER_TYPE_MESSAGE));
    }

    @Test
    void getRidesPage_shouldReturnBadRequestResponse_whenSizeTypeNotMatch() {
        given()
                .port(port)
                .params(Map.of(
                        PAGE_PARAM_NAME, VALID_PAGE,
                        SIZE_PARAM_NAME, PAGE_PARAM_OF_INVALID_TYPE,
                        ORDER_BY_PARAM_NAME, VALID_ORDER_BY
                ))
                .when()
                .get(RIDE_SERVICE_BASE_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo(INVALID_PARAMETER_TYPE_MESSAGE));
    }

    @Test
    void getDriverRideHistory_shouldReturnRidePageResponseWithDriverFinishedRides() {
        var ridesPage = rideRepository.findAllByDriverIdAndStatus(
                DEFAULT_ID, RideStatus.FINISHED,
                PageRequest.of(VALID_PAGE - 1, VALID_SIZE, Sort.by(VALID_ORDER_BY))
        );
        var rides = rideMapper.fromEntityListToResponseList(ridesPage.getContent());
        var expected = RidePageResponse.builder()
                .rides(rides)
                .pageNumber(VALID_PAGE)
                .total(1L)
                .build();

        var actual = given()
                .port(port)
                .pathParam(DRIVER_ID_PARAM_NAME, DEFAULT_ID)
                .params(Map.of(
                        PAGE_PARAM_NAME, VALID_PAGE,
                        SIZE_PARAM_NAME, VALID_SIZE,
                        ORDER_BY_PARAM_NAME, VALID_ORDER_BY
                ))
                .when()
                .get(RIDE_SERVICE_BASE_PATH + GET_DRIVER_RIDE_HISTORY_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RidePageResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getPassengerRideHistory_shouldReturnRidePageResponseWithPassengerFinishedRides() {
        var ridesPage = rideRepository.findAllByPassengerIdAndStatus(
                DEFAULT_ID, RideStatus.FINISHED,
                PageRequest.of(VALID_PAGE - 1, VALID_SIZE, Sort.by(VALID_ORDER_BY))
        );
        var rides = rideMapper.fromEntityListToResponseList(ridesPage.getContent());
        var expected = RidePageResponse.builder()
                .rides(rides)
                .pageNumber(VALID_PAGE)
                .total(1L)
                .build();

        var actual = given()
                .port(port)
                .pathParam(PASSENGER_ID_PARAM_NAME, DEFAULT_ID)
                .params(Map.of(
                        PAGE_PARAM_NAME, VALID_PAGE,
                        SIZE_PARAM_NAME, VALID_SIZE,
                        ORDER_BY_PARAM_NAME, VALID_ORDER_BY
                ))
                .when()
                .get(RIDE_SERVICE_BASE_PATH + GET_PASSENGER_RIDE_HISTORY_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RidePageResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getRideById_shouldReturnRideResponse_whenRideExists() {
        var ride = rideRepository.findById(FINISHED_RIDE_ID);
        var expected = rideMapper.fromEntityToResponse(ride.get());

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, FINISHED_RIDE_ID)
                .when()
                .get(RIDE_SERVICE_BASE_PATH + GET_BY_ID_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RideResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getRideById_shouldReturnNotFoundResponse_whenRideNotExist() {
        var expected = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(String.format(RIDE_NOT_FOUND_WITH_ID_MESSAGE, NON_EXISTING_ID))
                .build();

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, NON_EXISTING_ID)
                .when()
                .get(RIDE_SERVICE_BASE_PATH + GET_BY_ID_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void createRide_shouldReturnCreatedRideResponse_whenPassengerExists() {
        var createRequest = getCreateRideRequest();
        var passenger = getPassengerResponse();

        mockServer.stubFor(get(GET_PASSENGER_BY_ID_PATH).willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBody(fromObjectToString(passenger)))
        );

        var actual = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(createRequest)
                .when()
                .post(RIDE_SERVICE_BASE_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(RideResponse.class);

        assertThat(actual.status()).isEqualTo(RideStatus.CREATED);
        assertThat(actual.createdDate()).isNotNull();
        assertThat(actual.estimatedCost()).isNotNull();
    }

    @Test
    void createRide_shouldReturnBadRequestResponse_whenPassengerNotExist() {
        var createRequest = getCreateRideRequest();
        var errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(PASSENGER_NOT_FOUND_MESSAGE)
                .build();

        mockServer.stubFor(get(GET_PASSENGER_BY_ID_PATH).willReturn(aResponse()
                .withStatus(HttpStatus.NOT_FOUND.value())
                .withHeader("Content-Type", "application/json")
                .withBody(fromObjectToString(errorResponse)))
        );

        var actual = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(createRequest)
                .when()
                .post(RIDE_SERVICE_BASE_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual.message()).isEqualTo(PASSENGER_NOT_FOUND_MESSAGE);
        assertThat(actual.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void createRide_shouldReturnBadRequestResponse_whenDataNotValid() {
        String passengerIdValidationMessage = messageService.getMessage(PASSENGER_ID_VALIDATION_MESSAGE_KEY);
        String startPointValidationMessage = messageService.getMessage(START_POINT_VALIDATION_MESSAGE_KEY);
        String destinationPointValidationMessage = messageService.getMessage(DESTINATION_POINT_VALIDATION_MESSAGE_KEY);

        var createRequest = CreateRideRequest.builder()
                .passengerId(INVALID_PASSENGER_ID)
                .startPoint(null)
                .destinationPoint(null)
                .build();

        var expected = ValidationErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(VALIDATION_FAILED_MESSAGE)
                .errors(Map.of(
                        PASSENGER_ID_FIELD_NAME, passengerIdValidationMessage,
                        START_POINT_FIELD_NAME, startPointValidationMessage,
                        DESTINATION_POINT_FIELD_NAME, destinationPointValidationMessage
                ))
                .build();

        var actual = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(createRequest)
                .when()
                .post(RIDE_SERVICE_BASE_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ValidationErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteRide_shouldDeleteRide_whenRideExists() {
        var expected = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(String.format(RIDE_NOT_FOUND_WITH_ID_MESSAGE, DEFAULT_ID))
                .build();

        given()
                .port(port)
                .pathParam(ID_PARAM_NAME, DEFAULT_ID)
                .when()
                .delete(RIDE_SERVICE_BASE_PATH + DELETE_BY_ID_PATH)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, DEFAULT_ID)
                .when()
                .get(RIDE_SERVICE_BASE_PATH + DELETE_BY_ID_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteRide_shouldReturnNotFoundResponse_whenRideNotExist() {
        var expected = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(String.format(RIDE_NOT_FOUND_WITH_ID_MESSAGE, NON_EXISTING_ID))
                .build();

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, NON_EXISTING_ID)
                .when()
                .delete(RIDE_SERVICE_BASE_PATH + DELETE_BY_ID_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void startRide_shouldReturnStartedRideResponse_whenRideExistsAndHasValidStatus() {
        var passenger = getPassengerResponse();

        mockServer.stubFor(get(GET_PASSENGER_BY_ID_PATH).willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBody(fromObjectToString(passenger)))
        );

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, ACCEPTED_RIDE_ID)
                .when()
                .get(RIDE_SERVICE_BASE_PATH + START_RIDE_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RideResponse.class);

        assertThat(actual.status()).isEqualTo(RideStatus.STARTED);
        assertThat(actual.startDate()).isNotNull();
    }

    @Test
    void startRide_shouldReturnBadRequestResponse_whenRideExistsAndHasInvalidStatus() {
        var validStatusList = Collections.singletonList(RideStatus.ACCEPTED);
        String statusNames = convertRideStatusListToString(validStatusList);

        var expected = ErrorResponse.builder()
                .message(String.format(INVALID_RIDE_STATUS_MESSAGE, statusNames))
                .status(HttpStatus.CONFLICT.value())
                .build();

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, STARTED_RIDE_ID)
                .when()
                .get(RIDE_SERVICE_BASE_PATH + START_RIDE_PATH)
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void startRide_shouldReturnNotFoundResponse_whenRideNotExist() {
        var expected = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(String.format(RIDE_NOT_FOUND_WITH_ID_MESSAGE, NON_EXISTING_ID))
                .build();

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, NON_EXISTING_ID)
                .when()
                .get(RIDE_SERVICE_BASE_PATH + START_RIDE_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void finishRide_shouldReturnFinishedRideResponse_whenRideExistsAndHasValidStatus() {
        var passenger = getPassengerResponse();

        mockServer.stubFor(get(GET_PASSENGER_BY_ID_PATH).willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBody(fromObjectToString(passenger)))
        );

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, STARTED_RIDE_ID)
                .when()
                .get(RIDE_SERVICE_BASE_PATH + FINISH_RIDE_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RideResponse.class);

        assertThat(actual.status()).isEqualTo(RideStatus.FINISHED);
        assertThat(actual.finishDate()).isNotNull();
    }

    @Test
    void finishRide_shouldReturnConflictResponse_whenRideExistsAndHasInvalidStatus() {
        var validStatusList = Collections.singletonList(RideStatus.STARTED);
        String statusNames = convertRideStatusListToString(validStatusList);

        var expected = ErrorResponse.builder()
                .message(String.format(INVALID_RIDE_STATUS_MESSAGE, statusNames))
                .status(HttpStatus.CONFLICT.value())
                .build();

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, FINISHED_RIDE_ID)
                .when()
                .get(RIDE_SERVICE_BASE_PATH + FINISH_RIDE_PATH)
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void finishRide_shouldReturnNotFoundResponse_whenRideNotExist() {
        var expected = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(String.format(RIDE_NOT_FOUND_WITH_ID_MESSAGE, NON_EXISTING_ID))
                .build();

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, NON_EXISTING_ID)
                .when()
                .get(RIDE_SERVICE_BASE_PATH + FINISH_RIDE_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getDriverProfile_shouldReturnDriverResponse_whenRideExistsAndHasValidStatus() {
        var expected = getDriverResponse();

        mockServer.stubFor(get(GET_DRIVER_BY_ID_PATH).willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBody(fromObjectToString(expected)))
        );

        var actual = given()
                .port(port)
                .pathParam(RIDE_ID_PARAM_NAME, FINISHED_RIDE_ID)
                .when()
                .get(RIDE_SERVICE_BASE_PATH + GET_DRIVER_PROFILE_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(DriverResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getDriverProfile_shouldReturnConflictResponse_whenRideExistsAndHasInvalidStatus() {
        var validStatusList = List.of(
                RideStatus.ACCEPTED,
                RideStatus.STARTED,
                RideStatus.FINISHED
        );
        String statusNames = convertRideStatusListToString(validStatusList);

        var expected = ErrorResponse.builder()
                .message(String.format(INVALID_RIDE_STATUS_MESSAGE, statusNames))
                .status(HttpStatus.CONFLICT.value())
                .build();

        var actual = given()
                .port(port)
                .pathParam(RIDE_ID_PARAM_NAME, CREATED_RIDE_ID)
                .when()
                .get(RIDE_SERVICE_BASE_PATH + GET_DRIVER_PROFILE_PATH)
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getDriverProfile_shouldReturnNotFoundResponse_whenRideNotExist() {
        var expected = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(String.format(RIDE_NOT_FOUND_WITH_ID_MESSAGE, NON_EXISTING_ID))
                .build();

        var actual = given()
                .port(port)
                .pathParam(RIDE_ID_PARAM_NAME, NON_EXISTING_ID)
                .when()
                .get(RIDE_SERVICE_BASE_PATH + GET_DRIVER_PROFILE_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> getInvalidParamsForGetRidesPageTest() {
        return Stream.of(
                Arguments.of(INVALID_PAGE, VALID_SIZE),
                Arguments.of(VALID_PAGE, INVALID_SIZE)
        );
    }

    private String getInvalidSortingParameterMessage() {
        List<String> fieldNames = Arrays.stream(RideResponse.class.getDeclaredFields())
                .map(Field::getName)
                .toList();

        String acceptableParams = String.join(", ", fieldNames);
        return String.format(INVALID_SORTING_PARAMETER_MESSAGE, INVALID_ORDER_BY, acceptableParams);
    }

    private String convertRideStatusListToString(List<RideStatus> statuses) {
        return statuses.stream()
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
