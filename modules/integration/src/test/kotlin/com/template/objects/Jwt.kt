package com.template.objects

import org.springframework.security.oauth2.jwt.Jwt
import java.time.Instant

internal const val email = "test@email.com"
internal const val username = "username"
internal const val firstName = "firstName"
internal const val lastName = "lastName"

internal fun jwtBuilder(userId: String, roles: List<String> = emptyList()): Jwt.Builder = Jwt
    .withTokenValue(userId)
    .issuedAt(Instant.MIN)
    .expiresAt(Instant.MAX)
    .subject(userId)
    .claims { it.putAll(roleClaims(roles)) }
    .claim("email", email)
    .claim("preferred_username", username)
    .claim("given_name", firstName)
    .claim("family_name", lastName)
    .header("type", "JWT")

private fun roleClaims(roles: List<String>) = mapOf(
    "realm_access" to mapOf(
        "roles" to roles
    )
)
