package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.dto.message.DriverRatingMessage;
import com.modsen.driverservice.dto.request.CreateDriverRequest;
import com.modsen.driverservice.dto.request.UpdateDriverRequest;
import com.modsen.driverservice.dto.response.DriverPageResponse;
import com.modsen.driverservice.dto.response.DriverResponse;
import com.modsen.driverservice.entity.Driver;
import com.modsen.driverservice.entity.Status;
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
import java.util.*;

import static com.modsen.driverservice.util.ErrorMessages.*;

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
    public List<DriverResponse> getAvailableDrivers() {
        log.info("Retrieving available drivers");

        List<Driver> availableDrivers = driverRepository.findAllByStatus(Status.AVAILABLE);
        return driverMapper.fromEntityListToResponseList(availableDrivers);
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

        checkCreateDataUnique(createRequest);

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

        checkUpdateDataUnique(updateRequest, driver);

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

    @Override
    public void setDriverStatus(long id, Status status) {
        log.info("Changing status of driver with id {}", id);

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Driver with id {} was not found", id);
                    return new DriverNotFoundException(id);
                });

        driver.setStatus(status);
        driverRepository.save(driver);
    }

    @Override
    public void updateDriverRating(DriverRatingMessage updateRatingMessage) {
        long id = updateRatingMessage.driverId();

        log.info("Updating rating of driver with id {}", id);

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Driver with id {} was not found", id);
                    return new DriverNotFoundException(id);
                });

        driver.setRating(updateRatingMessage.rating());
        driverRepository.save(driver);
    }

    private PageRequest getPageRequest(int page, int size, String orderBy) {
        if (page < 1 || size < 1) {
            log.error("Invalid request parameter passed: page: {}, size: {}", page, size);
            throw new InvalidRequestParamException(INVALID_PAGE_PARAMETERS_MESSAGE);
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
            String acceptableParams = String.join(", ", fieldNames);
            String errorMessage = String.format(INVALID_SORTING_PARAMETER_MESSAGE, orderBy, acceptableParams);
            throw new InvalidRequestParamException(errorMessage);
        }
    }

    private void checkLicenceNumberUnique(String licenceNumber, Map<String, String> errors) {
        if (driverRepository.existsByLicenceNumber(licenceNumber)) {
            log.error("Driver with licence number {} already exists", licenceNumber);
            errors.put(
                    "licenceNumber",
                    String.format(DRIVER_WITH_LICENCE_NUMBER_EXISTS_MESSAGE, licenceNumber)
            );
        }
    }

    private void checkEmailUnique(String email, Map<String, String> errors) {
        if (driverRepository.existsByEmail(email)) {
            log.error("Driver with email {} already exists", email);
            errors.put(
                    "email",
                    String.format(DRIVER_WITH_EMAIL_EXISTS_MESSAGE, email)
            );
        }
    }

    private void checkPhoneUnique(String phone, Map<String, String> errors) {
        if (driverRepository.existsByPhone(phone)) {
            log.error("Driver with phone {} already exists", phone);
            errors.put(
                    "phone",
                    String.format(DRIVER_WITH_PHONE_EXISTS_MESSAGE, phone)
            );
        }
    }

    private void checkCreateDataUnique(CreateDriverRequest createRequest) {
        var errors = new HashMap<String, String>();

        checkLicenceNumberUnique(createRequest.licenceNumber(), errors);
        checkEmailUnique(createRequest.email(), errors);
        checkPhoneUnique(createRequest.phone(), errors);

        if (!errors.isEmpty()) {
            throw new DriverAlreadyExistsException(errors);
        }
    }

    private void checkUpdateDataUnique(UpdateDriverRequest updateRequest, Driver driver) {
        var errors = new HashMap<String, String>();

        if (!Objects.equals(updateRequest.licenceNumber(), driver.getLicenceNumber())) {
            checkLicenceNumberUnique(updateRequest.licenceNumber(), errors);
        }

        if (!Objects.equals(updateRequest.email(), driver.getEmail())) {
            checkEmailUnique(updateRequest.email(), errors);
        }

        if (!Objects.equals(updateRequest.phone(), driver.getPhone())) {
            checkPhoneUnique(updateRequest.phone(), errors);
        }

        if (!errors.isEmpty()) {
            throw new DriverAlreadyExistsException(errors);
        }
    }
}
