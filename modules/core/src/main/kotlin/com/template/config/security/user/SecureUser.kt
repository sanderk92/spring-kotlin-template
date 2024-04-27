package com.template.config.security.user

import java.util.UUID

internal interface SecureUser {
    val id: Id
    val email: String
    val username: String
    val firstName: String
    val lastName: String

    interface Id {
        val value: UUID
    }
}

internal data class SecureUserEntry(
    val id: UUID,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
)
