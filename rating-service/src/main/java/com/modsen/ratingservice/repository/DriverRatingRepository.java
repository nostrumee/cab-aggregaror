package com.modsen.ratingservice.repository;

import com.modsen.ratingservice.entity.DriverRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface DriverRatingRepository extends JpaRepository<DriverRating, Long> {

    @Query("SELECT AVG(r.rating) FROM DriverRating r WHERE r.driverId = :driverId ORDER BY r.id DESC LIMIT 40")
    BigDecimal findDriverRating(Long driverId);
}
