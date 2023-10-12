package com.template.config.security.user

import com.template.config.security.apikey.ApiKeyI
import java.util.*

interface SecureUser {
    val id: UUID
    val email: String
    val username: String
    val firstName: String
    val lastName: String
    val authorities: List<UserAuthority>
    val apiKeys: List<ApiKeyI>
}

data class SecureUserEntry(
    val id: UUID,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val authorities: List<UserAuthority>,
)
