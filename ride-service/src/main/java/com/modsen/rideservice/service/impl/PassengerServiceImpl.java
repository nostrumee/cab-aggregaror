package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.client.PassengerClient;
import com.modsen.rideservice.dto.response.PassengerResponse;
import com.modsen.rideservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerClient passengerClient;

    @Override
    public PassengerResponse getPassengerById(long id) {
        log.info("Retrieving passenger by id {}", id);

        return passengerClient.getPassenger(id);
    }
}
