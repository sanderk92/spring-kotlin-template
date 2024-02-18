@Integration
Feature: User

  Scenario: default authorities are assigned
    Given user without roles
    Then current user can be retrieved and has authorities 'READ,WRITE,DELETE'

  Scenario: role authorities are assigned
    Given user with roles 'admin'
    Then current user can be retrieved and has authorities 'READ,WRITE,DELETE,ADMIN'

  Scenario: users can be found by different attributes
    Given user without roles
    Then current user can be found by user name
    Then current user can be found by first name
    Then current user can be found by last name
    Then current user can be found by email
