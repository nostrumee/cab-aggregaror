package com.modsen.ratingservice.repository;

import com.modsen.ratingservice.entity.DriverRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface DriverRatingRepository extends JpaRepository<DriverRating, Long> {

    BigDecimal findDriverRating(long driverId);
}
