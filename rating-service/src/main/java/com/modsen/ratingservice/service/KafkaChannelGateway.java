package com.modsen.ratingservice.service;

import com.modsen.ratingservice.dto.message.DriverRatingMessage;
import com.modsen.ratingservice.dto.message.PassengerRatingMessage;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import static com.modsen.ratingservice.util.IntegrationProperties.*;

@MessagingGateway
public interface KafkaChannelGateway extends SendMessageHandler {

    @Override
    @Gateway(requestChannel = PASSENGER_RATING_CHANNEL_NAME)
    void handlePassengerRatingMessage(PassengerRatingMessage updateRatingMessage);

    @Override
    @Gateway(requestChannel = DRIVER_RATING_CHANNEL_NAME)
    void handleDriverRatingMessage(DriverRatingMessage updateRatingMessage);
}
