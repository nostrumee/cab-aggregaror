package com.modsen.rideservice.repository;

import com.modsen.rideservice.entity.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {

    Page<Ride> findAllByDriverId(Long driverId, PageRequest pageRequest);

    Page<Ride> findAllByPassengerId(Long passengerId, PageRequest pageRequest);
}
