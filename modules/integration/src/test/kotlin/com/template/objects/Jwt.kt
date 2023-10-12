package com.template.objects

import org.springframework.security.oauth2.jwt.Jwt
import java.time.Instant

const val jwtString = "token"
const val jwtSubject = "64a173fc-293e-4680-8b93-eeebf92ee19c"

const val email = "test@email.com"
const val username = "username"
const val firstName = "firstName"
const val lastName = "lastName"

val adminJwt: Jwt = jwtBuilder()
    .claims { it.putAll(roleClaims("admin")) }
    .build()

val userJwt: Jwt = jwtBuilder()
    .build()

private fun jwtBuilder() = Jwt
    .withTokenValue(jwtString)
    .issuedAt(Instant.MIN)
    .expiresAt(Instant.MAX)
    .subject(jwtSubject)
    .claim("email", email)
    .claim("preferred_username", username)
    .claim("given_name", firstName)
    .claim("family_name", lastName)
    .header("type", "JWT")

private fun roleClaims(vararg roles: String) = mapOf(
    "realm_access" to mapOf(
        "roles" to roles.toList()
    )
)
