package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.exception.DriverNotFoundException;
import feign.codec.ErrorDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.modsen.rideservice.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DriverClientErrorDecoderTest {

    private ErrorDecoder errorDecoder;

    @BeforeEach
    void setUp() {
        errorDecoder = new DriverClientErrorDecoder();
    }

    @Test
    void decode_shouldReturnPassengerNotFoundException_whenPassengerNotExist() {
        var expected = new DriverNotFoundException(DRIVER_NOT_FOUND_MESSAGE);

        var response = getResponseWithErrorCode(404, DRIVER_NOT_FOUND_MESSAGE);
        var actual = errorDecoder.decode("", response);

        assertThat(actual.getMessage()).isEqualTo(expected.getMessage());
    }
}
