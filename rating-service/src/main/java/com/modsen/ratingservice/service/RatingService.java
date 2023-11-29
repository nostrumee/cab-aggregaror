package com.modsen.ratingservice.service;

import com.modsen.ratingservice.dto.request.DriverRatingRequest;
import com.modsen.ratingservice.dto.request.PassengerRatingRequest;

public interface RatingService {

    void ratePassenger(PassengerRatingRequest ratingRequest);

    void rateDriver(DriverRatingRequest ratingMessage);
}
