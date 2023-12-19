Feature: Accept a ride
  Scenario: Assigning a driver for a ride with available drivers
    Given There are available drivers
    When The create ride message with ride id 1 passed to acceptRideOrder method
    Then Accept ride message with assigned driver id should be returned