Feature: Passenger Service
  Scenario: Retrieving a passenger by existing id
    Given A passenger with id 1 exists
    When The id 1 is passed to the getById method
    Then The response should contain details of the passenger with id 1

  Scenario: Retrieving a passenger by non-existing id
    Given A passenger with id 1 doesn't exist
    When The id 1 is passed to the getById method
    Then The PassengerNotFoundException with the message containing id 1 should be thrown

  Scenario: Creating a new passenger with unique data
    Given A passenger with email "johnsmith@example.com" and phone "123-45-67" doesn't exist
    When A create request with first name 'John', last name "Smith", email "johnsmith@example.com", phone "123-45-67" is passed to the addPassenger method
    Then The response should contain details of the newly created passenger

  Scenario: Creating a new passenger with non-unique email
    Given A passenger with email "johnsmith@example.com" exists
    When A create request with first name 'John', last name "Smith", email "johnsmith@example.com", phone "123-45-67" is passed to the addPassenger method
    Then The PassengerAlreadyExistsException should be thrown

  Scenario: Creating a new passenger with non-unique phone
    Given A passenger with phone "123-45-67" exists
    When A create request with first name 'John', last name "Smith", email "johnsmith@example.com", phone "123-45-67" is passed to the addPassenger method
    Then The PassengerAlreadyExistsException should be thrown

  Scenario: Deleting a passenger by existing id
    Given A passenger with id 1 exists
    When The id 1 is passed to the deletePassenger method
    Then The passenger with id 1 should be deleted from the database

  Scenario: Deleting a passenger by non-existing id
    Given A passenger with id 1 doesn't exist
    When The id 1 is passed to the deletePassenger method
    Then The PassengerNotFoundException with the message containing id 1 should be thrown


