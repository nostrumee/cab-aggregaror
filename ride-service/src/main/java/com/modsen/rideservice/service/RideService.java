package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.message.AcceptRideMessage;
import com.modsen.rideservice.dto.request.CreateRideRequest;
import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.dto.response.RidePageResponse;
import com.modsen.rideservice.dto.response.RideResponse;

import java.util.UUID;

public interface RideService {

    RidePageResponse getRidesPage(int page, int size, String orderBy);

    RidePageResponse getRidesByDriverId(UUID driverId, int page, int size, String orderBy);

    RidePageResponse getRidesByPassengerId(UUID passengerId, int page, int size, String orderBy);

    RideResponse getById(long id);

    RideResponse createRide(CreateRideRequest createRequest);

    void deleteRide(long id);

    void acceptRide(AcceptRideMessage acceptRequest);

    RideResponse startRide(long id);

    RideResponse finishRide(long id);

    DriverResponse getDriverProfile(long rideId);
}
