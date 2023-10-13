package com.template.config.security.user

import com.template.config.security.apikey.SecureApiKey
import java.util.*

interface SecureUser {
    val id: UUID
    val email: String
    val username: String
    val firstName: String
    val lastName: String
    val authorities: List<Authority>
    val apiKeys: List<SecureApiKey>
}

data class SecureUserEntry(
    val id: UUID,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val authorities: List<Authority>,
)
