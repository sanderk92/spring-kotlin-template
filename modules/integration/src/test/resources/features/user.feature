@Integration
Feature: User

  Scenario: default authorities are assigned
    Given current user without roles
    Then current user can be retrieved and has authorities 'READ,WRITE,DELETE'

  Scenario: role authorities are assigned
    Given current user with roles 'admin'
    Then current user can be retrieved and has authorities 'READ,WRITE,DELETE,ADMIN'

  Scenario: users can be found by different attributes
    Given current user without roles
    Then current user can be found by user name
    Then current user can be found by first name
    Then current user can be found by last name
    Then current user can be found by email
