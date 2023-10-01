package com.example.security.user

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.security.token")
data class JwtProperties(
    val claims: JwtClaims,
    val roleMappings: Map<String, List<UserAuthority>>
)

@ConfigurationProperties("spring.security.token.claims")
data class JwtClaims(
    val email: String,
    val firstName: String,
    val lastName: String,
    val authorities: String,
)