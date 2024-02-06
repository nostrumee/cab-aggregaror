package com.modsen.ratingservice.service.impl;

import com.modsen.ratingservice.client.RideClient;
import com.modsen.ratingservice.dto.response.RideResponse;
import com.modsen.ratingservice.service.RideService;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideClient rideClient;

    @Override
    @CircuitBreaker(name = "${ride-service.name}", fallbackMethod = "getFallbackRide")
    public RideResponse getRideById(long id) {
        log.info("Retrieving a ride by id {}", id);

        return rideClient.getRideById(id);
    }

    private RideResponse getFallbackRide(long id, RetryableException exception) {
        log.info("Fallback response from passenger service. Reason: {}", exception.getMessage());

        return RideResponse.builder()
                .driverId(new UUID(0, 0))
                .passengerId(new UUID(0, 0))
                .build();
    }
}
