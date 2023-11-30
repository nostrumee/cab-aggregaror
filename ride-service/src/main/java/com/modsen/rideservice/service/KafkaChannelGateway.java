package com.modsen.rideservice.service;

import com.modsen.rideservice.dto.message.CreateRideMessage;
import com.modsen.rideservice.dto.message.RideStatusMessage;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import static com.modsen.rideservice.util.IntegrationProperties.*;

@MessagingGateway
public interface KafkaChannelGateway extends SendMessageHandler {

    @Override
    @Gateway(requestChannel = CREATE_RIDE_CHANNEL_NAME)
    void handleCreateRideMessage(CreateRideMessage orderMessage);

    @Override
    @Gateway(requestChannel = RIDE_STATUS_CHANNEL_NAME)
    void handleRideStatusMessage(RideStatusMessage rideStatusMessage);
}
