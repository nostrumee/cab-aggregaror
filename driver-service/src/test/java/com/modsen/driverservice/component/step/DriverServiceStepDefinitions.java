package com.modsen.driverservice.component.step;

import com.modsen.driverservice.dto.message.DriverRatingMessage;
import com.modsen.driverservice.dto.request.CreateDriverRequest;
import com.modsen.driverservice.dto.response.DriverResponse;
import com.modsen.driverservice.entity.Driver;
import com.modsen.driverservice.entity.DriverStatus;
import com.modsen.driverservice.exception.DriverAlreadyExistsException;
import com.modsen.driverservice.exception.DriverNotFoundException;
import com.modsen.driverservice.mapper.DriverMapper;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.service.DriverService;
import com.modsen.driverservice.service.impl.DriverServiceImpl;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;
import java.util.UUID;

import static com.modsen.driverservice.util.ErrorMessages.*;
import static com.modsen.driverservice.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


public class DriverServiceStepDefinitions {

    private DriverRepository driverRepository;
    private DriverMapper driverMapper;
    private DriverService driverService;

    private DriverResponse driverResponse;
    private Exception exception;

    @Before
    public void setUp() {
        this.driverRepository = mock(DriverRepository.class);
        this.driverMapper = mock(DriverMapper.class);
        this.driverService = new DriverServiceImpl(driverRepository, driverMapper);
    }

    @Given("A driver with id {string} exists")
    public void driverWithIdExists(String id) {
        var expected = getDefaultDriverResponse();
        var retrievedDriver = getDefaultDriver();

        doReturn(Optional.of(retrievedDriver))
                .when(driverRepository)
                .findByExternalId(UUID.fromString(id));
        doReturn(expected)
                .when(driverMapper)
                .fromEntityToResponse(retrievedDriver);

        var driver = driverRepository.findByExternalId(UUID.fromString(id));
        assertThat(driver.isPresent()).isEqualTo(true);
    }

    @Given("A driver with id {string} doesn't exist")
    public void driverWithIdNotExist(String id) {
        var driver = driverRepository.findByExternalId(UUID.fromString(id));
        assertThat(driver.isPresent()).isEqualTo(false);
    }

    @When("The id {string} is passed to the getById method")
    public void idPassedToGetByIdMethod(String id) {
        try {
            driverResponse = driverService.getById(UUID.fromString(id));
        } catch (DriverNotFoundException e) {
            exception = e;
        }
    }

    @Then("The response should contain details of the driver with id {string}")
    public void responseContainsDriverDetails(String id) {
        var driver = driverRepository.findByExternalId(UUID.fromString(id)).get();
        var expected = driverMapper.fromEntityToResponse(driver);

        assertThat(driverResponse).isEqualTo(expected);
    }

    @Then("The DriverNotFoundException with the message containing id {string} should be thrown")
    public void driverNotFoundExceptionThrown(String id) {
        var expected = String.format(NOT_FOUND_WITH_ID_MESSAGE, id);
        var actual = exception.getMessage();

        assertThat(actual).isEqualTo(expected);
    }

    @Given("A driver with licence number {string}, email {string} and phone {string} doesn't exist")
    public void passengerWithEmailAndPhoneNotExist(String licenceNumber, String email, String phone) {
        var expected = getDefaultDriverResponse();

        var driverToSave = getNotSavedDriver();
        var savedDriver = getDefaultDriver();
        var createRequest = getCreateDriverRequest();

        doReturn(false)
                .when(driverRepository)
                .existsByEmail(email);
        doReturn(false)
                .when(driverRepository)
                .existsByPhone(phone);
        doReturn(false)
                .when(driverRepository)
                .existsByLicenceNumber(licenceNumber);
        doReturn(driverToSave)
                .when(driverMapper)
                .fromCreateRequestToEntity(createRequest);
        doReturn(savedDriver)
                .when(driverRepository)
                .save(driverToSave);
        doReturn(expected)
                .when(driverMapper)
                .fromEntityToResponse(savedDriver);

        assertThat(driverRepository.existsByEmail(email)).isEqualTo(false);
        assertThat(driverRepository.existsByPhone(phone)).isEqualTo(false);
    }

    @Given("A driver with email {string} exists")
    public void driverWithEmailExists(String email) {
        doReturn(true)
                .when(driverRepository)
                .existsByEmail(email);

        assertThat(driverRepository.existsByEmail(email)).isEqualTo(true);
    }

