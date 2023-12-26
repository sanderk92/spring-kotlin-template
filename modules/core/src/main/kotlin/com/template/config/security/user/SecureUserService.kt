package com.template.config.security.user

import java.util.UUID

internal interface SecureUserService {
    fun findById(userId: UUID): SecureUser?

    fun findByApiKey(apiKey: String): SecureUser?

    fun create(entry: SecureUserEntry): SecureUser
}
