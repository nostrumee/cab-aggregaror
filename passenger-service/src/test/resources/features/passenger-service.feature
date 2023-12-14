Feature: Passenger Service
  Scenario: Retrieving a passenger by id
    Given The passenger with id 1 exists
    When The getById method is called with id 1
    Then The response should contain details of the passenger with id 1

  Scenario: Retrieving a passenger by id
    Given The passenger with id 111 doesn't exist
    When The getById method is called with id 111
    Then The PassengerNotFoundException should be thrown

  Scenario: Creating a new passenger
    Given A new passenger request with first name "John", last name "Dorian", email "johndorian@example.com", phone "111-22-33"
    When The addPassenger method called with create passenger request with provided data
    Then The response should contain details of the newly created passenger
    And The new passenger should be added to the database