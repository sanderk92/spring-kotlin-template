package com.example.common

import com.example.common.ApplicationRoles.TEST_ROLE
import org.springframework.security.oauth2.jwt.Jwt
import java.time.Instant

const val jwtString = "token"
const val jwtSubject = "64a173fc-293e-4680-8b93-eeebf92ee19c"

val testJwt: Jwt = Jwt
    .withTokenValue(jwtString)
    .issuedAt(Instant.MIN)
    .expiresAt(Instant.MAX)
    .subject(jwtSubject)
    .claims { it.putAll(roleClaims(TEST_ROLE)) }
    .header("type", "JWT")
    .build()


val randomJwt: Jwt = Jwt
    .withTokenValue(jwtString)
    .issuedAt(Instant.MIN)
    .expiresAt(Instant.MAX)
    .subject(jwtSubject)
    .claims { it.putAll(roleClaims()) }
    .header("type", "JWT")
    .build()

private fun roleClaims(vararg roles: String) = mapOf(
    "realm_access" to mapOf(
        "roles" to roles.asList()
    )
)
