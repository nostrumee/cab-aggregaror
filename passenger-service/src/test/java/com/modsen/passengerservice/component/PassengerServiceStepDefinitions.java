package com.modsen.passengerservice.component;

import com.modsen.passengerservice.dto.message.PassengerRatingMessage;
import com.modsen.passengerservice.dto.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.entity.Passenger;
import com.modsen.passengerservice.exception.PassengerAlreadyExistsException;
import com.modsen.passengerservice.exception.PassengerNotFoundException;
import com.modsen.passengerservice.mapper.PassengerMapper;
import com.modsen.passengerservice.repository.PassengerRepository;
import com.modsen.passengerservice.service.impl.PassengerServiceImpl;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.RequiredArgsConstructor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;
import java.util.UUID;

import static com.modsen.passengerservice.util.ErrorMessages.NOT_FOUND_WITH_ID_MESSAGE;
import static com.modsen.passengerservice.util.ErrorMessages.PASSENGER_ALREADY_EXISTS_MESSAGE;
import static com.modsen.passengerservice.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


@RequiredArgsConstructor
@CucumberContextConfiguration
public class PassengerServiceStepDefinitions {

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private PassengerMapper passengerMapper;

    @InjectMocks
    private PassengerServiceImpl passengerService;

    private PassengerResponse passengerResponse;
    private Exception exception;


    @Given("A passenger with id {string} exists")
    public void passengerWithIdExists(String id) {
        var expected = getDefaultPassengerResponse();
        var retrievedPassenger = getDefaultPassenger();

        doReturn(Optional.of(retrievedPassenger))
                .when(passengerRepository)
                .findByExternalId(UUID.fromString(id));
        doReturn(expected)
                .when(passengerMapper)
                .fromEntityToResponse(retrievedPassenger);

        var passenger = passengerRepository.findByExternalId(UUID.fromString(id));
        assertThat(passenger.isPresent()).isEqualTo(true);
    }

    @Given("A passenger with id {string} doesn't exist")
    public void passengerWithIdNotExist(String id) {
        var passenger = passengerRepository.findByExternalId(UUID.fromString(id));
        assertThat(passenger.isPresent()).isEqualTo(false);
    }

    @When("The id {string} is passed to the getById method")
    public void idPassedToGetByIdMethod(String id) {
        try {
            passengerResponse = passengerService.getById(UUID.fromString(id));
        } catch (PassengerNotFoundException e) {
            exception = e;
        }
    }

    @Then("The response should contain details of the passenger with id {string}")
    public void responseContainsPassengerDetails(String id) {
        var passenger = passengerRepository.findByExternalId(UUID.fromString(id)).get();
        var expected = passengerMapper.fromEntityToResponse(passenger);

        assertThat(passengerResponse).isEqualTo(expected);
    }

    @Then("The PassengerNotFoundException with the message containing id {string} should be thrown")
    public void passengerNotFoundExceptionThrown(String id) {
        var expected = String.format(NOT_FOUND_WITH_ID_MESSAGE, id);
        var actual = exception.getMessage();

        assertThat(actual).isEqualTo(expected);
    }

    @Given("A passenger with email {string} and phone {string} doesn't exist")
    public void passengerWithEmailAndPhoneNotExist(String email, String phone) {
        var expected = getDefaultPassengerResponse();

        var passengerToSave = getNotSavedPassenger();
        var savedPassenger = getDefaultPassenger();
        var createRequest = getCreatePassengerRequest();

        doReturn(false)
                .when(passengerRepository)
                .existsByEmail(email);
        doReturn(false)
                .when(passengerRepository)
                .existsByPhone(phone);
        doReturn(passengerToSave)
                .when(passengerMapper)
                .fromCreateRequestToEntity(createRequest);
        doReturn(savedPassenger)
                .when(passengerRepository)
                .save(passengerToSave);
        doReturn(expected)
                .when(passengerMapper)
                .fromEntityToResponse(savedPassenger);

        assertThat(passengerRepository.existsByEmail(email)).isEqualTo(false);
        assertThat(passengerRepository.existsByPhone(phone)).isEqualTo(false);
    }

    @Given("A passenger with email {string} exists")
    public void passengerWithEmailExists(String email) {
        doReturn(true)
                .when(passengerRepository)
                .existsByEmail(email);
        doReturn(false)
                .when(passengerRepository)
                .existsByPhone(DEFAULT_PHONE);

        assertThat(passengerRepository.existsByEmail(email)).isEqualTo(true);
    }

    @Given("A passenger with phone {string} exists")
    public void passengerWithPhoneExists(String phone) {
        doReturn(false)
                .when(passengerRepository)
                .existsByEmail(DEFAULT_EMAIL);
        doReturn(true)
                .when(passengerRepository)
                .existsByPhone(phone);

        assertThat(passengerRepository.existsByPhone(phone)).isEqualTo(true);
    }

    @When("A create request with first name {string}, last name {string}, email {string}, phone {string} is passed to the addPassenger method")
    public void addPassengerMethodCalled(String firstName, String lastName, String email, String phone) {
        var createRequest = CreatePassengerRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .build();
        try {
            passengerResponse = passengerService.addPassenger(createRequest, DEFAULT_EXTERNAL_ID);
        } catch (PassengerAlreadyExistsException e) {
            exception = e;
        }
    }

    @Then("The response should contain details of the newly created passenger")
    public void responseContainsCreatedPassengerDetails() {
        var expected = getDefaultPassengerResponse();
        assertThat(passengerResponse).isEqualTo(expected);
    }

    @Then("The PassengerAlreadyExistsException should be thrown")
    public void notFoundExceptionThrown() {
        assertThat(exception.getMessage()).isEqualTo(PASSENGER_ALREADY_EXISTS_MESSAGE);
    }

    @When("The id {string} is passed to the deletePassenger method")
    public void idPassedToDeletePassengerMethod(String id) {
        try {
             passengerService.deletePassenger(UUID.fromString(id));
        } catch (PassengerNotFoundException e) {
            exception = e;
        }
    }

    @Then("The passenger with id {string} should be deleted from the database")
    public void passengerDeletedFromDatabase(String id) {
        var passenger = passengerRepository.findByExternalId(UUID.fromString(id));
        verify(passengerRepository).delete(passenger.get());
    }

    @When("The rating message with id {string} and rating {double} passed to the updatePassengerRating method")
    public void ratingMessagePassedToUpdatePassengerRatingMethod(String id, double rating) {
        var ratingMessage = PassengerRatingMessage.builder()
                .passengerId(UUID.fromString(id))
                .rating(rating)
                .build();

        try {
            passengerService.updatePassengerRating(ratingMessage);
        } catch (PassengerNotFoundException e) {
            exception = e;
        }
    }

    @Then("Rating of the passenger with id {string} updated to {double}")
    public void ratingOfPassengerUpdated(String id, double rating) {
        var passenger = Passenger.builder()
                .id(DEFAULT_ID)
                .externalId(UUID.fromString(id))
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .rating(rating)
                .build();

        doReturn(Optional.of(passenger))
                .when(passengerRepository)
                .findByExternalId(UUID.fromString(id));

        var actual = passengerRepository.findByExternalId(UUID.fromString(id)).get();
        assertThat(actual.getRating()).isEqualTo(rating);
    }
}
