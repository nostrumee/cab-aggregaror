package com.modsen.rideservice.repository;

import com.modsen.rideservice.entity.RideOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideOrderRepository extends JpaRepository<RideOrder, Long> {
}
