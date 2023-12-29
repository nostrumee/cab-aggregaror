package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.exception.PassengerNotFoundException;
import feign.codec.ErrorDecoder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.modsen.rideservice.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PassengerClientErrorDecoderTest {

    private static ErrorDecoder errorDecoder;

    @BeforeAll
    static void beforeAll() {
        errorDecoder = new PassengerClientErrorDecoder();
    }

    @Test
    void decode_shouldReturnPassengerNotFoundException_whenPassengerNotExist() {
        // arrange
        var expected = new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE);
        var response = getResponseWithErrorCode(HttpStatus.NOT_FOUND.value(), PASSENGER_NOT_FOUND_MESSAGE);

        // act
        var actual = errorDecoder.decode("", response);

        // assert
        assertThat(actual.getMessage()).isEqualTo(expected.getMessage());
    }
}
