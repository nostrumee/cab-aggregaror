Feature: Passenger Service
  Scenario: Retrieving a passenger by existing id
    Given A passenger with id "d6f3c9d1-de66-45ee-beb9-f371fa3a6378" exists
    When The id "d6f3c9d1-de66-45ee-beb9-f371fa3a6378" is passed to the getById method
    Then The response should contain details of the passenger with id "d6f3c9d1-de66-45ee-beb9-f371fa3a6378"

  Scenario: Retrieving a passenger by non-existing id
    Given A passenger with id "d6f3c9d1-de66-45ee-beb9-f371fa3a6378" doesn't exist
    When The id "d6f3c9d1-de66-45ee-beb9-f371fa3a6378" is passed to the getById method
    Then The PassengerNotFoundException with the message containing id "d6f3c9d1-de66-45ee-beb9-f371fa3a6378" should be thrown

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
    Given A passenger with id "d6f3c9d1-de66-45ee-beb9-f371fa3a6378" exists
    When The id "d6f3c9d1-de66-45ee-beb9-f371fa3a6378" is passed to the deletePassenger method
    Then The passenger with id "d6f3c9d1-de66-45ee-beb9-f371fa3a6378" should be deleted from the database

  Scenario: Deleting a passenger by non-existing id
    Given A passenger with id "d6f3c9d1-de66-45ee-beb9-f371fa3a6378" doesn't exist
    When The id "d6f3c9d1-de66-45ee-beb9-f371fa3a6378" is passed to the deletePassenger method
    Then The PassengerNotFoundException with the message containing id "d6f3c9d1-de66-45ee-beb9-f371fa3a6378" should be thrown

  Scenario: Updating rating of existing passenger
    Given A passenger with id "d6f3c9d1-de66-45ee-beb9-f371fa3a6378" exists
    When The rating message with id "d6f3c9d1-de66-45ee-beb9-f371fa3a6378" and rating 4.65 passed to the updatePassengerRating method
    Then Rating of the passenger with id "d6f3c9d1-de66-45ee-beb9-f371fa3a6378" updated to 4.65

  Scenario: Updating rating of non-existing passenger
    Given A passenger with id "d6f3c9d1-de66-45ee-beb9-f371fa3a6378" doesn't exist
    When The rating message with id "d6f3c9d1-de66-45ee-beb9-f371fa3a6378" and rating 4.65 passed to the updatePassengerRating method
    Then The PassengerNotFoundException with the message containing id "d6f3c9d1-de66-45ee-beb9-f371fa3a6378" should be thrown
