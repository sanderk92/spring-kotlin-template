package com.template.objects

import org.springframework.security.oauth2.jwt.Jwt
import java.time.Instant

internal fun jwtBuilder(userId: String, roles: List<String> = emptyList()): Jwt.Builder = Jwt
    .withTokenValue(userId)
    .issuedAt(Instant.MIN)
    .expiresAt(Instant.MAX)
    .subject(userId)
    .claims { it["cognito:groups"] = roles }
    .header("type", "JWT")
