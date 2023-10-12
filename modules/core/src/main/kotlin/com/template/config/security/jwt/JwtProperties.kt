package com.template.config.security.jwt

import com.template.config.security.user.UserAuthority
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("token")
data class JwtProperties(
    val claims: JwtClaims,
    val roleDefaults: List<UserAuthority>,
    val roleMappings: Map<String, List<UserAuthority>>
)

@ConfigurationProperties("token.claims")
data class JwtClaims(
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val authorities: String,
)