package com.modsen.ratingservice.repository;

import com.modsen.ratingservice.entity.PassengerRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface PassengerRatingRepository extends JpaRepository<PassengerRating, Long> {

    BigDecimal findPassengerRating(UUID passengerId);
}
