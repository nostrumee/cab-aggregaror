Feature: Ride Flow
  Scenario: A passenger books a ride
    Given An existing passenger with id 7 creates a ride request with start point "Start point" and destination point "Destination point"
    When A passenger sends this request to the create ride endpoint
    Then A passenger should get details of ride order with status "CREATED"
    And After a few sec a ride's status should be changed to "ACCEPTED"
    And A driver and accepted date should be set
    And A driver's status should be changed to "UNAVAILABLE"

  Scenario: A driver starts the ride
    Given The ride has status "ACCEPTED"
    When A driver starts the ride
    Then A ride's status should be changed to "STARTED"
    And Start date should be set

  Scenario: A driver finishes the ride
    Given The ride has status "STARTED"
    When A driver finishes the ride
    Then A ride's status should be changed to "FINISHED"
    And Finish date should be set
    And A driver's status should be changed to "AVAILABLE"

  Scenario: A driver rates passenger
    Given The ride has status "FINISHED"
    When A driver rates a passenger with rating 4
    Then After a few sec passenger's rating should be changed

  Scenario: A passenger rates driver
    Given The ride has status "FINISHED"
    When A passenger rates a driver with rating 4
    Then After a few sec driver's rating should be changed

  Scenario: A passenger views his rides history
    Given A passenger has at least one finished ride
    When A passenger retrieves his rides history
    Then Total number of rides in response should be equal or greater than 1

  Scenario: A driver views his rides history
    Given A driver has at least one finished ride
    When A driver retrieves his rides history
    Then Total number of rides in response should be equal or greater than 1