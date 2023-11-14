package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.request.AcceptRideOrderRequest;
import com.modsen.rideservice.dto.request.CreateRideOrderRequest;
import com.modsen.rideservice.dto.response.RideOrderResponse;

public interface RideOrderService {

    RideOrderResponse createRideOrder(CreateRideOrderRequest createRequest);

    RideOrderResponse acceptRideOrder(AcceptRideOrderRequest acceptRequest);

    RideOrderResponse startRide(long id);

    RideOrderResponse finishRide(long id);
}
