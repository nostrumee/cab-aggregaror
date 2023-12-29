Feature: Ride Service
  Scenario: Retrieving a ride by existing id
    Given A ride with id 1 exists
    When The id 1 is passed to the getById method
    Then The response should contain details of the ride with id 1

  Scenario: Retrieving a ride by non-existing id
    Given A ride with id 1 doesn't exist
    When The id 1 is passed to the getById method
    Then The RideNotFoundException with the message containing id 1 should be thrown

  Scenario: Creating a new ride
    Given A passenger with id 1 exists
    When A create request with passenger id 1, start point "Start point", destination point "Destination point" passed to createRide method
    Then The response should contain details of the newly created ride

  Scenario: Accepting a ride
    Given Driver with id 1 assigned to a ride with id 1
    When Accept ride message passed to the acceptRide method
    Then Ride status should be changed to "ACCEPTED"
    And Driver should be assigned to a ride
    And Accepted date should be set

  Scenario: Starting a ride with existing id and valid status
    Given A ride with id 1 exists and has "ACCEPTED" status
    When Id 1 passed to startRide method
    Then Ride response should have "STARTED" status
    And Ride response should have start date set

  Scenario: Starting a ride with existing id and invalid status
    Given A ride with id 1 exists and has "STARTED" status
    When Id 1 passed to startRide method
    Then InvalidRideStatusException should be thrown

  Scenario: Finishing a ride with existing id and valid status
    Given A ride with id 1 exists and has "STARTED" status
    When Id 1 passed to finishRide method
    Then Ride response should have "FINISHED" status
    And Ride response should have finish date set

  Scenario: Finishing a ride with existing id and invalid status
    Given A ride with id 1 exists and has "FINISHED" status
    When Id 1 passed to startRide method
    Then InvalidRideStatusException should be thrown

  Scenario: Retrieving a driver's profile of an existing ride which has valid status
    Given A ride with id 1 exists and has "FINISHED" status
    When Id 1 passed to getDriverProfile method
    Then The response should contain details of the driver

  Scenario: Retrieving a driver's profile of an existing ride which has valid status
    Given A ride with id 1 exists and has "REJECTED" status
    When Id 1 passed to getDriverProfile method
    Then InvalidRideStatusException should be thrown
