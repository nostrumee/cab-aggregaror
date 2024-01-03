Feature: Rating Service
  Scenario: Rating a passenger of the ride with the valid status
    Given Ride with id 1 exists and has "FINISHED" status
    When A passenger rating request with ride id 1 and rating 3 passed to ratePassenger method
    Then A new passenger rating saved to the database
  Scenario: Rating a passenger of the ride with an invalid status
    Given Ride with id 1 exists and has "STARTED" status
    When A passenger rating request with ride id 1 and rating 3 passed to ratePassenger method
    Then InvalidRideStatusException should be thrown
  Scenario: Rating a driver of the ride with the valid status
    Given Ride with id 1 exists and has "FINISHED" status
    When A driver rating request with ride id 1 and rating 3 passed to rateDriver method
    Then A new driver rating saved to the database
  Scenario: Rating a driver of the ride with an invalid status
    Given Ride with id 1 exists and has "STARTED" status
    When A driver rating request with ride id 1 and rating 3 passed to rateDriver method
    Then InvalidRideStatusException should be thrown