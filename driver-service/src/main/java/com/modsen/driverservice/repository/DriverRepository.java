package com.modsen.driverservice.repository;

import com.modsen.driverservice.entity.Driver;
import com.modsen.driverservice.entity.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByExternalId(UUID id);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByLicenceNumber(String licenceNumber);

    boolean existsByExternalId(UUID externalId);

    List<Driver> findAllByStatus(DriverStatus status);
}
