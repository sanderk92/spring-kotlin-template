package com.example.security.user

import com.example.security.apikey.ApiKey
import com.example.security.apikey.ApiKeyEntry
import java.util.*

interface UserService {
    fun findById(userId: UUID): User?
    fun findByApiKey(apiKey: String): User?
    fun createIfNotExists(userId: UUID): User
    fun addApiKey(userId: UUID, entry: ApiKeyEntry): ApiKey?
    fun deleteApiKey(userId: UUID, apiKeyId: UUID)
}