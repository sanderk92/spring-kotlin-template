package com.template.glue

import com.template.objects.*
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions.assertThat
import org.openapitools.client.apis.UsersApi

internal class UsersStepDefinitions(private val userApi : UsersApi) {

    @Then("current user can be retrieved and has authorities {string}")
    fun thenRetrieveCurrentUser(authorities: String) {
        val user = userApi.getCurrentUser()
        assertThat(user.username).isEqualTo(activeUsername)
        assertThat(user.firstName).isEqualTo(activeFirstName)
        assertThat(user.lastName).isEqualTo(activeLastName)
        assertThat(user.email).isEqualTo(activeEmail)
        assertThat(user.authorities).containsAll(authorities.split(","))
    }

    @Then("current user can be found by email")
    fun thenSearchUserByEmail() {
        val users = userApi.searchUsers(activeEmail)
        assertThat(users.size).isEqualTo(1)
        assertThat(users[0].username).isEqualTo(activeUsername)
        assertThat(users[0].firstName).isEqualTo(activeFirstName)
        assertThat(users[0].lastName).isEqualTo(activeLastName)
        assertThat(users[0].email).isEqualTo(activeEmail)
    }

    @Then("current user can be found by user name")
    fun thenSearchUserByUserName() {
        val users = userApi.searchUsers(activeUsername)
        assertThat(users.size).isEqualTo(1)
        assertThat(users[0].username).isEqualTo(activeUsername)
        assertThat(users[0].firstName).isEqualTo(activeFirstName)
        assertThat(users[0].lastName).isEqualTo(activeLastName)
        assertThat(users[0].email).isEqualTo(activeEmail)
    }

    @Then("current user can be found by first name")
    fun thenSearchUserByFirstName() {
        val users = userApi.searchUsers(activeUsername)
        assertThat(users.size).isEqualTo(1)
        assertThat(users[0].username).isEqualTo(activeUsername)
        assertThat(users[0].firstName).isEqualTo(activeFirstName)
        assertThat(users[0].lastName).isEqualTo(activeLastName)
        assertThat(users[0].email).isEqualTo(activeEmail)
    }

    @Then("current user can be found by last name")
    fun thenSearchUserByLastName() {
        val users = userApi.searchUsers(activeLastName)
        assertThat(users.size).isEqualTo(1)
        assertThat(users[0].username).isEqualTo(activeUsername)
        assertThat(users[0].firstName).isEqualTo(activeFirstName)
        assertThat(users[0].lastName).isEqualTo(activeLastName)
        assertThat(users[0].email).isEqualTo(activeEmail)
    }
}
