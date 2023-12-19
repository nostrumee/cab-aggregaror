Feature: Driver Service
  Scenario: Retrieving a driver by existing id
    Given A driver with id 1 exists
    When The id 1 is passed to the getById method
    Then The response should contain details of the driver with id 1

  Scenario: Retrieving a driver by non-existing id
    Given A driver with id 1 doesn't exist
    When The id 1 is passed to the getById method
    Then The DriverNotFoundException with the message containing id 1 should be thrown

  Scenario: Creating a new driver with unique data
    Given A driver with licence number "123456789", email "johndoe@example.com" and phone "123-45-67" doesn't exist
    When A create request with first name 'John', last name "Doe", licence number "123456789", email "johndoe@example.com", phone "123-45-67" is passed to the addDriver method
    Then The response should contain details of the newly created driver

  Scenario: Creating a new driver with non-unique email
    Given A driver with email "johndoe@example.com" exists
    When A create request with first name 'John', last name "Doe", licence number "123456789", email "johndoe@example.com", phone "123-45-67" is passed to the addDriver method
    Then The DriverAlreadyExistsException should be thrown

  Scenario: Creating a new driver with non-unique phone
    Given A driver with phone "123-45-67" exists
    When A create request with first name 'John', last name "Doe", licence number "123456789", email "johndoe@example.com", phone "123-45-67" is passed to the addDriver method
    Then The DriverAlreadyExistsException should be thrown

  Scenario: Creating a new passenger with non-unique licence number
    Given A driver with licence number "123456789" exists
    When A create request with first name 'John', last name "Doe", licence number "123456789", email "johndoe@example.com", phone "123-45-67" is passed to the addDriver method
    Then The DriverAlreadyExistsException should be thrown

  Scenario: Deleting a driver by existing id
    Given A driver with id 1 exists
    When The id 1 is passed to the deleteDriver method
    Then The driver with id 1 should be deleted from the database

  Scenario: Deleting a driver by non-existing id
    Given A driver with id 1 doesn't exist
    When The id 1 is passed to the deleteDriver method
    Then The DriverNotFoundException with the message containing id 1 should be thrown

  Scenario: Updating rating of existing driver
    Given A driver with id 1 exists
    When The rating message with id 1 and rating 4.65 passed to the updateDriverRating method
    Then Rating of the driver with id 1 is updated to 4.65

  Scenario: Updating rating of non-existing driver
    Given A driver with id 1 doesn't exist
    When The rating message with id 1 and rating 4.65 passed to the updateDriverRating method
    Then The DriverNotFoundException with the message containing id 1 should be thrown

  Scenario: Updating status of existing driver
    Given A driver with id 1 exists
    When The driver id 1 and status "UNAVAILABLE" passed to the updateDriverStatus method
    Then Status of the driver with id 1 is updated to "UNAVAILABLE"

  Scenario: Updating status of non-existing driver
    Given A driver with id 1 doesn't exist
    When The driver id 1 and status "UNAVAILABLE" passed to the updateDriverStatus method
    Then The DriverNotFoundException with the message containing id 1 should be thrown


