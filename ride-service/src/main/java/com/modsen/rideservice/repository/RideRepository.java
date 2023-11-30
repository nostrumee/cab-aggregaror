package com.modsen.rideservice.repository;

import com.modsen.rideservice.entity.Ride;
import com.modsen.rideservice.entity.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride, Long> {

    Page<Ride> findAllByDriverIdAndStatus(Long driverId, RideStatus status, PageRequest pageRequest);

    Page<Ride> findAllByPassengerIdAndStatus(Long passengerId, RideStatus status, PageRequest pageRequest);
}
