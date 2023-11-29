package com.modsen.ratingservice.service;

import com.modsen.ratingservice.dto.message.UpdateDriverRatingMessage;
import com.modsen.ratingservice.dto.message.UpdatePassengerRatingMessage;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import static com.modsen.ratingservice.util.IntegrationProperties.*;

@MessagingGateway
public interface KafkaChannelGateway extends SendMessageHandler {

    @Override
    @Gateway(requestChannel = UPDATE_PASSENGER_RATING_CHANNEL_NAME)
    void handleUpdatePassengerRatingMessage(UpdatePassengerRatingMessage updateRatingMessage);

    @Override
    @Gateway(requestChannel = UPDATE_DRIVER_RATING_CHANNEL_NAME)
    void handleUpdateDriverRatingMessage(UpdateDriverRatingMessage updateRatingMessage);
}
