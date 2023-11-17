package com.modsen.ratingservice.repository;

import com.modsen.ratingservice.entity.PassengerRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface PassengerRatingRepository extends JpaRepository<PassengerRating, Long> {

    @Query("select avg(r.rating) from PassengerRating r where r.passengerId=:passengerId")
    BigDecimal findPassengerRating(@Param("passengerId") Long passengerId);
}
