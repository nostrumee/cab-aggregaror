package com.modsen.ratingservice.component.step;

import com.modsen.ratingservice.client.RideClient;
import com.modsen.ratingservice.dto.request.DriverRatingRequest;
import com.modsen.ratingservice.dto.request.PassengerRatingRequest;
import com.modsen.ratingservice.dto.response.RideResponse;
import com.modsen.ratingservice.entity.DriverRating;
import com.modsen.ratingservice.entity.PassengerRating;
import com.modsen.ratingservice.entity.RideStatus;
import com.modsen.ratingservice.exception.InvalidRideStatusException;
import com.modsen.ratingservice.mapper.RatingMapper;
import com.modsen.ratingservice.repository.DriverRatingRepository;
import com.modsen.ratingservice.repository.PassengerRatingRepository;
import com.modsen.ratingservice.service.RatingService;
import com.modsen.ratingservice.service.RideService;
import com.modsen.ratingservice.service.SendMessageHandler;
import com.modsen.ratingservice.service.impl.RatingServiceImpl;
import com.modsen.ratingservice.service.impl.RideServiceImpl;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static com.modsen.ratingservice.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RatingServiceStepDefinitions {

    private PassengerRatingRepository passengerRatingRepository;
    private DriverRatingRepository driverRatingRepository;
    private RideClient rideClient;
    private SendMessageHandler sendMessageHandler;
    private RatingMapper ratingMapper;
    private RideService rideService;
    private RatingService ratingService;

    private RideResponse rideResponse;
    private PassengerRating passengerRating;
    private DriverRating driverRating;
    private Exception exception;

    @Before
    public void setUp() {
        this.passengerRatingRepository = mock(PassengerRatingRepository.class);
        this.driverRatingRepository = mock(DriverRatingRepository.class);
        this.rideClient = mock(RideClient.class);
        this.sendMessageHandler = mock(SendMessageHandler.class);
        this.ratingMapper = mock(RatingMapper.class);
        this.rideService = new RideServiceImpl(rideClient);
        this.ratingService = new RatingServiceImpl(
                passengerRatingRepository,
                driverRatingRepository,
                rideService,
                sendMessageHandler,
                ratingMapper
        );
    }

    @Given("Ride with id {long} exists and has {string} status")
    public void rideExistsWithIdAndStatus(long id, String status) {
        rideResponse = RideResponse.builder()
                .passengerId(DEFAULT_ID)
                .driverId(DEFAULT_ID)
                .status(RideStatus.valueOf(status))
                .build();
        doReturn(rideResponse)
                .when(rideClient)
                .getRideById(id);
    }

    @When("A passenger rating request with ride id {long} and rating {int} passed to ratePassenger method")
    public void passengerRatingRequestPassedToRatePassengerMethod(long rideId, int rating) {
        var ratingRequest = PassengerRatingRequest.builder()
                .rideId(rideId)
                .rating(rating)
                .build();
        passengerRating = getPassengerRating();

        doReturn(passengerRating)
                .when(ratingMapper)
                .fromRideResponseAndRatingRequest(rideResponse, ratingRequest);
        doReturn(UPDATED_RATING)
                .when(passengerRatingRepository)
                .findPassengerRating(rideResponse.passengerId());

        try {
            ratingService.ratePassenger(ratingRequest);
        } catch (InvalidRideStatusException e) {
            exception = e;
        }
    }

    @Then("A new passenger rating saved to the database")
    public void newPassengerRatingSavedToDatabase() {
        verify(passengerRatingRepository).save(passengerRating);
    }

    @When("A driver rating request with ride id {long} and rating {int} passed to rateDriver method")
    public void driverRatingRequestPassedToRateDriverMethod(long rideId, int rating) {
        var ratingRequest = DriverRatingRequest.builder()
                .rideId(rideId)
                .rating(rating)
                .build();
        driverRating = getDriverRating();

        doReturn(driverRating)
                .when(ratingMapper)
                .fromRideResponseAndRatingRequest(rideResponse, ratingRequest);
        doReturn(UPDATED_RATING)
                .when(driverRatingRepository)
                .findDriverRating(rideResponse.passengerId());

        try {
            ratingService.rateDriver(ratingRequest);
        } catch (InvalidRideStatusException e) {
            exception = e;
        }

    }

    @Then("A new driver rating saved to the database")
    public void newDriverRatingSavedToDatabase() {
        verify(driverRatingRepository).save(driverRating);
    }

    @Then("InvalidRideStatusException should be thrown")
    public void invalidRideStatusExceptionThrown() {
        assertThat(exception).isInstanceOf(InvalidRideStatusException.class);
    }
}
