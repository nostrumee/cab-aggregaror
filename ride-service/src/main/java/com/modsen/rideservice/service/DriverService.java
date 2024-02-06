package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.response.DriverResponse;

import java.util.UUID;

public interface DriverService {

    DriverResponse getDriverById(UUID id);
}
