package com.modsen.ratingservice.service;

import com.modsen.ratingservice.dto.message.DriverRatingMessage;
import com.modsen.ratingservice.dto.message.PassengerRatingMessage;

public interface SendMessageHandler {

    void handlePassengerRatingMessage(PassengerRatingMessage updateRatingMessage);

    void handleDriverRatingMessage(DriverRatingMessage updateRatingMessage);
}
