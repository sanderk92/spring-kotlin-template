package com.template.glue

import com.template.objects.*
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.openapitools.client.apis.UserApi

class UserStepDefinitions(private val userApi : UserApi) {

    @Then("user details can be retrieved")
    fun thenRetrieveCurrentUser() {
        val user = userApi.getCurrentUser().block() ?: fail("user details could not be retrieved")
        assertThat(user.firstName).isEqualTo(firstName)
        assertThat(user.lastName).isEqualTo(lastName)
        assertThat(user.email).isEqualTo(email)
    }

    @Then("user can be found by first name")
    fun thenSearchUserByFirstName() {
        val users = userApi.searchUsers(firstName).block() ?: fail("user could not be searched for")
        assertThat(users.size).isEqualTo(1)
        assertThat(users[0].firstName).isEqualTo(firstName)
        assertThat(users[0].lastName).isEqualTo(lastName)
        assertThat(users[0].email).isEqualTo(email)
    }

    @Then("user can be found by last name")
    fun thenSearchUserByLastName() {
        val users = userApi.searchUsers(firstName).block() ?: fail("user could not be searched for")
        assertThat(users.size).isEqualTo(1)
        assertThat(users[0].firstName).isEqualTo(firstName)
        assertThat(users[0].lastName).isEqualTo(lastName)
        assertThat(users[0].email).isEqualTo(email)
    }

    @Then("user can be found by email")
    fun thenSearchUserByEmail() {
        val users = userApi.searchUsers(firstName).block() ?: fail("user could not be searched for")
        assertThat(users.size).isEqualTo(1)
        assertThat(users[0].firstName).isEqualTo(firstName)
        assertThat(users[0].lastName).isEqualTo(lastName)
        assertThat(users[0].email).isEqualTo(email)
    }
}
