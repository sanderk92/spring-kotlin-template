package com.template.config.security.jwt

import com.template.config.security.user.Authority
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("auth.user-info")
internal data class UserInfoProperties(
    val endpoint: String,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
)

@ConfigurationProperties("auth.role")
internal data class RoleProperties(
    val claim: String,
    val defaults: List<Authority>,
    val mappings: Map<String, List<Authority>>,
)
