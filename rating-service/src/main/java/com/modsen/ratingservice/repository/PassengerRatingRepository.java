package com.modsen.ratingservice.repository;

import com.modsen.ratingservice.entity.PassengerRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface PassengerRatingRepository extends JpaRepository<PassengerRating, Long> {

    @Query("SELECT AVG(r.rating) FROM PassengerRating r WHERE r.passengerId = :passengerId ORDER BY r.id DESC LIMIT 40")
    BigDecimal findPassengerRating(Long passengerId);
}
