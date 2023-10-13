@Integration
Feature: Keys

  Scenario: api keys can be managed
    Given an authenticated user

    When creating api key with name 'test' and all authorities
    Then api key with name 'test' exists with all authorities

    When deleting api key with name 'test'
    Then api key with name 'test' does not exist

