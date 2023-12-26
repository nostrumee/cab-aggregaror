package com.modsen.e2etestsservice.step;

import com.modsen.e2etestsservice.client.DriverClient;
import com.modsen.e2etestsservice.client.PassengerClient;
import com.modsen.e2etestsservice.client.RatingClient;
import com.modsen.e2etestsservice.client.RideClient;
import com.modsen.e2etestsservice.dto.request.CreateRideRequest;
import com.modsen.e2etestsservice.dto.request.DriverRatingRequest;
import com.modsen.e2etestsservice.dto.request.PassengerRatingRequest;
import com.modsen.e2etestsservice.dto.response.DriverResponse;
import com.modsen.e2etestsservice.dto.response.RidePageResponse;
import com.modsen.e2etestsservice.dto.response.RideResponse;
import com.modsen.e2etestsservice.entity.DriverStatus;
import com.modsen.e2etestsservice.entity.RideStatus;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@RequiredArgsConstructor
public class RideFlowStepDefinitions {

    private final PassengerClient passengerClient;
    private final RideClient rideClient;
    private final DriverClient driverClient;
    private final RatingClient ratingClient;

    private static long rideId;
    private static long driverId;
    private static long passengerId;

    private CreateRideRequest createRideRequest;
    private RideResponse rideResponse;
    private DriverResponse driverResponse;
    private RidePageResponse ridePageResponse;
    private double initialRating;
    private double updatedRating;

    @Given("An existing passenger with id {long} creates a ride request with start point {string} and destination point {string}")
    public void passengerCreatesRideRequest(long passengerId, String startPoint, String destinationPoint) {
        createRideRequest = CreateRideRequest.builder()
                .passengerId(passengerId)
                .startPoint(startPoint)
                .destinationPoint(destinationPoint)
                .build();
    }

    @When("A passenger sends this request to the create ride endpoint")
    public void passengerSendsCreateRideRequest() {
        rideResponse = rideClient.createRide(createRideRequest);
        rideId = rideResponse.id();
    }

    @Then("A passenger should get details of ride order with status {string}")
    public void passengerGetsDetailsOfCreatedRide(String status) {
        assertThat(rideResponse.status()).isEqualTo(RideStatus.valueOf(status));
        assertThat(rideResponse.createdDate()).isNotNull();
        assertThat(rideResponse.estimatedCost()).isNotNull();
    }

    @And("After a few sec a ride's status should be changed to {string}")
    public void afterFewSecRideStatusShouldBeChanged(String status) {
        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    rideResponse = rideClient.getRideById(rideId);
                    assertThat(rideResponse.status()).isEqualTo(RideStatus.valueOf(status));
                });
    }

    @And("A driver and accepted date should be set")
    public void driverAndAcceptedDateShouldBeSet() {
        assertThat(rideResponse.driverId()).isNotNull();
        assertThat(rideResponse.acceptedDate()).isNotNull();
        driverId = rideResponse.driverId();
    }

    @And("A driver's status should be changed to {string}")
    public void driverStatusShouldBeChanged(String status) {
        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    driverResponse = driverClient.getDriverById(driverId);
                    assertThat(driverResponse.status()).isEqualTo(DriverStatus.valueOf(status));
                });
    }

    @Given("The ride has status {string}")
    public void rideHasStatus(String status) {
        rideResponse = rideClient.getRideById(rideId);
        assertThat(rideResponse.status()).isEqualTo(RideStatus.valueOf(status));
    }

    @When("A driver starts the ride")
    public void driverStartsRide() {
        rideClient.startRide(rideId);
    }

    @Then("A ride's status should be changed to {string}")
    public void driverStatusChanged(String status) {
        rideResponse = rideClient.getRideById(rideId);
        assertThat(rideResponse.status()).isEqualTo(RideStatus.valueOf(status));
    }

    @And("Start date should be set")
    public void startDateShouldBeSet() {
        assertThat(rideResponse.startDate()).isNotNull();
    }

    @When("A driver finishes the ride")
    public void driverFinishesRide() {
        rideClient.finishRide(rideId);
    }

    @And("Finish date should be set")
    public void finishDateShouldBeSet() {
        assertThat(rideResponse.finishDate()).isNotNull();
    }

    @When("A driver rates a passenger with rating {int}")
    public void driverRatesPassenger(int rating) {
        rideResponse = rideClient.getRideById(rideId);
        initialRating = passengerClient
                .getPassengerById(rideResponse.passengerId())
                .rating();
        var ratingRequest = PassengerRatingRequest.builder()
                .rideId(rideId)
                .rating(rating)
                .build();
        ratingClient.ratePassenger(ratingRequest);
    }

    @Then("After a few sec passenger's rating should be changed")
    public void passengerRatingShouldBeChanged() {
        rideResponse = rideClient.getRideById(rideId);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    updatedRating = passengerClient
                            .getPassengerById(rideResponse.passengerId())
                            .rating();
                    assertThat(initialRating).isNotEqualTo(updatedRating);
                });
    }

    @When("A passenger rates a driver with rating {int}")
    public void passengerRatesDriver(int rating) {
        rideResponse = rideClient.getRideById(rideId);
        initialRating = driverClient
                .getDriverById(rideResponse.driverId())
                .rating();
        var ratingRequest = DriverRatingRequest.builder()
                .rideId(rideId)
                .rating(rating)
                .build();
        ratingClient.rateDriver(ratingRequest);
    }

    @Then("After a few sec driver's rating should be changed")
    public void driverRatingShouldBeChanged() {
        rideResponse = rideClient.getRideById(rideId);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    updatedRating = driverClient
                            .getDriverById(rideResponse.driverId())
                            .rating();
                    assertThat(initialRating).isNotEqualTo(updatedRating);
                });
    }

    @Given("A passenger has at least one finished ride")
    public void passengerHadAtLeastOneFinishedRide() {
        rideResponse = rideClient.getRideById(rideId);
        passengerId = rideResponse.passengerId();
    }

    @When("A passenger retrieves his rides history")
    public void passengerRetrievesDriverProfile() {
        ridePageResponse = rideClient.getPassengerRideHistory(passengerId);
    }

    @Then("Total number of rides in response should be equal or greater than 1")
    public void numberOfRidesShouldBeEqualOrGreaterThanOne() {
        assertThat(ridePageResponse.total()).isGreaterThanOrEqualTo(1L);
    }

    @Given("A driver has at least one finished ride")
    public void driverHadAtLeastOneFinishedRide() {
        rideResponse = rideClient.getRideById(rideId);
        driverId = rideResponse.driverId();
    }

    @When("A driver retrieves his rides history")
    public void driverRetrievesDriverProfile() {
        ridePageResponse = rideClient.getDriverRideHistory(driverId);
    }
}
