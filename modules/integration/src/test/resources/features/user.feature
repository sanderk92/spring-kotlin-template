@Integration
Feature: User

  Scenario: user information can be retrieved
    Given an authenticated user
    Then user details can be retrieved

  Scenario: users can be searched by user name
    Given an authenticated user
    Then user can be found by user name

  Scenario: users can be searched by first name
    Given an authenticated user
    Then user can be found by first name

  Scenario: users can be searched by last name
    Given an authenticated user
    Then user can be found by last name

  Scenario: users can be searched by email name
    Given an authenticated user
    Then user can be found by email
