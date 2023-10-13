package com.template.config.security.user

import com.template.config.security.apikey.SecureApiKey
import com.template.config.security.apikey.HashedApiKeyEntry
import java.util.*

interface SecureUserService {
    fun findById(userId: UUID): SecureUser?
    fun findByApiKey(apiKey: String): SecureUser?
    fun save(entry: SecureUserEntry): SecureUser
    fun search(query: String): List<SecureUser>
    fun update(userId: UUID, authorities: List<UserAuthority>): SecureUser?
    fun addApiKey(userId: UUID, entry: HashedApiKeyEntry): SecureApiKey?
    fun deleteApiKey(userId: UUID, apiKeyId: UUID)
}
