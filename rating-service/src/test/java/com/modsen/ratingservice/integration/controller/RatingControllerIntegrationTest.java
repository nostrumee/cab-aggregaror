package com.modsen.ratingservice.integration.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.modsen.ratingservice.dto.request.DriverRatingRequest;
import com.modsen.ratingservice.dto.request.PassengerRatingRequest;
import com.modsen.ratingservice.dto.response.ErrorResponse;
import com.modsen.ratingservice.entity.RideStatus;
import com.modsen.ratingservice.integration.IntegrationTestBase;
import com.modsen.ratingservice.repository.DriverRatingRepository;
import com.modsen.ratingservice.repository.PassengerRatingRepository;
import com.modsen.ratingservice.service.MessageService;
import com.modsen.ratingservice.service.SendMessageHandler;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.modsen.ratingservice.util.ErrorMessages.INVALID_RIDE_STATUS_MESSAGE;
import static com.modsen.ratingservice.util.ErrorMessages.RATING_CREATE_ERROR_MESSAGE;
import static com.modsen.ratingservice.util.TestUtils.*;
import static com.modsen.ratingservice.util.UriPaths.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class RatingControllerIntegrationTest extends IntegrationTestBase {

    private final DriverRatingRepository driverRatingRepository;
    private final PassengerRatingRepository passengerRatingRepository;
    private final MessageService messageService;
    private final WireMockServer mockServer;
    private final MockMvc mockMvc;

    @MockBean
    private SendMessageHandler sendMessageHandler;

    @AfterEach
    void tearDown() {
        mockServer.resetAll();
    }

    @Test
    void rateDriver_shouldSaveDriverRating_whenRideExistsAndHasValidStatus_andDataValid() throws Exception {
        var rideResponse = getRideResponse(RideStatus.FINISHED);
        var ratingRequest = getDriverRatingRequest();
        var expected = 11;
        var requestBuilder = post(RATING_SERVICE_BASE_PATH + RATE_DRIVER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(fromObjectToString(ratingRequest));

        mockServer.stubFor(get(GET_RIDE_BY_ID_PATH).willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBody(fromObjectToString(rideResponse)))
        );

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());

        var actual = driverRatingRepository.count();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void rateDriver_shouldReturnBadRequestResponse_whenRideExistsAndHasValidStatus_andDataInvalid() throws Exception {
        String ratingValidationMessageKey = messageService.getMessage(RATING_VALIDATION_MESSAGE_KEY);
        String rideIdValidationMessageKey = messageService.getMessage(RIDE_ID_VALIDATION_MESSAGE_KEY);

        var ratingRequest = DriverRatingRequest.builder()
                .rideId(INVALID_RIDE_ID)
                .rating(INVALID_RATING)
                .build();
        var expected = getValidationErrorResponse(rideIdValidationMessageKey, ratingValidationMessageKey);
        var requestBuilder = post(RATING_SERVICE_BASE_PATH + RATE_DRIVER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(fromObjectToString(ratingRequest));

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(fromObjectToString(expected))
                );
    }

    @Test
    void rateDriver_shouldReturnConflictResponse_whenRideExistsAndHasInvalidStatus_andDataValid() throws Exception {
        var rideResponse = getRideResponse(RideStatus.STARTED);
        var ratingRequest = getDriverRatingRequest();
        var expected = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(String.format(INVALID_RIDE_STATUS_MESSAGE, RideStatus.FINISHED))
                .build();
        var requestBuilder = post(RATING_SERVICE_BASE_PATH + RATE_DRIVER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(fromObjectToString(ratingRequest));

        mockServer.stubFor(get(GET_RIDE_BY_ID_PATH).willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBody(fromObjectToString(rideResponse)))
        );

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isConflict(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(fromObjectToString(expected))
                );
    }

    @Test
    void rateDriver_shouldReturnBadRequestResponse_whenRideNotExists_andDataValid() throws Exception {
        var ratingRequest = getDriverRatingRequest();
        var rideNotFoundResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(RIDE_NOT_FOUND_MESSAGE)
                .build();
        var expected = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(String.format(RATING_CREATE_ERROR_MESSAGE, RIDE_NOT_FOUND_MESSAGE))
                .build();
        var requestBuilder = post(RATING_SERVICE_BASE_PATH + RATE_DRIVER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(fromObjectToString(ratingRequest));

        mockServer.stubFor(get(GET_RIDE_BY_ID_PATH).willReturn(aResponse()
                .withStatus(HttpStatus.NOT_FOUND.value())
                .withHeader("Content-Type", "application/json")
                .withBody(fromObjectToString(rideNotFoundResponse)))
        );

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(fromObjectToString(expected))
                );
    }

    @Test
    void ratePassenger_shouldSavePassengerRating_whenRideExistsAndHasValidStatus_andDataValid() throws Exception {
        var rideResponse = getRideResponse(RideStatus.FINISHED);
        var ratingRequest = getPassengerRatingRequest();
        var expected = 11;
        var requestBuilder = post(RATING_SERVICE_BASE_PATH + RATE_PASSENGER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(fromObjectToString(ratingRequest));

        mockServer.stubFor(get(GET_RIDE_BY_ID_PATH).willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBody(fromObjectToString(rideResponse)))
        );

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());

        var actual = passengerRatingRepository.count();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void ratePassenger_shouldReturnBadRequestResponse_whenRideExistsAndHasValidStatus_andDataInvalid() throws Exception {
        var ratingValidationMessageKey = messageService.getMessage(RATING_VALIDATION_MESSAGE_KEY);
        var rideIdValidationMessageKey = messageService.getMessage(RIDE_ID_VALIDATION_MESSAGE_KEY);
        var ratingRequest = PassengerRatingRequest.builder()
                .rideId(INVALID_RIDE_ID)
                .rating(INVALID_RATING)
                .build();
        var expected = getValidationErrorResponse(rideIdValidationMessageKey, ratingValidationMessageKey);
        var requestBuilder = post(RATING_SERVICE_BASE_PATH + RATE_PASSENGER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(fromObjectToString(ratingRequest));

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(fromObjectToString(expected))
                );
    }

    @Test
    void ratePassenger_shouldReturnConflictResponse_whenRideExistsAndHasInvalidStatus_andDataValid() throws Exception {
        var rideResponse = getRideResponse(RideStatus.STARTED);
        var ratingRequest = getPassengerRatingRequest();
        var expected = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(String.format(INVALID_RIDE_STATUS_MESSAGE, RideStatus.FINISHED))
                .build();
        var requestBuilder = post(RATING_SERVICE_BASE_PATH + RATE_PASSENGER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(fromObjectToString(ratingRequest));

        mockServer.stubFor(get(GET_RIDE_BY_ID_PATH).willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBody(fromObjectToString(rideResponse)))
        );

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isConflict(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(fromObjectToString(expected))
                );
    }

    @Test
    void ratePassenger_shouldReturnBadRequestResponse_whenRideNotExists_andDataValid() throws Exception {
        var ratingRequest = getPassengerRatingRequest();
        var rideNotFoundResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(RIDE_NOT_FOUND_MESSAGE)
                .build();
        var expected = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(String.format(RATING_CREATE_ERROR_MESSAGE, RIDE_NOT_FOUND_MESSAGE))
                .build();
        var requestBuilder = post(RATING_SERVICE_BASE_PATH + RATE_PASSENGER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(fromObjectToString(ratingRequest));

        mockServer.stubFor(get(GET_RIDE_BY_ID_PATH).willReturn(aResponse()
                .withStatus(HttpStatus.NOT_FOUND.value())
                .withHeader("Content-Type", "application/json")
                .withBody(fromObjectToString(rideNotFoundResponse)))
        );

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(fromObjectToString(expected))
                );
    }
}
