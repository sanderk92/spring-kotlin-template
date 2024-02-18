package com.template.glue


import com.github.tomakehurst.wiremock.client.WireMock.*
import com.template.CucumberTest
import com.template.CucumberTest.Companion.wiremock
import com.template.config.MockJwtDecoder
import com.template.objects.jwtBuilder
import com.template.utils.toJson
import io.cucumber.java.en.Given
import io.mockk.every
import java.util.*
import org.springframework.security.oauth2.jwt.JwtDecoder

private const val currentUserId = "6be3e66e-bc25-4e8f-8552-3f199cb3286d"
private const val currentUsername = "currentUsername"
private const val currentFirstName = "currentFirstName"
private const val currentLastName = "currentLastName"
private const val currentEmail = "currentEmail"

private const val anotherUserId = "81caca7b-9af5-4a4a-b555-869674ed1fe0"
private const val anotherUsername = "anotherUsername"
private const val anotherFirstName = "anotherFirstName"
private const val anotherLastName = "anotherLastName"
private const val anotherEmail = "anotherEmail"

var activeUserId: String = currentUserId
var activeUsername: String = currentUsername
var activeFirstName: String = currentFirstName
var activeLastName: String = currentLastName
var activeEmail: String = currentEmail

internal class AuthStepDefinitions(@MockJwtDecoder private val jwtDecoder: JwtDecoder) {

    @Given("user without roles")
    fun givenUserWithoutRoles() {
        setCurrentUser()
    }

    @Given("user with roles {string}")
    fun givenUserWithRoles(roles: String) {
        setCurrentUser(roles.split(","))
    }

    @Given("another user without roles")
    fun givenAnotherUserWithoutRoles() {
        setAnotherUser()
    }

    @Given("another user with roles {string}")
    fun givenAnotherUserWithRoles(roles: String) {
        setAnotherUser(roles.split(","))
    }

    private fun setCurrentUser(roles: List<String> = emptyList()) {
        activeUserId = currentUserId
        activeUsername = currentUsername
        activeFirstName = currentFirstName
        activeLastName = currentLastName
        activeEmail = currentEmail
        setStubs(roles)
    }

    private fun setAnotherUser(roles: List<String> = emptyList()) {
        activeUserId = anotherUserId
        activeUsername = anotherUsername
        activeFirstName = anotherFirstName
        activeLastName = anotherLastName
        activeEmail = anotherEmail
        setStubs(roles)
    }

    private fun setStubs(roles: List<String>) {
        wiremock.stubFor(get(urlEqualTo("/auth/userinfo")).willReturn(okJson(activeUserInfo())))
        every { jwtDecoder.decode(any()) } returns jwtBuilder(activeUserId, roles).build()
    }
}

private fun activeUserInfo() = mapOf(
    "sub" to activeUserId,
    "preferred_username" to activeUsername,
    "given_name" to activeFirstName,
    "family_name" to activeLastName,
    "email" to activeEmail,
).toJson()
