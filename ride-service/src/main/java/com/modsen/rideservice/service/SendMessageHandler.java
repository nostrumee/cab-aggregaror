package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.message.CreateRideMessage;

public interface SendMessageHandler {
    void handleRideOrderMessage(CreateRideMessage orderMessage);
}
