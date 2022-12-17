package com.example.security.apikey.model

import com.example.security.apikey.ApiKeyEntry
import java.util.*

interface UserEntityService {
    fun findById(userId: UUID): Optional<UserEntity>
    fun findByApiKey(apiKey: String): Optional<UserEntity>
    fun addApiKey(userId: UUID, entry: ApiKeyEntry): Optional<UserEntity>
    fun deleteApiKey(userId: UUID, apiKeyId: UUID)
}