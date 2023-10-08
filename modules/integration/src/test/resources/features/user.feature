@Integration
Feature: User

  Scenario: User information can be retrieved
    Given an authenticated user
    Then user details can be retrieved
