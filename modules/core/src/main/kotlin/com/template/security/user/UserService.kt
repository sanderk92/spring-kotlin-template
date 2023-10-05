package com.template.security.user

import com.template.security.apikey.ApiKey
import com.template.security.apikey.ApiKeyEntry
import java.util.*

interface UserService {
    fun findById(userId: UUID): User?
    fun findByApiKey(apiKey: String): User?
    fun create(userId: UUID, email: String, firstName: String, lastName: String, authorities: List<UserAuthority>): User
    fun search(query: String): List<User>
    fun update(userId: UUID, authorities: List<UserAuthority>): User?
    fun addApiKey(userId: UUID, entry: ApiKeyEntry): ApiKey?
    fun deleteApiKey(userId: UUID, apiKeyId: UUID)
}
