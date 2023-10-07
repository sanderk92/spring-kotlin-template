package com.template.glue

import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions.assertThat
import org.openapitools.client.apis.UserApi

class StepDefinitions(
    private val userApi : UserApi,
) {
    @Then("authenticated user can be retrieved")
    fun validateCurrentUser() {
        val user = userApi.getCurrentUser().block()
        assertThat(user).isNotNull
        assertThat(user?.firstName).isEqualTo("dev")
        assertThat(user?.lastName).isEqualTo("user")
        assertThat(user?.email).isEqualTo("dev@user.com")
    }
}
