package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.client.DriverClient;
import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {

    private final DriverClient driverClient;

    @Override
    public DriverResponse getDriverById(long id) {
        log.info("Retrieving driver by id {}", id);

        return driverClient.getDriverById(id);
    }
}
