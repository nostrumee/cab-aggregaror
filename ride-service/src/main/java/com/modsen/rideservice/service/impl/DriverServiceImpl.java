package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.client.DriverClient;
import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.service.DriverService;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {

    private final DriverClient driverClient;

    @Override
    @CircuitBreaker(name = "${driver-service.name}", fallbackMethod = "getFallbackDriver")
    public DriverResponse getDriverById(long id) {
        log.info("Retrieving driver by id {}", id);
        return driverClient.getDriverById(id);
    }

    private DriverResponse getFallbackDriver(long id, RetryableException exception) {
        log.info("Fallback response from driver service. Reason: {}", exception.getMessage());

        return DriverResponse.builder()
                .firstName("fallback")
                .lastName("fallback")
                .email("fallback")
                .phone("fallback")
                .rating(0.0)
                .build();
    }
}
