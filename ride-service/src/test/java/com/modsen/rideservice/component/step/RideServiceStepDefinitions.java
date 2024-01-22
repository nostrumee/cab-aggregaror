package com.modsen.rideservice.component.step;

import com.modsen.rideservice.client.DriverClient;
import com.modsen.rideservice.client.PassengerClient;
import com.modsen.rideservice.dto.message.AcceptRideMessage;
import com.modsen.rideservice.dto.request.CreateRideRequest;
import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.entity.Ride;
import com.modsen.rideservice.entity.RideStatus;
import com.modsen.rideservice.exception.InvalidRideStatusException;
import com.modsen.rideservice.exception.RideNotFoundException;
import com.modsen.rideservice.mapper.MessageMapper;
import com.modsen.rideservice.mapper.RideMapper;
import com.modsen.rideservice.repository.RideRepository;
import com.modsen.rideservice.service.DriverService;
import com.modsen.rideservice.service.PassengerService;
import com.modsen.rideservice.service.SendMessageHandler;
import com.modsen.rideservice.service.impl.DriverServiceImpl;
import com.modsen.rideservice.service.impl.PassengerServiceImpl;
import com.modsen.rideservice.service.impl.RideServiceImpl;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static com.modsen.rideservice.util.ErrorMessages.*;
import static com.modsen.rideservice.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class RideServiceStepDefinitions {

    private RideRepository rideRepository;
    private RideMapper rideMapper;
    private MessageMapper messageMapper;
    private DriverClient driverClient;
    private PassengerClient passengerClient;
    private SendMessageHandler sendMessageHandler;
    private RideServiceImpl rideService;
    private DriverService driverService;
    private PassengerService passengerService;

    private RideResponse rideResponse;
    private DriverResponse driverResponse;
    private AcceptRideMessage acceptRideMessage;
    private Ride ride;
    private Exception exception;

    @Before
    public void setUp() {
        this.rideRepository = mock(RideRepository.class);
        this.rideMapper = mock(RideMapper.class);
        this.messageMapper = mock(MessageMapper.class);
        this.driverClient = mock(DriverClient.class);
        this.passengerClient = mock(PassengerClient.class);
        this.sendMessageHandler = mock(SendMessageHandler.class);

        this.driverService = new DriverServiceImpl(driverClient);
        this.passengerService = new PassengerServiceImpl(passengerClient);
        this.rideService = new RideServiceImpl(
                rideRepository, rideMapper, messageMapper,
                driverService, passengerService, sendMessageHandler
        );
    }

    @Given("A ride with id {long} exists")
    public void rideWithIdExists(long id) {
        var expected = getFinishedRideResponse();
        var retrievedRide = getFinishedRide();

        doReturn(Optional.of(retrievedRide))
                .when(rideRepository)
                .findById(id);
        doReturn(expected)
                .when(rideMapper)
                .fromEntityToResponse(retrievedRide);

        var ride = rideRepository.findById(id);
        assertThat(ride.isPresent()).isEqualTo(true);
    }

    @Given("A ride with id {long} doesn't exist")
    public void rideWithIdNotExist(long id) {
        var ride = rideRepository.findById(id);
        assertThat(ride.isPresent()).isEqualTo(false);
    }

    @When("The id {long} is passed to the getById method")
    public void isPassedToGetByIdMethod(long id) {
        try {
            rideResponse = rideService.getById(id);
        } catch (RideNotFoundException e) {
            exception = e;
        }
    }

    @Then("The response should contain details of the ride with id {long}")
    public void responseContainsRideDetails(long id) {
        var ride = rideRepository.findById(id).get();
        var expected = rideMapper.fromEntityToResponse(ride);

        assertThat(rideResponse).isEqualTo(expected);
    }

    @Then("The RideNotFoundException with the message containing id {long} should be thrown")
    public void rideNotFoundExceptionThrown(long id) {
        var expected = String.format(RIDE_NOT_FOUND_WITH_ID_MESSAGE, id);
        var actual = exception.getMessage();

        assertThat(actual).isEqualTo(expected);
    }

    @Given("A passenger with id {long} exists")
    public void passengerWithIdExists(long id) {
        var expected = getPassengerResponse();

        doReturn(expected)
                .when(passengerClient)
                .getPassengerById(id);


        var actual = passengerService.getPassengerById(id);
        assertThat(actual).isEqualTo(expected);
    }

    @When("A create request with passenger id {long}, start point {string}, destination point {string} passed to createRide method")
    public void createRequestPassedToCreateRideMethod(long id, String startPoint, String destinationPoint) {
        var rideToSave = getNotSavedRide();
        var savedRide = getCreatedRide();
        var createRequest = CreateRideRequest.builder()
                .passengerId(id)
                .startPoint(startPoint)
                .destinationPoint(destinationPoint)
                .build();;

        doReturn(rideToSave)
                .when(rideMapper)
                .fromCreateRequestToEntity(createRequest);
        doReturn(savedRide)
                .when(rideRepository)
                .save(rideToSave);
        doReturn(getCreatedRideResponse())
                .when(rideMapper)
                .fromEntityToResponse(savedRide);

        rideResponse = rideService.createRide(createRequest);
    }

    @Then("The response should contain details of the newly created ride")
    public void responseContainsCreatedRideDetails() {
        var expected = getCreatedRideResponse();
        assertThat(rideResponse).isEqualTo(expected);
    }

    @Given("Driver with id {long} assigned to a ride with id {long}")
    public void driverAssignedToRide(long driverId, long rideId) {
        acceptRideMessage = AcceptRideMessage.builder()
                .rideId(rideId)
                .driverId(driverId)
                .build();
        ride = getCreatedRide();

        var passengerResponse = getPassengerResponse();
        var rideStatusMessage = getRideStatusMessage(RideStatus.ACCEPTED);

        doReturn(Optional.of(ride))
                .when(rideRepository)
                .findById(rideId);
        doReturn(passengerResponse)
                .when(passengerClient)
                .getPassengerById(ride.getPassengerId());
        doReturn(rideStatusMessage)
                .when(messageMapper)
                .fromRideAndPassengerResponse(ride, passengerResponse);
    }

    @When("Accept ride message passed to the acceptRide method")
    public void acceptRideMessagePassedToAcceptRideMethod() {
        rideService.acceptRide(acceptRideMessage);
    }

    @Then("Ride status should be changed to {string}")
    public void rideStatusChangeTo(String status) {
        assertThat(ride.getStatus()).isEqualTo(RideStatus.valueOf(status));
    }

    @And("Driver should be assigned to a ride")
    public void driverAssignedToRide() {
        assertThat(ride.getDriverId()).isEqualTo(acceptRideMessage.driverId());
    }

    @And("Accepted date should be set")
    public void acceptedDateSet() {
        assertThat(ride.getAcceptedDate()).isNotNull();
    }

    @Given("A ride with id {long} exists and has {string} status")
    public void rideWithIdAndStatusExists(long id, String status) {
        ride = switch (RideStatus.valueOf(status)) {
            case ACCEPTED -> getAcceptedRide();
            case STARTED -> getStartedRide();
            case FINISHED -> getFinishedRide();
            case REJECTED -> getRejectedRide();
            case CREATED -> getCreatedRide();
        };

        doReturn(Optional.of(ride))
                .when(rideRepository)
                .findById(id);
        doReturn(ride)
                .when(rideRepository)
                .save(ride);

        var actual = rideRepository.findById(id).get();
        assertThat(actual.getStatus()).isEqualTo(RideStatus.valueOf(status));
    }

    @When("Id {long} passed to startRide method")
    public void idPassedToStartRideMethod(long id) {
        var expected = getStartedRideResponse();
        doReturn(expected)
                .when(rideMapper)
                .fromEntityToResponse(ride);
        doReturn(getPassengerResponse())
                .when(passengerClient)
                .getPassengerById(DEFAULT_ID);

        try {
            rideResponse = rideService.startRide(id);
        } catch (InvalidRideStatusException e) {
            exception = e;
        }
    }

    @When("Id {long} passed to finishRide method")
    public void idPassedToFinishRideMethod(long id) {
        var expected = getFinishedRideResponse();
        doReturn(expected)
                .when(rideMapper)
                .fromEntityToResponse(ride);
        doReturn(getPassengerResponse())
                .when(passengerClient)
                .getPassengerById(DEFAULT_ID);

        try {
            rideResponse = rideService.finishRide(id);
        } catch (InvalidRideStatusException e) {
            exception = e;
        }
    }

    @Then("Ride response should have {string} status")
    public void rideResponseHasStatus(String status) {
        assertThat(rideResponse.status()).isEqualTo(RideStatus.valueOf(status));
    }

    @And("Ride response should have start date set")
    public void rideResponseHasStartDateSet() {
        assertThat(rideResponse.startDate()).isNotNull();
    }

    @And("Ride response should have finish date set")
    public void rideResponseHasFinishDateSet() {
        assertThat(rideResponse.finishDate()).isNotNull();
    }

    @Then("InvalidRideStatusException should be thrown")
    public void invalidRideStatusExceptionThrown() {
        assertThat(exception).isInstanceOf(InvalidRideStatusException.class);
    }

    @When("Id {long} passed to getDriverProfile method")
    public void idPassedToGetDriverProfileMethod(long id) {
        if (ride.getDriverId() != null) {
            doReturn(getDriverResponse())
                    .when(driverClient)
                    .getDriverById(ride.getDriverId());
        }

        try {
            driverResponse = rideService.getDriverProfile(id);
        } catch (InvalidRideStatusException e) {
            exception = e;
        }
    }

    @Then("The response should contain details of the driver")
    public void responseContainsDriverDetails() {
        var expected = getDriverResponse();

        assertThat(driverResponse).isEqualTo(expected);
    }
}
