Feature: Accept a ride
  Scenario: Assigning a driver for a ride with available drivers
    Given There are available drivers
    When The create ride message with ride id 1 passed to acceptRideOrder method
    Then Accept ride message with assigned driver id should be returned

  Scenario: Assigning a driver for a ride without available drivers
    Given There are no available drivers
    When The create ride message with ride id 1 passed to acceptRideOrder method
    Then Accept ride message without assigned driver id should be returned