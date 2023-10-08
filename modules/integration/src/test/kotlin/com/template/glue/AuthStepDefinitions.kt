package com.template.glue

import com.template.config.MockJwtDecoder
import com.template.objects.adminJwt
import com.template.objects.jwtString
import com.template.objects.userJwt
import io.cucumber.java.en.Given
import io.mockk.every
import org.springframework.security.oauth2.jwt.JwtDecoder

class AuthStepDefinitions(@MockJwtDecoder private val jwtDecoder: JwtDecoder) {

    @Given("an authenticated user")
    fun givenAuthenticatedUser() {
        every { jwtDecoder.decode(jwtString) } returns userJwt
    }

    @Given("an authenticated admin")
    fun givenAuthenticatedAdmin() {
        every { jwtDecoder.decode(jwtString) } returns adminJwt
    }
}
