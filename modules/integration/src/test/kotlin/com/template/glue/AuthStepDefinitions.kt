package com.template.glue

import com.template.config.MockJwtDecoder
import com.template.objects.jwtBuilder
import io.cucumber.java.en.Given
import io.mockk.every
import org.springframework.security.oauth2.jwt.JwtDecoder
import java.util.*

private val currentUserId = UUID.randomUUID();

class AuthStepDefinitions(@MockJwtDecoder private val jwtDecoder: JwtDecoder) {

    @Given("current user without roles")
    fun givenDefaultAuthenticatedUserWithoutRoles() {
        every { jwtDecoder.decode(any()) } returns jwtBuilder(currentUserId.toString()).build()
    }

    @Given("current user with roles {string}")
    fun givenDefaultAuthenticatedUserWithRoles(roles: String) {
        every { jwtDecoder.decode(any()) } returns jwtBuilder(currentUserId.toString(), roles.split(",")).build()
    }
}
