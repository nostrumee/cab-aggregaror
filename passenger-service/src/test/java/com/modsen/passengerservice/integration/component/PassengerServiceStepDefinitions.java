package com.modsen.passengerservice.integration.component;

import com.modsen.passengerservice.PassengerServiceApplication;
import com.modsen.passengerservice.dto.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.exception.PassengerNotFoundException;
import com.modsen.passengerservice.integration.TestcontainersBase;
import com.modsen.passengerservice.mapper.PassengerMapper;
import com.modsen.passengerservice.repository.PassengerRepository;
import com.modsen.passengerservice.service.PassengerService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;

import static com.modsen.passengerservice.util.ErrorMessages.*;
import static com.modsen.passengerservice.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;


@RequiredArgsConstructor
@SpringBootTest(classes = {
        ComponentIntegrationTest.class,
        PassengerServiceApplication.class
})
@CucumberContextConfiguration
public class PassengerServiceStepDefinitions extends TestcontainersBase {

    private final PassengerService passengerService;
    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    private long passengerId;
    private PassengerResponse passengerResponse;
    private CreatePassengerRequest createRequest;
    private PassengerNotFoundException passengerNotFoundException;

    @Given("The passenger with id {long} exists")
    public void passengerWithIdExists(long id) {
        passengerId = id;
        var passenger = passengerRepository.findById(id);
        assertThat(passenger.isPresent()).isEqualTo(true);
    }

    @Given("The passenger with id {long} doesn't exist")
    public void passengerWithIdNotExist(long id) {
        passengerId = id;
        var passenger = passengerRepository.findById(id);
        assertThat(passenger.isPresent()).isEqualTo(true);
    }

    @When("The getById method is called with id {long}")
    public void getByIdMethodCalled(long id) {
        try {
            passengerResponse = passengerService.getById(id);
        } catch (PassengerNotFoundException e) {
            passengerNotFoundException = e;
        }
    }

    @Then("The response should contain details of the passenger with id {long}")
    public void responseContainsPassengerDetails(long id) {
        var passenger = passengerRepository.findById(id).get();
        var expected = passengerMapper.fromEntityToResponse(passenger);

        assertThat(passengerResponse).isEqualTo(expected);
    }

    @Then("The PassengerNotFoundException should be thrown")
    public void passengerNotFoundExceptionThrown() {
        var expected = String.format(NOT_FOUND_WITH_ID_MESSAGE, passengerId);
        var actual = passengerNotFoundException.getMessage();

        assertThat(actual).isEqualTo(expected);
    }

    @Given("A new passenger request with first name {string}, last name {string}, email {string}, phone {string}")
    public void newPassengerRequestGiven(String firstName, String lastName, String email, String phone) {
        createRequest = CreatePassengerRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .build();
    }

    @When("The addPassenger method called with create passenger request with provided data")
    public void addPassengerMethodCalled() {
        passengerResponse = passengerService.addPassenger(createRequest);
    }

    @Then("The response should contain details of the newly created passenger")
    public void responseContainsCreatedPassengerDetails() {
        var expected = PassengerResponse.builder()
                .id(NEW_ID)
                .firstName(NEW_FIRST_NAME)
                .lastName(NEW_LAST_NAME)
                .email(NEW_EMAIL)
                .phone(NEW_PHONE)
                .rating(DEFAULT_RATING)
                .build();

        assertThat(passengerResponse).isEqualTo(expected);
    }

    @And("The new passenger should be added to the database")
    public void newPassengerAddedToDatabase() {
        var passenger = passengerRepository.findById(NEW_ID);
        assertThat(passenger.isPresent()).isEqualTo(true);
    }

}
