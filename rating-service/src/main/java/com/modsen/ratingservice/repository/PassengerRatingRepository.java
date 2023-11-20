package com.modsen.ratingservice.repository;

import com.modsen.ratingservice.entity.PassengerRating;

import java.math.BigDecimal;

public interface PassengerRatingRepository {

    void save(PassengerRating rating);

    BigDecimal findPassengerRating(long passengerId);
}
