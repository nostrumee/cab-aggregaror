package com.modsen.passengerservice.integration.component;

import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.mapper.PassengerMapper;
import com.modsen.passengerservice.repository.PassengerRepository;
import com.modsen.passengerservice.service.PassengerService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;


@RequiredArgsConstructor
public class PassengerServiceStepDefinitions {

    private final PassengerService passengerService;
    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    private long passengerId;
    private PassengerResponse passengerResponse;


    @Given("The id {long} of existing passenger")
    public void existingPassengerIdGiven(long id) {
        passengerId = id;
    }

    @When("The getById method is called with this id")
    public void getByIdMethodCalled() {
        passengerResponse = passengerService.getById(passengerId);
    }

    @Then("The response should contain details of the passenger")
    public void responseContainsPassengerDetails() {
        var passenger = passengerRepository.findById(passengerId).get();
        var expected = passengerMapper.fromEntityToResponse(passenger);

        assertThat(passengerResponse).isEqualTo(expected);
    }
}
