package com.template.glue

import com.template.objects.*
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions.assertThat
import org.openapitools.client.apis.UserApi

internal class UserStepDefinitions(private val userApi : UserApi) {

    @Then("current user can be retrieved and has authorities {string}")
    fun thenRetrieveCurrentUser(authorities: String) {
        val user = userApi.getCurrentUser().block()!!
        assertThat(user.username).isEqualTo(username)
        assertThat(user.firstName).isEqualTo(firstName)
        assertThat(user.lastName).isEqualTo(lastName)
        assertThat(user.email).isEqualTo(email)
        assertThat(user.authorities).containsAll(authorities.split(","))
    }

    @Then("current user can be found by email")
    fun thenSearchUserByEmail() {
        val users = userApi.searchUsers(email).block()!!
        assertThat(users.size).isEqualTo(1)
        assertThat(users[0].username).isEqualTo(username)
        assertThat(users[0].firstName).isEqualTo(firstName)
        assertThat(users[0].lastName).isEqualTo(lastName)
        assertThat(users[0].email).isEqualTo(email)
    }

    @Then("current user can be found by user name")
    fun thenSearchUserByUserName() {
        val users = userApi.searchUsers(username).block()!!
        assertThat(users.size).isEqualTo(1)
        assertThat(users[0].username).isEqualTo(username)
        assertThat(users[0].firstName).isEqualTo(firstName)
        assertThat(users[0].lastName).isEqualTo(lastName)
        assertThat(users[0].email).isEqualTo(email)
    }

    @Then("current user can be found by first name")
    fun thenSearchUserByFirstName() {
        val users = userApi.searchUsers(firstName).block()!!
        assertThat(users.size).isEqualTo(1)
        assertThat(users[0].username).isEqualTo(username)
        assertThat(users[0].firstName).isEqualTo(firstName)
        assertThat(users[0].lastName).isEqualTo(lastName)
        assertThat(users[0].email).isEqualTo(email)
    }

    @Then("current user can be found by last name")
    fun thenSearchUserByLastName() {
        val users = userApi.searchUsers(lastName).block()!!
        assertThat(users.size).isEqualTo(1)
        assertThat(users[0].username).isEqualTo(username)
        assertThat(users[0].firstName).isEqualTo(firstName)
        assertThat(users[0].lastName).isEqualTo(lastName)
        assertThat(users[0].email).isEqualTo(email)
    }
}
