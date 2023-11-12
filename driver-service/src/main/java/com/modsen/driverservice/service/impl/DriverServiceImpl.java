package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.dto.request.CreateDriverRequest;
import com.modsen.driverservice.dto.request.UpdateDriverRequest;
import com.modsen.driverservice.dto.response.DriverPageResponse;
import com.modsen.driverservice.dto.response.DriverResponse;
import com.modsen.driverservice.entity.Driver;
import com.modsen.driverservice.exception.*;
import com.modsen.driverservice.mapper.DriverMapper;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    @Override
    public DriverPageResponse getDriverPage(int page, int size, String orderBy) {
        log.info("Retrieving drivers page");

        PageRequest pageRequest = getPageRequest(page, size, orderBy);
        Page<Driver> driversPage = driverRepository.findAll(pageRequest);

        List<Driver> retrievedDrivers = driversPage.getContent();
        Long total = driversPage.getTotalElements();

        List<DriverResponse> drivers =
                driverMapper.fromEntityListToResponseList(retrievedDrivers);

        return DriverPageResponse.builder()
                .drivers(drivers)
                .pageNumber(page)
                .total(total)
                .build();
    }

    @Override
    public DriverResponse getById(long id) {
        log.info("Retrieving driver by id {}", id);

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Driver with id {} was not found", id);
                    return new DriverNotFoundException(id);
                });

        return driverMapper.fromEntityToResponse(driver);
    }

    @Override
    public DriverResponse addDriver(CreateDriverRequest createRequest) {
        log.info("Adding driver");

        checkEmailAndPhoneUnique(createRequest.email(), createRequest.phone());

        Driver driverToCreate = driverMapper.fromCreateRequestToEntity(createRequest);
        Driver createdDriver = driverRepository.save(driverToCreate);

        return driverMapper.fromEntityToResponse(createdDriver);
    }

    @Override
    public DriverResponse updateDriver(UpdateDriverRequest updateRequest, long id) {
        log.info("Updating driver with id {}", id);

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Driver with id {} was not found", id);
                    return new DriverNotFoundException(id);
                });

        checkEmailAndPhoneUnique(updateRequest.email(), updateRequest.phone());

        driverMapper.updateEntityFromUpdateRequest(updateRequest, driver);
        driverRepository.save(driver);

        return driverMapper.fromEntityToResponse(driver);
    }

    @Override
    public void deleteDriver(long id) {
        log.info("Deleting driver with id {}", id);

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Driver with id {} was not found", id);
                    return new DriverNotFoundException(id);
                });

        driverRepository.delete(driver);
    }

    private PageRequest getPageRequest(int page, int size, String orderBy) {
        if (page < 1 || size < 1) {
            log.error("Invalid request parameter passed: page: {}, size: {}", page, size);
            throw new InvalidPageParameterException();
        }

        PageRequest pageRequest;
        if (orderBy == null) {
            pageRequest = PageRequest.of(page - 1, size);
        } else {
            validateSortingParameter(orderBy);
            pageRequest = PageRequest.of(page - 1, size, Sort.by(orderBy));
        }

        return pageRequest;
    }

    private void validateSortingParameter(String orderBy) {
        List<String> fieldNames = Arrays.stream(DriverResponse.class.getDeclaredFields())
                .map(Field::getName)
                .toList();

        if (!fieldNames.contains(orderBy)) {
            throw new InvalidSortingParameterException(orderBy, String.join(", ", fieldNames));
        }
    }

    private void checkEmailAndPhoneUnique(String email, String phone) {
        if (driverRepository.existsByEmail(email)) {
            log.error("Email {} is already taken", email);
            throw new EmailTakenException(email);
        }

        if (driverRepository.existsByPhone(phone)) {
            log.error("Phone {} is already taken", phone);
            throw new PhoneTakenException(phone);
        }
    }
}
