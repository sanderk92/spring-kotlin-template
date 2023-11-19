package com.template.glue

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.template.CucumberTest.Companion.wiremock
import com.template.config.MockJwtDecoder
import com.template.objects.jwtBuilder
import com.template.utils.toJson
import io.cucumber.java.en.Given
import io.mockk.every
import jakarta.annotation.PostConstruct
import org.springframework.security.oauth2.jwt.JwtDecoder
import java.util.*

const val userId = "6be3e66e-bc25-4e8f-8552-3f199cb3286d"
const val username = "username"
const val firstName = "firstName"
const val lastName = "lastName"
const val email = "email"

internal class AuthStepDefinitions(@MockJwtDecoder private val jwtDecoder: JwtDecoder) {

    @PostConstruct
    fun stubUserInfo() {
        wiremock.stubFor(get(urlEqualTo("/auth/userinfo")).willReturn(okJson(userInfoJson())))
    }

    @Given("current user without roles")
    fun givenDefaultAuthenticatedUserWithoutRoles() {
        every { jwtDecoder.decode(any()) } returns jwtBuilder(userId).build()
    }

    @Given("current user with roles {string}")
    fun givenDefaultAuthenticatedUserWithRoles(roles: String) {
        every { jwtDecoder.decode(any()) } returns jwtBuilder(userId, roles.split(",")).build()
    }
}

private fun userInfoJson() = mapOf(
    "sub" to userId,
    "preferred_username" to username,
    "given_name" to firstName,
    "family_name" to lastName,
    "email" to email
).toJson()