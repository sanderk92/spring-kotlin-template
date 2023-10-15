@Integration
Feature: User

  Scenario: default authorities are assigned
    Given current user without roles
    Then current user can be retrieved and has authorities 'READ,WRITE,DELETE'

  Scenario: default authorities are updated when roles are changed
    Given current user without roles
    Given current user with roles 'admin'
    Then current user can be retrieved and has authorities 'READ,WRITE,DELETE,ADMIN'

  Scenario: users can be searched by user name
    Given current user without roles
    Then current user can be found by user name

  Scenario: users can be searched by first name
    Given current user without roles
    Then current user can be found by first name

  Scenario: users can be searched by last name
    Given current user without roles
    Then current user can be found by last name

  Scenario: users can be searched by email name
    Given current user without roles
    Then current user can be found by email
