package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.message.CreateRideMessage;
import com.modsen.rideservice.dto.message.RideStatusMessage;

public interface SendMessageHandler {
    void handleCreateRideMessage(CreateRideMessage orderMessage);

    void handleRideStatusMessage(RideStatusMessage rideStatusMessage);
}
