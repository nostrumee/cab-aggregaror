package com.modsen.driverservice.repository;

import com.modsen.driverservice.entity.Driver;
import com.modsen.driverservice.entity.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByLicenceNumber(String licenceNumber);

    List<Driver> findAllByStatus(DriverStatus status);
}
