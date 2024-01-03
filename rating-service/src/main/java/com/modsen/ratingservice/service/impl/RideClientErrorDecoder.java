package com.modsen.ratingservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.ratingservice.dto.response.ErrorResponse;
import com.modsen.ratingservice.exception.RideNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;

import static com.modsen.ratingservice.util.ErrorMessages.*;

@Slf4j
public class RideClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String requestUrl = response.request().url();
        Response.Body responseBody = response.body();
        int responseStatus = response.status();

        try (InputStream error = responseBody.asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();

            if (responseStatus == HttpStatus.NOT_FOUND.value()) {
                ErrorResponse errorResponse = mapper.readValue(error, ErrorResponse.class);

                log.error("Error during ride client request. Reason: {}", errorResponse.message());
                return new RideNotFoundException(errorResponse.message());
            } else {
                return new Exception(
                        String.format(RESPONSE_HANDLER_MISSING,
                                responseStatus,
                                requestUrl
                        )
                );
            }
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
    }
}
