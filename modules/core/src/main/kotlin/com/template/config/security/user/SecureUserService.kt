package com.template.config.security.user

import java.util.*

interface SecureUserService {
    fun findById(userId: UUID): SecureUser?
    fun findByApiKey(apiKey: String): SecureUser?
    fun create(entry: SecureUserEntry): SecureUser
    fun search(query: String): List<SecureUser>
    fun update(userId: UUID, authorities: List<Authority>): SecureUser?
}
