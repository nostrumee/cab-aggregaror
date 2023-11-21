package com.modsen.ratingservice.service;

import com.modsen.ratingservice.message.DriverRatingMessage;
import com.modsen.ratingservice.message.PassengerRatingMessage;

public interface RatingService {

    void ratePassenger(PassengerRatingMessage ratingMessage);

    void rateDriver(DriverRatingMessage ratingMessage);
}
