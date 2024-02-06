package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.response.PassengerResponse;

import java.util.UUID;

public interface PassengerService {

    PassengerResponse getPassengerById(UUID id);
}
