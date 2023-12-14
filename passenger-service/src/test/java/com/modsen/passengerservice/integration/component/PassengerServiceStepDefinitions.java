package com.modsen.passengerservice.integration.component;

import com.modsen.passengerservice.PassengerServiceApplication;
import com.modsen.passengerservice.dto.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.exception.PassengerAlreadyExistsException;
import com.modsen.passengerservice.exception.PassengerNotFoundException;
import com.modsen.passengerservice.integration.TestcontainersBase;
import com.modsen.passengerservice.mapper.PassengerMapper;
import com.modsen.passengerservice.repository.PassengerRepository;
import com.modsen.passengerservice.service.PassengerService;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;

import static com.modsen.passengerservice.util.ErrorMessages.NOT_FOUND_WITH_ID_MESSAGE;
import static com.modsen.passengerservice.util.ErrorMessages.PASSENGER_ALREADY_EXISTS_MESSAGE;
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

    private PassengerResponse passengerResponse;
    private Exception exception;

    @BeforeAll
    public static void setUp() {
        postgres.start();
        kafka.start();
    }

    @AfterAll
    public static void tearDown() {
        postgres.stop();
        kafka.stop();
    }

    @Given("A passenger with id {long} exists")
    public void passengerWithIdExists(long id) {
        var passenger = passengerRepository.findById(id);
        assertThat(passenger.isPresent()).isEqualTo(true);
    }

    @Given("A passenger with id {long} doesn't exist")
    public void passengerWithIdNotExist(long id) {
        var passenger = passengerRepository.findById(id);
        assertThat(passenger.isPresent()).isEqualTo(false);
    }

    @When("The id {long} is passed to the getById method")
    public void getByIdMethodCalled(long id) {
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
        assertThat(passengerRepository.existsByEmail(email)).isEqualTo(false);
        assertThat(passengerRepository.existsByPhone(phone)).isEqualTo(false);
    }

    @Given("A passenger with email {string} exists")
    public void passengerWithEmailExists(String email) {
        assertThat(passengerRepository.existsByEmail(email)).isEqualTo(true);
    }

    @Given("A passenger with phone {string} exists")
    public void passengerWithPhoneExists(String phone) {
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

    @And("A new passenger should be added to the database")
    public void newPassengerAddedToDatabase() {
        var passenger = passengerRepository.findById(NEW_ID);
        assertThat(passenger.isPresent()).isEqualTo(true);
    }


    @Then("The PassengerAlreadyExistsException should be thrown")
    public void exceptionWithEmailMessageThrown() {
        assertThat(exception.getMessage()).isEqualTo(PASSENGER_ALREADY_EXISTS_MESSAGE);
    }
}
