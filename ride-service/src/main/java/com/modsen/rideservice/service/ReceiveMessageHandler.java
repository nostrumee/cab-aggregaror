package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.message.AcceptRideMessage;

public interface ReceiveMessageHandler {
    void handleAcceptRideMessage(AcceptRideMessage acceptRideMessage);
}
