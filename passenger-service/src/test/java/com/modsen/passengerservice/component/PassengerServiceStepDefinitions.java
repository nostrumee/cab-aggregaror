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
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.RequiredArgsConstructor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

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


    @Given("A passenger with id {long} exists")
    public void passengerWithIdExists(long id) {
        var expected = getDefaultPassengerResponse();
        var retrievedPassenger = getDefaultPassenger();

        doReturn(Optional.of(retrievedPassenger))
                .when(passengerRepository)
                .findById(id);
        doReturn(expected)
                .when(passengerMapper)
                .fromEntityToResponse(retrievedPassenger);

        var passenger = passengerRepository.findById(id);
        assertThat(passenger.isPresent()).isEqualTo(true);
    }

    @Given("A passenger with id {long} doesn't exist")
    public void passengerWithIdNotExist(long id) {
        var passenger = passengerRepository.findById(id);
        assertThat(passenger.isPresent()).isEqualTo(false);
    }

    @When("The id {long} is passed to the getById method")
    public void idPassedToGetByIdMethod(long id) {
        try {
            passengerResponse = passengerService.getById(id);
        } catch (PassengerNotFoundException e) {
            exception = e;
        }
    }

    @Then("The response should contain details of the passenger with id {long}")
    public void responseContainsPassengerDetails(long id) {
        var passenger = passengerRepository.findById(id).get();
        var expected = passengerMapper.fromEntityToResponse(passenger);

        assertThat(passengerResponse).isEqualTo(expected);
    }

    @Then("The PassengerNotFoundException with the message containing id {long} should be thrown")
    public void passengerNotFoundExceptionThrown(long id) {
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
            passengerResponse = passengerService.addPassenger(createRequest);
        } catch (PassengerAlreadyExistsException e) {
            exception = e;
        }

    }

    @Then("The response should contain details of the newly created passenger")
    public void responseContainsCreatedPassengerDetails() {
        var expected = getDefaultPassengerResponse();
        assertThat(passengerResponse).isEqualTo(expected);
    }

    @And("A new passenger should be added to the database")
    public void newPassengerAddedToDatabase() {
        var passenger = passengerRepository.findById(NEW_ID);
        assertThat(passenger.isPresent()).isEqualTo(true);
    }


    @Then("The PassengerAlreadyExistsException should be thrown")
    public void exceptionWithEmailMessageThrown() {
        assertThat(exception.getMessage()).isEqualTo(PASSENGER_ALREADY_EXISTS_MESSAGE);
    }

    @When("The id {long} is passed to the deletePassenger method")
    public void idPassedToDeletePassengerMethod(long id) {
        try {
             passengerService.deletePassenger(id);
        } catch (PassengerNotFoundException e) {
            exception = e;
        }
    }

    @Then("The passenger with id {long} should be deleted from the database")
    public void passengerDeletedFromDatabase(long id) {
        var passenger = passengerRepository.findById(id);
        verify(passengerRepository).delete(passenger.get());
    }

    @When("The rating message with id {long} and rating {double} passed to the updatePassengerRating method")
    public void ratingMessagePassedToUpdatePassengerRatingMethod(long id, double rating) {
        var ratingMessage = PassengerRatingMessage.builder()
                .passengerId(id)
                .rating(rating)
                .build();

        try {
            passengerService.updatePassengerRating(ratingMessage);
        } catch (PassengerNotFoundException e) {
            exception = e;
        }
    }

    @Then("Rating of the passenger with id {long} updated to {double}")
    public void ratingOfPassengerUpdated(long id, double rating) {
        var passenger = Passenger.builder()
                .id(id)
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .rating(rating)
                .build();

        doReturn(Optional.of(passenger))
                .when(passengerRepository)
                .findById(id);

        var actual = passengerRepository.findById(id).get();
        assertThat(actual.getRating()).isEqualTo(rating);
    }
}
