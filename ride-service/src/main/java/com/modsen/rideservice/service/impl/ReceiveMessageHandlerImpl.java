package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.dto.message.AcceptRideMessage;
import com.modsen.rideservice.service.ReceiveMessageHandler;
import com.modsen.rideservice.service.RideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReceiveMessageHandlerImpl implements ReceiveMessageHandler {

    private final RideService rideService;

    @Override
    public void handleAcceptRideMessage(AcceptRideMessage acceptRideMessage) {
        log.info("Accept ride message received --- {}", acceptRideMessage);
        rideService.acceptRide(acceptRideMessage);
    }
}
