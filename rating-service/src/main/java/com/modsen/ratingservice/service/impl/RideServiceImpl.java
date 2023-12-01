package com.modsen.ratingservice.service.impl;

import com.modsen.ratingservice.client.RideClient;
import com.modsen.ratingservice.dto.response.RideResponse;
import com.modsen.ratingservice.service.RideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideClient rideClient;

    @Override
    public RideResponse getRideById(long id) {
        log.info("Retrieving a ride by id {}", id);

        return rideClient.getRide(id);
    }
}
