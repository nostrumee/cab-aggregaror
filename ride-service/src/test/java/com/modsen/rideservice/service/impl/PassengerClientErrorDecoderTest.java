package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.exception.PassengerNotFoundException;
import feign.codec.ErrorDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.modsen.rideservice.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PassengerClientErrorDecoderTest {

    private ErrorDecoder errorDecoder;

    @BeforeEach
    void setUp() {
        errorDecoder = new PassengerClientErrorDecoder();
    }

    @Test
    void decode_shouldReturnPassengerNotFoundException_whenPassengerNotExist() {
        var expected = new PassengerNotFoundException(PASSENGER_NOT_FOUND_MESSAGE);

        var response = getResponseWithErrorCode(404, PASSENGER_NOT_FOUND_MESSAGE);
        var actual = errorDecoder.decode("", response);

        assertThat(actual.getMessage()).isEqualTo(expected.getMessage());
    }
}
