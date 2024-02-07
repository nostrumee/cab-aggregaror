package com.modsen.driverservice.integration.controller;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.modsen.driverservice.dto.request.CreateDriverRequest;
import com.modsen.driverservice.dto.request.UpdateDriverRequest;
import com.modsen.driverservice.dto.response.*;
import com.modsen.driverservice.entity.DriverStatus;
import com.modsen.driverservice.integration.IntegrationTestBase;
import com.modsen.driverservice.mapper.DriverMapper;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.service.MessageService;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.modsen.driverservice.util.ErrorMessages.*;
import static com.modsen.driverservice.util.TestUtils.*;
import static com.modsen.driverservice.util.UriPaths.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;


@RequiredArgsConstructor
public class DriverControllerIntegrationTest extends IntegrationTestBase {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final MessageService messageService;

    private static String accessToken;

    @LocalServerPort
    private int port;

    @BeforeAll
    static void beforeAll() {
        accessToken = getAccessToken("isabelle", "password");
    }

    @Test
    void getDriverPage_shouldReturnDriverPageResponse_whenValidParamsPassed() {
        var driverPage = driverRepository.findAll(
                PageRequest.of(VALID_PAGE - 1, VALID_SIZE, Sort.by(VALID_ORDER_BY))
        );
        var drivers = driverMapper.fromEntityListToResponseList(driverPage.getContent());

        var expected = DriverPageResponse.builder()
                .drivers(drivers)
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
                .get(DRIVER_SERVICE_BASE_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(DriverPageResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("getInvalidParamsForGetDriverPageTest")
    void getDriverPage_shouldReturnBadRequestResponse_whenInvalidPageOrSizePassed(int page, int size) {
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
                .get(DRIVER_SERVICE_BASE_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getDriverPage_shouldReturnBadRequestResponse_whenInvalidOrderByParamPassed() {
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
                .get(DRIVER_SERVICE_BASE_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getDriverPage_shouldReturnBadRequestResponse_whenPageTypeNotMatch() {
        given()
                .port(port)
                .params(Map.of(
                        PAGE_PARAM_NAME, PAGE_PARAM_OF_INVALID_TYPE,
                        SIZE_PARAM_NAME, VALID_SIZE,
                        ORDER_BY_PARAM_NAME, VALID_ORDER_BY
                ))
                .when()
                .get(DRIVER_SERVICE_BASE_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo(INVALID_PARAMETER_TYPE_MESSAGE));
    }

    @Test
    void getDriverPage_shouldReturnBadRequestResponse_whenSizeTypeNotMatch() {
        given()
                .port(port)
                .params(Map.of(
                        PAGE_PARAM_NAME, VALID_PAGE,
                        SIZE_PARAM_NAME, PAGE_PARAM_OF_INVALID_TYPE,
                        ORDER_BY_PARAM_NAME, VALID_ORDER_BY
                ))
                .when()
                .get(DRIVER_SERVICE_BASE_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo(INVALID_PARAMETER_TYPE_MESSAGE));
    }

    @Test
    void getDriverById_shouldReturnDriverResponse_whenDriverExists_andUserAuthenticated() {
        var driver = driverRepository.findById(DEFAULT_ID);
        var expected = driverMapper.fromEntityToResponse(driver.get());

        var actual = given()
                .port(port)
                .header(
                        AUTHORIZATION_HEADER_NAME, ACCESS_TOKEN_PREFIX + accessToken
                )
                .pathParam(ID_PARAM_NAME, DEFAULT_EXTERNAL_ID)
                .when()
                .get(DRIVER_SERVICE_BASE_PATH + GET_DRIVER_BY_ID_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(DriverResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getDriverById_shouldReturnUnauthorizedStatus_whenUserNotAuthenticated() {
        given()
                .port(port)
                .pathParam(ID_PARAM_NAME, DEFAULT_EXTERNAL_ID)
                .when()
                .get(DRIVER_SERVICE_BASE_PATH + GET_DRIVER_BY_ID_PATH)
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());

    }

    @Test
    void getDriverById_shouldReturnNotFoundResponse_whenDriverNotExist() {
        var expected = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(String.format(NOT_FOUND_WITH_ID_MESSAGE, NON_EXISTING_ID))
                .build();

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, NON_EXISTING_ID)
                .when()
                .get(DRIVER_SERVICE_BASE_PATH + GET_DRIVER_BY_ID_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addDriver_shouldReturnDriverResponse_whenDataIsValidAndUnique() {
        var createRequest = CreateDriverRequest.builder()
                .firstName(OTHER_FIRST_NAME)
                .lastName(OTHER_LAST_NAME)
                .licenceNumber(OTHER_LICENCE_NUMBER)
                .email(OTHER_EMAIL)
                .phone(OTHER_PHONE)
                .build();

        var expected = DriverResponse.builder()
                .id(OTHER_ID)
                .firstName(OTHER_FIRST_NAME)
                .lastName(OTHER_LAST_NAME)
                .licenceNumber(OTHER_LICENCE_NUMBER)
                .email(OTHER_EMAIL)
                .phone(OTHER_PHONE)
                .rating(DEFAULT_RATING)
                .status(DriverStatus.AVAILABLE)
                .build();

        var actual = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(createRequest)
                .when()
                .post(DRIVER_SERVICE_BASE_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(DriverResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addDriver_shouldReturnConflictResponse_whenDataNotUnique() {
        var createRequest = CreateDriverRequest.builder()
                .firstName(OTHER_FIRST_NAME)
                .lastName(OTHER_LAST_NAME)
                .licenceNumber(DEFAULT_LICENCE_NUMBER)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .build();

        var expected = AlreadyExistsResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(DRIVER_ALREADY_EXISTS_MESSAGE)
                .errors(Map.of(
                        LICENCE_NUMBER_FIELD_NAME, String.format(DRIVER_WITH_LICENCE_NUMBER_EXISTS_MESSAGE, DEFAULT_LICENCE_NUMBER),
                        EMAIL_FIELD_NAME, String.format(DRIVER_WITH_EMAIL_EXISTS_MESSAGE, DEFAULT_EMAIL),
                        PHONE_FIELD_NAME, String.format(DRIVER_WITH_PHONE_EXISTS_MESSAGE, DEFAULT_PHONE)
                ))
                .build();

        var actual = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(createRequest)
                .when()
                .post(DRIVER_SERVICE_BASE_PATH)
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .as(AlreadyExistsResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addDriver_shouldReturnBadRequestResponse_whenDataNotValid() {
        String firstNameValidationMessage = messageService.getMessage(FIRST_NAME_VALIDATION_MESSAGE_KEY);
        String lastNameValidationMessage = messageService.getMessage(LAST_NAME_VALIDATION_MESSAGE_KEY);
        String licenceNumberValidationMessage = messageService.getMessage(LICENCE_NUMBER_VALIDATION_MESSAGE_KEY);
        String emailValidationMessage = messageService.getMessage(EMAIL_VALIDATION_MESSAGE_KEY);
        String phoneValidationMessage = messageService.getMessage(PHONE_VALIDATION_MESSAGE_KEY);

        var createRequest = CreateDriverRequest.builder()
                .firstName(null)
                .lastName(null)
                .licenceNumber(INVALID_LICENCE_NUMBER)
                .email(INVALID_EMAIL)
                .phone(INVALID_PHONE)
                .build();

        var expected = ValidationErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(VALIDATION_FAILED_MESSAGE)
                .errors(Map.of(
                        FIRST_NAME_FIELD_NAME, firstNameValidationMessage,
                        LAST_NAME_FIELD_NAME, lastNameValidationMessage,
                        LICENCE_NUMBER_FIELD_NAME, licenceNumberValidationMessage,
                        EMAIL_FIELD_NAME, emailValidationMessage,
                        PHONE_FIELD_NAME, phoneValidationMessage
                ))
                .build();

        var actual = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(createRequest)
                .when()
                .post(DRIVER_SERVICE_BASE_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ValidationErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateDriver_shouldReturnDriverResponse_whenDataIsValidAndUnique() {
        var updateRequest = UpdateDriverRequest.builder()
                .firstName(OTHER_FIRST_NAME)
                .lastName(OTHER_LAST_NAME)
                .licenceNumber(OTHER_LICENCE_NUMBER)
                .email(OTHER_EMAIL)
                .phone(OTHER_PHONE)
                .build();

        var expected = DriverResponse.builder()
                .id(DEFAULT_ID)
                .firstName(OTHER_FIRST_NAME)
                .lastName(OTHER_LAST_NAME)
                .licenceNumber(OTHER_LICENCE_NUMBER)
                .email(OTHER_EMAIL)
                .phone(OTHER_PHONE)
                .rating(DEFAULT_RATING)
                .status(DriverStatus.AVAILABLE)
                .build();

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, DEFAULT_ID)
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put(DRIVER_SERVICE_BASE_PATH + UPDATE_DRIVER_BY_ID_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(DriverResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateDriver_shouldReturnNotFoundStatus_whenDriverNotExist() {
        var updateRequest = UpdateDriverRequest.builder()
                .firstName(OTHER_FIRST_NAME)
                .lastName(OTHER_LAST_NAME)
                .licenceNumber(OTHER_LICENCE_NUMBER)
                .email(OTHER_EMAIL)
                .phone(OTHER_PHONE)
                .build();

        var expected = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(String.format(NOT_FOUND_WITH_ID_MESSAGE, NON_EXISTING_ID))
                .build();

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, NON_EXISTING_ID)
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put(DRIVER_SERVICE_BASE_PATH + UPDATE_DRIVER_BY_ID_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateDriver_shouldReturnConflictResponse_whenDataNotUnique() {
        var updateRequest = UpdateDriverRequest.builder()
                .firstName(OTHER_FIRST_NAME)
                .lastName(OTHER_LAST_NAME)
                .licenceNumber(DEFAULT_LICENCE_NUMBER)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .build();

        var expected = AlreadyExistsResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(DRIVER_ALREADY_EXISTS_MESSAGE)
                .errors(Map.of(
                        LICENCE_NUMBER_FIELD_NAME, String.format(DRIVER_WITH_LICENCE_NUMBER_EXISTS_MESSAGE, DEFAULT_LICENCE_NUMBER),
                        EMAIL_FIELD_NAME, String.format(DRIVER_WITH_EMAIL_EXISTS_MESSAGE, DEFAULT_EMAIL),
                        PHONE_FIELD_NAME, String.format(DRIVER_WITH_PHONE_EXISTS_MESSAGE, DEFAULT_PHONE)
                ))
                .build();

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, EXISTING_ID)
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put(DRIVER_SERVICE_BASE_PATH + UPDATE_DRIVER_BY_ID_PATH)
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .as(AlreadyExistsResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateDriver_shouldReturnBadRequestResponse_whenDataNotValid() {
        String firstNameValidationMessage = messageService.getMessage(FIRST_NAME_VALIDATION_MESSAGE_KEY);
        String lastNameValidationMessage = messageService.getMessage(LAST_NAME_VALIDATION_MESSAGE_KEY);
        String licenceNumberValidationMessage = messageService.getMessage(LICENCE_NUMBER_VALIDATION_MESSAGE_KEY);
        String emailValidationMessage = messageService.getMessage(EMAIL_VALIDATION_MESSAGE_KEY);
        String phoneValidationMessage = messageService.getMessage(PHONE_VALIDATION_MESSAGE_KEY);

        var updateRequest = UpdateDriverRequest.builder()
                .firstName(null)
                .lastName(null)
                .licenceNumber(INVALID_LICENCE_NUMBER)
                .email(INVALID_EMAIL)
                .phone(INVALID_PHONE)
                .build();

        var expected = ValidationErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(VALIDATION_FAILED_MESSAGE)
                .errors(Map.of(
                        FIRST_NAME_FIELD_NAME, firstNameValidationMessage,
                        LAST_NAME_FIELD_NAME, lastNameValidationMessage,
                        LICENCE_NUMBER_FIELD_NAME, licenceNumberValidationMessage,
                        EMAIL_FIELD_NAME, emailValidationMessage,
                        PHONE_FIELD_NAME, phoneValidationMessage
                ))
                .build();

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, DEFAULT_ID)
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put(DRIVER_SERVICE_BASE_PATH + UPDATE_DRIVER_BY_ID_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ValidationErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteDriver_shouldDeleteDriver_whenDriverExists() {
        var expected = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(String.format(NOT_FOUND_WITH_ID_MESSAGE, DEFAULT_ID))
                .build();

        given()
                .port(port)
                .pathParam(ID_PARAM_NAME, DEFAULT_ID)
                .when()
                .delete(DRIVER_SERVICE_BASE_PATH + DELETE_DRIVER_BY_ID_PATH)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, DEFAULT_ID)
                .when()
                .get(DRIVER_SERVICE_BASE_PATH + GET_DRIVER_BY_ID_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteDriver_shouldReturnNotFoundResponse_whenDriverNotExist() {
        var expected = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(String.format(NOT_FOUND_WITH_ID_MESSAGE, NON_EXISTING_ID))
                .build();

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, NON_EXISTING_ID)
                .when()
                .delete(DRIVER_SERVICE_BASE_PATH + DELETE_DRIVER_BY_ID_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> getInvalidParamsForGetDriverPageTest() {
        return Stream.of(
                Arguments.of(INVALID_PAGE, VALID_SIZE),
                Arguments.of(VALID_PAGE, INVALID_SIZE)
        );
    }

    private String getInvalidSortingParameterMessage() {
        List<String> fieldNames = Arrays.stream(DriverResponse.class.getDeclaredFields())
                .map(Field::getName)
                .toList();

        String acceptableParams = String.join(", ", fieldNames);
        return String.format(INVALID_SORTING_PARAMETER_MESSAGE, INVALID_ORDER_BY, acceptableParams);
    }

    private static String getAccessToken(String username, String password) {
        return given()
                .baseUri(keycloak.getAuthServerUrl() + GET_ACCESS_TOKEN_URL)
                .contentType(ContentType.URLENC)
                .formParam("grant_type", "password")
                .formParam("client_id", "cab-aggregator-test")
                .formParam("username", username)
                .formParam("password", password)
                .post()
                .jsonPath()
                .get("access_token")
                .toString();
    }
}
