Feature: Passenger Service
  Scenario: Retrieving a passenger by existing id
    Given A passenger with id 1 exists
    When The id 1 is passed to the getById method
    Then The response should contain details of the passenger with id 1

  Scenario: Retrieving a passenger by non-existing id
    Given A passenger with id 111 doesn't exist
    When The id 111 is passed to the getById method
    Then The PassengerNotFoundException with the message containing id 111 should be thrown

  Scenario: Creating a new passenger with unique data
    Given A passenger with email "johndorian@example.com" and phone "111-22-33" doesn't exist
    When A create request with first name "John", last name "Dorian", email "johndorian@example.com", phone "111-22-33" is passed to the addPassenger method
    Then The response should contain details of the newly created passenger
    And A new passenger should be added to the database

  Scenario: Creating a new passenger with non-unique email
    Given A passenger with email "johnsmith@example.com" exists
    When A create request with first name "John", last name "Dorian", email "johnsmith@example.com", phone "111-22-33" is passed to the addPassenger method
    Then The PassengerAlreadyExistsException should be thrown

  Scenario: Creating a new passenger with non-unique phone
    Given A passenger with phone "123-45-67" exists
    When A create request with first name "John", last name "Dorian", email "johndorian@example.com", phone "123-45-67" is passed to the addPassenger method
    Then The PassengerAlreadyExistsException should be thrown
