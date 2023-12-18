package com.modsen.passengerservice.integration.conroller;

import com.modsen.passengerservice.config.TestcontainersConfig;
import com.modsen.passengerservice.dto.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.request.UpdatePassengerRequest;
import com.modsen.passengerservice.dto.response.*;
import com.modsen.passengerservice.mapper.PassengerMapper;
import com.modsen.passengerservice.repository.PassengerRepository;
import com.modsen.passengerservice.service.MessageService;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.modsen.passengerservice.util.ErrorMessages.*;
import static com.modsen.passengerservice.util.TestUtils.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestcontainersConfig.class
)
@Sql(
        scripts = {
                "classpath:sql/delete-data.sql",
                "classpath:sql/insert-data.sql"
        },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
@RequiredArgsConstructor
public class PassengerControllerIntegrationTest {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;
    private final MessageService messageService;

    @LocalServerPort
    private int port;

    @Test
    void getPassengerById_shouldReturnPassengerResponse_whenPassengerExists() {
        var passenger = passengerRepository.findById(DEFAULT_ID);
        var expected = passengerMapper.fromEntityToResponse(passenger.get());

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, DEFAULT_ID)
                .when()
                .get(GET_PASSENGER_BY_ID_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PassengerResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getPassengerById_shouldReturnNotFoundResponse_whenPassengerNotExist() {
        var expected = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(String.format(NOT_FOUND_WITH_ID_MESSAGE, NON_EXISTING_ID))
                .build();

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, NON_EXISTING_ID)
                .when()
                .get(GET_PASSENGER_BY_ID_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getPassengerPage_shouldReturnPassengerPageResponse_whenValidParamsPassed() {
        var passengerPage = passengerRepository.findAll(
                PageRequest.of(VALID_PAGE - 1, VALID_SIZE, Sort.by(VALID_ORDER_BY))
        );
        var passengers = passengerMapper.fromEntityListToResponseList(passengerPage.getContent());

        var expected = PassengerPageResponse.builder()
                .passengers(passengers)
                .pageNumber(PAGE_NUMBER)
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
                .get(GET_PASSENGER_PAGE_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PassengerPageResponse.class);

        assertThat(actual).isEqualTo(expected);
        assertThat(passengerRepository.findAll().size()).isEqualTo(10);
    }

    @Test
    void getPassengerPage_shouldReturnBadRequestResponse_whenInvalidPagePassed() throws Exception {
        var expected = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(INVALID_PAGE_PARAMETERS_MESSAGE)
                .build();

        var actual = given()
                .port(port)
                .params(Map.of(
                        PAGE_PARAM_NAME, INVALID_PAGE,
                        SIZE_PARAM_NAME, VALID_SIZE,
                        ORDER_BY_PARAM_NAME, VALID_ORDER_BY
                ))
                .when()
                .get(GET_PASSENGER_PAGE_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getPassengerPage_shouldReturnBadRequestResponse_whenInvalidSizePassed() {
        var expected = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(INVALID_PAGE_PARAMETERS_MESSAGE)
                .build();

        var actual = given()
                .port(port)
                .params(Map.of(
                        PAGE_PARAM_NAME, VALID_PAGE,
                        SIZE_PARAM_NAME, INVALID_SIZE,
                        ORDER_BY_PARAM_NAME, VALID_ORDER_BY
                ))
                .when()
                .get(GET_PASSENGER_PAGE_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getPassengerPage_shouldReturnBadRequestResponse_whenInvalidOrderByParamPassed() {
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
                .get(GET_PASSENGER_PAGE_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getPassengerPage_shouldReturnBadRequestResponse_whenPageParamTypeNotMatch() {
        given()
                .port(port)
                .params(Map.of(
                        PAGE_PARAM_NAME, INVALID_ORDER_BY,
                        SIZE_PARAM_NAME, VALID_SIZE,
                        ORDER_BY_PARAM_NAME, VALID_ORDER_BY
                ))
                .when()
                .get(GET_PASSENGER_PAGE_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo(INVALID_PARAMETER_TYPE_MESSAGE));
    }

    @Test
    void getPassengerPage_shouldReturnBadRequestResponse_whenSizeParamTypeNotMatch() {
        given()
                .port(port)
                .params(Map.of(
                        PAGE_PARAM_NAME, VALID_PAGE,
                        SIZE_PARAM_NAME, INVALID_ORDER_BY,
                        ORDER_BY_PARAM_NAME, VALID_ORDER_BY
                ))
                .when()
                .get(GET_PASSENGER_PAGE_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo(INVALID_PARAMETER_TYPE_MESSAGE));
    }

    @Test
    void addPassenger_shouldReturnPassengerResponse_whenDataIsValidAndUnique() {
        var createRequest = CreatePassengerRequest.builder()
                .firstName(NEW_FIRST_NAME)
                .lastName(NEW_LAST_NAME)
                .email(NEW_EMAIL)
                .phone(NEW_PHONE)
                .build();

        var expected = PassengerResponse.builder()
                .id(NEW_ID)
                .firstName(NEW_FIRST_NAME)
                .lastName(NEW_LAST_NAME)
                .email(NEW_EMAIL)
                .phone(NEW_PHONE)
                .rating(DEFAULT_RATING)
                .build();

        var actual = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(createRequest)
                .when()
                .post(ADD_PASSENGER_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(PassengerResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addPassenger_shouldReturnConflictResponse_whenDataNotUnique() {
        var createRequest = CreatePassengerRequest.builder()
                .firstName(NEW_FIRST_NAME)
                .lastName(NEW_LAST_NAME)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .build();

        var expected = AlreadyExistsResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(PASSENGER_ALREADY_EXISTS_MESSAGE)
                .errors(Map.of(
                        EMAIL_FIELD_NAME, String.format(PASSENGER_WITH_EMAIL_EXISTS_MESSAGE, DEFAULT_EMAIL),
                        PHONE_FIELD_NAME, String.format(PASSENGER_WITH_PHONE_EXISTS_MESSAGE, DEFAULT_PHONE)
                ))
                .build();

        var actual = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(createRequest)
                .when()
                .post(ADD_PASSENGER_PATH)
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .as(AlreadyExistsResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addPassenger_shouldReturnBadRequestResponse_whenDataNotValid() {
        String firstNameValidationMessage = messageService.getMessage(FIRST_NAME_VALIDATION_MESSAGE_KEY);
        String lastNameValidationMessage = messageService.getMessage(LAST_NAME_VALIDATION_MESSAGE_KEY);
        String emailValidationMessage = messageService.getMessage(EMAIL_VALIDATION_MESSAGE_KEY);
        String phoneValidationMessage = messageService.getMessage(PHONE_VALIDATION_MESSAGE_KEY);

        var createRequest = CreatePassengerRequest.builder()
                .firstName(null)
                .lastName(null)
                .email(INVALID_EMAIL)
                .phone(INVALID_PHONE)
                .build();

        var expected = ValidationErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(VALIDATION_FAILED_MESSAGE)
                .errors(Map.of(
                        FIRST_NAME_FIELD_NAME, firstNameValidationMessage,
                        LAST_NAME_FIELD_NAME, lastNameValidationMessage,
                        EMAIL_FIELD_NAME, emailValidationMessage,
                        PHONE_FIELD_NAME, phoneValidationMessage
                ))
                .build();

        var actual = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(createRequest)
                .when()
                .post(ADD_PASSENGER_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ValidationErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updatePassenger_shouldReturnPassengerResponse_whenDataIsValidAndUnique() {
        var updateRequest = UpdatePassengerRequest.builder()
                .firstName(NEW_FIRST_NAME)
                .lastName(NEW_LAST_NAME)
                .email(NEW_EMAIL)
                .phone(NEW_PHONE)
                .build();

        var expected = PassengerResponse.builder()
                .id(DEFAULT_ID)
                .firstName(NEW_FIRST_NAME)
                .lastName(NEW_LAST_NAME)
                .email(NEW_EMAIL)
                .phone(NEW_PHONE)
                .rating(DEFAULT_RATING)
                .build();

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, DEFAULT_ID)
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put(UPDATE_PASSENGER_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PassengerResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updatePassenger_shouldReturnNotFoundStatus_whenPassengerNotExist() {
        var updateRequest = UpdatePassengerRequest.builder()
                .firstName(NEW_FIRST_NAME)
                .lastName(NEW_LAST_NAME)
                .email(NEW_EMAIL)
                .phone(NEW_PHONE)
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
                .put(UPDATE_PASSENGER_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updatePassenger_shouldReturnConflictResponse_whenDataNotUnique() {
        var updateRequest = UpdatePassengerRequest.builder()
                .firstName(NEW_FIRST_NAME)
                .lastName(NEW_LAST_NAME)
                .email(OTHER_EMAIL)
                .phone(OTHER_PHONE)
                .build();

        var expected = AlreadyExistsResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(PASSENGER_ALREADY_EXISTS_MESSAGE)
                .errors(Map.of(
                        EMAIL_FIELD_NAME, String.format(PASSENGER_WITH_EMAIL_EXISTS_MESSAGE, OTHER_EMAIL),
                        PHONE_FIELD_NAME, String.format(PASSENGER_WITH_PHONE_EXISTS_MESSAGE, OTHER_PHONE)
                ))
                .build();

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, DEFAULT_ID)
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put(UPDATE_PASSENGER_PATH)
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .as(AlreadyExistsResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updatePassenger_shouldReturnBadRequestResponse_whenDataNotValid() {
        String firstNameValidationMessage = messageService.getMessage(FIRST_NAME_VALIDATION_MESSAGE_KEY);
        String lastNameValidationMessage = messageService.getMessage(LAST_NAME_VALIDATION_MESSAGE_KEY);
        String emailValidationMessage = messageService.getMessage(EMAIL_VALIDATION_MESSAGE_KEY);
        String phoneValidationMessage = messageService.getMessage(PHONE_VALIDATION_MESSAGE_KEY);

        var updateRequest = UpdatePassengerRequest.builder()
                .firstName(null)
                .lastName(null)
                .email(INVALID_EMAIL)
                .phone(INVALID_PHONE)
                .build();

        var expected = ValidationErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(VALIDATION_FAILED_MESSAGE)
                .errors(Map.of(
                        FIRST_NAME_FIELD_NAME, firstNameValidationMessage,
                        LAST_NAME_FIELD_NAME, lastNameValidationMessage,
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
                .put(UPDATE_PASSENGER_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ValidationErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deletePassenger_shouldDeletePassenger_whenPassengerExists() {
        var expected = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(String.format(NOT_FOUND_WITH_ID_MESSAGE, DEFAULT_ID))
                .build();

        given()
                .port(port)
                .pathParam(ID_PARAM_NAME, DEFAULT_ID)
                .when()
                .delete(DELETE_PASSENGER_PATH)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, DEFAULT_ID)
                .when()
                .get(GET_PASSENGER_BY_ID_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deletePassenger_shouldReturnNotFoundResponse_whenPassengerNotExist() {
        var expected = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(String.format(NOT_FOUND_WITH_ID_MESSAGE, NON_EXISTING_ID))
                .build();

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, NON_EXISTING_ID)
                .when()
                .delete(DELETE_PASSENGER_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    private String getInvalidSortingParameterMessage() {
        List<String> fieldNames = Arrays.stream(PassengerResponse.class.getDeclaredFields())
                .map(Field::getName)
                .toList();

        String acceptableParams = String.join(", ", fieldNames);
        return String.format(INVALID_SORTING_PARAMETER_MESSAGE, INVALID_ORDER_BY, acceptableParams);
    }
}
