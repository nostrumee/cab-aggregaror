package com.modsen.driverservice.service;

import com.modsen.driverservice.dto.message.DriverRatingMessage;
import com.modsen.driverservice.dto.request.CreateDriverRequest;
import com.modsen.driverservice.dto.request.UpdateDriverRequest;
import com.modsen.driverservice.dto.response.DriverPageResponse;
import com.modsen.driverservice.dto.response.DriverResponse;
import com.modsen.driverservice.entity.DriverStatus;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

public interface DriverService {

    DriverPageResponse getDriverPage(int page, int size, String orderBy);

    List<DriverResponse> getAvailableDrivers();

    DriverResponse getById(UUID id);

    DriverResponse addDriver(CreateDriverRequest createRequest, UUID externalId);

    DriverResponse updateDriver(UpdateDriverRequest updateRequest, UUID id);

    void deleteDriver(UUID id);

    void updateDriverStatus(UUID id, DriverStatus status);

    void updateDriverRating(DriverRatingMessage updateRatingMessage);
}
