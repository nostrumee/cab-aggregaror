package com.modsen.ratingservice.repository;

import com.modsen.ratingservice.entity.DriverRating;

import java.math.BigDecimal;

public interface DriverRatingRepository {

    void save(DriverRating rating);

    BigDecimal findDriverRating(long passengerId);
}
