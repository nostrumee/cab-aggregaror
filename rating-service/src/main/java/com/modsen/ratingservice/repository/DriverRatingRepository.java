package com.modsen.ratingservice.repository;

import com.modsen.ratingservice.entity.DriverRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface DriverRatingRepository extends JpaRepository<DriverRating, Long> {

    @Query("select avg(r.rating) from DriverRating r where r.driverId=:driverId")
    BigDecimal findDriverRating(@Param("driverId") Long driverId);
}
