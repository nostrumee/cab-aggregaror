package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.message.AcceptRideMessage;
import com.modsen.rideservice.dto.request.CreateRideRequest;
import com.modsen.rideservice.dto.request.RatingRequest;
import com.modsen.rideservice.dto.response.RidePageResponse;
import com.modsen.rideservice.dto.response.RideResponse;

public interface RideService {

    RidePageResponse getAllRides(int page, int size, String orderBy);

    RidePageResponse getAllRidesByDriverId(long driverId, int page, int size, String orderBy);

    RidePageResponse getAllRidesByPassengerId(long passengerId, int page, int size, String orderBy);

    RideResponse getById(long id);

    RideResponse createRide(CreateRideRequest createRequest);

    void deleteRide(long id);

    RideResponse acceptRide(AcceptRideMessage acceptRequest);

    RideResponse startRide(long id);

    RideResponse finishRide(long id);

    void rateDriver(RatingRequest ratingRequest, long rideId);

    void ratePassenger(RatingRequest ratingRequest, long rideId);
}
