package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.dto.request.CreateDriverRequest;
import com.modsen.driverservice.dto.request.UpdateDriverRequest;
import com.modsen.driverservice.dto.response.DriverPageResponse;
import com.modsen.driverservice.dto.response.DriverResponse;
import com.modsen.driverservice.service.DriverService;
import org.springframework.stereotype.Service;

@Service
public class DriverServiceImpl implements DriverService {

    @Override
    public DriverPageResponse getDriverPage(int page, int size, String orderBy) {
        return null;
    }

    @Override
    public DriverResponse getById(long id) {
        return null;
    }

    @Override
    public DriverResponse addDriver(CreateDriverRequest createRequest) {
        return null;
    }

    @Override
    public DriverResponse updateDriver(UpdateDriverRequest updateRequest, long id) {
        return null;
    }

    @Override
    public void deleteDriver(long id) {

    }
}
