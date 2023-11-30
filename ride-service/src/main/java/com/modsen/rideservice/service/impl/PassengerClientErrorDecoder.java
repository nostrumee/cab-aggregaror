package com.modsen.rideservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.rideservice.dto.response.ErrorResponse;
import com.modsen.rideservice.exception.PassengerNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;

import static com.modsen.rideservice.util.ErrorMessages.*;

public class PassengerClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String requestUrl = response.request().url();
        Response.Body responseBody = response.body();
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());

        try (InputStream error = responseBody.asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();

            if (responseStatus.equals(HttpStatus.NOT_FOUND)) {
                ErrorResponse errorResponse = mapper.readValue(error, ErrorResponse.class);
                return new PassengerNotFoundException(errorResponse.message());
            } else {
                return new Exception(
                        String.format(RESPONSE_HANDLER_MISSING,
                                responseStatus.value(),
                                requestUrl
                        )
                );
            }
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
    }
}
