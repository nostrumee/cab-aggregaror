package com.modsen.rideservice.repository;

import com.modsen.rideservice.entity.Ride;
import com.modsen.rideservice.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {

    Page<Ride> findAllByDriverIdAndStatus(Long driverId, Status status, PageRequest pageRequest);

    Page<Ride> findAllByPassengerIdAndStatus(Long passengerId, Status status, PageRequest pageRequest);
}
