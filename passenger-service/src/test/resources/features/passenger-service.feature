Feature: Passenger Service
  Scenario: Retrieving a passenger by id
    Given The id 1 of existing passenger
    When The getById method is called with this id
    Then The response should contain details of the passenger

  Scenario: Creating a new passenger
    Given A new passenger request with first name "John", last name "Dorian", email "johndorian@example.com", phone "111-22-33"
    When The addPassenger method called with create passenger request with provided data
    Then The response should contain details of the newly created passenger
    And The new passenger should be added to the database