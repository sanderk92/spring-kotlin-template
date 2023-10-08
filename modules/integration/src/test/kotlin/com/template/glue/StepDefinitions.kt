package com.template.glue

import com.template.config.MockJwtDecoder
import com.template.objects.*
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.openapitools.client.apis.UserApi
import org.springframework.security.oauth2.jwt.JwtDecoder

class StepDefinitions(
    @MockJwtDecoder private val jwtDecoder: JwtDecoder,
    private val userApi : UserApi,
) {
    @Given("an authenticated user")
    fun authenticatedUser() {
        every { jwtDecoder.decode(jwtString) } returns userJwt
    }

    @Given("an authenticated admin")
    fun authenticatedAdmin() {
        every { jwtDecoder.decode(jwtString) } returns adminJwt
    }

    @Then("user details can be retrieved")
    fun testRetrieveCurrentUser() {
        val user = userApi.getCurrentUser().block()
        assertThat(user).isNotNull
        assertThat(user?.firstName).isEqualTo(firstName)
        assertThat(user?.lastName).isEqualTo(lastName)
        assertThat(user?.email).isEqualTo(email)
    }
}