    @Given("A driver with phone {string} exists")
    public void driverWithPhoneExists(String phone) {
        doReturn(true)
                .when(driverRepository)
                .existsByPhone(phone);

        assertThat(driverRepository.existsByPhone(phone)).isEqualTo(true);
    }

    @Given("A driver with licence number {string} exists")
    public void driverWithLicenceNumberExists(String licenceNumber) {
        doReturn(true)
                .when(driverRepository)
                .existsByLicenceNumber(licenceNumber);

        assertThat(driverRepository.existsByLicenceNumber(licenceNumber)).isEqualTo(true);
    }

    @When("A create request with first name {string}, last name {string}, licence number {string}, email {string}, phone {string} is passed to the addDriver method")
    public void createRequestPassedToAddDriverMethod(
            String firstName, String lastName, String licenceNumber, String email, String phone
    ) {
        var createRequest = CreateDriverRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .licenceNumber(licenceNumber)
                .email(email)
                .phone(phone)
                .build();
        try {
            driverResponse = driverService.addDriver(createRequest, DEFAULT_EXTERNAL_ID);
        } catch (DriverAlreadyExistsException e) {
            exception = e;
        }
    }

    @Then("The response should contain details of the newly created driver")
    public void responseContainsCreatedDriverDetails() {
        var expected = getDefaultDriverResponse();
        assertThat(driverResponse).isEqualTo(expected);
    }

    @Then("The DriverAlreadyExistsException should be thrown")
    public void notFoundExceptionThrown() {
        assertThat(exception.getMessage()).isEqualTo(DRIVER_ALREADY_EXISTS_MESSAGE);
    }

    @When("The id {string} is passed to the deleteDriver method")
    public void idPassedToDeleteDriverMethod(String id) {
        try {
            driverService.deleteDriver(UUID.fromString(id));
        } catch (DriverNotFoundException e) {
            exception = e;
        }
    }

    @Then("The driver with id {string} should be deleted from the database")
    public void driverDeletedFromDatabase(String id) {
        var driver = driverRepository.findByExternalId(UUID.fromString(id));
        verify(driverRepository).delete(driver.get());
    }

    @When("The rating message with id {string} and rating {double} passed to the updateDriverRating method")
    public void ratingMessagePassedToUpdateDriverRatingMethod(String id, double rating) {
        var ratingMessage = DriverRatingMessage.builder()
                .driverId(UUID.fromString(id))
                .rating(rating)
                .build();

        try {
            driverService.updateDriverRating(ratingMessage);
        } catch (DriverNotFoundException e) {
            exception = e;
        }
    }

    @Then("Rating of the driver with id {string} is updated to {double}")
    public void ratingOfDriverUpdated(String id, double rating) {
        var driver = Driver.builder()
                .id(DEFAULT_ID)
                .externalId(UUID.fromString(id))
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .licenceNumber(DEFAULT_LICENCE_NUMBER)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .rating(rating)
                .status(DEFAULT_STATUS)
                .build();

        doReturn(Optional.of(driver))
                .when(driverRepository)
                .findByExternalId(UUID.fromString(id));

        var actual = driverRepository.findByExternalId(UUID.fromString(id)).get();
        assertThat(actual.getRating()).isEqualTo(rating);
    }

    @When("The driver id {string} and status {string} passed to the updateDriverStatus method")
    public void idAndStatusPassedToUpdateDriverStatusMethod(String id, String status) {
        try {
            driverService.updateDriverStatus(UUID.fromString(id), DriverStatus.valueOf(status));
        } catch (DriverNotFoundException e) {
            exception = e;
        }
    }

    @Then("Status of the driver with id {string} is updated to {string}")
    public void statusOfDriverUpdated(String id, String status) {
        var driver = Driver.builder()
                .id(DEFAULT_ID)
                .externalId(UUID.fromString(id))
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .licenceNumber(DEFAULT_LICENCE_NUMBER)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .rating(DEFAULT_RATING)
                .status(DriverStatus.valueOf(status))
                .build();

        doReturn(Optional.of(driver))
                .when(driverRepository)
                .findByExternalId(UUID.fromString(id));

        var actual = driverRepository.findByExternalId(UUID.fromString(id)).get();
        assertThat(actual.getStatus().name()).isEqualTo(status);
    }
}
