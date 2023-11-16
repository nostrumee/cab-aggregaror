package com.modsen.driverservice.service;

import com.modsen.driverservice.dto.request.CreateDriverRequest;
import com.modsen.driverservice.dto.request.UpdateDriverRequest;
import com.modsen.driverservice.dto.response.DriverPageResponse;
import com.modsen.driverservice.dto.response.DriverResponse;
import com.modsen.driverservice.entity.Driver;

import java.util.List;

public interface DriverService {

    DriverPageResponse getDriverPage(int page, int size, String orderBy);

    List<DriverResponse> getAvailableDrivers();

    DriverResponse getById(long id);

    DriverResponse addDriver(CreateDriverRequest createRequest);

    DriverResponse updateDriver(UpdateDriverRequest updateRequest, long id);

    void deleteDriver(long id);

    void setAvailableStatus(long id);

    void setUnavailableStatus(long id);
}
