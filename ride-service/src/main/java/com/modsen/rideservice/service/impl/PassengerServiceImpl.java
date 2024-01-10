package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.client.PassengerClient;
import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.dto.response.PassengerResponse;
import com.modsen.rideservice.service.PassengerService;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerClient passengerClient;

    @Override
    @CircuitBreaker(name = "${passenger-service.name}", fallbackMethod = "getFallbackPassenger")
    public PassengerResponse getPassengerById(long id) {
        log.info("Retrieving passenger by id {}", id);

        return passengerClient.getPassengerById(id);
    }

    private PassengerResponse getFallbackPassenger(long id, RetryableException exception) {
        log.info("Fallback response from passenger service. Reason: {}", exception.getMessage());

        return PassengerResponse.builder()
                .id(0L)
                .firstName("fallback")
                .email("fallback")
                .build();
    }
}
