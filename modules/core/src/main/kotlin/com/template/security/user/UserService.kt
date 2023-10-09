package com.template.security.user

import com.template.security.apikey.ApiKey
import com.template.security.apikey.HashedApiKeyEntry
import java.util.*

interface UserService {
    fun findById(userId: UUID): User?
    fun findByApiKey(apiKey: String): User?
    fun create(userId: UUID, entry: UserEntry): User
    fun search(query: String): List<User>
    fun update(userId: UUID, authorities: List<UserAuthority>): User?
    fun addApiKey(userId: UUID, entry: HashedApiKeyEntry): ApiKey?
    fun deleteApiKey(userId: UUID, apiKeyId: UUID)
}
