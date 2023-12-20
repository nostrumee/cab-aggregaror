package com.modsen.rideservice.integration.controller;

import com.modsen.rideservice.dto.response.ErrorResponse;
import com.modsen.rideservice.dto.response.RidePageResponse;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.integration.TestcontainersBase;
import com.modsen.rideservice.mapper.RideMapper;
import com.modsen.rideservice.repository.RideRepository;
import com.modsen.rideservice.service.MessageService;
import com.modsen.rideservice.service.SendMessageHandler;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.modsen.rideservice.util.ErrorMessages.*;
import static com.modsen.rideservice.util.TestUtils.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Sql(
        scripts = {
                "classpath:sql/delete-data.sql",
                "classpath:sql/insert-data.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@RequiredArgsConstructor
public class RideControllerIntegrationTest extends TestcontainersBase {

    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    private final MessageService messageService;

    @MockBean
    private SendMessageHandler sendMessageHandler;

    @LocalServerPort
    private int port;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        kafka.start();
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
                .get(GET_RIDE_PAGE_PATH)
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
                .get(GET_RIDE_PAGE_PATH)
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
                .get(GET_RIDE_PAGE_PATH)
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
                .get(GET_RIDE_PAGE_PATH)
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
                .get(GET_RIDE_PAGE_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo(INVALID_PARAMETER_TYPE_MESSAGE));
    }

    @Test
    void getRideById_shouldReturnDriverResponse_whenRideExists() {
        var passenger = rideRepository.findById(DEFAULT_ID);
        var expected = rideMapper.fromEntityToResponse(passenger.get());

        var actual = given()
                .port(port)
                .pathParam(ID_PARAM_NAME, DEFAULT_ID)
                .when()
                .get(GET_DRIVER_BY_ID_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(DriverResponse.class);

        assertThat(actual).isEqualTo(expected);
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
                .get(GET_DRIVER_BY_ID_PATH)
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
}
