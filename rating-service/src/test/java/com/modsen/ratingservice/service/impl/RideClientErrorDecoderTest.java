package com.modsen.ratingservice.service.impl;

import com.modsen.ratingservice.exception.RideNotFoundException;
import feign.codec.ErrorDecoder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.modsen.ratingservice.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

public class RideClientErrorDecoderTest {

    private static ErrorDecoder errorDecoder;

    @BeforeAll
    static void beforeAll() {
        errorDecoder = new RideClientErrorDecoder();
    }

    @Test
    void decode_shouldReturnPassengerNotFoundException_whenPassengerNotExist() {
        // arrange
        var expected = new RideNotFoundException(RIDE_NOT_FOUND_MESSAGE);
        var response = getResponseWithErrorCode(HttpStatus.NOT_FOUND.value(), RIDE_NOT_FOUND_MESSAGE);

        // act
        var actual = errorDecoder.decode("", response);

        // assert
        assertThat(actual.getMessage()).isEqualTo(expected.getMessage());
    }

}
