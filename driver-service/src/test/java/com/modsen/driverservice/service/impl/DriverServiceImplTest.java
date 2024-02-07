package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.dto.response.DriverPageResponse;
import com.modsen.driverservice.dto.response.DriverResponse;
import com.modsen.driverservice.entity.Driver;
import com.modsen.driverservice.entity.DriverStatus;
import com.modsen.driverservice.exception.DriverAlreadyExistsException;
import com.modsen.driverservice.exception.DriverNotFoundException;
import com.modsen.driverservice.exception.InvalidRequestParamException;
import com.modsen.driverservice.mapper.DriverMapper;
import com.modsen.driverservice.repository.DriverRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.modsen.driverservice.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DriverServiceImplTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverMapper driverMapper;

    @InjectMocks
    private DriverServiceImpl driverService;

    @Test
    void getDriverPage_shouldReturnPassengerPage_whenValidParamsPassed() {
        // arrange
        var driverResponseList = getDriverResponseList();
        var driversPage = getDriverPage();
        var retrievedDrivers = getDriverList();
        var pageRequest = getPageRequest(VALID_PAGE, VALID_SIZE, VALID_ORDER_BY);
        var expected = DriverPageResponse.builder()
                .drivers(driverResponseList)
                .pageNumber(VALID_PAGE)
                .total(2L)
                .build();

        doReturn(driversPage)
                .when(driverRepository)
                .findAll(pageRequest);
        doReturn(driverResponseList)
                .when(driverMapper)
                .fromEntityListToResponseList(retrievedDrivers);

        // act
        var actual = driverService.getDriverPage(VALID_PAGE, VALID_SIZE, VALID_ORDER_BY);

        // assert
        assertThat(actual).isEqualTo(expected);
        verify(driverRepository).findAll(pageRequest);
        verify(driverMapper).fromEntityListToResponseList(retrievedDrivers);
    }

    @ParameterizedTest
    @MethodSource("getInvalidParamsForGetDriverPageTest")
    void getDriverPage_shouldThrowInvalidRequestParamException_whenInvalidParamsPassed(int page, int size, String orderBy) {
        // act and assert
        assertThrows(
                InvalidRequestParamException.class,
                () -> driverService.getDriverPage(page, size, orderBy)
        );
    }

    @Test
    void getAvailableDrivers_shouldReturnNotEmptyList_whenThereAreAvailableDrivers() {
        // arrange
        var retrievedDrivers = List.of(getDefaultDriver());

        doReturn(retrievedDrivers)
                .when(driverRepository)
                .findAllByStatus(DriverStatus.AVAILABLE);
        doReturn(List.of(getDefaultDriverResponse()))
                .when(driverMapper)
                .fromEntityListToResponseList(retrievedDrivers);

        // act
        var actual = driverService.getAvailableDrivers();

        // assert
        assertThat(actual).isNotEmpty();
        verify(driverRepository).findAllByStatus(DriverStatus.AVAILABLE);
    }

    @Test
    void getAvailableDrivers_shouldReturnEmptyList_whenThereAreNoAvailableDrivers() {
        // arrange
        List<Driver> retrievedDrivers = Collections.emptyList();

        doReturn(retrievedDrivers)
                .when(driverRepository)
                .findAllByStatus(DriverStatus.AVAILABLE);
        doReturn(new ArrayList<DriverResponse>())
                .when(driverMapper)
                .fromEntityListToResponseList(retrievedDrivers);

        // act
        var actual = driverService.getAvailableDrivers();

        // assert
        assertThat(actual).isEmpty();
        verify(driverRepository).findAllByStatus(DriverStatus.AVAILABLE);
    }

    @Test
    void getById_shouldReturnDriverResponse_whenDriverExists() {
        // arrange
        var expected = getDefaultDriverResponse();
        var retrievedDriver = getDefaultDriver();

        doReturn(Optional.of(retrievedDriver))
                .when(driverRepository)
                .findByExternalId(DEFAULT_EXTERNAL_ID);
        doReturn(expected)
                .when(driverMapper)
                .fromEntityToResponse(retrievedDriver);

        // act
        var actual = driverService.getById(DEFAULT_EXTERNAL_ID);

        // assert
        assertThat(actual).isEqualTo(expected);
        verify(driverRepository).findByExternalId(DEFAULT_EXTERNAL_ID);
        verify(driverMapper).fromEntityToResponse(retrievedDriver);
    }

    @Test
    void getById_shouldThrowDriverNotFoundException_whenDriverNotExist() {
        // arrange
        doReturn(Optional.empty())
                .when(driverRepository)
                .findByExternalId(DEFAULT_EXTERNAL_ID);

        // act and assert
        assertThrows(
                DriverNotFoundException.class,
                () -> driverService.getById(DEFAULT_EXTERNAL_ID)
        );
        verify(driverRepository).findByExternalId(DEFAULT_EXTERNAL_ID);
    }

    @Test
    void addDriver_shouldReturnDriverResponse_whenDataUnique() {
        // arrange
        var expected = getDefaultDriverResponse();
        var driverToSave = getNotSavedDriver();
        var savedDriver = getDefaultDriver();
        var createRequest = getCreateDriverRequest();

        doReturn(false)
                .when(driverRepository)
                .existsByEmail(createRequest.email());
        doReturn(false)
                .when(driverRepository)
                .existsByPhone(createRequest.phone());
        doReturn(false)
                .when(driverRepository)
                .existsByLicenceNumber(createRequest.licenceNumber());
        doReturn(driverToSave)
                .when(driverMapper)
                .fromCreateRequestToEntity(createRequest);
        doReturn(savedDriver)
                .when(driverRepository)
                .save(driverToSave);
        doReturn(expected)
                .when(driverMapper)
                .fromEntityToResponse(savedDriver);

        // act
        var actual = driverService.addDriver(createRequest, DEFAULT_EXTERNAL_ID);

        // assert
        assertThat(actual).isEqualTo(expected);

        verify(driverRepository).existsByEmail(createRequest.email());
        verify(driverRepository).existsByPhone(createRequest.phone());
        verify(driverRepository).existsByLicenceNumber(createRequest.licenceNumber());
        verify(driverRepository).save(driverToSave);

        verify(driverMapper).fromCreateRequestToEntity(createRequest);
        verify(driverMapper).fromEntityToResponse(savedDriver);
    }

    @Test
    void addDriver_shouldThrowDriverAlreadyExistsException_whenEmailNotUnique() {
        // arrange
        var createRequest = getCreateDriverRequest();

        doReturn(true)
                .when(driverRepository)
                .existsByEmail(createRequest.email());
        doReturn(false)
                .when(driverRepository)
                .existsByPhone(createRequest.phone());
        doReturn(false)
                .when(driverRepository)
                .existsByLicenceNumber(createRequest.licenceNumber());

        // act and assert
        assertThrows(
                DriverAlreadyExistsException.class,
                () -> driverService.addDriver(createRequest, DEFAULT_EXTERNAL_ID)
        );
        verify(driverRepository).existsByEmail(createRequest.email());
        verify(driverRepository).existsByPhone(createRequest.phone());
        verify(driverRepository).existsByLicenceNumber(createRequest.licenceNumber());
    }

    @Test
    void addDriver_shouldThrowDriverAlreadyExistsException_whenPhoneNotUnique() {
        // arrange
        var createRequest = getCreateDriverRequest();

        doReturn(false)
                .when(driverRepository)
                .existsByEmail(createRequest.email());
        doReturn(true)
                .when(driverRepository)
                .existsByPhone(createRequest.phone());
        doReturn(false)
                .when(driverRepository)
                .existsByLicenceNumber(createRequest.licenceNumber());

        // act and assert
        assertThrows(
                DriverAlreadyExistsException.class,
                () -> driverService.addDriver(createRequest, DEFAULT_EXTERNAL_ID)
        );
        verify(driverRepository).existsByEmail(createRequest.email());
        verify(driverRepository).existsByPhone(createRequest.phone());
        verify(driverRepository).existsByLicenceNumber(createRequest.licenceNumber());
    }

    @Test
    void addDriver_shouldThrowDriverAlreadyExistsException_whenLicenceNumberNotUnique() {
        // arrange
        var createRequest = getCreateDriverRequest();

        doReturn(false)
                .when(driverRepository)
                .existsByEmail(createRequest.email());
        doReturn(false)
                .when(driverRepository)
                .existsByPhone(createRequest.phone());
        doReturn(true)
                .when(driverRepository)
                .existsByLicenceNumber(createRequest.licenceNumber());

        // act and assert
        assertThrows(
                DriverAlreadyExistsException.class,
                () -> driverService.addDriver(createRequest, DEFAULT_EXTERNAL_ID)
        );
        verify(driverRepository).existsByEmail(createRequest.email());
        verify(driverRepository).existsByPhone(createRequest.phone());
        verify(driverRepository).existsByLicenceNumber(createRequest.licenceNumber());
    }

    @Test
    void updateDriver_shouldReturnDriverResponse_whenDriverExistsAndDataUnique() {
        // arrange
        var expected = getUpdatedDriverResponse();
        var driver = getDefaultDriver();
        var updateRequest = getUpdateDriverRequest();

        doReturn(Optional.of(driver))
                .when(driverRepository)
                .findByExternalId(DEFAULT_EXTERNAL_ID);
        doReturn(false)
                .when(driverRepository)
                .existsByEmail(updateRequest.email());
        doReturn(false)
                .when(driverRepository)
                .existsByPhone(updateRequest.phone());
        doReturn(false)
                .when(driverRepository)
                .existsByLicenceNumber(updateRequest.licenceNumber());
        doReturn(expected)
                .when(driverMapper)
                .fromEntityToResponse(driver);

        // act
        var actual = driverService.updateDriver(updateRequest, DEFAULT_EXTERNAL_ID);

        // assert
        assertThat(actual).isEqualTo(expected);

        verify(driverRepository).findByExternalId(DEFAULT_EXTERNAL_ID);
        verify(driverRepository).existsByEmail(updateRequest.email());
        verify(driverRepository).existsByPhone(updateRequest.phone());
        verify(driverRepository).save(driver);

        verify(driverMapper).updateEntityFromUpdateRequest(updateRequest, driver);
        verify(driverMapper).fromEntityToResponse(driver);
    }

    @Test
    void updateDriver_shouldThrowDriverNotFoundException_whenDriverNotExist() {
        // arrange
        var updateRequest = getUpdateDriverRequest();
        doReturn(Optional.empty())
                .when(driverRepository)
                .findByExternalId(DEFAULT_EXTERNAL_ID);

        // act and assert
        assertThrows(
                DriverNotFoundException.class,
                () -> driverService.updateDriver(updateRequest, DEFAULT_EXTERNAL_ID)
        );
        verify(driverRepository).findByExternalId(DEFAULT_EXTERNAL_ID);
    }

    @Test
    void updateDriver_shouldThrowDriverAlreadyExistsException_whenEmailNotUnique() {
        // arrange
        var driver = getDefaultDriver();
        var updateRequest = getUpdateDriverRequest();

        doReturn(Optional.of(driver))
                .when(driverRepository)
                .findByExternalId(DEFAULT_EXTERNAL_ID);
        doReturn(true)
                .when(driverRepository)
                .existsByEmail(updateRequest.email());
        doReturn(false)
                .when(driverRepository)
                .existsByPhone(updateRequest.phone());
        doReturn(false)
                .when(driverRepository)
                .existsByLicenceNumber(updateRequest.licenceNumber());

        // act and assert
        assertThrows(
                DriverAlreadyExistsException.class,
                () -> driverService.updateDriver(updateRequest, DEFAULT_EXTERNAL_ID)
        );
        verify(driverRepository).findByExternalId(DEFAULT_EXTERNAL_ID);
        verify(driverRepository).existsByEmail(OTHER_EMAIL);
        verify(driverRepository).existsByPhone(OTHER_PHONE);
    }

    @Test
    void updateDriver_shouldThrowDriverAlreadyExistsException_whenPhoneNotUnique() {
        // arrange
        var driver = getDefaultDriver();
        var updateRequest = getUpdateDriverRequest();

        doReturn(Optional.of(driver))
                .when(driverRepository)
                .findByExternalId(DEFAULT_EXTERNAL_ID);
        doReturn(false)
                .when(driverRepository)
                .existsByEmail(updateRequest.email());
        doReturn(true)
                .when(driverRepository)
                .existsByPhone(updateRequest.phone());
        doReturn(false)
                .when(driverRepository)
                .existsByLicenceNumber(updateRequest.licenceNumber());

        // act and assert
        assertThrows(
                DriverAlreadyExistsException.class,
                () -> driverService.updateDriver(updateRequest, DEFAULT_EXTERNAL_ID)
        );
        verify(driverRepository).findByExternalId(DEFAULT_EXTERNAL_ID);
        verify(driverRepository).existsByEmail(OTHER_EMAIL);
        verify(driverRepository).existsByPhone(OTHER_PHONE);
    }

    @Test
    void updateDriver_shouldThrowDriverAlreadyExistsException_whenLicenceNumberNotUnique() {
        // arrange
        var driver = getDefaultDriver();
        var updateRequest = getUpdateDriverRequest();

        doReturn(Optional.of(driver))
                .when(driverRepository)
                .findByExternalId(DEFAULT_EXTERNAL_ID);
        doReturn(false)
                .when(driverRepository)
                .existsByEmail(updateRequest.email());
        doReturn(false)
                .when(driverRepository)
                .existsByPhone(updateRequest.phone());
        doReturn(true)
                .when(driverRepository)
                .existsByLicenceNumber(updateRequest.licenceNumber());

        // act and assert
        assertThrows(
                DriverAlreadyExistsException.class,
                () -> driverService.updateDriver(updateRequest, DEFAULT_EXTERNAL_ID)
        );
        verify(driverRepository).findByExternalId(DEFAULT_EXTERNAL_ID);
        verify(driverRepository).existsByEmail(OTHER_EMAIL);
        verify(driverRepository).existsByPhone(OTHER_PHONE);
    }

    @Test
    void deleteDriver_shouldDeleteDriver_whenDriverExists() {
        // arrange
        var driver = getDefaultDriver();

        doReturn(Optional.of(driver))
                .when(driverRepository)
                .findByExternalId(DEFAULT_EXTERNAL_ID);

        // act
        driverService.deleteDriver(DEFAULT_EXTERNAL_ID);

        // assert
        verify(driverRepository).findByExternalId(DEFAULT_EXTERNAL_ID);
        verify(driverRepository).delete(driver);
    }

    @Test
    void deleteDriver_shouldThrowDriverNotFoundException_whenDriverNotExist() {
        // arrange
        doReturn(Optional.empty())
                .when(driverRepository)
                .findByExternalId(DEFAULT_EXTERNAL_ID);

        // act and assert
        assertThrows(
                DriverNotFoundException.class,
                () -> driverService.deleteDriver(DEFAULT_EXTERNAL_ID)
        );
        verify(driverRepository).findByExternalId(DEFAULT_EXTERNAL_ID);
    }

    @Test
    void updateDriverRating_shouldUpdateRating_whenDriverExists() {
        // arrange
        var ratingMessage = getDriverRatingMessage();
        var driver = getDefaultDriver();
        var expected = ratingMessage.rating();

        doReturn(Optional.of(driver))
                .when(driverRepository)
                .findByExternalId(ratingMessage.driverId());

        // act
        driverService.updateDriverRating(ratingMessage);

        // assert
        var actual = driver.getRating();
        assertThat(actual).isEqualTo(expected);
        verify(driverRepository).findByExternalId(ratingMessage.driverId());
        verify(driverRepository).save(driver);
    }

    @Test
    void updateDriverRating_shouldThrowDriverNotFoundException_whenDriverNotExist() {
        // arrange
        var ratingMessage = getDriverRatingMessage();

        doReturn(Optional.empty())
                .when(driverRepository)
                .findByExternalId(ratingMessage.driverId());

        // act and assert
        assertThrows(
                DriverNotFoundException.class,
                () -> driverService.updateDriverRating(ratingMessage)
        );
        verify(driverRepository).findByExternalId(ratingMessage.driverId());
    }

    @Test
    void updateDriverStatus_shouldUpdateStatus_whenDriverExists() {
        // arrange
        var statusMessage = getDriverStatusMessage();
        var driver = getDefaultDriver();
        var expected = statusMessage.status();

        doReturn(Optional.of(driver))
                .when(driverRepository)
                .findByExternalId(statusMessage.driverId());

        // act
        driverService.updateDriverStatus(statusMessage.driverId(), statusMessage.status());

        // assert
        var actual = driver.getStatus();
        assertThat(actual).isEqualTo(expected);
        verify(driverRepository).findByExternalId(statusMessage.driverId());
        verify(driverRepository).save(driver);
    }

    @Test
    void updateDriverStatus_shouldThrowDriverNotFoundException_whenDriverNotExist() {
        // arrange
        var statusMessage = getDriverStatusMessage();

        doReturn(Optional.empty())
                .when(driverRepository)
                .findByExternalId(statusMessage.driverId());

        // act and assert
        assertThrows(
                DriverNotFoundException.class,
                () -> driverService.updateDriverStatus(statusMessage.driverId(), statusMessage.status())
        );
        verify(driverRepository).findByExternalId(statusMessage.driverId());
    }

    private static Stream<Arguments> getInvalidParamsForGetDriverPageTest() {
        return Stream.of(
                Arguments.of(INVALID_PAGE, VALID_SIZE, VALID_ORDER_BY),
                Arguments.of(VALID_PAGE, INVALID_SIZE, VALID_ORDER_BY),
                Arguments.of(VALID_PAGE, VALID_SIZE, INVALID_ORDER_BY)
        );
    }
}
