package com.modsen.ratingservice.service;

import com.modsen.ratingservice.dto.message.UpdateDriverRatingMessage;
import com.modsen.ratingservice.dto.message.UpdatePassengerRatingMessage;

public interface SendMessageHandler {

    void handleUpdatePassengerRatingMessage(UpdatePassengerRatingMessage updateRatingMessage);

    void handleUpdateDriverRatingMessage(UpdateDriverRatingMessage updateRatingMessage);
}
