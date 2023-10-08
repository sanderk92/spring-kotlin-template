package com.template.objects

import org.springframework.security.oauth2.jwt.Jwt
import java.time.Instant

const val jwtString = "token"
const val jwtSubject = "64a173fc-293e-4680-8b93-eeebf92ee19c"

const val firstName = "firstName"
const val lastName = "lastName"
const val email = "test@email.com"

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
    .claim("given_name", "firstName")
    .claim("family_name", "lastName")
    .claim("email", "test@email.com")
    .header("type", "JWT")

private fun roleClaims(vararg roles: String) = mapOf(
    "realm_access" to mapOf(
        "roles" to roles.toList()
    )
)
