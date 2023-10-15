package com.template.config.security.jwt

import com.template.config.security.user.Authority
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("token")
internal data class JwtProperties(
    val claims: JwtClaims,
    val roleDefaults: List<Authority>,
    val roleMappings: Map<String, List<Authority>>
)

@ConfigurationProperties("token.claims")
internal data class JwtClaims(
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val authorities: String,
)
