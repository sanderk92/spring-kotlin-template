package com.example.security.apikey

import java.util.*

interface UserService {
    fun findById(userId: UUID): User?
    fun findByApiKey(apiKey: String): User?
    fun createIfNotExists(userId: UUID)
    fun addApiKey(userId: UUID, entry: ApiKeyEntry): User?
    fun deleteApiKey(userId: UUID, apiKeyId: UUID)
}