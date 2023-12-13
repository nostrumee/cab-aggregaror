Feature: Passenger Service
  Scenario: Retrieving a passenger by id
    Given The id 1 of existing passenger
    When The getById method is called with this id
    Then The response should contain details of the passenger