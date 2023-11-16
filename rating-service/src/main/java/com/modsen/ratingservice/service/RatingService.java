package com.modsen.ratingservice.service;

import com.modsen.ratingservice.dto.RatingResponse;
import com.modsen.ratingservice.dto.message.DriverRatingMessage;
import com.modsen.ratingservice.dto.message.PassengerRatingMessage;

public interface RatingService {

    void ratePassenger(PassengerRatingMessage ratingMessage);

    void rateDriver(DriverRatingMessage ratingMessage);

    RatingResponse getPassengerRating(long passengerId);

    RatingResponse getDriverRating(long driverId);
}
